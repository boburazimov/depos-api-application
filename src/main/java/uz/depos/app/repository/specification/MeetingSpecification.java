package uz.depos.app.repository.specification;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import uz.depos.app.domain.City;
import uz.depos.app.domain.Company;
import uz.depos.app.domain.Meeting;
import uz.depos.app.domain.enums.MeetingStatusEnum;
import uz.depos.app.service.dto.MeetingFilterDTO;

@Component
public class MeetingSpecification {

    public Specification<Meeting> getMeetings(MeetingFilterDTO request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getCompanyId() != null) {
                Join<Meeting, Company> join = root.join("company"); // company.id = ?
                predicates.add(criteriaBuilder.equal(join.get("id"), request.getCompanyId()));
            }
            if (request.getStatus() != null && EnumUtils.isValidEnum(MeetingStatusEnum.class, request.getStatus().toString())) {
                predicates.add(criteriaBuilder.equal(root.get("status"), request.getStatus()));
            }
            if (request.getStartRegistration() != null) {
                predicates.add(
                    criteriaBuilder.and(
                        criteriaBuilder.greaterThanOrEqualTo(
                            root.get("startRegistration").as(Instant.class),
                            request.getStartRegistration()
                        ),
                        criteriaBuilder.lessThanOrEqualTo(
                            root.get("startRegistration").as(Instant.class),
                            request.getStartRegistration().plus(1, ChronoUnit.MINUTES)
                        )
                    )
                );
            }
            if (request.getStartDate() != null) {
                predicates.add(
                    criteriaBuilder.and(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("startDate").as(Instant.class), request.getStartDate()),
                        criteriaBuilder.lessThanOrEqualTo(
                            root.get("startDate").as(Instant.class),
                            request.getStartDate().plus(1, ChronoUnit.MINUTES)
                        )
                    )
                );
            }
            if (request.getCityId() != null) {
                Join<Meeting, City> join = root.join("city"); // city.id = ?
                predicates.add(criteriaBuilder.equal(join.get("id"), request.getCityId()));
            }
            query.orderBy(criteriaBuilder.desc(root.get("startDate")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
