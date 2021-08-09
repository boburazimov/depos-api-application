package uz.depos.app.service.dto;

import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.validation.constraints.*;
import uz.depos.app.config.Constants;
import uz.depos.app.domain.Authority;
import uz.depos.app.domain.User;
import uz.depos.app.domain.enums.UserAuthTypeEnum;
import uz.depos.app.domain.enums.UserGroupEnum;

/**
 * A DTO representing a user for Depository table, with his authorities
 */

public class DeposUserDTO {

    private Long id;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @NotNull
    private String login;

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    private boolean activated;

    private Set<String> authorities;

    @Size(max = 50)
    @Column(length = 50)
    private String fullName;

    private String passport;

    private String pinfl;

    private UserGroupEnum groupEnum;

    private UserAuthTypeEnum authTypeEnum;

    private String country;

    private boolean isUzb;

    private Integer inn;

    private String phoneNumber;

    public DeposUserDTO() {}

    public DeposUserDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.activated = user.isActivated();
        this.authorities = user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet());
        this.fullName = user.getFullName();
        this.passport = user.getPassport();
        this.pinfl = user.getPinfl();
        this.groupEnum = user.getGroupEnum();
        this.authTypeEnum = user.getAuthTypeEnum();
        this.country = user.getCountry();
        this.isUzb = user.getUzb();
        this.inn = user.getInn();
        this.phoneNumber = user.getPhoneNumber();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public static int getPasswordMinLength() {
        return PASSWORD_MIN_LENGTH;
    }

    public static int getPasswordMaxLength() {
        return PASSWORD_MAX_LENGTH;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
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
            "login='" +
            login +
            '\'' +
            ", password='" +
            password +
            '\'' +
            ", email='" +
            email +
            '\'' +
            ", activated=" +
            activated +
            ", authorities=" +
            authorities +
            ", fullName='" +
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
