package uz.depos.app.service.dto;

import java.util.List;
import uz.depos.app.domain.Company;

/**
 * A DTO representing a company table, with his authorities
 */
public class CompanyByUserDTO {

    private Long id;
    private Boolean isActive;
    private String name;
    private String description;
    private String webPage;
    private String imageUrl;
    private List<MeetingByUserDTO> meetings;
    private long meetingCount;

    public CompanyByUserDTO() {}

    public CompanyByUserDTO(Company company) {
        this.id = company.getId();
        this.isActive = company.getActive();
        this.name = company.getName();
        this.description = company.getDescription();
        this.webPage = company.getWebPage();
        this.imageUrl = company.getImageUrl();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebPage() {
        return webPage;
    }

    public void setWebPage(String webPage) {
        this.webPage = webPage;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<MeetingByUserDTO> getMeetings() {
        return meetings;
    }

    public void setMeetings(List<MeetingByUserDTO> meetings) {
        this.meetings = meetings;
    }

    public long getMeetingCount() {
        return meetingCount;
    }

    public void setMeetingCount(long meetingCount) {
        this.meetingCount = meetingCount;
    }

    @Override
    public String toString() {
        return (
            "CompanyByUserDTO{" +
            "id=" +
            id +
            ", isActive=" +
            isActive +
            ", name='" +
            name +
            '\'' +
            ", description='" +
            description +
            '\'' +
            ", webPage='" +
            webPage +
            '\'' +
            ", imageUrl='" +
            imageUrl +
            '\'' +
            ", meetings=" +
            meetings +
            ", meetingCount=" +
            meetingCount +
            '}'
        );
    }
}
