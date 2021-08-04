package uz.depos.app.service.dto;

import uz.depos.app.domain.enums.UserAuthTypeEnum;
import uz.depos.app.domain.enums.UserStatusEnum;

public class ReqRegister {

    private String fullName;

    private String passport;

    private String pinfl;

    private String email;

    private Integer groupId;

    private UserAuthTypeEnum authTypeEnum;

    private String country;

    private Integer inn;

    private String username;

    private String password;

    private String prePassword;

    private String phoneNumber;

    private UserStatusEnum status;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
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

    public Integer getInn() {
        return inn;
    }

    public void setInn(Integer inn) {
        this.inn = inn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrePassword() {
        return prePassword;
    }

    public void setPrePassword(String prePassword) {
        this.prePassword = prePassword;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserStatusEnum getStatus() {
        return status;
    }

    public void setStatus(UserStatusEnum status) {
        this.status = status;
    }

    public ReqRegister() {}

    public ReqRegister(
        String fullName,
        String passport,
        String pinfl,
        String email,
        Integer groupId,
        UserAuthTypeEnum authTypeEnum,
        String country,
        Integer inn,
        String username,
        String password,
        String prePassword,
        String phoneNumber,
        UserStatusEnum status
    ) {
        this.fullName = fullName;
        this.passport = passport;
        this.pinfl = pinfl;
        this.email = email;
        this.groupId = groupId;
        this.authTypeEnum = authTypeEnum;
        this.country = country;
        this.inn = inn;
        this.username = username;
        this.password = password;
        this.prePassword = prePassword;
        this.phoneNumber = phoneNumber;
        this.status = status;
    }

    @Override
    public String toString() {
        return (
            "ReqRegister{" +
            "fullName='" +
            fullName +
            '\'' +
            ", passport='" +
            passport +
            '\'' +
            ", pinfl='" +
            pinfl +
            '\'' +
            ", email='" +
            email +
            '\'' +
            ", groupId=" +
            groupId +
            ", authTypeEnum=" +
            authTypeEnum +
            ", country='" +
            country +
            '\'' +
            ", inn=" +
            inn +
            ", username='" +
            username +
            '\'' +
            ", password='" +
            password +
            '\'' +
            ", prePassword='" +
            prePassword +
            '\'' +
            ", phoneNumber='" +
            phoneNumber +
            '\'' +
            ", status=" +
            status +
            '}'
        );
    }
}
