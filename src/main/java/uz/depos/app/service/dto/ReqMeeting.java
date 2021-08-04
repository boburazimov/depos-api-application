package uz.depos.app.service.dto;

import java.sql.Timestamp;
import java.util.List;
import uz.depos.app.domain.enums.MeetingStatusEnum;

public class ReqMeeting {

    private Long id;

    private MeetingStatusEnum status;

    private Timestamp startDate;

    private Timestamp startRegistration;

    private Timestamp endRegistration;

    private Long companyId;

    private Integer cityId;

    private String address;

    private String description;

    private List<Long> attachmentsId;

    private String extraInfo;

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

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getStartRegistration() {
        return startRegistration;
    }

    public void setStartRegistration(Timestamp startRegistration) {
        this.startRegistration = startRegistration;
    }

    public Timestamp getEndRegistration() {
        return endRegistration;
    }

    public void setEndRegistration(Timestamp endRegistration) {
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

    public List<Long> getAttachmentsId() {
        return attachmentsId;
    }

    public void setAttachmentsId(List<Long> attachmentsId) {
        this.attachmentsId = attachmentsId;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public ReqMeeting() {}

    public ReqMeeting(
        Long id,
        MeetingStatusEnum status,
        Timestamp startDate,
        Timestamp startRegistration,
        Timestamp endRegistration,
        Long companyId,
        Integer cityId,
        String address,
        String description,
        List<Long> attachmentsId,
        String extraInfo
    ) {
        this.id = id;
        this.status = status;
        this.startDate = startDate;
        this.startRegistration = startRegistration;
        this.endRegistration = endRegistration;
        this.companyId = companyId;
        this.cityId = cityId;
        this.address = address;
        this.description = description;
        this.attachmentsId = attachmentsId;
        this.extraInfo = extraInfo;
    }

    @Override
    public String toString() {
        return (
            "ReqMeeting{" +
            "id=" +
            id +
            ", status=" +
            status +
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
            ", attachmentsId=" +
            attachmentsId +
            ", extraInfo='" +
            extraInfo +
            '\'' +
            '}'
        );
    }
}
