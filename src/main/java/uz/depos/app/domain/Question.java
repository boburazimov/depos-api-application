package uz.depos.app.domain;

import java.io.Serializable;
import javax.persistence.*;
import lombok.EqualsAndHashCode;

/**
 * Вопросы от участников
 */

@EqualsAndHashCode(callSuper = true)
@Entity
public class Question extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    // Документ заседание "meetingID"
    @ManyToOne(optional = false)
    private Meeting meeting;

    // Пользователь
    @ManyToOne
    private User user;

    // Вопрос
    @Column(nullable = false, name = "question_text")
    private String questionText;

    // Ответ
    @Column(name = "question_answer")
    private String questionAnswer;

    // Участник заседания
    @ManyToOne
    private Member member;

    // Статус повестки дня
    @Column(name = "is_active")
    private Boolean isActive;

    public Question() {}

    public Question(Long id, Meeting meeting, User user, String questionText, String questionAnswer, Member member, Boolean isActive) {
        this.id = id;
        this.meeting = meeting;
        this.user = user;
        this.questionText = questionText;
        this.questionAnswer = questionAnswer;
        this.member = member;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
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
            "Question{" +
            "id=" +
            id +
            ", meeting=" +
            meeting +
            ", user=" +
            user +
            ", questionText='" +
            questionText +
            '\'' +
            ", questionAnswer='" +
            questionAnswer +
            '\'' +
            ", member=" +
            member +
            ", isActive=" +
            isActive +
            '}'
        );
    }
}
