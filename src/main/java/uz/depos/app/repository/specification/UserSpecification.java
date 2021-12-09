package uz.depos.app.repository.specification;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import uz.depos.app.domain.User;
import uz.depos.app.domain.enums.UserGroupEnum;
import uz.depos.app.service.dto.UserFilterDTO;

@Component
public class UserSpecification {

    public Specification<User> getUsers(UserFilterDTO request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotEmpty(request.getFullName())) { // fullName
                predicates.add(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), "%" + request.getFullName().toLowerCase() + "%")
                );
            }
            if (StringUtils.isNotEmpty(request.getEmail())) { // email
                predicates.add(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + request.getEmail().toLowerCase() + "%")
                );
            }
            if (StringUtils.isNotEmpty(request.getPhoneNumber())) { // phoneNumber
                predicates.add(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("phoneNumber")), "%" + request.getPhoneNumber().toLowerCase() + "%")
                );
            }
            if (request.getGroupEnum() != null && EnumUtils.isValidEnum(UserGroupEnum.class, request.getGroupEnum().toString())) { // groupEnum
                predicates.add(criteriaBuilder.equal(root.get("groupEnum"), request.getGroupEnum()));
            }
            if (StringUtils.isNotEmpty(request.getPinfl())) { // PINFL
                predicates.add(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("pinfl")), "%" + request.getPinfl().toLowerCase() + "%")
                );
            }

            query.orderBy(criteriaBuilder.desc(root.get("fullName")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
