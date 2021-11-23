package uz.depos.app.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.depos.app.domain.MemberSession;

@Repository
public interface MemberSessionRepository extends JpaRepository<MemberSession, Long> {
    Optional<MemberSession> findOneBySessionId(String sessionId);
}
