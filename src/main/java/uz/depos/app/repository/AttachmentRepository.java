package uz.depos.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.depos.app.domain.Attachment;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    // Get List of filesInfos by MeetingID
    Optional<List<Attachment>> findAllByMeetingIdAndIsReestrFalse(Long meetingId);

    // Get Reestr excel file
    Optional<Attachment> findOneByMeetingIdAndIsReestrTrue(Long meetingId);

    void deleteByMeetingIdAndIsReestrTrue(Long meetingId);

    // Get Company logo file
    Optional<Attachment> findByCompanyIdAndMeetingIdIsNullAndIsReestrFalse(Long companyId);

    void deleteAllByMeetingId(Long meetingId);
}
