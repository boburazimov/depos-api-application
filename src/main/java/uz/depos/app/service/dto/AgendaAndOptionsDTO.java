package uz.depos.app.service.dto;

import java.util.List;
import uz.depos.app.domain.Agenda;
import uz.depos.app.domain.enums.AgendaDebateEnum;
import uz.depos.app.domain.enums.AgendaSpeakTimeEnum;
import uz.depos.app.domain.enums.AgendaTypeEnum;

/**
 * A DTO representing a agenda and voting-options.
 */
public class AgendaAndOptionsDTO {

    private Long id;
    private Long meetingId;
    private String subject;
    private Long speakerId;
    private AgendaTypeEnum typeEnum;
    private AgendaSpeakTimeEnum speakTimeEnum;
    private AgendaDebateEnum debateEnum;
    private Boolean isActive;
    private List<VotingDTO> votingOptions;
    private String userName;

    public AgendaAndOptionsDTO() {}

    public AgendaAndOptionsDTO(Agenda agenda, List<VotingDTO> votingOptions) {
        this.id = agenda.getId();
        this.meetingId = agenda.getMeeting().getId();
        this.subject = agenda.getSubject();
        this.speakerId = agenda.getSpeaker() != null ? agenda.getSpeaker().getId() : null;
        this.typeEnum = agenda.getTypeEnum();
        this.speakTimeEnum = agenda.getSpeakTimeEnum();
        this.debateEnum = agenda.getDebateEnum();
        this.isActive = agenda.getActive();
        this.votingOptions = votingOptions;
        this.userName = agenda.getSpeaker() != null ? agenda.getSpeaker().getUser().getFullName() : null;
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

    public List<VotingDTO> getVotingOptions() {
        return votingOptions;
    }

    public void setVotingOptions(List<VotingDTO> votingOptions) {
        this.votingOptions = votingOptions;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
