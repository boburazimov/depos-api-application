package uz.depos.app.service.dto;

import com.fasterxml.jackson.annotation.JsonView;
import java.util.List;
import uz.depos.app.domain.Agenda;
import uz.depos.app.domain.enums.AgendaDebateEnum;
import uz.depos.app.domain.enums.AgendaSpeakTimeEnum;
import uz.depos.app.domain.enums.AgendaTypeEnum;
import uz.depos.app.service.view.View;

/**
 * Повестка дня - вопросы для голосование
 */
public class AgendaEditDTO {

    @JsonView(value = { View.ModelView.External.class, View.ModelView.PUT.class, View.ModelView.PATCH.class })
    private Long id;

    @JsonView(value = { View.ModelView.External.class })
    private Long meetingId;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.PUT.class, View.ModelView.PATCH.class })
    private String subject;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.PUT.class, View.ModelView.PATCH.class })
    private Long speakerId;

    @JsonView(value = { View.ModelView.External.class })
    private AgendaTypeEnum typeEnum;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.PUT.class, View.ModelView.PATCH.class })
    private AgendaSpeakTimeEnum speakTimeEnum;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.PUT.class, View.ModelView.PATCH.class })
    private AgendaDebateEnum debateEnum;

    @JsonView(value = { View.ModelView.External.class })
    private Boolean isActive;

    private List<VotingEditDTO> votingOptions;

    private String extraInfo;

    public AgendaEditDTO() {}

    public AgendaEditDTO(Agenda agenda, List<VotingEditDTO> votingOptions) {
        this.id = agenda.getId();
        this.meetingId = agenda.getMeeting().getId();
        this.subject = agenda.getSubject();
        this.speakerId = agenda.getSpeaker() != null ? agenda.getSpeaker().getId() : null;
        this.typeEnum = agenda.getTypeEnum();
        this.speakTimeEnum = agenda.getSpeakTimeEnum();
        this.debateEnum = agenda.getDebateEnum();
        this.isActive = agenda.getActive();
        this.votingOptions = votingOptions;
        this.extraInfo = agenda.getExtraInfo();
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

    public List<VotingEditDTO> getVotingOptions() {
        return votingOptions;
    }

    public void setVotingOptions(List<VotingEditDTO> votingOptions) {
        this.votingOptions = votingOptions;
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
            "AgendaEditDTO{" +
            "id=" +
            id +
            ", meetingId=" +
            meetingId +
            ", subject='" +
            subject +
            '\'' +
            ", speakerId=" +
            speakerId +
            ", typeEnum=" +
            typeEnum +
            ", speakTimeEnum=" +
            speakTimeEnum +
            ", debateEnum=" +
            debateEnum +
            ", isActive=" +
            isActive +
            ", votingOptions=" +
            votingOptions +
            ", extraInfo='" +
            extraInfo +
            '\'' +
            '}'
        );
    }
}
