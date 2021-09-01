package uz.depos.app.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.depos.app.domain.Company;
import uz.depos.app.service.dto.ApiResponse;
import uz.depos.app.service.dto.CompanyDTO;

public interface CompanyService {
    CompanyDTO createCompany(CompanyDTO companyDTO);

    Optional<CompanyDTO> updateCompany(CompanyDTO companyDTO);

    ApiResponse deleteCompany(Long id);

    Optional<Company> getCompanyById(Long id);

    Page<CompanyDTO> getAllManagedCompanies(Pageable pageable);

    void deleteCompanyLogo(Long companyId);
}
