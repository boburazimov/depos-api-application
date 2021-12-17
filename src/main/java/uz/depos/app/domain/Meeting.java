package uz.depos.app.domain;

import java.time.Instant;
import java.util.Objects;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import uz.depos.app.domain.enums.MeetingStatusEnum;
import uz.depos.app.domain.enums.MeetingTypeEnum;

/**
 * Meeting of Company
 **/

@Getter
@Setter
@Entity
public class Meeting extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    // Status of meeting
    @Enumerated(value = EnumType.STRING)
    private MeetingStatusEnum status;

    // Type of meeting
    @Enumerated(value = EnumType.STRING)
    private MeetingTypeEnum typeEnum;

    // Дата и время проведения заседания
    @Column(nullable = false, name = "start_date")
    private Instant startDate;

    // Начало регистрации
    @Column(nullable = false, name = "start_registration")
    private Instant startRegistration;

    // Окончание регистрации
    @Column(nullable = false, name = "end_registration")
    private Instant endRegistration;

    // Привязка к компанию / организации
    @ManyToOne(optional = false)
    private Company company;

    // Привязка к Город/область
    @ManyToOne
    private City city;

    // Адрес - место проведения
    @Column(nullable = false)
    private String address;

    // Описание заседания / Повестка дня всего заседания
    @Column(length = 256)
    private String description;

    // Доп инфо
    @Column(length = 128, name = "extra_info")
    private String extraInfo;

    public Meeting() {}

    public Meeting(
        Long id,
        MeetingStatusEnum status,
        MeetingTypeEnum typeEnum,
        Instant startDate,
        Instant startRegistration,
        Instant endRegistration,
        Company company,
        City city,
        String address,
        String description,
        String extraInfo
    ) {
        this.id = id;
        this.status = status;
        this.typeEnum = typeEnum;
        this.startDate = startDate;
        this.startRegistration = startRegistration;
        this.endRegistration = endRegistration;
        this.company = company;
        this.city = city;
        this.address = address;
        this.description = description;
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

    public MeetingTypeEnum getTypeEnum() {
        return typeEnum;
    }

    public void setTypeEnum(MeetingTypeEnum typeEnum) {
        this.typeEnum = typeEnum;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getStartRegistration() {
        return startRegistration;
    }

    public void setStartRegistration(Instant startRegistration) {
        this.startRegistration = startRegistration;
    }

    public Instant getEndRegistration() {
        return endRegistration;
    }

    public void setEndRegistration(Instant endRegistration) {
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
            ", typeEnum=" +
            typeEnum +
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
            ", extraInfo='" +
            extraInfo +
            '\'' +
            '}'
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Meeting meeting = (Meeting) o;
        return id != null && Objects.equals(id, meeting.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
