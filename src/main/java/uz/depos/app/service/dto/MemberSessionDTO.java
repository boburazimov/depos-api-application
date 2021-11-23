package uz.depos.app.service.dto;

public class MemberSessionDTO {

    private Long memberId;

    private Boolean online;

    public MemberSessionDTO() {}

    public MemberSessionDTO(Long memberId, Boolean online) {
        this.memberId = memberId;
        this.online = online;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }
}
