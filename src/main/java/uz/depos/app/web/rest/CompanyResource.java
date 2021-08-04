package uz.depos.app.web.rest;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.depos.app.config.Constants;
import uz.depos.app.domain.Company;
import uz.depos.app.repository.CompanyRepository;
import uz.depos.app.service.CompanyService;
import uz.depos.app.service.dto.ApiResponse;
import uz.depos.app.service.dto.ReqCompany;

@RestController
@RequestMapping(path = "/api/company")
public class CompanyResource {

    final CompanyService companyService;
    final CompanyRepository companyRepository;

    public CompanyResource(CompanyService companyService, CompanyRepository companyRepository) {
        this.companyService = companyService;
        this.companyRepository = companyRepository;
    }

    @PostMapping
    public HttpEntity<?> addCompany(@RequestBody ReqCompany request) {
        ApiResponse response = companyService.addCompany(request);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(response);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getCompany(@PathVariable Long id) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getCompany"));
        ApiResponse response = companyService.getCompany(company);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(response);
    }

    @GetMapping
    public HttpEntity<?> getCompanies(
        @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE) int page,
        @RequestParam(value = "size", defaultValue = Constants.DEFAULT_SIZE) int size,
        @RequestParam(value = "sort", defaultValue = "false") boolean sort
    ) {
        return ResponseEntity.ok(companyService.getCompanies(page, size, sort));
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteCompany(@PathVariable Long id) {
        ApiResponse response = companyService.deleteCompany(id);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(response);
    }
}
