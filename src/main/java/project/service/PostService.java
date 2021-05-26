package project.service;

import org.springframework.http.ResponseEntity;
import project.dto.post.*;
import project.dto.statistic.PostStatisticView;
import project.dto.statistic.StatisticDto;
import project.dto.vote.VoteCounterView;
import project.exception.*;
import project.model.Post;
import project.model.PostComment;

/**
 * <h2>Сервис обработки запросов постов</h2>
 */
public interface PostService {

  ResponseEntity<PostResponse> addPost(PostRequest request) throws NotFoundException, UnauthorizedException, ForbiddenException;

  ResponseEntity<PostResponse> updatePost(long postId, PostRequest update) throws UnauthorizedException, NotFoundException, ForbiddenException;

  Post getPost(long postId) throws NotFoundException;

  void save(Post post);

  PostComment getComment(long commentId) throws NotFoundException;

  void save(PostComment comment);

  PostComment saveAndFlush(PostComment comment);

  ResponseEntity<PostDto> getPostForResponse(long postId) throws NotFoundException;

  /**
   * Получение списка постов
   *
   * @param offset сдвиг для постраничного вывода
   * @param limit  кол-во запрашиваемых постов
   * @param mode режим вывода (сортировка)
   * @return {@link PostListDto объект с данными постов}
   */
  ResponseEntity<PostListDto> getAnnounceList(int offset, int limit, String mode);

  ResponseEntity<PostListDto> getAnnounceListToModeration(int offset, int limit, String status) throws NotFoundException, UnauthorizedException;

  ResponseEntity<PostListDto> getAnnounceListByAuthUser(int offset, int limit, String status) throws NotFoundException, UnauthorizedException;

  ResponseEntity<PostListDto> getAnnounceListByTag(int offset, int limit, String tag) throws NotFoundException;

  ResponseEntity<PostListDto> getAnnounceListByDate(int offset, int limit, String date);

  ResponseEntity<PostListDto> getAnnounceListBySearch(int offset, int limit, String search);

  PostStatisticView getAllPostStatistic();

  PostStatisticView getUserPostStatistic(long userId);

  VoteCounterView getAllVote();

  VoteCounterView getUserVote(long userId);

  StatisticDto getAllStatistic();

  StatisticDto getUserStatistic(long userId);

  void saveAndActivateTags(Post post) throws NotFoundException;

}
