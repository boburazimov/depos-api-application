package uz.depos.app.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.depos.app.domain.Meeting;

/**
 * Spring Data JPA repository for the {@link Meeting} entity.
 */
@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    Optional<Meeting> findOneById(Long id);

    Optional<Meeting> findOneByStartDate(Instant date);

    @Query(
        value = "select distinct met.* from meeting as met inner join member as mem on met.id=mem.meeting_id where mem.user_id=:user_id and mem.company_id=:company_id",
        nativeQuery = true
    )
    List<Meeting> findMeetingsByUser(@Param("user_id") Long userId, @Param("company_id") Long companyId);

    Optional<Meeting> findFirstByCompanyId(Long companyId);
}
