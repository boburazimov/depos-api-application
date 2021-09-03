package uz.depos.app.service.mapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import uz.depos.app.domain.City;
import uz.depos.app.domain.Company;
import uz.depos.app.service.dto.CityDTO;
import uz.depos.app.service.dto.CompanyDTO;
import uz.depos.app.service.dto.CompanyNameDTO;

/**
 * Mapper for the entity {@link Company} and its DTO called {@link CompanyDTO}.
 * <p>
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class CompanyMapper {

    public CompanyDTO companyToCompanyDTO(Company company) {
        return new CompanyDTO(company);
    }

    public List<CompanyDTO> companiesToCompanyDTOs(List<Company> companies) {
        return companies.stream().filter(Objects::nonNull).map(this::companyToCompanyDTO).collect(Collectors.toList());
    }

    public CompanyNameDTO companyToCompanyNameDTO(Company company) {
        return new CompanyNameDTO(company);
    }

    public List<CompanyNameDTO> companiesToCompanyNameDTOs(List<Company> companies) {
        return companies.stream().filter(Objects::nonNull).map(this::companyToCompanyNameDTO).collect(Collectors.toList());
    }

    public CityDTO cityToCityDTO(City city) {
        return new CityDTO(city);
    }
}
