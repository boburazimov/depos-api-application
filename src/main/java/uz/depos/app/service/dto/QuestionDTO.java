package uz.depos.app.service.dto;

import com.fasterxml.jackson.annotation.JsonView;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.ObjectUtils;
import uz.depos.app.domain.Question;
import uz.depos.app.service.view.View;

/**
 * A DTO representing a question, with only the public attributes.
 */
public class QuestionDTO {

    @JsonView(value = { View.ModelView.External.class, View.ModelView.PUT.class })
    private Long id;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private Long meetingId;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.PUT.class })
    private Long userId;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private Long memberId;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private String questionText;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.PUT.class })
    private String questionAnswer;

    @JsonView(value = { View.ModelView.External.class })
    private Boolean active;

    public QuestionDTO() {}

    public QuestionDTO(Question question) {
        this.id = question.getId();
        this.meetingId = ObjectUtils.isNotEmpty(question.getMeeting()) ? question.getMeeting().getId() : null;
        this.userId = ObjectUtils.isNotEmpty(question.getUser()) ? question.getUser().getId() : null;
        this.memberId = ObjectUtils.isNotEmpty(question.getMember()) ? question.getMember().getId() : null;
        this.questionText = question.getQuestionText();
        this.questionAnswer = question.getQuestionAnswer();
        this.active = question.getActive();
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

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionAnswer() {
        return questionAnswer;
    }

    public void setQuestionAnswer(String questionAnswer) {
        this.questionAnswer = questionAnswer;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return (
            "QuestionDTO{" +
            "id=" +
            id +
            ", meetingId=" +
            meetingId +
            ", userId=" +
            userId +
            ", memberId=" +
            memberId +
            ", questionText='" +
            questionText +
            '\'' +
            ", questionAnswer='" +
            questionAnswer +
            '\'' +
            ", active=" +
            active +
            '}'
        );
    }
}
