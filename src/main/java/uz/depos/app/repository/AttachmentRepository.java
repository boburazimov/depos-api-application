package uz.depos.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.depos.app.domain.Attachment;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    Optional<List<Attachment>> findAllByMeetingIdAndIsReestrTrue(Long meeting_id);

    // Get Reestr excel file
    Optional<Attachment> findOneByMeetingIdAndIsReestrTrue(Long meeting_id);

    // Get Company logo file
    Optional<Attachment> findByCompanyIdAndIsReestrFalse(Long company_id);

    void deleteByMeetingIdAndIsReestrTrue(Long meeting_id);
}
