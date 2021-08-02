package uz.depos.app.domain;

import java.sql.Timestamp;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import uz.depos.app.domain.enums.MeetingStatusEnum;

/**
 * Заседание (Собрание) - Наблюдательного совета
 **/

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Meeting extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    // Статус заседания
    @Enumerated(value = EnumType.STRING)
    private MeetingStatusEnum status;

    // Дата и время проведения заседания
    @Column(nullable = false, name = "start_date")
    private Timestamp startDate;

    // Начало регистрации
    @Column(nullable = false, name = "start_registration")
    private Timestamp startRegistration;

    // Окончание регистрации
    @Column(nullable = false, name = "end_registration")
    private Timestamp endRegistration;

    // Привязка к компанию / организации
    @ManyToOne(optional = false)
    private Company company;

    // Привязка к Город/область
    @ManyToOne(optional = false)
    private City city;

    // Адрес - место проведения
    @Column(nullable = false)
    private String address;

    // Описание заседания / Повестка дня всего заседания
    @Column(length = 256)
    private String description;

    // Список наблюдателей в заседании
    @OneToMany(mappedBy = "meeting")
    private List<Member> members;

    // Прикрепляемы файлы для всего заседания
    @OneToMany(cascade = CascadeType.ALL)
    private List<Attachment> attachments;

    // Привязка к повесткам дня
    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
    private List<Agenda> agendas;

    // Доп инфо
    @Column(length = 128, name = "extra_info")
    private String extraInfo;
}
