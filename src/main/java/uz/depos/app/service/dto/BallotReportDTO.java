package uz.depos.app.service.dto;

import uz.depos.app.domain.Ballot;
import uz.depos.app.domain.enums.BallotOptionEnum;

/**
 * A DTO representing a ballot's report.
 */
public class BallotReportDTO {

    private Long id;

    private Long meetingId;

    private Long memberId;

    private String username;

    private String pinfl;

    private Long agendaId;

    private Long votingOptionId;

    private BallotOptionEnum options;

    public BallotReportDTO() {}

    public BallotReportDTO(Ballot ballot) {
        this.id = ballot.getId();
        this.memberId = ballot.getMember().getId();
        this.username = ballot.getMember().getUser().getFullName();
        this.pinfl = ballot.getMember().getUser().getPinfl();
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

    public Long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPinfl() {
        return pinfl;
    }

    public void setPinfl(String pinfl) {
        this.pinfl = pinfl;
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
            "BallotReportDTO{" +
            "id=" +
            id +
            ", meetingId=" +
            meetingId +
            ", memberId=" +
            memberId +
            ", username='" +
            username +
            '\'' +
            ", pinfl='" +
            pinfl +
            '\'' +
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
