package uz.depos.app.service.dto;

public class ResUploadFile {

    private Long fileId;

    private String fileName;

    private String fileDownloadUri;

    private String fileType;

    private long size;

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDownloadUri() {
        return fileDownloadUri;
    }

    public void setFileDownloadUri(String fileDownloadUri) {
        this.fileDownloadUri = fileDownloadUri;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public ResUploadFile() {}

    public ResUploadFile(Long fileId, String fileName, String fileDownloadUri, String fileType, long size) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
    }

    @Override
    public String toString() {
        return (
            "ResUploadFile{" +
            "fileId=" +
            fileId +
            ", fileName='" +
            fileName +
            '\'' +
            ", fileDownloadUri='" +
            fileDownloadUri +
            '\'' +
            ", fileType='" +
            fileType +
            '\'' +
            ", size=" +
            size +
            '}'
        );
    }
}
