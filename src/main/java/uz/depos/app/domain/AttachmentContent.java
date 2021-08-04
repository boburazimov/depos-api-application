package uz.depos.app.domain;

import java.util.Arrays;
import javax.persistence.*;
import lombok.EqualsAndHashCode;

/**
 * Для хранение тело файлов, информация о файле (размер, тип и.тд.) будут храниться в Attachment.
 **/

@EqualsAndHashCode(callSuper = true)
@Entity
public class AttachmentContent extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    // Информация об файле
    @OneToOne
    private Attachment attachment;

    // Тело самого файла в байтах
    private byte[] content;

    public AttachmentContent() {}

    public AttachmentContent(Long id, Attachment attachment, byte[] content) {
        this.id = id;
        this.attachment = attachment;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "AttachmentContent{" + "id=" + id + ", attachment=" + attachment + ", content=" + Arrays.toString(content) + '}';
    }
}
