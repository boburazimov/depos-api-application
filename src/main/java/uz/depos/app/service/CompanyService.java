package uz.depos.app.service;

import uz.depos.app.domain.Company;
import uz.depos.app.service.dto.ApiResponse;
import uz.depos.app.service.dto.ReqCompany;
import uz.depos.app.service.dto.ResPageable;
import uz.depos.app.web.rest.errors.BadRequestAlertException;

public interface CompanyService {
    ApiResponse addCompany(ReqCompany request);

    ApiResponse getCompany(Company company);

    ResPageable getCompanies(int page, int size, boolean sort) throws BadRequestAlertException;

    ApiResponse deleteCompany(Long id);
}
