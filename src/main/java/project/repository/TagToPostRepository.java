package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.dto.global.MapDto;
import project.model.TagToPost;

import java.util.List;

@Repository
public interface TagToPostRepository extends JpaRepository<TagToPost, Long> {

    @Query(nativeQuery = true, value = "select t.name as `key`, count(*) as `value` from tag2post tp"
            + " inner join tags t on tp.tag_id = t.id where post_id in"
            + " (select p.id from posts p where is_active=1 AND moderation_status='ACCEPTED' AND time < NOW() )"
            + " group by tp.tag_id")
    List<MapDto> getTagCounterList();



}