package uz.depos.app.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.depos.app.domain.Authority;
import uz.depos.app.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findOneById(Long id);

    //    Optional<Member> findByMeetingIdAndUserAuthorities(Long meeting_id, Set<Authority> user_authorities);

    Optional<Member> findByUserAuthoritiesContainsAndMeetingId(Authority authority, Long meeting_id);

    Optional<Member> findOneByUserId(Long user_id);

    Page<Member> findAllByIdNotNullAndMeetingIdNotNull(Pageable pageable);
}
