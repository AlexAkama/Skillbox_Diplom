package project.dto;

import project.config.Connection;
import project.model.PostComment;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.*;

import static project.model.emun.GlobalSettingsValue.getSetValue;

public class Dto {

    public static final String baseCondition = "p.isActive=1 AND p.moderationStatus='ACCEPTED' AND p.time < NOW()";

    public static String dateToSqlDate(Date date) {
        return new java.sql.Timestamp(date.getTime()).toString();
    }


    public static String randomString(int length) {
        char[] chars = "ACEFGHJKLMNPQRUVWXYabcdefhijkprstuvwx1234567890".toCharArray();
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(chars[random.nextInt(chars.length)]);
        }
        return stringBuilder.toString();
    }

    public static List<String> getTagsList(long postId) {
        List<String> tags;
        try (Session session = Connection.getSession()) {
            Transaction transaction = session.beginTransaction();

            String hql = "select t.name from TagToPost tp"
                    + " join Tag t on tp.tag.id = t.id"
                    + " where tp.post.id=:id";
            tags = session.createQuery(hql).setParameter("id", postId).getResultList();

            transaction.commit();
        }
        return tags;
    }

    public static List<CommentDto> getCommentsList(long postId) {
        List<CommentDto> comments = new ArrayList<>();
        try (Session session = Connection.getSession()) {
            Transaction transaction = session.beginTransaction();

            String hql = "from PostComment where post.id=:id";
            List<PostComment> resultList = session.createQuery(hql).setParameter("id", postId).getResultList();

            for (PostComment comment : resultList) {
                comments.add(new CommentDto().createFrom(comment));
            }

            transaction.commit();
        }
        return comments;
    }

    public static BufferedImage resizeForCaptcha(BufferedImage image) {
        return createResizedImage(image, 100, 35);
    }

    private static BufferedImage createResizedImage(BufferedImage original, int width, int heigth) {
        BufferedImage result = new BufferedImage(width, heigth, original.getType());
        Graphics2D graphics2D = result.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(original, 0, 0, width, heigth, null);
        graphics2D.dispose();
        return result;
    }

    public static void saveGlobalParameter(String name, boolean value) {
        try (Session session = Connection.getSession()) {
            Transaction transaction = session.beginTransaction();

            String hql = "update GlobalSetting set value=:value where code=:code";
            session.createQuery(hql)
                    .setParameter("value", getSetValue(value))
                    .setParameter("code", name)
                    .executeUpdate();

            transaction.commit();
        }
    }


}
