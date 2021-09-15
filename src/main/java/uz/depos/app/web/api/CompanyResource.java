package uz.depos.app.web.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import uz.depos.app.domain.Company;
import uz.depos.app.domain.enums.CompanySearchFieldEnum;
import uz.depos.app.repository.CompanyRepository;
import uz.depos.app.service.CompanyService;
import uz.depos.app.service.dto.ApiResponse;
import uz.depos.app.service.dto.CompanyByUserDTO;
import uz.depos.app.service.dto.CompanyDTO;
import uz.depos.app.service.dto.CompanyNameDTO;
import uz.depos.app.service.mapper.CompanyMapper;
import uz.depos.app.web.rest.errors.*;
import uz.depos.app.web.rest.errors.company.CompanyEmailAlreadyUsedException;
import uz.depos.app.web.rest.errors.company.CompanyInnAlreadyUsedException;
import uz.depos.app.web.rest.errors.company.CompanyNameAlreadyUsedException;

@RestController
@Api(tags = "Company")
@RequestMapping(path = "/api/company")
public class CompanyResource {

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Logger log = LoggerFactory.getLogger(CompanyResource.class);

    final CompanyService companyService;
    final CompanyRepository companyRepository;
    final CompanyMapper companyMapper;

    public CompanyResource(CompanyService companyService, CompanyRepository companyRepository, CompanyMapper companyMapper) {
        this.companyService = companyService;
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    /**
     * {@code POST  /company/create} : Create company.
     *
     * @param companyDTO the managed company View Model.
     * @return CompanyDTO with status {@code 201 (Created)}
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @ApiOperation(value = "Create company", notes = "This method creates a new company")
    public ResponseEntity<CompanyDTO> createCompany(@RequestBody CompanyDTO companyDTO) throws URISyntaxException {
        log.debug("REST request to create Company : {}", companyDTO);

        if (companyDTO.getId() != null && companyDTO.getId() > 0) {
            throw new BadRequestAlertException("A new company cannot already have an ID", "companyManagement", "idExists");
        } else if (
            StringUtils.isNoneBlank(companyDTO.getName()) && companyRepository.findOneByName(companyDTO.getName().toLowerCase()).isPresent()
        ) {
            throw new CompanyNameAlreadyUsedException();
        } else if (
            StringUtils.isNoneBlank(companyDTO.getEmail()) && companyRepository.findOneByEmailIgnoreCase(companyDTO.getEmail()).isPresent()
        ) {
            throw new CompanyEmailAlreadyUsedException();
        } else if (StringUtils.isNoneBlank(companyDTO.getInn()) && companyRepository.findOneByInn(companyDTO.getInn()).isPresent()) {
            throw new CompanyInnAlreadyUsedException();
        } else if (companyRepository.findOneByPhoneNumberIgnoreCase(companyDTO.getPhoneNumber()).isPresent()) {
            throw new PhoneNumberAlreadyUsedException();
        } else {
            CompanyDTO company = companyService.createCompany(companyDTO);
            return ResponseEntity
                .created(new URI("/api/moder/company/"))
                .headers(HeaderUtil.createAlert(applicationName, "companyManagement.created", company.getName()))
                .body(company);
        }
    }

    /**
     * {@code PUT /company/update} : Updates an existing Company.
     *
     * @param companyDTO the user to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated user.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already in use.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already in use.
     */
    @PutMapping
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @ApiOperation(value = "Update company", notes = "This method to update company fields")
    public ResponseEntity<CompanyDTO> updateUser(@Valid @RequestBody CompanyDTO companyDTO) {
        log.debug("REST request to update Company : {}", companyDTO);

        Optional<Company> existingCompanyByEmail = companyRepository.findOneByEmailIgnoreCase(companyDTO.getEmail());
        if (existingCompanyByEmail.isPresent() && (!existingCompanyByEmail.get().getId().equals(companyDTO.getId()))) {
            throw new CompanyEmailAlreadyUsedException();
        }
        Optional<Company> existingCompanyByName = companyRepository.findOneByName(companyDTO.getName().toLowerCase());
        if (existingCompanyByName.isPresent() && (!existingCompanyByName.get().getId().equals(companyDTO.getId()))) {
            throw new CompanyNameAlreadyUsedException();
        }
        Optional<Company> existingCompanyByInn = companyRepository.findOneByInn(companyDTO.getInn());
        if (existingCompanyByInn.isPresent() && (!existingCompanyByInn.get().getId().equals(companyDTO.getId()))) {
            throw new CompanyInnAlreadyUsedException();
        }
        Optional<CompanyDTO> updatedCompany = companyService.updateCompany(companyDTO);

        return ResponseUtil.wrapOrNotFound(
            updatedCompany,
            HeaderUtil.createAlert(applicationName, "companyManagement.updated", companyDTO.getName())
        );
    }

    /**
     * {@code GET /company/:id} : get the "id" company.
     *
     * @param id the id of the company to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the company, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "Get company", notes = "This method to get one of company by ID")
    public HttpEntity<CompanyDTO> getCompany(@PathVariable Long id) {
        log.debug("REST request to get Company : {}", id);

        return ResponseUtil.wrapOrNotFound(companyService.getCompanyById(id).map(CompanyDTO::new));
    }

    /**
     * {@code GET /company} : get all companies with all the details - calling this are only allowed for the administrators.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all companies.
     */
    @GetMapping
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @ApiOperation(value = "Get companies", notes = "This method to get companies of pageable")
    public ResponseEntity<List<CompanyDTO>> getAllCompanies(Pageable pageable) {
        log.debug("REST request to get all Company for an admin");

        final Page<CompanyDTO> page = companyService.getAllManagedCompanies(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code GET /company/user/:userId} : get all companies with all the details - calling this are only allowed for the Chaerman and Secretary.
     *
     * @param id the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all companies.
     */
    @GetMapping("/user/{id}")
    @ApiOperation(value = "Get companies", notes = "This method to get companies by user")
    public ResponseEntity<List<CompanyByUserDTO>> getCompaniesByUser(@PathVariable Long id) {
        log.debug("REST request to get all Companies by User for an chairman or secretary");

        final List<CompanyByUserDTO> companies = companyService.getCompaniesByUser(id);
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete company", notes = "This method to delete one of company by ID")
    public HttpEntity<ApiResponse> deleteCompany(@PathVariable Long id) {
        log.debug("REST request to delete Company : {}", id);
        ApiResponse response = companyService.deleteCompany(id);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(response);
    }

    /**
     * Search Companies by name
     *
     * @param name for search must be min 3 characters.
     * @return List of CompanyNameDTO (ID, NAME).
     */
    @GetMapping("/search")
    @ApiOperation(value = "Search company", notes = "This method to search companies by name containing")
    public ResponseEntity<List<CompanyNameDTO>> searchCompany(@RequestParam(required = false) String name) {
        log.debug("REST request to search Companies name of : {}", name);
        try {
            List<CompanyNameDTO> companyNameDTOs = companyService.searchCompanyByName(name);
            return new ResponseEntity<>(companyNameDTOs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Filters for header table Company.
     *
     * @param field    - Column in the table (entity).
     * @param value     - fragment of word to search by him.
     * @param pageable - params for pageable.
     * @return - List of CompanyDTO.
     */
    @GetMapping("/filter")
    @ApiOperation(value = "Filter company", notes = "This method to filter companies by field and search-value in pageable form.")
    public ResponseEntity<List<CompanyDTO>> filterCompany(
        @RequestParam CompanySearchFieldEnum field,
        @RequestParam String value,
        Pageable pageable
    ) {
        log.debug("REST request to filter Companies field : {}", field);

        final Page<CompanyDTO> page = companyService.filterCompany(field, value, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
