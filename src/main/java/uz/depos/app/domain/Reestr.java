package uz.depos.app.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;

/**
 * Реестр для загрузки участников по каждому заседанию
 */

@Entity
@EqualsAndHashCode(callSuper = true)
public class Reestr extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "meeting_id")
    private Long meetingId;

    @NotNull
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "is_chairmen", columnDefinition = "boolean default false")
    private boolean isChairmen;

    // Порядковый номер.
    @Column(name = "hld_num")
    private Integer hldNum;

    // ФИО - FullName from User
    @Column(name = "hld_name")
    private String hldName;

    // ПИНФЛ - PINFL from User
    @Column(name = "hld_Pinfl")
    private String hldPinfl;

    // Тип документа, удостоверяющего личность
    @Column(name = "hld_it")
    private String hldIt;

    // Номер паспорта - from User
    @Column(name = "hld_pass")
    private String hldPass;

    // Контактный номер - from User
    @Column(name = "hld_phone")
    private String hldPhone;

    // Почтовый адресс - from User
    @NotNull
    @Column(name = "hld_email")
    private String hldEmail;

    // Должность участника
    @Column(name = "position")
    private String position;

    public Reestr() {}

    public Reestr(
        Long id,
        Long meetingId,
        Long memberId,
        boolean isChairmen,
        Integer hldNum,
        String hldName,
        String hldPinfl,
        String hldIt,
        String hldPass,
        String hldPhone,
        String hldEmail,
        String position
    ) {
        this.id = id;
        this.meetingId = meetingId;
        this.memberId = memberId;
        this.isChairmen = isChairmen;
        this.hldNum = hldNum;
        this.hldName = hldName;
        this.hldPinfl = hldPinfl;
        this.hldIt = hldIt;
        this.hldPass = hldPass;
        this.hldPhone = hldPhone;
        this.hldEmail = hldEmail;
        this.position = position;
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
            "Reestr{" +
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
