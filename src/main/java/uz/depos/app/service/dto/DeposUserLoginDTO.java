package uz.depos.app.service.dto;

import javax.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

public class DeposUserLoginDTO {

    @NotNull(message = "INN must not be null and 0 for generate new login!")
    @Min(value = 100000000, message = "This field can't be less then 9 characters!")
    @Max(value = 999999999, message = "This field can't be more then 9 characters!")
    private Integer inn;

    private String login;

    @NotNull(message = "Uzb field for generate new login can't be Null!")
    @AssertTrue(message = "Uzb field for generate new login must be True!")
    private boolean isUzb;

    public DeposUserLoginDTO() {}

    public DeposUserLoginDTO(Integer inn, String login, boolean isUzb) {
        this.inn = inn;
        this.login = login;
        this.isUzb = isUzb;
    }

    public Integer getInn() {
        return inn;
    }

    public void setInn(Integer inn) {
        this.inn = inn;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public boolean isUzb() {
        return isUzb;
    }

    public void setUzb(boolean uzb) {
        isUzb = uzb;
    }
}
