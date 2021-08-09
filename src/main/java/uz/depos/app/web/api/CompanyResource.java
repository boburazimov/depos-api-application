package uz.depos.app.web.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.depos.app.config.Constants;
import uz.depos.app.domain.Company;
import uz.depos.app.repository.CompanyRepository;
import uz.depos.app.security.AuthoritiesConstants;
import uz.depos.app.service.CompanyService;
import uz.depos.app.service.dto.ApiResponse;
import uz.depos.app.service.dto.CompanyDTO;

@RestController
@Api(tags = "Company")
@RequestMapping(path = "/api/company")
public class CompanyResource {

    final CompanyService companyService;
    final CompanyRepository companyRepository;

    public CompanyResource(CompanyService companyService, CompanyRepository companyRepository) {
        this.companyService = companyService;
        this.companyRepository = companyRepository;
    }

    @PostMapping
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @ApiOperation(value = "Create new company", notes = "This method creates a new company")
    public ResponseEntity<CompanyDTO> createCompany(@RequestBody CompanyDTO companyDTO) {
        CompanyDTO company = companyService.createCompany(companyDTO);
        return ResponseEntity.ok(company);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get one company", notes = "This method to get one of company by ID")
    public HttpEntity<?> getCompany(@PathVariable Long id) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getCompany"));
        ApiResponse response = companyService.getCompany(company);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(response);
    }

    @GetMapping
    @ApiOperation(value = "Get companies", notes = "This method to get companies of pageable")
    public HttpEntity<?> getCompanies(
        @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE) int page,
        @RequestParam(value = "size", defaultValue = Constants.DEFAULT_SIZE) int size,
        @RequestParam(value = "sort", defaultValue = "false") boolean sort
    ) {
        return ResponseEntity.ok(companyService.getCompanies(page, size, sort));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete company", notes = "This method to delete one of company by ID")
    public HttpEntity<?> deleteCompany(@PathVariable Long id) {
        ApiResponse response = companyService.deleteCompany(id);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(response);
    }
}
