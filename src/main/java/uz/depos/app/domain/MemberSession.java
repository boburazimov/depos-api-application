package uz.depos.app.domain;

import java.io.Serializable;
import javax.persistence.*;
import lombok.EqualsAndHashCode;
import uz.depos.app.domain.enums.AgendaDebateEnum;
import uz.depos.app.domain.enums.AgendaSpeakTimeEnum;
import uz.depos.app.domain.enums.AgendaTypeEnum;

/**
 * Временная таблица для подхвата member
 */
@EqualsAndHashCode(callSuper = true)
@Entity
public class MemberSession extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    private Long meetingId;

    private String sessionId;

    private Long memberId;

    public MemberSession() {}

    public MemberSession(Long id, Long meetingId, String sessionId, Long memberId) {
        this.id = id;
        this.meetingId = meetingId;
        this.sessionId = sessionId;
        this.memberId = memberId;
    }

    public MemberSession(Long meetingId, String sessionId, Long memberId) {
        this.meetingId = meetingId;
        this.sessionId = sessionId;
        this.memberId = memberId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
