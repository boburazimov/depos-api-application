package uz.depos.app.service.dto;

import javax.validation.constraints.NotNull;
import uz.depos.app.domain.Attachment;

/**
 * Uploading files model DTO
 **/

public class AttachLogoDTO {

    private Long id;

    @NotNull(message = "Company ID must not be null!")
    private Long companyId;

    @NotNull(message = "Path must not be null!")
    private String path;

    private String originalFileName;

    private String fileName;

    private String contentType;

    private Long fileSize;

    public AttachLogoDTO() {}

    public AttachLogoDTO(Attachment attachment) {
        this.id = attachment.getId();
        this.companyId = attachment.getCompanyId();
        this.path = attachment.getPath();
        this.originalFileName = attachment.getOriginalFileName();
        this.fileName = attachment.getFileName();
        this.contentType = attachment.getContentType();
        this.fileSize = attachment.getFileSize();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return (
            "AttachLogoDTO{" +
            "id=" +
            id +
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
            '}'
        );
    }
}
