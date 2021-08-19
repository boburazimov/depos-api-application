package uz.depos.app.service.mapper;

import org.springframework.stereotype.Service;
import uz.depos.app.domain.City;
import uz.depos.app.domain.Company;
import uz.depos.app.service.dto.CityDTO;
import uz.depos.app.service.dto.CompanyDTO;

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

    public CityDTO cityToCityDTO(City city) {
        return new CityDTO(city);
    }
}
