package uz.depos.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.depos.app.domain.Question;

/**
 * Spring Data JPA repository for the {@link Question} entity.
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findOneByMeetingIdAndQuestionText(Long meetingId, String questionText);

    Page<Question> findAllByMeetingId(Long meetingId, Pageable pageable);

    List<Question> findAllByMeetingId(Long meetingId);

    List<Question> findAllByMemberId(Long memberId);

    Optional<Question> findOneById(Long id);

    void deleteAllByMeetingId(Long meetingId);
}
