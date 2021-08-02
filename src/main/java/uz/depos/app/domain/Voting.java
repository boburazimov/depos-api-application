package uz.depos.app.domain;

import javax.persistence.*;
import lombok.*;
import uz.depos.app.domain.enums.VotingTypeEnum;

/**
 * Голосование - процесс голосование (каждая строка - один голос)
 */

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Voting extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    // Привязка к участнику заседания "userID"
    @ManyToOne(optional = false)
    private Member member;

    // Привязка к заседанию "meetingID"
    @ManyToOne(optional = false)
    private Meeting meeting;

    // Привязка к повестки дня "agendaID"
    @ManyToOne(optional = false)
    private Agenda agenda;

    // Привязка к варианту решения "optionsID"
    @ManyToOne(optional = false)
    private VotingOption option;

    // Привязка к варианту ответа (За, Против, Воздержался)
    @Enumerated(EnumType.STRING)
    private VotingTypeEnum typeEnum;

    // Доп инфо
    @Column(length = 128, name = "extra_info")
    private String extraInfo;
}
