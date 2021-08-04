package uz.depos.app.domain;

import javax.persistence.*;
import lombok.EqualsAndHashCode;

/**
 * Компания по которой проводится заседание
 */

@EqualsAndHashCode(callSuper = true)
@Entity
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

    public Company() {}

    public Company(
        Long id,
        Boolean isActive,
        String name,
        String inn,
        String legalAddress,
        String email,
        String description,
        String mailingAddress,
        String webPage,
        String phoneNumber,
        Attachment logo,
        User chairman,
        User secretary,
        String extraInfo
    ) {
        this.id = id;
        this.isActive = isActive;
        this.name = name;
        this.inn = inn;
        this.legalAddress = legalAddress;
        this.email = email;
        this.description = description;
        this.mailingAddress = mailingAddress;
        this.webPage = webPage;
        this.phoneNumber = phoneNumber;
        this.logo = logo;
        this.chairman = chairman;
        this.secretary = secretary;
        this.extraInfo = extraInfo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getLegalAddress() {
        return legalAddress;
    }

    public void setLegalAddress(String legalAddress) {
        this.legalAddress = legalAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public String getWebPage() {
        return webPage;
    }

    public void setWebPage(String webPage) {
        this.webPage = webPage;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Attachment getLogo() {
        return logo;
    }

    public void setLogo(Attachment logo) {
        this.logo = logo;
    }

    public User getChairman() {
        return chairman;
    }

    public void setChairman(User chairman) {
        this.chairman = chairman;
    }

    public User getSecretary() {
        return secretary;
    }

    public void setSecretary(User secretary) {
        this.secretary = secretary;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    @Override
    public String toString() {
        return (
            "Company{" +
            "id=" +
            id +
            ", isActive=" +
            isActive +
            ", name='" +
            name +
            '\'' +
            ", inn='" +
            inn +
            '\'' +
            ", legalAddress='" +
            legalAddress +
            '\'' +
            ", email='" +
            email +
            '\'' +
            ", description='" +
            description +
            '\'' +
            ", mailingAddress='" +
            mailingAddress +
            '\'' +
            ", webPage='" +
            webPage +
            '\'' +
            ", phoneNumber='" +
            phoneNumber +
            '\'' +
            ", logo=" +
            logo +
            ", chairman=" +
            chairman +
            ", secretary=" +
            secretary +
            ", extraInfo='" +
            extraInfo +
            '\'' +
            '}'
        );
    }
}
