package uz.depos.app.domain;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.*;
import lombok.EqualsAndHashCode;

/**
 * Для хранение данных о файлах а именно тип файла и его размер,
 * сам файл будет храниться в "AttachmentContent"
 **/

@EqualsAndHashCode(callSuper = true)
@Entity
public class Attachment extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    // Имя файла
    @Column(nullable = false)
    private String name;

    // Тип файла - расширение (pdf, jpg...)
    @Column(nullable = false, name = "content_type")
    private String contentType;

    // Размер файла - bytea
    @Column(nullable = false)
    private Long size;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Attachment() {}

    public Attachment(Long id, String name, String contentType, Long size) {
        this.id = id;
        this.name = name;
        this.contentType = contentType;
        this.size = size;
    }

    @Override
    public String toString() {
        return "Attachment{" + "id=" + id + ", name='" + name + '\'' + ", contentType='" + contentType + '\'' + ", size=" + size + '}';
    }
}
