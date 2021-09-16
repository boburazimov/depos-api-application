package uz.depos.app.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Uploading files model
 **/

@Entity
@Table(name = "attachment")
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "meeting_id")
    private Long meetingId;

    @Column(name = "is_reestr")
    private boolean isReestr;

    @Column(name = "agenda_id")
    private Long agendaId;

    @Column(name = "company_id")
    private Long companyId;

    @NotNull
    @Column(name = "path")
    private String path;

    @NotNull
    @Column(name = "original_file_name")
    private String originalFileName;

    @NotNull
    @Column(name = "file_name")
    private String fileName;

    @NotNull
    @Column(name = "content_type")
    private String contentType;

    @NotNull
    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "created_date")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdDate;

    public Attachment() {}

    public Attachment(
        Long id,
        Long meetingId,
        boolean isReestr,
        Long agendaId,
        Long companyId,
        String path,
        String originalFileName,
        String fileName,
        String contentType,
        Long fileSize,
        LocalDateTime createdDate
    ) {
        this.id = id;
        this.meetingId = meetingId;
        this.isReestr = isReestr;
        this.agendaId = agendaId;
        this.companyId = companyId;
        this.path = path;
        this.originalFileName = originalFileName;
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.createdDate = createdDate;
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

    public boolean isReestr() {
        return isReestr;
    }

    public void setReestr(boolean reestr) {
        isReestr = reestr;
    }

    public Long getAgendaId() {
        return agendaId;
    }

    public void setAgendaId(Long agendaId) {
        this.agendaId = agendaId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return (
            "Attachment{" +
            "id=" +
            id +
            ", meetingId=" +
            meetingId +
            ", isReestr=" +
            isReestr +
            ", agendaId=" +
            agendaId +
            ", companyId=" +
            companyId +
            ", path='" +
            path +
            '\'' +
            ", originalFileName='" +
            originalFileName +
            '\'' +
            ", fileName='" +
            fileName +
            '\'' +
            ", contentType='" +
            contentType +
            '\'' +
            ", fileSize=" +
            fileSize +
            ", createdDate=" +
            createdDate +
            '}'
        );
    }
}
