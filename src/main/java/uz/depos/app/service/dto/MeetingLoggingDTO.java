package uz.depos.app.service.dto;

import com.fasterxml.jackson.annotation.JsonView;
import java.time.Instant;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import uz.depos.app.domain.MeetingLogging;
import uz.depos.app.service.view.View;

/**
 * Модель "Комментирование" для "Председателя и Секретаря"
 */
public class MeetingLoggingDTO {

    @JsonView(value = { View.ModelView.External.class })
    private Long id;

    @NotNull(message = "Meeting ID must not be null!")
    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private Long meetingId;

    @NotNull(message = "User ID must not be null!")
    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private Long userId;

    @NotBlank(message = "Logging text must not be empty!")
    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private String loggingText;

    @JsonView(value = { View.ModelView.External.class })
    private Boolean active = true;

    @JsonView(value = { View.ModelView.External.class })
    private Instant createdDate = Instant.now();

    public MeetingLoggingDTO() {}

    public MeetingLoggingDTO(MeetingLogging logging) {
        this.id = logging.getId();
        this.meetingId = logging.getMeetingId();
        this.userId = logging.getUserId();
        this.loggingText = logging.getLoggingText();
        this.active = logging.getActive();
        this.createdDate = logging.getCreatedDate();
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

    public String getLoggingText() {
        return loggingText;
    }

    public void setLoggingText(String loggingText) {
        this.loggingText = loggingText;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return (
            "MeetingLoggingDTO{" +
            "id=" +
            id +
            ", meetingId=" +
            meetingId +
            ", userId=" +
            userId +
            ", loggingText='" +
            loggingText +
            '\'' +
            ", active=" +
            active +
            ", createdDate=" +
            createdDate +
            '}'
        );
    }
}
