package uz.depos.app.service.mapper;

import org.springframework.stereotype.Service;
import uz.depos.app.domain.Attachment;
import uz.depos.app.service.dto.AttachmentMeetingDTO;

/**
 * Mapper for the entity {@link Attachment} and its DTO called {@link AttachmentMeetingDTO}.
 * <p>
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class AttachmentMapper {

    public AttachmentMeetingDTO attachmentToAttachmentMeetingDTO(Attachment attachment) {
        return new AttachmentMeetingDTO(attachment);
    }
}
