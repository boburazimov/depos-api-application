package uz.depos.app.domain;

import javax.persistence.*;
import lombok.*;

/**
 * Участники заседание (отдельно от users -> могут быть приглашенные) TODO - новый файл от Ёркинжон для реестр
 **/

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
    uniqueConstraints = { // Уникальность в совокупности 3 полей
        @UniqueConstraint(columnNames = { "code", "meeting_id", "user_id" }),
    }
)
public class Member extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    // Внешний уникальный код участника в рамках одного заседания
    @Column(length = 4, nullable = false)
    private String code;

    // Заседание
    @ManyToOne(optional = false)
    private Meeting meeting;

    // Привязка пользователя к участнику заседание
    @OneToOne
    private User user;

    // Тип участье в заседании добавленных пользователей
    @Column(name = "is_remotely")
    private Boolean isRemotely;

    // Ознакомлен с правилами
    @Column(name = "is_confirmed")
    private Boolean isConfirmed;

    // Находится ли участник в заседании или покинул его (сигнал)
    @Column(name = "is_involved")
    private Boolean isInvolved;

    // Доп инфо
    @Column(length = 128, name = "extra_info")
    private String extraInfo;
}
