package project.dto.post;

import lombok.Getter;
import project.model.User;
import project.model.enums.ModerationStatus;

import java.util.Date;

@Getter
public class PostRequestDto {

    private final boolean active;
    private final String title;
    private final String text;
    private final Date time;
    private final User user;
    private final ModerationStatus status;
    private final String[] tagArray;

    public PostRequestDto(boolean active,
                          String title,
                          String text,
                          Date date,
                          User user,
                          ModerationStatus status,
                          String[] tagArray) {
        this.active = active;
        this.title = title;
        this.tagArray = tagArray;
        this.text = text;
        this.time = date;
        this.user = user;
        this.status = status;
    }

}
