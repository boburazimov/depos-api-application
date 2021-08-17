package uz.depos.app.service.dto;

import javax.persistence.*;
import uz.depos.app.domain.City;

public class CityDTO {

    private Integer id;

    @Column(length = 64, unique = true, nullable = false)
    private String nameUz;

    @Column(length = 64, unique = true, nullable = false)
    private String nameRu;

    @Column(length = 64, unique = true, nullable = false)
    private String nameEn;

    private String extraInfo;

    public CityDTO() {}

    public CityDTO(City city) {
        this.id = city.getId();
        this.nameUz = city.getNameUz();
        this.nameRu = city.getNameRu();
        this.nameEn = city.getNameEn();
        this.extraInfo = city.getExtraInfo();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameUz() {
        return nameUz;
    }

    public void setNameUz(String nameUz) {
        this.nameUz = nameUz;
    }

    public String getNameRu() {
        return nameRu;
    }

    public void setNameRu(String nameRu) {
        this.nameRu = nameRu;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
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
            "City{" +
            "id=" +
            id +
            ", nameUz='" +
            nameUz +
            '\'' +
            ", nameRu='" +
            nameRu +
            '\'' +
            ", nameEn='" +
            nameEn +
            '\'' +
            ", extraInfo='" +
            extraInfo +
            '\'' +
            '}'
        );
    }
}
