package uz.depos.app.service.dto;

import java.io.Serializable;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import uz.depos.app.domain.Attachment;

public class AttachmentMeetingDTO implements Serializable {

    private Long id;

    @NotNull(message = "Meeting ID must not be null!")
    private Long meetingId;

    private Long agendaId;

    @NotNull(message = "Path must not be null!")
    private String path;

    private String originalFileName;

    private String contentType;

    private Long fileSize;

    public AttachmentMeetingDTO() {}

    public AttachmentMeetingDTO(Attachment attachment) {
        this.id = attachment.getId();
        this.meetingId = attachment.getMeetingId() != null ? attachment.getMeetingId() : null;
        this.agendaId = attachment.getAgendaId() != null ? attachment.getAgendaId() : null;
        this.path = attachment.getPath();
        this.originalFileName = attachment.getOriginalFileName();
        this.contentType = attachment.getContentType();
        this.fileSize = attachment.getFileSize();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }

    public Long getAgendaId() {
        return agendaId;
    }

    public void setAgendaId(Long agendaId) {
        this.agendaId = agendaId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public String toString() {
        return (
            "AttachmentMeetingDTO{" +
            "id=" +
            id +
            ", meetingId=" +
            meetingId +
            ", agendaId=" +
            agendaId +
            ", path='" +
            path +
            '\'' +
            ", originalFileName='" +
            originalFileName +
            '\'' +
            ", contentType='" +
            contentType +
            '\'' +
            ", fileSize=" +
            fileSize +
            '}'
        );
    }
}
