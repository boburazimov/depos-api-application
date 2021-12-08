package uz.depos.app.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.depos.app.domain.Meeting;

/**
 * Spring Data JPA repository for the {@link Meeting} entity.
 */
@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long>, JpaSpecificationExecutor<Meeting> {
    Optional<Meeting> findOneById(Long id);

    Optional<Meeting> findOneByStartDateAndCompanyId(Instant date, Long companyId);

    @Query(
        value = "select distinct met.* from meeting as met inner join member as mem on met.id=mem.meeting_id left join company c on c.id = met.company_id where (mem.user_id=:user_id  or c.secretary_id = :user_id or c.chairman_id = :user_id) and mem.company_id=:company_id",
        nativeQuery = true
    )
    List<Meeting> findMeetingsByQuery(@Param("user_id") Long userId, @Param("company_id") Long companyId);

    @Query(
        value = "select distinct met.* from meeting as met inner join member as mem on met.id=mem.meeting_id left join company c on c.id = met.company_id where (mem.user_id=:user_id  or c.secretary_id = :user_id or c.chairman_id = :user_id) and mem.company_id=:company_id",
        nativeQuery = true
    )
    Page<Meeting> findMeetingsByUserAndCompany(@Param("user_id") Long userId, @Param("company_id") Long companyId, Pageable pageable);

    Optional<Meeting> findFirstByCompanyId(Long companyId);

    Page<Meeting> findAllByCompanyId(Long companyId, Pageable pageable);

    Page<Meeting> findAll(Specification<Meeting> specification, Pageable pageable);

    List<Meeting> findAll(Specification<Meeting> specification);
}
