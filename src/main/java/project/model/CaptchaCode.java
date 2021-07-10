package project.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "captcha_codes")
@NoArgsConstructor
@AllArgsConstructor
public class CaptchaCode extends Identified {

    @Column(nullable = false)
    private Date time;

    @Column(columnDefinition = "tinytext", nullable = false)
    private String code;

    @Column(name = "secret_code", columnDefinition = "tinytext", nullable = false)
    private String secretCode;

    public CaptchaCode() {
    }

    public CaptchaCode(Date time, String code, String secretCode) {
        this.time = time;
        this.code = code;
        this.secretCode = secretCode;
    }

    public String getCode() {
        return code;
    }

}
