package uz.depos.app.service.dto;

import com.fasterxml.jackson.annotation.JsonView;
import javax.validation.constraints.NotNull;
import uz.depos.app.domain.VotingOption;
import uz.depos.app.service.view.View;

/**
 * A DTO representing a voting-option, with only the public attributes.
 */
public class VotingDTO {

    @JsonView(value = { View.ModelView.External.class, View.ModelView.PUT.class, View.ModelView.PATCH.class })
    private Long id;

    @NotNull(message = "Voting text must not be null.")
    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private String votingText;

    @NotNull(message = "Meeting ID must not be null.")
    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class, View.ModelView.PUT.class })
    private Long meetingId;

    @NotNull(message = "Agenda ID must not be null.")
    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class, View.ModelView.PUT.class })
    private Long agendaId;

    public VotingDTO() {}

    public VotingDTO(VotingOption votingOption) {
        this.id = votingOption.getId();
        this.votingText = votingOption.getVotingText();
        this.meetingId = votingOption.getMeeting().getId();
        this.agendaId = votingOption.getAgenda().getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVotingText() {
        return votingText;
    }

    public void setVotingText(String votingText) {
        this.votingText = votingText;
    }

    public Long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }

    public Long getAgendaId() {
        return agendaId;
    }

    public void setAgendaId(Long agendaId) {
        this.agendaId = agendaId;
    }

    @Override
    public String toString() {
        return (
            "VotingOptionDTO{" +
            "id=" +
            id +
            ", votingText='" +
            votingText +
            '\'' +
            ", meetingId=" +
            meetingId +
            ", agendaId=" +
            agendaId +
            '}'
        );
    }
}
