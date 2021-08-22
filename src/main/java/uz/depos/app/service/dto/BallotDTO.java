package uz.depos.app.service.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import uz.depos.app.domain.Ballot;
import uz.depos.app.domain.enums.BallotOptionEnum;

/**
 * A DTO representing a ballot.
 */
public class BallotDTO {

    private Long id;

    @NotNull(message = "Member ID must NOT be null!")
    private Long memberId;

    @NotNull(message = "Meeting ID must NOT be null!")
    private Long meetingId;

    @NotNull(message = "Agenda ID must NOT be null!")
    private Long agendaId;

    @NotNull(message = "Voting-option ID must NOT be null!")
    private Long votingOptionId;

    @NotBlank(message = "Ballot-option must NOT be blank!")
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
