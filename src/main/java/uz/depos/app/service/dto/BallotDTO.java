package uz.depos.app.service.dto;

import com.fasterxml.jackson.annotation.JsonView;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import uz.depos.app.domain.Ballot;
import uz.depos.app.domain.enums.BallotOptionEnum;
import uz.depos.app.service.view.View;

/**
 * A DTO representing a ballot.
 */
public class BallotDTO {

    @JsonView(value = { View.ModelView.External.class, View.ModelView.PUT.class })
    private Long id;

    @NotNull(message = "Meeting ID must NOT be null!")
    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class, View.ModelView.PUT.class })
    private Long meetingId;

    @NotNull(message = "Member ID must NOT be null!")
    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class, View.ModelView.PUT.class })
    private Long memberId;

    @NotNull(message = "Agenda ID must NOT be null!")
    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class, View.ModelView.PUT.class })
    private Long agendaId;

    @NotNull(message = "Voting-option ID must NOT be null!")
    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class, View.ModelView.PUT.class })
    private Long votingOptionId;

    @NotNull(message = "Ballot-option must NOT be null!")
    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class, View.ModelView.PUT.class })
    private BallotOptionEnum options;

    public BallotDTO() {}

    public BallotDTO(Ballot ballot) {
        this.id = ballot.getId();
        this.memberId = ballot.getMember().getId();
        this.meetingId = ballot.getMeeting().getId();
        this.agendaId = ballot.getAgenda().getId();
        this.votingOptionId = ballot.getVotingOption().getId();
        this.options = ballot.getOptions();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
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

    public Long getVotingOptionId() {
        return votingOptionId;
    }

    public void setVotingOptionId(Long votingOptionId) {
        this.votingOptionId = votingOptionId;
    }

    public BallotOptionEnum getOptions() {
        return options;
    }

    public void setOptions(BallotOptionEnum options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return (
            "BallotDTO{" +
            "id=" +
            id +
            ", memberId=" +
            memberId +
            ", meetingId=" +
            meetingId +
            ", agendaId=" +
            agendaId +
            ", votingOptionId=" +
            votingOptionId +
            ", options=" +
            options +
            '}'
        );
    }
}
