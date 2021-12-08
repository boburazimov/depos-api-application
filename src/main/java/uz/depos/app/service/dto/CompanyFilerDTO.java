package uz.depos.app.service.dto;

import com.fasterxml.jackson.annotation.JsonView;
import javax.validation.constraints.NotBlank;
import org.apache.commons.lang3.ObjectUtils;
import uz.depos.app.domain.Company;
import uz.depos.app.service.view.View;

/**
 * A DTO representing for a filter company table, with his authorities
 */
public class CompanyFilerDTO {

    @JsonView(value = { View.ModelView.External.class, View.ModelView.PUT.class })
    private Long id;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private String name;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.PUT.class })
    private Boolean isActive = false;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private String inn;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private String email;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private String webPage;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private String phoneNumber;

    public CompanyFilerDTO() {}

    public CompanyFilerDTO(Company company) {
        this.id = company.getId();
        this.isActive = company.getActive();
        this.name = company.getName();
        this.inn = company.getInn();
        this.email = company.getEmail();
        this.webPage = company.getWebPage();
        this.phoneNumber = company.getPhoneNumber();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebPage() {
        return webPage;
    }

    public void setWebPage(String webPage) {
        this.webPage = webPage;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
