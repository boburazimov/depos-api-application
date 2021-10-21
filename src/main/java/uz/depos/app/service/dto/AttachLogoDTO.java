package uz.depos.app.service.dto;

import uz.depos.app.domain.Attachment;

/**
 * Uploading files model DTO
 **/

public class AttachLogoDTO {

    private Long id;

    private Long companyId;

    private String url;

    private String originalFileName;

    private String fileName;

    private String contentType;

    private Long fileSize;

    public AttachLogoDTO() {}

    public AttachLogoDTO(Attachment attachment) {
        this.id = attachment.getId();
        this.companyId = attachment.getCompanyId();
        this.url = attachment.getPath();
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
            ", url='" +
            url +
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
