package uz.depos.app.service.dto;

import javax.validation.constraints.NotNull;
import uz.depos.app.domain.Attachment;

/**
 * Uploading files model DTO
 **/
public class AttachReestrDTO {

    private Long id;

    @NotNull(message = "Meeting ID must not be null!")
    private Long meetingId;

    @NotNull(message = "Path must not be null!")
    private String path;

    private String originalFileName;

    private Long fileSize;

    private String extraInfo;

    public AttachReestrDTO() {}

    public AttachReestrDTO(Attachment attachment) {
        this.id = attachment.getId();
        this.meetingId = attachment.getMeetingId();
        this.path = attachment.getPath();
        this.originalFileName = attachment.getOriginalFileName();
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

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    @Override
    public String toString() {
        return (
            "AttachReestrDTO{" +
            "id=" +
            id +
            ", meetingId=" +
            meetingId +
            ", path='" +
            path +
            '\'' +
            ", originalFileName='" +
            originalFileName +
            '\'' +
            ", fileSize=" +
            fileSize +
            ", extraInfo='" +
            extraInfo +
            '\'' +
            '}'
        );
    }
}
