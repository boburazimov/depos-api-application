package uz.depos.app.domain;

import javax.persistence.*;

/**
 * Города Республики: (г. Ташкент, Самаркандская обл...)
 **/

@Entity
public class City {

    // Уникальный идентификатор
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Наименование города - Uzbek
    @Column(length = 64, unique = true, nullable = false, name = "name_uz")
    private String nameUz;

    // Наименование города - Russian
    @Column(length = 64, unique = true, nullable = false, name = "name_ru")
    private String nameRu;

    // Наименование города - English
    @Column(length = 64, unique = true, nullable = false, name = "name_en")
    private String nameEn;

    // Доп инфо
    @Column(length = 128, name = "extra_info")
    private String extraInfo;

    public City() {}

    public City(Integer id, String nameUz, String nameRu, String nameEn, String extraInfo) {
        this.id = id;
        this.nameUz = nameUz;
        this.nameRu = nameRu;
        this.nameEn = nameEn;
        this.extraInfo = extraInfo;
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
