package uz.depos.app.service.dto;

import javax.validation.constraints.NotNull;
import uz.depos.app.domain.Member;
import uz.depos.app.domain.enums.MemberTypeEnum;

/**
 * A DTO representing a member.
 */
public class MemberDTO {

    private Long id;

    @NotNull(message = "MeetingID not must be null")
    private Long meetingId;

    @NotNull(message = "CompanyID not must be null")
    private Long companyId;

    @NotNull(message = "UserID not must be null")
    private Long userId;

    private Boolean isRemotely;

    private Boolean isConfirmed;

    private Boolean isInvolved;

    private MemberTypeEnum memberTypeEnum;

    private String hldIt;

    private String position;

    private Boolean fromReestr;

    public MemberDTO() {}

    public MemberDTO(Member member) {
        this.id = member.getId();
        this.meetingId = member.getMeeting().getId();
        this.companyId = member.getCompany().getId();
        this.userId = member.getUser().getId();
        this.isRemotely = member.getRemotely();
        this.isConfirmed = member.getConfirmed();
        this.isInvolved = member.getInvolved();
        this.memberTypeEnum = member.getMemberTypeEnum();
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

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
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

    public MemberTypeEnum getMemberTypeEnum() {
        return memberTypeEnum;
    }

    public void setMemberTypeEnum(MemberTypeEnum memberTypeEnum) {
        this.memberTypeEnum = memberTypeEnum;
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
            ", companyId=" +
            companyId +
            ", userId=" +
            userId +
            ", isRemotely=" +
            isRemotely +
            ", isConfirmed=" +
            isConfirmed +
            ", isInvolved=" +
            isInvolved +
            ", memberTypeEnum=" +
            memberTypeEnum +
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
