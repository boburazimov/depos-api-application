package uz.depos.app.repository.rest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.depos.app.domain.Role;
import uz.depos.app.domain.enums.RoleNameEnum;
import uz.depos.app.repository.rest.projection.CustomRole;

@RepositoryRestResource(path = "role", collectionResourceRel = "list", excerptProjection = CustomRole.class)
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(RoleNameEnum name);
}
