package uz.depos.app.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @Column(length = 64, nullable = false, name = "postal_address")
    private String postalAddress;

    // Веб сайт
    @Column(length = 64, name = "web_page")
    private String webPage;

    // Телефон номер
    @Column(unique = true, nullable = false, length = 13, name = "phone_number")
    private String phoneNumber;

    // Логотип
    @Size(max = 256)
    @Column(name = "image_url", length = 256)
    private String imageUrl;

    // Председатель наб. совета
    @ManyToOne
    private User chairman;

    // Секретарь наб. совета
    @ManyToOne
    private User secretary;

    public Company() {}

    public Company(
        Long id,
        Boolean isActive,
        String name,
        String inn,
        String legalAddress,
        String email,
        String description,
        String postalAddress,
        String webPage,
        String phoneNumber,
        String imageUrl,
        User chairman,
        User secretary
    ) {
        this.id = id;
        this.isActive = isActive;
        this.name = name;
        this.inn = inn;
        this.legalAddress = legalAddress;
        this.email = email;
        this.description = description;
        this.postalAddress = postalAddress;
        this.webPage = webPage;
        this.phoneNumber = phoneNumber;
        this.imageUrl = imageUrl;
        this.chairman = chairman;
        this.secretary = secretary;
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

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
            ", postalAddress='" +
            postalAddress +
            '\'' +
            ", webPage='" +
            webPage +
            '\'' +
            ", phoneNumber='" +
            phoneNumber +
            '\'' +
            ", imageUrl='" +
            imageUrl +
            '\'' +
            ", chairman=" +
            chairman +
            ", secretary=" +
            secretary +
            '}'
        );
    }
}
