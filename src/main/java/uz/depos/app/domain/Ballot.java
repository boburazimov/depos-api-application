package uz.depos.app.domain;

import javax.persistence.*;
import lombok.*;
import uz.depos.app.domain.enums.BallotOptionEnum;

/**
 * Голосование - процесс голосование (каждая строка - один голос)
 */
@EqualsAndHashCode(callSuper = true)
@Entity
public class Ballot extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    // Привязка к заседанию "meetingID"
    @ManyToOne(optional = false)
    private Meeting meeting;

    // Привязка к участнику заседания "userID"
    @ManyToOne(optional = false)
    private Member member;

    // Привязка к повестки дня "agendaID"
    @ManyToOne(optional = false)
    private Agenda agenda;

    // Привязка к варианту решения "optionsID"
    @ManyToOne(optional = false)
    private VotingOption votingOption;

    // Привязка к варианту ответа (За, Против, Воздержался)
    @Enumerated(EnumType.STRING)
    private BallotOptionEnum options;

    public Ballot() {}

    public Ballot(Long id, Member member, Meeting meeting, Agenda agenda, VotingOption votingOption, BallotOptionEnum options) {
        this.id = id;
        this.member = member;
        this.meeting = meeting;
        this.agenda = agenda;
        this.votingOption = votingOption;
        this.options = options;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    public VotingOption getVotingOption() {
        return votingOption;
    }

    public void setVotingOption(VotingOption votingOption) {
        this.votingOption = votingOption;
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
            "Ballot{" +
            "id=" +
            id +
            ", member=" +
            member +
            ", meeting=" +
            meeting +
            ", agenda=" +
            agenda +
            ", votingOption=" +
            votingOption +
            ", options=" +
            options +
            '}'
        );
    }
}
