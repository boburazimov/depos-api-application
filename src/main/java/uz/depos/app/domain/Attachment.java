package uz.depos.app.domain;

import javax.persistence.*;
import lombok.*;

/**
 * Для хранение данных о файлах а именно тип файла и его размер,
 * сам файл будет храниться в "AttachmentContent"
 **/

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
}
