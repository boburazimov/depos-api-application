package uz.depos.app.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
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
@JsonIgnoreProperties(value = { "langKey" })
public class DeposUserDTO {

    private Long id;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @NotNull
    @ApiModelProperty(example = "UZ-123456789", required = true, dataType = "java.lang.String")
    private String login;

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    @ApiModelProperty(example = "password2021", required = true)
    private String password;

    @Email
    @Size(min = 5, max = 254)
    @ApiModelProperty(example = "1@mail.ru", required = true)
    private String email;

    @ApiModelProperty(example = "false")
    private boolean activated;

    @ApiModelProperty(example = "USER")
    private Set<String> authorities;

    @Size(max = 50)
    @Column(length = 50)
    @ApiModelProperty(example = "Бахромов Тохир Асманович", required = true)
    private String fullName;

    @ApiModelProperty(example = "AB1234567")
    private String passport;

    @ApiModelProperty(example = "12345678912345")
    private String pinfl;

    @ApiModelProperty(example = "ENTITY")
    private UserGroupEnum groupEnum;

    @ApiModelProperty(example = "INNPASS")
    private UserAuthTypeEnum authTypeEnum;

    @ApiModelProperty(example = "UZB")
    private String country;

    @ApiModelProperty(example = "true")
    private Boolean isUzb;

    @ApiModelProperty(example = "123456787")
    private Integer inn;

    @ApiModelProperty(example = "+998977777777")
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
