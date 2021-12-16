package uz.depos.app.service.dto;

import com.fasterxml.jackson.annotation.JsonView;
import javax.validation.constraints.NotNull;
import uz.depos.app.domain.Member;
import uz.depos.app.domain.User;
import uz.depos.app.domain.enums.MemberTypeEnum;
import uz.depos.app.service.view.View;

/**
 * A DTO representing a member types.
 */
public class MemberTypeDTO {

    private Long id;

    private MemberTypeEnum memberTypeEnum;

    private Boolean fromReestr;

    public MemberTypeDTO() {}

    public MemberTypeDTO(Member member) {
        this.id = member.getId();
        this.memberTypeEnum = member.getMemberTypeEnum();
        this.fromReestr = member.getFromReestr();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MemberTypeEnum getMemberTypeEnum() {
        return memberTypeEnum;
    }

    public void setMemberTypeEnum(MemberTypeEnum memberTypeEnum) {
        this.memberTypeEnum = memberTypeEnum;
    }

    public Boolean getFromReestr() {
        return fromReestr;
    }

    public void setFromReestr(Boolean fromReestr) {
        this.fromReestr = fromReestr;
    }
}
