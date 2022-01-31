package uz.depos.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.depos.app.domain.MemberSession;

@Repository
public interface MemberSessionRepository extends JpaRepository<MemberSession, Long> {
    Optional<MemberSession> findOneBySessionId(String sessionId);
    Optional<MemberSession> findOneByMemberId(Long memberId);
    Optional<MemberSession> findOneByMeetingIdAndZoomIsTrueAndZoomPasswordNotNull(Long meetingId);
    Optional<List<MemberSession>> findAllByMeetingIdAndZoomIsTrueAndZoomPasswordNotNull(Long meetingId);
    Optional<List<MemberSession>> findAllByMeetingIdAndMemberId(Long meetingId, Long memberId);
    void deleteAllByMeetingId(Long meetingId);
}
