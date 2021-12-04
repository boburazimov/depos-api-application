package uz.depos.app.service;

import io.undertow.util.BadRequestException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import uz.depos.app.domain.Company;
import uz.depos.app.domain.enums.CompanySearchFieldEnum;
import uz.depos.app.service.dto.ApiResponse;
import uz.depos.app.service.dto.CompanyByUserDTO;
import uz.depos.app.service.dto.CompanyDTO;
import uz.depos.app.service.dto.CompanyNameDTO;

public interface CompanyService {
    CompanyDTO createCompany(CompanyDTO companyDTO);

    Optional<CompanyDTO> updateCompany(CompanyDTO companyDTO);

    ApiResponse deleteCompany(Long id) throws BadRequestException;

    @Transactional(readOnly = true)
    Optional<Company> getCompanyById(Long id);

    Page<CompanyDTO> getAllManagedCompanies(Pageable pageable);

    void deleteCompanyLogo(Long companyId);

    @Transactional(readOnly = true)
    List<CompanyNameDTO> searchCompanyByName(String name);

    Page<CompanyDTO> filterCompany(CompanySearchFieldEnum field, String text, Pageable pageable);

    @Transactional(readOnly = true)
    Page<CompanyDTO> getCompanyListByFilterCompany(CompanyDTO companyDTO, Pageable pageable);

    @Transactional(readOnly = true)
    Page<CompanyDTO> getAllCompaniesByChairman(Long userId, Pageable pageable);

    @Transactional(readOnly = true)
    Page<CompanyDTO> getAllCompaniesBySecretary(Long userId, Pageable pageable);

    @Transactional(readOnly = true)
    List<CompanyByUserDTO> getAllCompaniesByChairmanAndSecretary(Long userId);

    @Transactional(readOnly = true)
    List<CompanyByUserDTO> getCompaniesByUser(Long id);
}
