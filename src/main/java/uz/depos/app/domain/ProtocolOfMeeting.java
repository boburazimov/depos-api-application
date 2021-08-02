package uz.depos.app.domain;

import javax.persistence.*;
import lombok.*;

/**
 * Протокол собрание - заключительный документ (минуты встречи)
 * TODO - нужно дополнить таблицу уточнив список полей
 **/

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProtocolOfMeeting extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    // Заседание по которому составляется данный протокол "meetingID"
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Meeting meeting;

    // Доп инфо
    @Column(length = 128, name = "extra_info")
    private String extraInfo;
}
