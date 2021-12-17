package uz.depos.app.domain;

import javax.persistence.*;
import lombok.*;
import uz.depos.app.domain.enums.MemberTypeEnum;

/**
 * Участники заседание (отдельно от users -> могут быть приглашенные)
 **/

@EqualsAndHashCode(callSuper = true)
@Entity
public class Member extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    // Заседание
    @ManyToOne(optional = false)
    private Meeting meeting;

    // Компания
    @ManyToOne(optional = false)
    private Company company;

    // Привязка пользователя к участнику заседание
    @ManyToOne(optional = false)
    private User user;

    // Тип участье в заседании добавленных пользователей
    @Column(name = "is_remotely")
    private Boolean isRemotely;

    // Ознакомлен с правилами
    @Column(name = "is_confirmed")
    private Boolean isConfirmed;

    // Находится ли участник в заседании или покинул его (сигнал)
    @Column(name = "is_involved")
    private Boolean isInvolved;

    // Тип участника - роль
    @Enumerated(EnumType.STRING)
    private MemberTypeEnum memberTypeEnum;

    // Является ли участник заседание докладчиком
    //    @Column(name = "is_speaker")
    //    private Boolean isSpeaker;

    //    @Column(name = "is_chairmen", columnDefinition = "boolean default false")
    //    private Boolean isChairmen;

    // Тип документа, удостоверяющего личность
    @Column(name = "hld_it")
    private String hldIt;

    // Должность участника
    @Column(name = "position")
    private String position;

    @Column(name = "from_reestr")
    private Boolean fromReestr;

    //    // Представитель государство
    //    @Column(name = "nation_vakeel")
    //    private Boolean nationVakeel;

    public Member() {}

    public Member(
        Long id,
        Meeting meeting,
        Company company,
        User user,
        Boolean isRemotely,
        Boolean isConfirmed,
        Boolean isInvolved,
        MemberTypeEnum memberTypeEnum,
        String hldIt,
        String position,
        Boolean fromReestr
    ) {
        this.id = id;
        this.meeting = meeting;
        this.company = company;
        this.user = user;
        this.isRemotely = isRemotely;
        this.isConfirmed = isConfirmed;
        this.isInvolved = isInvolved;
        this.memberTypeEnum = memberTypeEnum;
        this.hldIt = hldIt;
        this.position = position;
        this.fromReestr = fromReestr;
    }

    public Member(Member member, User user, Boolean fromReestr) {
        this.meeting = member.getMeeting();
        this.company = member.getCompany();
        this.user = user;
        this.isRemotely = member.isRemotely;
        this.isConfirmed = member.isConfirmed;
        this.isInvolved = member.isInvolved;
        this.memberTypeEnum = member.getMemberTypeEnum();
        this.hldIt = member.getHldIt();
        this.position = member.getPosition();
        this.fromReestr = fromReestr;
    }

    public Member(
        Meeting meeting,
        Company company,
        User user,
        Boolean isRemotely,
        Boolean isConfirmed,
        Boolean isInvolved,
        MemberTypeEnum memberTypeEnum,
        String hldIt,
        String position,
        Boolean fromReestr
    ) {
        this.meeting = meeting;
        this.company = company;
        this.user = user;
        this.isRemotely = isRemotely;
        this.isConfirmed = isConfirmed;
        this.isInvolved = isInvolved;
        this.memberTypeEnum = memberTypeEnum;
        this.hldIt = hldIt;
        this.position = position;
        this.fromReestr = fromReestr;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
            "Member{" +
            "id=" +
            id +
            ", meeting=" +
            meeting +
            ", company=" +
            company +
            ", user=" +
            user +
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
