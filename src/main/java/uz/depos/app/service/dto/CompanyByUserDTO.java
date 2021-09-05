package uz.depos.app.service.dto;

import javax.validation.constraints.NotBlank;
import org.apache.commons.lang3.ObjectUtils;
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
            '}'
        );
    }
}
