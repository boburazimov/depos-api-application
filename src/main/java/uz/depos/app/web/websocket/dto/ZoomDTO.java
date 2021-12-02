package uz.depos.app.web.websocket.dto;

public class ZoomDTO {

    private Long meetingId;
    private Long memberId;
    private String password;
    private boolean zoom = false;

    public ZoomDTO() {}

    public ZoomDTO(Long meetingId, Long memberId, String password, boolean zoom) {
        this.meetingId = meetingId;
        this.memberId = memberId;
        this.password = password;
        this.zoom = zoom;
    }

    public Long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isZoom() {
        return zoom;
    }

    public void setZoom(boolean zoom) {
        this.zoom = zoom;
    }
}
