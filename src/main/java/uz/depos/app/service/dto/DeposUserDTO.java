package uz.depos.app.service.dto;

import javax.persistence.Column;
import javax.validation.constraints.Size;
import uz.depos.app.domain.enums.UserAuthTypeEnum;
import uz.depos.app.domain.enums.UserGroupEnum;
import uz.depos.app.web.rest.vm.ManagedUserVM;

/**
 * A DTO representing a user for Depository table, with his authorities
 */

public class DeposUserDTO extends ManagedUserVM {

    @Size(max = 50)
    @Column(length = 50)
    private String fullName;

    private String passport;

    private String pinfl;

    private UserGroupEnum groupEnum;

    private UserAuthTypeEnum authTypeEnum;

    private String country;

    private Boolean isUzb;

    private Integer inn;

    private String phoneNumber;

    public DeposUserDTO() {}

    public DeposUserDTO(
        String fullName,
        String passport,
        String pinfl,
        UserGroupEnum groupEnum,
        UserAuthTypeEnum authTypeEnum,
        String country,
        Boolean isUzb,
        Integer inn,
        String phoneNumber
    ) {
        this.fullName = fullName;
        this.passport = passport;
        this.pinfl = pinfl;
        this.groupEnum = groupEnum;
        this.authTypeEnum = authTypeEnum;
        this.country = country;
        this.isUzb = isUzb;
        this.inn = inn;
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getPinfl() {
        return pinfl;
    }

    public void setPinfl(String pinfl) {
        this.pinfl = pinfl;
    }

    public UserGroupEnum getGroupEnum() {
        return groupEnum;
    }

    public void setGroupEnum(UserGroupEnum groupEnum) {
        this.groupEnum = groupEnum;
    }

    public UserAuthTypeEnum getAuthTypeEnum() {
        return authTypeEnum;
    }

    public void setAuthTypeEnum(UserAuthTypeEnum authTypeEnum) {
        this.authTypeEnum = authTypeEnum;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Boolean getUzb() {
        return isUzb;
    }

    public void setUzb(Boolean uzb) {
        isUzb = uzb;
    }

    public Integer getInn() {
        return inn;
    }

    public void setInn(Integer inn) {
        this.inn = inn;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return (
            "DeposUserDTO{" +
            "fullName='" +
            fullName +
            '\'' +
            ", passport='" +
            passport +
            '\'' +
            ", pinfl='" +
            pinfl +
            '\'' +
            ", groupEnum=" +
            groupEnum +
            ", authTypeEnum=" +
            authTypeEnum +
            ", country='" +
            country +
            '\'' +
            ", isUzb=" +
            isUzb +
            ", inn=" +
            inn +
            ", phoneNumber='" +
            phoneNumber +
            '\'' +
            '}'
        );
    }
}
