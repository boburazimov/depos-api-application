package uz.depos.app.service.dto;

import com.fasterxml.jackson.annotation.JsonView;
import java.time.Instant;
import uz.depos.app.domain.City;
import uz.depos.app.domain.Meeting;
import uz.depos.app.domain.enums.MeetingStatusEnum;
import uz.depos.app.domain.enums.MeetingTypeEnum;
import uz.depos.app.service.view.View;

/**
 * A DTO representing a meeting filter for Depository table, with his authorities
 */
public class MeetingFilterDTO {

    @JsonView(value = { View.ModelView.External.class })
    private Long id;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private Long companyId;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private MeetingStatusEnum status;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private MeetingTypeEnum typeEnum;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private Instant startDate;

    @JsonView(value = { View.ModelView.External.class })
    private String companyName;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private Integer cityId;

    @JsonView(value = { View.ModelView.External.class })
    private City city;

    public MeetingFilterDTO() {}

    public MeetingFilterDTO(Meeting meeting) {
        this.id = meeting.getId();
        this.status = meeting.getStatus();
        this.typeEnum = meeting.getTypeEnum();
        this.startDate = meeting.getStartDate();
        this.companyId = meeting.getCompany().getId();
        this.companyName = meeting.getCompany().getName();
        this.cityId = meeting.getCity() != null ? meeting.getCity().getId() : null;
        this.city = meeting.getCity() != null ? meeting.getCity() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return (
            "MeetingFilterDTO{" +
            "id=" +
            id +
            ", companyId=" +
            companyId +
            ", status=" +
            status +
            ", typeEnum=" +
            typeEnum +
            ", startDate=" +
            startDate +
            ", companyName='" +
            companyName +
            '\'' +
            ", cityId=" +
            cityId +
            ", city=" +
            city +
            '}'
        );
    }
}
