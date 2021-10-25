package uz.depos.app.service.dto;

import com.fasterxml.jackson.annotation.JsonView;
import javax.validation.constraints.NotNull;
import uz.depos.app.domain.Member;
import uz.depos.app.domain.User;
import uz.depos.app.domain.enums.MemberTypeEnum;
import uz.depos.app.service.view.View;

/**
 * A DTO representing a member.
 */
public class MemberDTO {

    @JsonView(value = { View.ModelView.External.class, View.ModelView.PUT.class, View.ModelView.PATCH.class })
    private Long id;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    @NotNull(message = "MeetingID not must be null")
    private Long meetingId;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    @NotNull(message = "CompanyID not must be null")
    private Long companyId;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    @NotNull(message = "UserID not must be null")
    private Long userId;

    //    @JsonView(value = {View.ModelView.External.class, View.ModelView.})
    private User user;

    //    private String userName;

    //    private String pinfl;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private Boolean isRemotely;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private Boolean isConfirmed;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private Boolean isInvolved;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private MemberTypeEnum memberTypeEnum;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private String hldIt;

    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class })
    private String position;

    //    @JsonView(value = { View.ModelView.External.class, View.ModelView.Post.class})
    private Boolean fromReestr;

    public MemberDTO() {}

    public MemberDTO(Member member) {
        this.id = member.getId();
        this.meetingId = member.getMeeting().getId();
        this.companyId = member.getCompany().getId();
        this.userId = member.getUser().getId();
        this.user = member.getUser();
        //        this.userName = member.getUser().getFullName();
        //        this.pinfl = member.getUser().getPinfl();
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

    //    public String getUserName() {
    //        return userName;
    //    }
    //
    //    public void setUserName(String userName) {
    //        this.userName = userName;
    //    }
    //
    //    public String getPinfl() {
    //        return pinfl;
    //    }
    //
    //    public void setPinfl(String pinfl) {
    //        this.pinfl = pinfl;
    //    }

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
            ", user=" +
            user +
            //            ", userName='" + userName + '\'' +
            //            ", pinfl='" + pinfl + '\'' +
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
