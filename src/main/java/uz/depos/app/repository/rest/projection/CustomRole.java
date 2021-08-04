package uz.depos.app.repository.rest.projection;

import org.springframework.data.rest.core.config.Projection;
import uz.depos.app.domain.Role;
import uz.depos.app.domain.enums.RoleNameEnum;

@Projection(name = "customRole", types = { Role.class })
public interface CustomRole {
    Integer getId();
    RoleNameEnum getName();
}
