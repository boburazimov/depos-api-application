package uz.depos.app.service.dto;

import uz.depos.app.domain.User;

/**
 * A DTO representing a user for Depository table, with his authorities
 */
public class DeposUserNameDTO {

    private Long id;
    private String fullName;
    private String pinfl;

    public DeposUserNameDTO() {}

    public DeposUserNameDTO(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.pinfl = user.getPinfl();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPinfl() {
        return pinfl;
    }

    public void setPinfl(String pinfl) {
        this.pinfl = pinfl;
    }

    @Override
    public String toString() {
        return "DeposUserNameDTO{" + "id=" + id + ", fullName='" + fullName + '\'' + ", pinfl='" + pinfl + '\'' + '}';
    }
}
