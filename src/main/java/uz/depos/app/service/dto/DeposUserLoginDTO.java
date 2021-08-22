package uz.depos.app.service.dto;

import javax.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

/**
 * A DTO representing a depos-user-login for Depository table
 */
public class DeposUserLoginDTO {

    @NotNull(message = "INN must not be null and 0 for generate new login!")
    @Min(value = 100000000, message = "This field can't be less then 9 characters!")
    @Max(value = 999999999, message = "This field can't be more then 9 characters!")
    private Integer inn;

    public DeposUserLoginDTO() {}

    public DeposUserLoginDTO(Integer inn) {
        this.inn = inn;
    }

    public Integer getInn() {
        return inn;
    }

    public void setInn(Integer inn) {
        this.inn = inn;
    }
}
