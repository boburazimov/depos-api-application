package uz.depos.app.domain;

import java.io.Serializable;
import javax.persistence.*;
import lombok.EqualsAndHashCode;
import uz.depos.app.domain.enums.AgendaDebateEnum;
import uz.depos.app.domain.enums.AgendaSpeakTimeEnum;
import uz.depos.app.domain.enums.AgendaTypeEnum;

/**
 * Повестка дня - вопросы для голосование
 */
@EqualsAndHashCode(callSuper = true)
@Entity
public class Agenda extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    // Документ заседание "meetingID"
    @ManyToOne(optional = false)
    private Meeting meeting;

    // Тема вопроса
    @Column(nullable = false)
    private String subject;

    // Докладчик "memberID"
    @ManyToOne
    private Member speaker;

    @Enumerated(EnumType.STRING)
    private AgendaTypeEnum typeEnum;

    // Время для доклада
    @Enumerated(EnumType.STRING)
    private AgendaSpeakTimeEnum speakTimeEnum;

    // Время для обсуждения
    @Enumerated(EnumType.STRING)
    private AgendaDebateEnum debateEnum;

    // Статус повестки дня
    @Column(name = "is_active")
    private Boolean isActive;

    //    // Файлы для повестки дня
    //    @OneToMany(cascade = CascadeType.ALL)
    //    private List<Attachment> attachments;

    //    // Варианты решения для голосования
    //    @OneToMany(mappedBy = "agenda", cascade = CascadeType.ALL)
    //    private List<VotingOption> votingOptions;

    // Доп инфо
    @Column(length = 255, name = "extra_info")
    private String extraInfo;

    public Agenda() {}

    public Agenda(
        Long id,
        Meeting meeting,
        String subject,
        Member speaker,
        AgendaTypeEnum typeEnum,
        AgendaSpeakTimeEnum speakTimeEnum,
        AgendaDebateEnum debateEnum,
        Boolean isActive,
        String extraInfo
    ) {
        this.id = id;
        this.meeting = meeting;
        this.subject = subject;
        this.speaker = speaker;
        this.typeEnum = typeEnum;
        this.speakTimeEnum = speakTimeEnum;
        this.debateEnum = debateEnum;
        this.isActive = isActive;
        this.extraInfo = extraInfo;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Member getSpeaker() {
        return speaker;
    }

    public void setSpeaker(Member speaker) {
        this.speaker = speaker;
    }

    public AgendaTypeEnum getTypeEnum() {
        return typeEnum;
    }

    public void setTypeEnum(AgendaTypeEnum typeEnum) {
        this.typeEnum = typeEnum;
    }

    public AgendaSpeakTimeEnum getSpeakTimeEnum() {
        return speakTimeEnum;
    }

    public void setSpeakTimeEnum(AgendaSpeakTimeEnum speakTimeEnum) {
        this.speakTimeEnum = speakTimeEnum;
    }

    public AgendaDebateEnum getDebateEnum() {
        return debateEnum;
    }

    public void setDebateEnum(AgendaDebateEnum debateEnum) {
        this.debateEnum = debateEnum;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    @Override
    public String toString() {
        return (
            "Agenda{" +
            "id=" +
            id +
            ", meeting=" +
            meeting +
            ", subject='" +
            subject +
            '\'' +
            ", speaker=" +
            speaker +
            ", typeEnum=" +
            typeEnum +
            ", speakTimeEnum=" +
            speakTimeEnum +
            ", debateEnum=" +
            debateEnum +
            ", isActive=" +
            isActive +
            ", extraInfo='" +
            extraInfo +
            '\'' +
            '}'
        );
    }
}
