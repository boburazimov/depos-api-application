package uz.depos.app.repository.specification;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
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
                Instant startRegistration1 = request.getStartRegistration();
                Instant startRegistration2 = startRegistration1.plus(1, ChronoUnit.MINUTES);
                predicates.add(
                    criteriaBuilder.and(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("startRegistration").as(Instant.class), startRegistration1),
                        criteriaBuilder.lessThanOrEqualTo(root.get("startRegistration").as(Instant.class), startRegistration2)
                    )
                );
            }
            if (request.getStartDate() != null) {
                Instant startDate1 = request.getStartDate();
                Instant startDate2 = request.getStartDate().plus(1, ChronoUnit.MINUTES);
                predicates.add(
                    criteriaBuilder.and(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("startDate").as(Instant.class), startDate1),
                        criteriaBuilder.lessThanOrEqualTo(root.get("startDate").as(Instant.class), startDate2)
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
