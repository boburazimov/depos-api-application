package uz.depos.app.domain;

import javax.persistence.*;
import lombok.*;

/**
 * Для хранение тело файлов, информация о файле (размер, тип и.тд.) будут храниться в Attachment.
 **/

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
}
