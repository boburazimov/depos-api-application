package uz.depos.app.service.dto;

import lombok.Getter;
import lombok.Setter;
import uz.depos.app.domain.VotingOption;
import uz.depos.app.domain.enums.VotingOptionTypeEnum;

/**
 * A DTO representing a voting-option, with only the public attributes.
 */

public class VotingOptionDTO {

    private Long id;

    private String votingText;

    private VotingOptionTypeEnum optionTypeEnum;

    private Long meetingId;

    private Long agendaId;

    public VotingOptionDTO() {}

    public VotingOptionDTO(VotingOption votingOption) {
        this.id = votingOption.getId();
        this.votingText = votingOption.getVotingText();
        this.optionTypeEnum = votingOption.getOptionTypeEnum();
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

    public VotingOptionTypeEnum getOptionTypeEnum() {
        return optionTypeEnum;
    }

    public void setOptionTypeEnum(VotingOptionTypeEnum optionTypeEnum) {
        this.optionTypeEnum = optionTypeEnum;
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
            ", optionTypeEnum=" +
            optionTypeEnum +
            ", meetingId=" +
            meetingId +
            ", agendaId=" +
            agendaId +
            '}'
        );
    }
}
