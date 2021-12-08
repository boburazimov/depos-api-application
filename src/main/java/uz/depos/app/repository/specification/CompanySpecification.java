package uz.depos.app.repository.specification;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import uz.depos.app.domain.Company;
import uz.depos.app.service.dto.CompanyFilerDTO;

@Component
public class CompanySpecification {

    public Specification<Company> getCompanies(CompanyFilerDTO request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotEmpty(request.getName())) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + request.getName().toLowerCase() + "%"));
            }
            if (StringUtils.isNotEmpty(request.getInn())) {
                predicates.add(criteriaBuilder.like(root.get("inn"), "%" + request.getInn() + "%"));
            }
            if (StringUtils.isNotEmpty(request.getEmail())) {
                predicates.add(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + request.getEmail().toLowerCase() + "%")
                );
            }
            if (StringUtils.isNotEmpty(request.getPhoneNumber())) {
                predicates.add(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("phoneNumber")), "%" + request.getPhoneNumber().toLowerCase() + "%")
                );
            }
            if (StringUtils.isNotEmpty(request.getWebPage())) {
                predicates.add(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("webPage")), "%" + request.getWebPage().toLowerCase() + "%")
                );
            }

            query.orderBy(criteriaBuilder.desc(root.get("name")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
