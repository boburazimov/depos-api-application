package uz.depos.app.service.dto;

import io.swagger.annotations.*;
import javax.validation.constraints.NotNull;
import uz.depos.app.domain.Company;

@ApiModel(value = "CompanyDTO")
public class CompanyDTO {

    private Long id;

    private Boolean isActive;

    @NotNull
    @ApiModelProperty(example = "DEPOS OOO", required = true)
    private String name;

    @ApiModelProperty(example = "123456789", required = true)
    private String inn;

    @ApiModelProperty(example = "Тошкент ш. Чилонзор т. Нурали к. 42д.", required = true)
    private String legalAddress;

    private String email;

    private String description;

    private String mailingAddress;

    private String webPage;

    private String phoneNumber;

    private Long logoId;

    private Long chairmanId;

    private Long secretaryId;

    private String extraInfo;

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

    public Long getLogoId() {
        return logoId;
    }

    public void setLogoId(Long logoId) {
        this.logoId = logoId;
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

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public CompanyDTO() {}

    public CompanyDTO(Company company) {
        this.id = company.getId();
        this.isActive = company.getActive();
        this.name = company.getName();
        this.inn = company.getInn();
        this.legalAddress = company.getLegalAddress();
        this.email = company.getEmail();
        this.description = company.getDescription();
        this.mailingAddress = company.getMailingAddress();
        this.webPage = company.getWebPage();
        this.phoneNumber = company.getPhoneNumber();
        this.logoId = company.getLogo().getId();
        this.chairmanId = company.getChairman().getId();
        this.secretaryId = company.getSecretary().getId();
        this.extraInfo = company.getExtraInfo();
    }

    @Override
    public String toString() {
        return (
            "ReqCompany{" +
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
            ", logoId=" +
            logoId +
            ", chairmanId=" +
            chairmanId +
            ", secretaryId=" +
            secretaryId +
            ", extraInfo='" +
            extraInfo +
            '\'' +
            '}'
        );
    }
}
