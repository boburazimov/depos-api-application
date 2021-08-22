package uz.depos.app.domain;

import javax.persistence.*;
import lombok.*;

/**
 * Участники заседание (отдельно от users -> могут быть приглашенные) TODO - новый файл от Ёркинжон для реестр
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

    // Является ли участник заседание докладчиком
    @Column(name = "is_speaker")
    private Boolean isSpeaker;

    //    // Представитель государство
    //    @Column(name = "nation_vakeel")
    //    private Boolean nationVakeel;

    public Member() {}

    public Member(Long id, Meeting meeting, User user, Boolean isRemotely, Boolean isConfirmed, Boolean isInvolved, Boolean isSpeaker) {
        this.id = id;
        this.meeting = meeting;
        this.user = user;
        this.isRemotely = isRemotely;
        this.isConfirmed = isConfirmed;
        this.isInvolved = isInvolved;
        this.isSpeaker = isSpeaker;
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

    public Boolean getSpeaker() {
        return isSpeaker;
    }

    public void setSpeaker(Boolean speaker) {
        isSpeaker = speaker;
    }

    @Override
    public String toString() {
        return (
            "Member{" +
            "id=" +
            id +
            ", meeting=" +
            meeting +
            ", user=" +
            user +
            ", isRemotely=" +
            isRemotely +
            ", isConfirmed=" +
            isConfirmed +
            ", isInvolved=" +
            isInvolved +
            ", isSpeaker=" +
            isSpeaker +
            '}'
        );
    }
}
