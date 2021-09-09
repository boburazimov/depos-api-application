package uz.depos.app.service.dto;

import java.time.Instant;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import uz.depos.app.domain.Meeting;
import uz.depos.app.domain.enums.MeetingStatusEnum;
import uz.depos.app.domain.enums.MeetingTypeEnum;

/**
 * A DTO representing a meeting for Depository table, with his authorities
 */
public class MeetingByUserDTO {

    private Long id;
    private MeetingStatusEnum status;
    private MeetingTypeEnum typeEnum;
    private Instant startDate;
    private Instant startRegistration;
    private Instant endRegistration;

    @NotNull(message = "Company have must be not empty")
    private Long companyId;

    private Integer cityId;
    private String address;

    @NotBlank(message = "Description must not be null")
    private String description;

    public MeetingByUserDTO() {}

    public MeetingByUserDTO(Meeting meeting) {
        this.id = meeting.getId();
        this.status = meeting.getStatus();
        this.typeEnum = meeting.getTypeEnum();
        this.startDate = meeting.getStartDate();
        this.startRegistration = meeting.getStartRegistration();
        this.endRegistration = meeting.getEndRegistration();
        this.companyId = meeting.getCompany().getId();
        this.cityId = meeting.getCity() != null ? meeting.getCity().getId() : null;
        this.address = meeting.getAddress();
        this.description = meeting.getDescription();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MeetingStatusEnum getStatus() {
        return status;
    }

    public void setStatus(MeetingStatusEnum status) {
        this.status = status;
    }

    public MeetingTypeEnum getTypeEnum() {
        return typeEnum;
    }

    public void setTypeEnum(MeetingTypeEnum typeEnum) {
        this.typeEnum = typeEnum;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getStartRegistration() {
        return startRegistration;
    }

    public void setStartRegistration(Instant startRegistration) {
        this.startRegistration = startRegistration;
    }

    public Instant getEndRegistration() {
        return endRegistration;
    }

    public void setEndRegistration(Instant endRegistration) {
        this.endRegistration = endRegistration;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return (
            "MeetingDTO{" +
            "id=" +
            id +
            ", status=" +
            status +
            ", typeEnum=" +
            typeEnum +
            ", startDate=" +
            startDate +
            ", startRegistration=" +
            startRegistration +
            ", endRegistration=" +
            endRegistration +
            ", companyId=" +
            companyId +
            ", cityId=" +
            cityId +
            ", address='" +
            address +
            '\'' +
            ", description='" +
            description +
            '\'' +
            '}'
        );
    }
}
