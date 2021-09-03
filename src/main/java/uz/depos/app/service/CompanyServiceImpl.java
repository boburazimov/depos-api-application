package uz.depos.app.service;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.depos.app.domain.Company;
import uz.depos.app.domain.enums.CompanySearchFieldEnum;
import uz.depos.app.repository.CompanyRepository;
import uz.depos.app.repository.UserRepository;
import uz.depos.app.service.dto.ApiResponse;
import uz.depos.app.service.dto.CompanyDTO;
import uz.depos.app.service.dto.CompanyNameDTO;
import uz.depos.app.service.mapper.CompanyMapper;

/**
 * Service class for managing company.
 */
@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

    private final Logger log = LoggerFactory.getLogger(CompanyServiceImpl.class);

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final CompanyMapper companyMapper;
    private final CacheManager cacheManager;
    private final FilesStorageService filesStorageService;

    public CompanyServiceImpl(
        CompanyRepository companyRepository,
        UserRepository userRepository,
        CompanyMapper companyMapper,
        CacheManager cacheManager,
        FilesStorageService filesStorageService
    ) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.companyMapper = companyMapper;
        this.cacheManager = cacheManager;
        this.filesStorageService = filesStorageService;
    }

    /**
     * Create new company, and return it.
     *
     * @param companyDTO company to create.
     * @return created company.
     */
    @Override
    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        Company company = companyDTO.getId() == null || companyDTO.getId() == 0
            ? new Company()
            : companyRepository.findById(companyDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("getCompany"));
        company.setActive(companyDTO.getActive());
        if (StringUtils.isNoneBlank(companyDTO.getName())) company.setName(companyDTO.getName());
        if (StringUtils.isNoneBlank(companyDTO.getInn())) company.setInn(companyDTO.getInn());
        if (StringUtils.isNoneBlank(companyDTO.getLegalAddress())) company.setLegalAddress(companyDTO.getLegalAddress());
        if (StringUtils.isNoneBlank(companyDTO.getEmail())) company.setEmail(companyDTO.getEmail().toLowerCase());
        if (StringUtils.isNoneBlank(companyDTO.getDescription())) company.setDescription(companyDTO.getDescription());
        if (StringUtils.isNoneBlank(companyDTO.getPostalAddress())) company.setPostalAddress(companyDTO.getPostalAddress());
        if (StringUtils.isNoneBlank(companyDTO.getWebPage())) company.setWebPage(companyDTO.getWebPage());
        if (StringUtils.isNoneBlank(companyDTO.getPhoneNumber())) company.setPhoneNumber(companyDTO.getPhoneNumber());
        if (StringUtils.isNoneBlank(companyDTO.getImageUrl())) company.setImageUrl(companyDTO.getImageUrl());
        if (companyDTO.getChairmanId() != null && companyDTO.getChairmanId() > 0) company.setChairman(
            userRepository.findById(companyDTO.getChairmanId()).orElseThrow(() -> new ResourceNotFoundException("getChairman"))
        );
        if (companyDTO.getSecretaryId() != null && companyDTO.getSecretaryId() > 0) company.setSecretary(
            userRepository.findById(companyDTO.getSecretaryId()).orElseThrow(() -> new ResourceNotFoundException("getSecretary"))
        );
        Company savedCompany = companyRepository.save(company);
        CompanyDTO companyDTO1 = companyMapper.companyToCompanyDTO(savedCompany);
        //        this.clearCompanyCaches(savedCompany);
        log.debug("Created Information for Company: {}", companyDTO1);
        return companyDTO1;
    }

    /**
     * Edit all information for a specific company, and return the modified company.
     *
     * @param companyDTO company to edit.
     * @return edited company.
     */
    @Override
    public Optional<CompanyDTO> updateCompany(CompanyDTO companyDTO) {
        return Optional
            .of(companyRepository.findById(companyDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(
                company -> {
                    // Name
                    if (StringUtils.isNoneBlank(companyDTO.getName())) company.setName(companyDTO.getName().toLowerCase());
                    // Email
                    if (StringUtils.isNoneBlank(companyDTO.getEmail())) company.setEmail(companyDTO.getEmail().toLowerCase());
                    // Active
                    company.setActive(companyDTO.getActive());
                    // INN
                    if (StringUtils.isNoneBlank(companyDTO.getInn())) company.setInn(companyDTO.getInn());
                    // Legal-Address
                    if (StringUtils.isNoneBlank(companyDTO.getLegalAddress())) company.setLegalAddress(companyDTO.getLegalAddress());
                    // Description
                    if (StringUtils.isNoneBlank(companyDTO.getDescription())) company.setDescription(companyDTO.getDescription());
                    // Postal-Address
                    if (StringUtils.isNoneBlank(companyDTO.getPostalAddress())) company.setPostalAddress(companyDTO.getPostalAddress());
                    // Web-Page
                    company.setWebPage(companyDTO.getWebPage());
                    // Phone-number
                    if (StringUtils.isNoneBlank(companyDTO.getPhoneNumber())) company.setPhoneNumber(companyDTO.getPhoneNumber());
                    // Image-URL
                    company.setImageUrl(companyDTO.getImageUrl());
                    // Chairman
                    if (ObjectUtils.isNotEmpty(companyDTO.getChairmanId())) company.setChairman(
                        userRepository.findById(companyDTO.getChairmanId()).orElseThrow(() -> new ResourceNotFoundException("getChairman"))
                    );
                    // Secretary
                    if (ObjectUtils.isNotEmpty(companyDTO.getSecretaryId())) company.setSecretary(
                        userRepository
                            .findById(companyDTO.getSecretaryId())
                            .orElseThrow(() -> new ResourceNotFoundException("getSecretary"))
                    );

                    log.debug("Changed Information for Company: {}", company);
                    return company;
                }
            )
            .map(CompanyDTO::new);
    }

    /**
     * Get one company by the id, and return it.
     *
     * @param id company to get.
     * @return company.
     */
    @Transactional(readOnly = true)
    @Override
    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }

    /**
     * Get all companies in pageable form for the moderator.
     *
     * @param pageable companies to get all.
     * @return pageable companies.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CompanyDTO> getAllManagedCompanies(Pageable pageable) {
        return companyRepository.findAll(pageable).map(CompanyDTO::new);
    }

    @Override
    public void deleteCompanyLogo(Long companyId) {
        try {
            filesStorageService.deleteCompanyLogo(companyId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<CompanyNameDTO> searchCompanyByName(String name) {
        List<Company> companies = new ArrayList<Company>();
        if (name == null) companies.addAll(companyRepository.findAll()); else companyRepository
            .findByNameIgnoreCaseContaining(name)
            .ifPresent(companies::addAll);
        if (companies.isEmpty()) {
            return null;
        }
        return companyMapper.companiesToCompanyNameDTOs(companies);
    }

    @Override
    public Page<CompanyDTO> filterCompany(CompanySearchFieldEnum field, String value, Pageable pageable) {
        Company company = new Company();
        switch (field) {
            case NAME:
                company.setName(value);
                break;
            case INN:
                company.setInn(value);
                break;
            case EMAIL:
                company.setEmail(value);
                break;
            case PHONE_NUMBER:
                company.setPhoneNumber(value);
                break;
            case WEB_PAGE:
                company.setWebPage(value);
                break;
            default:
                break;
        }

        ExampleMatcher matcher = ExampleMatcher
            .matching()
            .withMatcher("name", contains().ignoreCase())
            .withMatcher("inn", contains().ignoreCase())
            .withMatcher("email", contains().ignoreCase())
            .withMatcher("phoneNumber", contains().ignoreCase())
            .withMatcher("webPage", contains().ignoreCase())
            .withIgnorePaths("createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate");

        if (value == null) {
            return companyRepository.findAll(pageable).map(CompanyDTO::new);
        } else {
            //            Example<Company> searchExample = Example.of(company, matcher);
            //            Page<Company> all = companyRepository.findAll(searchExample, pageable);
            //            List<CompanyDTO> companyDTOS = companyMapper.companiesToCompanyDTOs(all.getContent());
            Page<CompanyDTO> dtoPage = companyRepository.findAll(Example.of(company, matcher), pageable).map(CompanyDTO::new);
            return dtoPage;
        }
    }

    @Override
    public ApiResponse deleteCompany(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getCompany"));
        try {
            companyRepository.delete(company);
            return new ApiResponse(company.getName() + " Удален!", true);
        } catch (Exception e) {
            return new ApiResponse(company.getName() + "НЕ Удален! Error: " + e.getMessage(), false);
        }
    }

    private void clearCompanyCaches(Company company) {
        Objects.requireNonNull(cacheManager.getCache(CompanyRepository.COMPANIES_BY_NAME_CACHE)).evict(company.getName());
        if (company.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(CompanyRepository.COMPANIES_BY_EMAIL_CACHE)).evict(company.getEmail());
        }
    }
}
