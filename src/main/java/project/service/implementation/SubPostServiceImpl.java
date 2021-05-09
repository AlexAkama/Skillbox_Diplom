package project.service.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.dto.comment.*;
import project.dto.main.AppResponse;
import project.dto.moderation.ModerationRequest;
import project.exception.*;
import project.model.*;
import project.model.emun.ModerationDecision;
import project.model.emun.ModerationStatus;
import project.service.*;

import java.util.Date;

import static project.model.emun.ModerationDecision.ACCEPT;
import static project.model.emun.ModerationDecision.valueOf;
import static project.model.emun.ModerationStatus.ACCEPTED;
import static project.model.emun.ModerationStatus.DECLINED;

@Service
public class SubPostServiceImpl implements SubPostService {

    private final UserService userService;
    private final PostService postService;

    @Value("${config.comment.minlength.text}")
    private int minTextLength;

    public SubPostServiceImpl(UserService userService,
                              PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @Override
    public ResponseEntity<AppResponse> setModerationDecision(ModerationRequest request)
            throws NotFoundException, UnauthorizedException, ForbiddenException {
        User user = userService.checkUser();
        if (user.isModerator()) {
            ModerationDecision decision = valueOf(request.getDecision().toUpperCase());
            Post post = postService.getPost(request.getPostId());
            ModerationStatus status = (decision == ACCEPT) ? ACCEPTED : DECLINED;
            post.setModerationStatus(status);
            post.setModerator(user);
            postService.saveAndActivateTags(post);
            return ResponseEntity.ok(new AppResponse().ok());
        } else
            throw new ForbiddenException("Нет прав для модерации");
    }

    @Override
    public ResponseEntity<CommentResponse> addComment(CommentRequest request)
            throws NotFoundException, UnauthorizedException {
        User user = userService.checkUser();
        CommentResponse response = new CommentResponse();
        String text = request.getText();
        if (text.length() > minTextLength) {
            Post post = postService.getPost(request.getPostId());
            PostComment comment = new PostComment();
            comment.setText(text);
            comment.setPost(post);
            comment.setUser(user);
            comment.setTime(new Date());
            long parentId = request.getParentId();
            if (parentId != 0) {
                PostComment parent = postService.getComment(parentId);
                comment.setParent(parent);
            }
            comment = postService.saveAndFlush(comment);
            response.setId(comment.getId());
        } else {
            CommentErrorMap errors = new CommentErrorMap();
            errors.addTextError();
            response.setErrors(errors.getErrors());
        }
        return ResponseEntity.ok(response);
    }

}
