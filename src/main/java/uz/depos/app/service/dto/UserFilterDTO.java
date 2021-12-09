package uz.depos.app.service.dto;

import com.fasterxml.jackson.annotation.JsonView;
import uz.depos.app.domain.User;
import uz.depos.app.domain.enums.UserGroupEnum;
import uz.depos.app.service.view.View;

/**
 * A DTO representing a filtered user for Depository table, with his authorities
 */
public class UserFilterDTO {

    @JsonView(value = { View.ModelView.External.class })
    private Long id;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private String email;

    @JsonView(value = { View.ModelView.External.class })
    private boolean activated = true;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private String fullName;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private String pinfl;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private UserGroupEnum groupEnum;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private String phoneNumber;

    public UserFilterDTO() {}

    public UserFilterDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.activated = user.isActivated();
        this.fullName = user.getFullName();
        this.pinfl = user.getPinfl();
        this.groupEnum = user.getGroupEnum();
        this.phoneNumber = user.getPhoneNumber();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public UserGroupEnum getGroupEnum() {
        return groupEnum;
    }

    public void setGroupEnum(UserGroupEnum groupEnum) {
        this.groupEnum = groupEnum;
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
            "UserFilterDTO{" +
            "id=" +
            id +
            ", email='" +
            email +
            '\'' +
            ", activated=" +
            activated +
            ", fullName='" +
            fullName +
            '\'' +
            ", pinfl='" +
            pinfl +
            '\'' +
            ", groupEnum=" +
            groupEnum +
            ", phoneNumber='" +
            phoneNumber +
            '\'' +
            '}'
        );
    }
}
