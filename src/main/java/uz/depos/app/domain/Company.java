package uz.depos.app.domain;

import javax.persistence.*;
import lombok.*;

/**
 * Компания по которой проводится заседание
 */

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Company extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    // Статус компании
    @Column(name = "is_active")
    private Boolean isActive;

    //  Наименование компании
    @Column(unique = true, nullable = false, length = 64)
    private String name;

    // ИНН Компании
    @Column(nullable = false, length = 9, unique = true)
    private String inn;

    // Юридический адрес
    @Column(length = 264, nullable = false, name = "legal_address")
    private String legalAddress;

    // Электронный адрес
    @Column(length = 64, unique = true, nullable = false)
    private String email;

    // Описание компании
    @Column(length = 128, nullable = false)
    private String description;

    // Почтовый адрес
    @Column(length = 64, nullable = false, name = "mailing_address")
    private String mailingAddress;

    // Веб сайт
    @Column(length = 64, nullable = false, name = "web_page")
    private String webPage;

    // Телефон номер
    @Column(unique = true, nullable = false, length = 13, name = "phone_number")
    private String phoneNumber;

    // Логотип
    @OneToOne
    private Attachment logo;

    // Председатель наб. совета
    @ManyToOne
    private User chairman;

    // Секретарь наб. совета
    @ManyToOne
    private User secretary;

    // Доп инфо
    @Column(length = 128, name = "extra_info")
    private String extraInfo;
}
