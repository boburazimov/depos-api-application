package uz.depos.app.service.dto;

import javax.validation.constraints.NotNull;
import uz.depos.app.domain.Attachment;

public class AttachMeetingDTO {

    private Long id;

    @NotNull(message = "Meeting ID must not be null!")
    private Long meetingId;

    private Long agendaId;

    private String agendaSubject;

    @NotNull(message = "Path must not be null!")
    private String path;

    private String originalFileName;

    private String contentType;

    private Long fileSize;

    public AttachMeetingDTO() {}

    public AttachMeetingDTO(Attachment attachment) {
        this.id = attachment.getId();
        this.meetingId = attachment.getMeetingId();
        this.agendaId = attachment.getAgendaId();
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

    public String getAgendaSubject() {
        return agendaSubject;
    }

    public void setAgendaSubject(String agendaSubject) {
        this.agendaSubject = agendaSubject;
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
            "AttachMeetingDTO{" +
            "id=" +
            id +
            ", meetingId=" +
            meetingId +
            ", agendaId=" +
            agendaId +
            ", agendaSubject='" +
            agendaSubject +
            '\'' +
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
