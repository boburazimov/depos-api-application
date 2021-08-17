package uz.depos.app.web.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import uz.depos.app.config.Constants;
import uz.depos.app.domain.Company;
import uz.depos.app.repository.CompanyRepository;
import uz.depos.app.security.AuthoritiesConstants;
import uz.depos.app.service.CompanyService;
import uz.depos.app.service.dto.AdminUserDTO;
import uz.depos.app.service.dto.ApiResponse;
import uz.depos.app.service.dto.CompanyDTO;
import uz.depos.app.web.rest.errors.BadRequestAlertException;
import uz.depos.app.web.rest.errors.EmailAlreadyUsedException;
import uz.depos.app.web.rest.errors.InvalidPasswordException;
import uz.depos.app.web.rest.errors.LoginAlreadyUsedException;
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

    public CompanyResource(CompanyService companyService, CompanyRepository companyRepository) {
        this.companyService = companyService;
        this.companyRepository = companyRepository;
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

    //    @GetMapping
    //    @ApiOperation(value = "Get companies", notes = "This method to get companies of pageable")
    //    public HttpEntity<?> getCompanies(
    //        @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE) int page,
    //        @RequestParam(value = "size", defaultValue = Constants.DEFAULT_SIZE) int size,
    //        @RequestParam(value = "sort", defaultValue = "false") boolean sort
    //    ) {
    //        return ResponseEntity.ok(companyService.getCompanies(page, size, sort));
    //    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete company", notes = "This method to delete one of company by ID")
    public HttpEntity<?> deleteCompany(@PathVariable Long id) {
        ApiResponse response = companyService.deleteCompany(id);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(response);
    }
}
