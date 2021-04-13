package project.service.impementation;

import org.hibernate.Session;
import org.hibernate.Transaction;
import project.config.Connection;
import project.dto.Dto;
import project.dto._post.PostDto;
import project.dto._post.PostListDto;
import project.model.Post;
import project.model.emun.PostViewMode;

import java.util.Comparator;
import java.util.List;

public class PostServiceImpl {

    public PostListDto makeAnnounces(int offset, int limit, PostViewMode sort) {
        return getResult(true, "", offset, limit, sort);
    }

    public PostListDto makeAnnounces(String conditions, int offset, int limit) {
        return getResult(true, conditions, offset, limit, PostViewMode.NONE);
    }

    public PostListDto makeAnnounces(String conditions, int offset, int limit, PostViewMode sort) {
        return getResult(true, conditions, offset, limit, sort);
    }

    private PostListDto getResult(Boolean useBaseCondition, String conditions, int offset, int limit, PostViewMode mode) {

        //FIXME облагородить HQL
        String hql = "from Post p where ";
        String countHql = "select count(*) from Post p where ";
        if (useBaseCondition) {
            hql += Dto.baseCondition;
            countHql += Dto.baseCondition;
        }
        if (!conditions.isEmpty()) {
            hql += " and (" + conditions + ")";
            countHql += " and (" + conditions + ")";
        }

        String orderByTime = "";
        switch (mode) {
            case EARLY:
                orderByTime = " ORDER BY p.time";
                break;
            case RECENT:
                orderByTime = " ORDER BY p.time DESC";
                break;
        }

        List<Post> postList;
        try (Session session = Connection.getSession()) {
            Transaction transaction = session.beginTransaction();

            count = (long) session.createQuery(countHql).uniqueResult();

            hql += orderByTime;
            postList = session.createQuery(hql)
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .getResultList();

            for (Post post : postList) {
                posts.add(new PostDto().makeAnnounce(post));
            }

            transaction.commit();
        }

        switch (mode) {
            case POPULAR:
                posts.sort(Comparator.comparing(PostDto::getCommentCount)
                        .reversed());
                break;
            case BEST:
                posts.sort(Comparator.comparing(PostDto::getLikeCount)
                        .thenComparing((o1, o2) -> Long.compare(o2.getDislikeCount(), o1.getDislikeCount()))
                        .reversed());
                break;
        }

        return this;
    }



}
