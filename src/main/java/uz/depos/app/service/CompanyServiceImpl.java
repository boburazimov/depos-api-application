package uz.depos.app.service;

import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import uz.depos.app.domain.Company;
import uz.depos.app.repository.AttachmentRepository;
import uz.depos.app.repository.CompanyRepository;
import uz.depos.app.repository.UserRepository;
import uz.depos.app.service.dto.ApiResponse;
import uz.depos.app.service.dto.ReqCompany;
import uz.depos.app.service.dto.ResPageable;
import uz.depos.app.web.rest.errors.BadRequestAlertException;

@Service
public class CompanyServiceImpl implements CompanyService {

    final CompanyRepository companyRepository;
    final AttachmentRepository attachmentRepository;
    final UserRepository userRepository;

    public CompanyServiceImpl(
        CompanyRepository companyRepository,
        AttachmentRepository attachmentRepository,
        UserRepository userRepository
    ) {
        this.companyRepository = companyRepository;
        this.attachmentRepository = attachmentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ApiResponse addCompany(ReqCompany request) {
        try {
            Company company = request.getId() == null
                ? new Company()
                : companyRepository.findById(request.getId()).orElseThrow(() -> new ResourceNotFoundException("getCompany"));
            company.setActive(request.getActive());
            company.setName(request.getName());
            company.setInn(request.getInn());
            company.setLegalAddress(request.getLegalAddress());
            company.setEmail(request.getEmail());
            company.setDescription(request.getDescription());
            company.setMailingAddress(request.getMailingAddress());
            company.setWebPage(request.getWebPage());
            company.setPhoneNumber(request.getPhoneNumber());
            company.setLogo(
                request.getLogoId() == null
                    ? null
                    : attachmentRepository.findById(request.getLogoId()).orElseThrow(() -> new ResourceNotFoundException("getAttachment"))
            );
            company.setChairman(
                request.getChairmanId() == null
                    ? null
                    : userRepository.findById(request.getChairmanId()).orElseThrow(() -> new ResourceNotFoundException("getUser"))
            );
            company.setSecretary(
                request.getSecretaryId() == null
                    ? null
                    : userRepository.findById(request.getSecretaryId()).orElseThrow(() -> new ResourceNotFoundException("getUser"))
            );
            company.setExtraInfo(request.getExtraInfo());
            companyRepository.save(company);
            return new ApiResponse(request.getId() == null ? "Компания сохранен!" : "Компания изменен!", true);
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), false);
        }
    }

    @Override
    public ApiResponse getCompany(Company company) {
        try {
            return new ApiResponse("Компания получен!", true, company);
        } catch (Exception e) {
            return new ApiResponse("Ошибка при получении!", false, e.getMessage());
        }
    }

    @Override
    public ResPageable getCompanies(int page, int size, boolean sort) throws BadRequestAlertException {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Company> companies;
        if (sort) {
            companies = companyRepository.findAllByOrderByCreatedDateDesc(pageable);
        } else {
            companies = companyRepository.findAllByOrderByCreatedDateAsc(pageable);
        }
        return new ResPageable(
            companies.getContent().stream().map(this::getCompany).collect(Collectors.toList()),
            companies.getTotalElements(),
            page,
            companies.getTotalPages()
        );
    }

    @Override
    public ApiResponse deleteCompany(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getCompany"));
        try {
            companyRepository.delete(company);
            return new ApiResponse(company.getName() + " Удален!", true);
        } catch (Exception e) {
            return new ApiResponse(company.getName() + "НЕ Удален!", false);
        }
    }
}
