package uz.depos.app.service.dto;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import uz.depos.app.domain.Member;

/**
 * A DTO representing a member.
 */
public class MemberDTO {

    private Long id;

    @NotNull(message = "Meeting not must be null")
    private Long meetingId;

    @NotNull(message = "User not must be null")
    private Long userId;

    private Boolean isRemotely;

    private Boolean isConfirmed;

    private Boolean isInvolved;

    private Boolean isSpeaker;

    private Boolean isChairmen;

    private String hldIt;

    private String position;

    private Boolean fromReestr;

    public MemberDTO() {}

    public MemberDTO(Member member) {
        this.id = member.getId();
        this.meetingId = member.getMeeting().getId();
        this.userId = member.getUser().getId();
        this.isRemotely = member.getRemotely();
        this.isConfirmed = member.getConfirmed();
        this.isInvolved = member.getInvolved();
        this.isSpeaker = member.getSpeaker();
        this.isChairmen = member.getChairmen();
        this.hldIt = member.getHldIt();
        this.position = member.getPosition();
        this.fromReestr = member.getFromReestr();
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getRemotely() {
        return isRemotely;
    }

    public void setRemotely(Boolean remotely) {
        isRemotely = remotely;
    }

    public Boolean getConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        isConfirmed = confirmed;
    }

    public Boolean getInvolved() {
        return isInvolved;
    }

    public void setInvolved(Boolean involved) {
        isInvolved = involved;
    }

    public Boolean getSpeaker() {
        return isSpeaker;
    }

    public void setSpeaker(Boolean speaker) {
        isSpeaker = speaker;
    }

    public Boolean getChairmen() {
        return isChairmen;
    }

    public void setChairmen(Boolean chairmen) {
        isChairmen = chairmen;
    }

    public String getHldIt() {
        return hldIt;
    }

    public void setHldIt(String hldIt) {
        this.hldIt = hldIt;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Boolean getFromReestr() {
        return fromReestr;
    }

    public void setFromReestr(Boolean fromReestr) {
        this.fromReestr = fromReestr;
    }

    @Override
    public String toString() {
        return (
            "MemberDTO{" +
            "id=" +
            id +
            ", meetingId=" +
            meetingId +
            ", userId=" +
            userId +
            ", isRemotely=" +
            isRemotely +
            ", isConfirmed=" +
            isConfirmed +
            ", isInvolved=" +
            isInvolved +
            ", isSpeaker=" +
            isSpeaker +
            ", isChairmen=" +
            isChairmen +
            ", hldIt='" +
            hldIt +
            '\'' +
            ", position='" +
            position +
            '\'' +
            ", fromReestr=" +
            fromReestr +
            '}'
        );
    }
}
