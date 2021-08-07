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
import uz.depos.app.service.dto.CompanyDTO;
import uz.depos.app.service.dto.ResPageable;
import uz.depos.app.service.mapper.CompanyMapper;
import uz.depos.app.web.rest.errors.BadRequestAlertException;

@Service
public class CompanyServiceImpl implements CompanyService {

    final CompanyRepository companyRepository;
    final AttachmentRepository attachmentRepository;
    final UserRepository userRepository;
    final CompanyMapper companyMapper;

    public CompanyServiceImpl(
        CompanyRepository companyRepository,
        AttachmentRepository attachmentRepository,
        UserRepository userRepository,
        CompanyMapper companyMapper
    ) {
        this.companyRepository = companyRepository;
        this.attachmentRepository = attachmentRepository;
        this.userRepository = userRepository;
        this.companyMapper = companyMapper;
    }

    @Override
    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        try {
            Company company = companyDTO.getId() == null
                ? new Company()
                : companyRepository.findById(companyDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("getCompany"));
            company.setActive(companyDTO.getActive());
            company.setName(companyDTO.getName());
            company.setInn(companyDTO.getInn());
            company.setLegalAddress(companyDTO.getLegalAddress());
            company.setEmail(companyDTO.getEmail());
            company.setDescription(companyDTO.getDescription());
            company.setMailingAddress(companyDTO.getMailingAddress());
            company.setWebPage(companyDTO.getWebPage());
            company.setPhoneNumber(companyDTO.getPhoneNumber());
            company.setLogo(
                companyDTO.getLogoId() == null
                    ? null
                    : attachmentRepository
                        .findById(companyDTO.getLogoId())
                        .orElseThrow(() -> new ResourceNotFoundException("getAttachment"))
            );
            company.setChairman(
                companyDTO.getChairmanId() == null
                    ? null
                    : userRepository.findById(companyDTO.getChairmanId()).orElseThrow(() -> new ResourceNotFoundException("getUser"))
            );
            company.setSecretary(
                companyDTO.getSecretaryId() == null
                    ? null
                    : userRepository.findById(companyDTO.getSecretaryId()).orElseThrow(() -> new ResourceNotFoundException("getUser"))
            );
            company.setExtraInfo(companyDTO.getExtraInfo());
            Company savedCompany = companyRepository.save(company);
            CompanyDTO companyDTO1 = companyMapper.companyToCompanyDTO(savedCompany);
            return companyDTO1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
