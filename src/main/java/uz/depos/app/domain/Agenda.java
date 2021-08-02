package uz.depos.app.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import uz.depos.app.domain.enums.AgendaDebateEnum;
import uz.depos.app.domain.enums.AgendaSpeakTimeEnum;

/**
 * Повестка дня - вопросы для голосование
 */

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Agenda extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    // Документ заседание "meetingID"
    @ManyToOne(optional = false)
    private Meeting meeting;

    // Тема вопроса
    @Column(nullable = false)
    private String subject;

    // Докладчик "memberID"
    @ManyToOne
    private Member speaker;

    // Время для доклада
    @Enumerated(EnumType.STRING)
    private AgendaSpeakTimeEnum speakTimeEnum;

    // Время для обсуждения
    @Enumerated(EnumType.STRING)
    private AgendaDebateEnum debateEnum;

    // Статус повестки дня
    @Column(name = "is_active")
    private Boolean isActive;

    // Файлы для повестки дня
    @OneToMany(cascade = CascadeType.ALL)
    private List<Attachment> attachments;

    // Варианты решения для голосования
    @OneToMany(mappedBy = "agenda", cascade = CascadeType.ALL)
    private List<VotingOption> votingOptions;

    // Доп инфо
    @Column(length = 128, name = "extra_info")
    private String extraInfo;
}
