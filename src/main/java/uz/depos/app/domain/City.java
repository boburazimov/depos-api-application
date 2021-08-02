package uz.depos.app.domain;

import javax.persistence.*;
import lombok.*;

/**
 * Города Республики: (г. Ташкент, Самаркандская обл...)
 **/

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class City {

    // Уникальный идентификатор
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Наименование города
    @Column(length = 64, unique = true, nullable = false)
    private String name;

    // Доп инфо
    @Column(length = 128, name = "extra_info")
    private String extraInfo;
}
