package uz.depos.app.domain;

import java.util.Objects;
import javax.persistence.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 * Варианты решения для голосования
 */
@Entity
//@SQLDelete(sql = "UPDATE project SET deleted=true WHERE id=?")
//@Where(clause = "deleted=false")
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
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        VotingOption that = (VotingOption) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
