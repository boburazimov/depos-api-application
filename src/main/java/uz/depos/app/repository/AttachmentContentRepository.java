package uz.depos.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.depos.app.domain.Attachment;
import uz.depos.app.domain.AttachmentContent;

public interface AttachmentContentRepository extends JpaRepository<AttachmentContent, Long> {
    AttachmentContent findByAttachmentId(Long attachment_id);

    void deleteByAttachment(Attachment attachment);

    void deleteByAttachmentId(Long attachment_id);
    //    @Transactional
    //    @Modifying
    //    @Query(value = "delete from attachment_content where attachment_id in (select attachment_meeting_id from  meeting_attachments_docs where meeting_id=:meetingId)", nativeQuery = true)
    //    void deleteAllByMeeting(@Param("meetingId") UUID meetingId);
}
