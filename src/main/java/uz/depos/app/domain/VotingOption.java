package uz.depos.app.domain;

import javax.persistence.*;
import lombok.*;

/**
 * Варианты решения для голосования
 */
@EqualsAndHashCode(callSuper = true)
@Entity
public class VotingOption extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    // Наименование варианта
    @Column(nullable = false, name = "voting_text")
    private String votingText;

    // Привязка к заседанию "meetingID"
    @ManyToOne(optional = false)
    private Meeting meeting;

    // Привязка к повестки дня "agendaID"
    @ManyToOne(optional = false)
    private Agenda agenda;

    public VotingOption() {}

    public VotingOption(Long id, String votingText, Meeting meeting, Agenda agenda) {
        this.id = id;
        this.votingText = votingText;
        this.meeting = meeting;
        this.agenda = agenda;
    }

    public VotingOption(String votingText, Meeting meeting, Agenda agenda) {
        this.votingText = votingText;
        this.meeting = meeting;
        this.agenda = agenda;
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

    @Override
    public String toString() {
        return "VotingOption{" + "id=" + id + ", votingText='" + votingText + '\'' + ", meeting=" + meeting + ", agenda=" + agenda + '}';
    }
}
