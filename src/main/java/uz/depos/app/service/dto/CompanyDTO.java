package uz.depos.app.service.dto;

import javax.validation.constraints.NotBlank;
import org.apache.commons.lang3.ObjectUtils;
import uz.depos.app.domain.Company;
import uz.depos.app.domain.User;

/**
 * A DTO representing a company table, with his authorities
 */
public class CompanyDTO {

    private Long id;

    private Boolean isActive = false;

    @NotBlank(message = "Name must not be null!")
    private String name;

    @NotBlank(message = "INN must not be null!")
    private String inn;

    @NotBlank(message = "Legal address must not be null!")
    private String legalAddress;

    @NotBlank(message = "Email must not be null!")
    private String email;

    @NotBlank(message = "Description must not be null!")
    private String description;

    @NotBlank(message = "Postal address must not be null!")
    private String postalAddress;

    private String webPage;

    @NotBlank(message = "Phone-number must not be null!")
    private String phoneNumber;

    private String imageUrl;

    private Long chairmanId;

    private User chairman;

    private Long secretaryId;

    private User secretary;

    public CompanyDTO() {}

    public CompanyDTO(Company company) {
        this.id = company.getId();
        this.isActive = company.getActive();
        this.name = company.getName();
        this.inn = company.getInn();
        this.legalAddress = company.getLegalAddress();
        this.email = company.getEmail();
        this.description = company.getDescription();
        this.postalAddress = company.getPostalAddress();
        this.webPage = company.getWebPage();
        this.phoneNumber = company.getPhoneNumber();
        this.imageUrl = company.getImageUrl();
        this.chairmanId = ObjectUtils.isNotEmpty(company.getChairman()) ? company.getChairman().getId() : null;
        this.chairman = company.getChairman();
        this.secretary = company.getSecretary();
        this.secretaryId = ObjectUtils.isNotEmpty(company.getSecretary()) ? company.getSecretary().getId() : null;
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

    public Long getChairmanId() {
        return chairmanId;
    }

    public void setChairmanId(Long chairmanId) {
        this.chairmanId = chairmanId;
    }

    public Long getSecretaryId() {
        return secretaryId;
    }

    public void setSecretaryId(Long secretaryId) {
        this.secretaryId = secretaryId;
    }

    @Override
    public String toString() {
        return (
            "CompanyDTO{" +
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
            ", chairmanId=" +
            chairmanId +
            ", secretaryId=" +
            secretaryId +
            '}'
        );
    }
}
