package project.service;

import org.springframework.http.ResponseEntity;
import project.dto.post.PostDto;
import project.dto.post.PostListDto;

public interface PostService {

  ResponseEntity<PostDto> getPost(long postId);

  ResponseEntity<PostListDto> getAnnounceList(int offset, int limit, String mode);

  ResponseEntity<PostListDto> getAnnounceListByTag(int offset, int limit, String tag);

}
