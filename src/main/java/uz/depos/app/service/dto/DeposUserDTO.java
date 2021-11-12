package uz.depos.app.service.dto;

import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.Length;
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

    @NotBlank(message = "Login must not be null and whitespace character")
    @Size(min = 1, max = 50)
    private String login;

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 16;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    private boolean activated = true;

    private Set<String> authorities;

    @Size(max = 50)
    @Column(length = 50)
    @NotBlank(message = "FullName must not be null and whitespace character")
    private String fullName;

    private String passport;

    @NotBlank(message = "PINFL must not be null and whitespace character!")
    @Length(min = 14, max = 14, message = "PINFL Length must be 14 characters!")
    @Column(unique = true)
    private String pinfl;

    private UserGroupEnum groupEnum;

    private UserAuthTypeEnum authTypeEnum;

    private boolean isResident = true;

    @Length(min = 9, max = 9, message = "INN Length must be 9 characters!")
    private String inn;

    private String phoneNumber;

    public DeposUserDTO() {}

    public DeposUserDTO(String password) {
        this.password = password;
    }

    public DeposUserDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.password = null;
        this.email = user.getEmail();
        this.activated = user.isActivated();
        this.authorities = user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet());
        this.fullName = user.getFullName();
        this.passport = user.getPassport();
        this.pinfl = user.getPinfl();
        this.groupEnum = user.getGroupEnum();
        this.authTypeEnum = user.getAuthTypeEnum();
        this.isResident = user.isResident();
        this.inn = user.getInn();
        this.phoneNumber = user.getPhoneNumber();
    }

    public DeposUserDTO(
        Long id,
        String login,
        String password,
        String email,
        boolean activated,
        Set<String> authorities,
        String fullName,
        String passport,
        String pinfl,
        UserGroupEnum groupEnum,
        UserAuthTypeEnum authTypeEnum,
        boolean isResident,
        String inn,
        String phoneNumber
    ) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.email = email;
        this.activated = activated;
        this.authorities = authorities;
        this.fullName = fullName;
        this.passport = passport;
        this.pinfl = pinfl;
        this.groupEnum = groupEnum;
        this.authTypeEnum = authTypeEnum;
        this.isResident = isResident;
        this.inn = inn;
        this.phoneNumber = phoneNumber;
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

    public boolean isResident() {
        return isResident;
    }

    public void setResident(boolean resident) {
        isResident = resident;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
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
            "id=" +
            id +
            ", login='" +
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
            ", isResident=" +
            isResident +
            ", inn='" +
            inn +
            '\'' +
            ", phoneNumber='" +
            phoneNumber +
            '\'' +
            '}'
        );
    }
}
