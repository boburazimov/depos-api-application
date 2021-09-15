package uz.depos.app.domain;

import java.io.Serializable;
import javax.persistence.*;
import lombok.EqualsAndHashCode;

/**
 * Модель "Комментирование" для "Председателя и Секретаря"
 */

@EqualsAndHashCode(callSuper = true)
@Entity
public class MeetingLogging extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    // Документ заседание "meetingID"
    @Column(nullable = false, name = "meeting_id")
    private Long meetingId;

    // Пользователь
    @Column(nullable = false, name = "user_id")
    private Long userId;

    // Участник заседания
    @Column(nullable = false, name = "member_id")
    private Long memberId;

    // Вопрос
    @Column(nullable = false, name = "logging_text")
    private String loggingText;

    // Статус
    @Column(name = "is_active")
    private Boolean isActive;

    public MeetingLogging() {}

    public MeetingLogging(Long id, Long meetingId, Long userId, Long memberId, String loggingText, Boolean isActive) {
        this.id = id;
        this.meetingId = meetingId;
        this.userId = userId;
        this.memberId = memberId;
        this.loggingText = loggingText;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getLoggingText() {
        return loggingText;
    }

    public void setLoggingText(String loggingText) {
        this.loggingText = loggingText;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return (
            "MeetingLogging{" +
            "id=" +
            id +
            ", meetingId=" +
            meetingId +
            ", userId=" +
            userId +
            ", memberId=" +
            memberId +
            ", loggingText='" +
            loggingText +
            '\'' +
            ", isActive=" +
            isActive +
            '}'
        );
    }
}
