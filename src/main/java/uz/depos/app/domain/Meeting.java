package uz.depos.app.domain;

import java.sql.Timestamp;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import uz.depos.app.domain.enums.MeetingStatusEnum;

/**
 * Заседание (Собрание) - Наблюдательного совета
 **/

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Meeting extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    // Статус заседания
    @Enumerated(value = EnumType.STRING)
    private MeetingStatusEnum status;

    // Дата и время проведения заседания
    @Column(nullable = false, name = "start_date")
    private Timestamp startDate;

    // Начало регистрации
    @Column(nullable = false, name = "start_registration")
    private Timestamp startRegistration;

    // Окончание регистрации
    @Column(nullable = false, name = "end_registration")
    private Timestamp endRegistration;

    // Привязка к компанию / организации
    @ManyToOne(optional = false)
    private Company company;

    // Привязка к Город/область
    @ManyToOne(optional = false)
    private City city;

    // Адрес - место проведения
    @Column(nullable = false)
    private String address;

    // Описание заседания / Повестка дня всего заседания
    @Column(length = 256)
    private String description;

    // Список наблюдателей в заседании
    @OneToMany(mappedBy = "meeting")
    private List<Member> members;

    // Прикрепляемы файлы для всего заседания
    @OneToMany(cascade = CascadeType.ALL)
    private List<Attachment> attachments;

    // Привязка к повесткам дня
    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
    private List<Agenda> agendas;

    // Доп инфо
    @Column(length = 128, name = "extra_info")
    private String extraInfo;

    public Meeting() {}

    public Meeting(
        Long id,
        MeetingStatusEnum status,
        Timestamp startDate,
        Timestamp startRegistration,
        Timestamp endRegistration,
        Company company,
        City city,
        String address,
        String description,
        List<Member> members,
        List<Attachment> attachments,
        List<Agenda> agendas,
        String extraInfo
    ) {
        this.id = id;
        this.status = status;
        this.startDate = startDate;
        this.startRegistration = startRegistration;
        this.endRegistration = endRegistration;
        this.company = company;
        this.city = city;
        this.address = address;
        this.description = description;
        this.members = members;
        this.attachments = attachments;
        this.agendas = agendas;
        this.extraInfo = extraInfo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MeetingStatusEnum getStatus() {
        return status;
    }

    public void setStatus(MeetingStatusEnum status) {
        this.status = status;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getStartRegistration() {
        return startRegistration;
    }

    public void setStartRegistration(Timestamp startRegistration) {
        this.startRegistration = startRegistration;
    }

    public Timestamp getEndRegistration() {
        return endRegistration;
    }

    public void setEndRegistration(Timestamp endRegistration) {
        this.endRegistration = endRegistration;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public List<Agenda> getAgendas() {
        return agendas;
    }

    public void setAgendas(List<Agenda> agendas) {
        this.agendas = agendas;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    @Override
    public String toString() {
        return (
            "Meeting{" +
            "id=" +
            id +
            ", status=" +
            status +
            ", startDate=" +
            startDate +
            ", startRegistration=" +
            startRegistration +
            ", endRegistration=" +
            endRegistration +
            ", company=" +
            company +
            ", city=" +
            city +
            ", address='" +
            address +
            '\'' +
            ", description='" +
            description +
            '\'' +
            ", members=" +
            members +
            ", attachments=" +
            attachments +
            ", agendas=" +
            agendas +
            ", extraInfo='" +
            extraInfo +
            '\'' +
            '}'
        );
    }
}
