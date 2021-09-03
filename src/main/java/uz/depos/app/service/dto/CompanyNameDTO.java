package uz.depos.app.service.dto;

import uz.depos.app.domain.Company;

/**
 * A DTO representing a company table, with his authorities
 */
public class CompanyNameDTO {

    private Long id;

    private String name;

    public CompanyNameDTO() {}

    public CompanyNameDTO(Company company) {
        this.id = company.getId();
        this.name = company.getName();
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

    @Override
    public String toString() {
        return "CompanyNameListDTO{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
