package uz.depos.app.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;

/**
 * Uploading files model
 **/

//@Entity
@Table(name = "attachment")
public class Attachment {

    @Id
    @Column(name = "id")
    private Long id;

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

    @NotNull
    @Column(name = "application_name")
    private String applicationName;

    @Column(name = "created_date")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdDate;

    public Attachment() {}

    public Attachment(
        Long id,
        String path,
        String originalFileName,
        String fileName,
        String contentType,
        Long fileSize,
        String applicationName,
        LocalDateTime createdDate
    ) {
        this.id = id;
        this.path = path;
        this.originalFileName = originalFileName;
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.applicationName = applicationName;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
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
            "id='" +
            id +
            '\'' +
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
            ", applicationName='" +
            applicationName +
            '\'' +
            ", createdDate=" +
            createdDate +
            '}'
        );
    }
}
