package uz.depos.app.domain;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import uz.depos.app.domain.enums.RoleNameEnum;

/**
 * Роли сотрудников, пользователей в системе - для разграничение доступа к ресурсам и к данным.
 **/

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role implements GrantedAuthority {

    // Уникальный идентификатор
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Список ролей для пользователей
    @Enumerated(value = EnumType.STRING)
    private RoleNameEnum name;

    // Доп инфо
    @Column(length = 128, name = "extra_info")
    private String extraInfo;

    @Override
    public String getAuthority() {
        return name.name();
    }
}
