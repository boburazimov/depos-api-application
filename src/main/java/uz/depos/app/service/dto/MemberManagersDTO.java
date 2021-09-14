package uz.depos.app.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.NotNull;
import uz.depos.app.domain.Member;
import uz.depos.app.domain.enums.MemberTypeEnum;

/**
 * A DTO representing a member (managers).
 */
public class MemberManagersDTO {

    @JsonIgnore
    private Long id;

    @NotNull
    private Long meetingId;

    @NotNull
    private Long companyId;

    @NotNull
    private Long userId;

    @JsonIgnore
    private String pinfl;

    @NotNull
    private MemberTypeEnum memberTypeEnum;

    public MemberManagersDTO() {}

    public MemberManagersDTO(Member member) {
        this.id = member.getId();
        this.meetingId = member.getMeeting().getId();
        this.companyId = member.getCompany().getId();
        this.userId = member.getUser().getId();
        this.pinfl = member.getUser().getPinfl();
        this.memberTypeEnum = member.getMemberTypeEnum();
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

    public String getPinfl() {
        return pinfl;
    }

    public void setPinfl(String pinfl) {
        this.pinfl = pinfl;
    }

    public MemberTypeEnum getMemberTypeEnum() {
        return memberTypeEnum;
    }

    public void setMemberTypeEnum(MemberTypeEnum memberTypeEnum) {
        this.memberTypeEnum = memberTypeEnum;
    }

    @Override
    public String toString() {
        return (
            "MemberManagersDTO{" +
            "id=" +
            id +
            ", meetingId=" +
            meetingId +
            ", companyId=" +
            companyId +
            ", userId=" +
            userId +
            ", pinfl='" +
            pinfl +
            '\'' +
            ", memberTypeEnum=" +
            memberTypeEnum +
            '}'
        );
    }
}
