package uz.depos.app.service.dto;

import uz.depos.app.domain.Agenda;
import uz.depos.app.domain.enums.AgendaDebateEnum;
import uz.depos.app.domain.enums.AgendaSpeakTimeEnum;

/**
 * A DTO representing a agenda.
 */
public class AgendaDTO {

    private Long id;

    private Long meetingId;

    private String subject;

    private Long speakerId;

    private AgendaSpeakTimeEnum speakTimeEnum;

    private AgendaDebateEnum debateEnum;

    private Boolean isActive;

    public AgendaDTO() {}

    public AgendaDTO(Agenda agenda) {
        this.id = agenda.getId();
        this.meetingId = agenda.getMeeting().getId();
        this.subject = agenda.getSubject();
        this.speakerId = agenda.getSpeaker() != null ? agenda.getSpeaker().getId() : null;
        this.speakTimeEnum = agenda.getSpeakTimeEnum();
        this.debateEnum = agenda.getDebateEnum();
        this.isActive = agenda.getActive();
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Long getSpeakerId() {
        return speakerId;
    }

    public void setSpeakerId(Long speakerId) {
        this.speakerId = speakerId;
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

    @Override
    public String toString() {
        return (
            "AgendaDTO{" +
            "id=" +
            id +
            ", meetingId=" +
            meetingId +
            ", subject='" +
            subject +
            '\'' +
            ", speakerId=" +
            speakerId +
            ", speakTimeEnum=" +
            speakTimeEnum +
            ", debateEnum=" +
            debateEnum +
            ", isActive=" +
            isActive +
            '}'
        );
    }
}
