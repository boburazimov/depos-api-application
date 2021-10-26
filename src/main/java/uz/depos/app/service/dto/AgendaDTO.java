package uz.depos.app.service.dto;

import com.fasterxml.jackson.annotation.JsonView;
import java.util.Set;
import javax.validation.constraints.NotNull;
import uz.depos.app.domain.Agenda;
import uz.depos.app.domain.enums.AgendaDebateEnum;
import uz.depos.app.domain.enums.AgendaSpeakTimeEnum;
import uz.depos.app.domain.enums.AgendaTypeEnum;
import uz.depos.app.service.view.View;

/**
 * A DTO representing a agenda.
 */
public class AgendaDTO {

    @JsonView(value = { View.ModelView.External.class, View.ModelView.PUT.class, View.ModelView.PATCH.class })
    private Long id;

    @NotNull
    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private Long meetingId;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class, View.ModelView.PUT.class })
    private String subject;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class, View.ModelView.PUT.class })
    private Long speakerId;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class, View.ModelView.PUT.class })
    private AgendaTypeEnum typeEnum;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class, View.ModelView.PUT.class })
    private AgendaSpeakTimeEnum speakTimeEnum;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class, View.ModelView.PUT.class })
    private AgendaDebateEnum debateEnum;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class, View.ModelView.PUT.class })
    private Boolean active;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private Set<String> variants;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.PATCH.class })
    private String extraInfo;

    private String userName;

    public AgendaDTO() {}

    public AgendaDTO(Agenda agenda) {
        this.id = agenda.getId();
        this.meetingId = agenda.getMeeting().getId();
        this.subject = agenda.getSubject();
        this.speakerId = agenda.getSpeaker() != null ? agenda.getSpeaker().getId() : null;
        this.speakTimeEnum = agenda.getSpeakTimeEnum();
        this.typeEnum = agenda.getTypeEnum();
        this.debateEnum = agenda.getDebateEnum();
        this.active = agenda.getActive();
        this.extraInfo = agenda.getExtraInfo();
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
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public Set<String> getVariants() {
        return variants;
    }

    public void setVariants(Set<String> variants) {
        this.variants = variants;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
            ", typeEnum=" +
            typeEnum +
            ", speakTimeEnum=" +
            speakTimeEnum +
            ", debateEnum=" +
            debateEnum +
            ", active=" +
            active +
            ", variants=" +
            variants +
            ", extraInfo='" +
            extraInfo +
            '\'' +
            ", userName='" +
            userName +
            '\'' +
            '}'
        );
    }
}
