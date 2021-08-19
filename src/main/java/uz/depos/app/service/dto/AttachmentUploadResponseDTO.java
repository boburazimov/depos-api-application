package uz.depos.app.service.dto;

import java.io.Serializable;

public class AttachmentUploadResponseDTO implements Serializable {

    private String id;

    private String path;

    public AttachmentUploadResponseDTO() {}

    public AttachmentUploadResponseDTO(String id, String path) {
        this.id = id;
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "AttachmentUploadResponseDTO{" + "id='" + id + '\'' + ", path='" + path + '\'' + '}';
    }
}
