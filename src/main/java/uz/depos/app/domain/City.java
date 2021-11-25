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

    public City() {}

    public City(Integer id, String nameUz, String nameRu, String nameEn) {
        this.id = id;
        this.nameUz = nameUz;
        this.nameRu = nameRu;
        this.nameEn = nameEn;
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
}
//    INSERT INTO city(name_en, name_ru, name_uz) VALUES('City Tashkent', 'г. Ташкент', 'Toshkent sh.');
//    INSERT INTO city(name_en, name_ru, name_uz) VALUES('Region Tashkent', 'Ташкентская область', 'Toshkent v.');
//    INSERT INTO city(name_en, name_ru, name_uz) VALUES('Region Sirdarya', 'Сырдарьинская область', 'Sirdaryo v.');
//    INSERT INTO city(name_en, name_ru, name_uz) VALUES('Region Djizakh', 'Джизакская область', 'Jizzax v.');
//    INSERT INTO city(name_en, name_ru, name_uz) VALUES('Region Samarkand', 'Самаркандская область', 'Samarqand v.');
//    INSERT INTO city(name_en, name_ru, name_uz) VALUES('Region Fergana', 'Ферганская область', 'Fargona v.');
//    INSERT INTO city(name_en, name_ru, name_uz) VALUES('Region Andijan', 'Андижанская обасть', 'Andijon v.');
//    INSERT INTO city(name_en, name_ru, name_uz) VALUES('Region Namangan', 'Наманганская область', 'Namangan v.');
//    INSERT INTO city(name_en, name_ru, name_uz) VALUES('Region Kashkadarya', 'Кашкадарьинская область', 'Qashqadaryo v.');
//    INSERT INTO city(name_en, name_ru, name_uz) VALUES('Region Surkhandarya', 'Сурхандарьинская область', 'Surxandaryo v.');
//    INSERT INTO city(name_en, name_ru, name_uz) VALUES('Region Navoi', 'Навоийская область', 'Navoyi v.');
//    INSERT INTO city(name_en, name_ru, name_uz) VALUES('Region Bukhara', 'Бухарская область', 'Buhoro v.');
//    INSERT INTO city(name_en, name_ru, name_uz) VALUES('Region Harezm', 'Хорезмская обасть', 'Horazm v.');
//    INSERT INTO city(name_en, name_ru, name_uz) VALUES('Republic Karakalpakistan', 'Республика Каракалпакстан', 'Respublika Qoraqalpogiston');
