package uz.depos.app.service.dto;

import com.fasterxml.jackson.annotation.JsonView;
import javax.validation.constraints.NotBlank;
import org.apache.commons.lang3.ObjectUtils;
import uz.depos.app.domain.Company;
import uz.depos.app.service.view.View;

/**
 * A DTO representing for a filter company table, with his authorities
 */
public class CompanyFilerDTO {

    @JsonView(value = { View.ModelView.External.class, View.ModelView.PUT.class })
    private Long id;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.PUT.class })
    private Boolean isActive = false;

    @NotBlank(message = "Name must not be null!")
    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private String name;

    @NotBlank(message = "INN must not be null!")
    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private String inn;

    @NotBlank(message = "Legal address must not be null!")
    @JsonView(value = { View.ModelView.External.class, View.ModelView.PUT.class })
    private String legalAddress;

    @NotBlank(message = "Email must not be null!")
    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private String email;

    @NotBlank(message = "Description must not be null!")
    @JsonView(value = { View.ModelView.External.class, View.ModelView.PUT.class })
    private String description;

    @NotBlank(message = "Postal address must not be null!")
    @JsonView(value = { View.ModelView.External.class, View.ModelView.PUT.class })
    private String postalAddress;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private String webPage;

    @NotBlank(message = "Phone-number must not be null!")
    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private String phoneNumber;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.PUT.class })
    private String imageUrl;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.PUT.class })
    private Long chairmanId;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.PUT.class })
    private Long secretaryId;

    public CompanyFilerDTO() {}

    public CompanyFilerDTO(Company company) {
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
