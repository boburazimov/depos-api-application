package uz.depos.app.domain;

import static tech.jhipster.config.JHipsterDefaults.Registry.password;

import java.io.Serializable;
import javax.persistence.*;
import lombok.EqualsAndHashCode;

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

    private boolean zoom = false;

    private String zoomPassword;

    public MemberSession() {}

    public MemberSession(Long id, Long meetingId, String sessionId, Long memberId, boolean zoom, String zoomPassword) {
        this.id = id;
        this.meetingId = meetingId;
        this.sessionId = sessionId;
        this.memberId = memberId;
        this.zoom = zoom;
        this.zoomPassword = zoomPassword;
    }

    public MemberSession(Long meetingId, String sessionId, Long memberId, boolean zoom, String zoomPassword) {
        this.meetingId = meetingId;
        this.sessionId = sessionId;
        this.memberId = memberId;
        this.zoom = zoom;
        this.zoomPassword = zoomPassword;
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

    public boolean isZoom() {
        return zoom;
    }

    public void setZoom(boolean zoom) {
        this.zoom = zoom;
    }

    public String getZoomPassword() {
        return zoomPassword;
    }

    public void setZoomPassword(String zoomPassword) {
        this.zoomPassword = zoomPassword;
    }
}
