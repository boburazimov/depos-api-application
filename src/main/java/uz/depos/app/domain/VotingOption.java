package uz.depos.app.domain;

import javax.persistence.*;
import lombok.*;

/**
 * Варианты решения для голосования
 */

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VotingOption extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    // Привязка к заседанию "meetingID"
    @ManyToOne(optional = false)
    private Meeting meeting;

    // Привязка к повестки дня "agendaID"
    @ManyToOne(optional = false)
    private Agenda agenda;

    // Наименование варианта
    @Column(nullable = false, name = "voting_text")
    private String votingText;

    // Доп инфо
    @Column(length = 128, name = "extra_info")
    private String extraInfo;
}
