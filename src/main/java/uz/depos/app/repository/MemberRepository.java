package uz.depos.app.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.depos.app.domain.Authority;
import uz.depos.app.domain.Member;

/**
 * Spring Data JPA repository for the {@link Member} entity.
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findOneById(Long id);

    Optional<Member> findByUserAuthoritiesContainsAndMeetingId(Authority authority, Long meeting_id);

    Optional<Member> findOneByUserId(Long user_id);

    Page<Member> findAllByIdNotNullAndMeetingIdNotNull(Pageable pageable);
}
