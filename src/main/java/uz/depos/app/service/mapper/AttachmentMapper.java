package uz.depos.app.service.mapper;

import org.springframework.stereotype.Service;
import uz.depos.app.domain.Attachment;
import uz.depos.app.service.dto.AttachLogoDTO;
import uz.depos.app.service.dto.AttachMeetingDTO;
import uz.depos.app.service.dto.AttachReestrDTO;

/**
 * Mapper for the entity {@link Attachment} and its DTO called {@link AttachMeetingDTO}.
 * <p>
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class AttachmentMapper {

    public AttachMeetingDTO attachmentToAttachmentMeetingDTO(Attachment attachment) {
        return new AttachMeetingDTO(attachment);
    }

    public AttachReestrDTO attachmentToAttachmentReestrDTO(Attachment attachment) {
        return new AttachReestrDTO(attachment);
    }

    public AttachLogoDTO attachmentToAttachmentLogoDTO(Attachment attachment) {
        return new AttachLogoDTO(attachment);
    }
}
