package uz.depos.app.service.dto;

import javax.validation.constraints.NotNull;
import uz.depos.app.domain.AbstractAuditingEntity;
import uz.depos.app.domain.Reestr;

/**
 * A DTO representing a reestr, with only the public attributes.
 */

public class ReestrDTO extends AbstractAuditingEntity {

    private Long id;

    @NotNull(message = "Meeting ID must not be null!")
    private Long meetingId;

    @NotNull(message = "Member ID must not be null!")
    private Long memberId;

    private boolean isChairmen;

    // Порядковый номер.
    private Integer hldNum;

    // ФИО - FullName from User
    private String hldName;

    // ПИНФЛ - PINFL from User
    @NotNull(message = "PINFL must not be null!")
    private String hldPinfl;

    // Тип документа, удостоверяющего личность
    private String hldIt;

    // Номер паспорта - from User
    private String hldPass;

    // Контактный номер - from User
    private String hldPhone;

    // Почтовый адресс - from User
    @NotNull(message = "Email must not be null!")
    private String hldEmail;

    // Должность участника
    private String position;

    public ReestrDTO() {}

    public ReestrDTO(Reestr reestr) {
        this.id = reestr.getId();
        this.meetingId = reestr.getMeetingId();
        this.memberId = reestr.getMemberId();
        this.isChairmen = reestr.isChairmen();
        this.hldNum = reestr.getHldNum();
        this.hldName = reestr.getHldName();
        this.hldPinfl = reestr.getHldPinfl();
        this.hldIt = reestr.getHldIt();
        this.hldPass = reestr.getHldPass();
        this.hldPhone = reestr.getHldPhone();
        this.hldEmail = reestr.getHldEmail();
        this.position = reestr.getPosition();
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

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public boolean isChairmen() {
        return isChairmen;
    }

    public void setChairmen(boolean chairmen) {
        isChairmen = chairmen;
    }

    public Integer getHldNum() {
        return hldNum;
    }

    public void setHldNum(Integer hldNum) {
        this.hldNum = hldNum;
    }

    public String getHldName() {
        return hldName;
    }

    public void setHldName(String hldName) {
        this.hldName = hldName;
    }

    public String getHldPinfl() {
        return hldPinfl;
    }

    public void setHldPinfl(String hldPinfl) {
        this.hldPinfl = hldPinfl;
    }

    public String getHldIt() {
        return hldIt;
    }

    public void setHldIt(String hldIt) {
        this.hldIt = hldIt;
    }

    public String getHldPass() {
        return hldPass;
    }

    public void setHldPass(String hldPass) {
        this.hldPass = hldPass;
    }

    public String getHldPhone() {
        return hldPhone;
    }

    public void setHldPhone(String hldPhone) {
        this.hldPhone = hldPhone;
    }

    public String getHldEmail() {
        return hldEmail;
    }

    public void setHldEmail(String hldEmail) {
        this.hldEmail = hldEmail;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return (
            "ReestrDTO{" +
            "id=" +
            id +
            ", meetingId=" +
            meetingId +
            ", memberId=" +
            memberId +
            ", isChairmen=" +
            isChairmen +
            ", hldNum=" +
            hldNum +
            ", hldName='" +
            hldName +
            '\'' +
            ", hldPinfl='" +
            hldPinfl +
            '\'' +
            ", hldIt='" +
            hldIt +
            '\'' +
            ", hldPass='" +
            hldPass +
            '\'' +
            ", hldPhone='" +
            hldPhone +
            '\'' +
            ", hldEmail='" +
            hldEmail +
            '\'' +
            ", position='" +
            position +
            '\'' +
            '}'
        );
    }
}
