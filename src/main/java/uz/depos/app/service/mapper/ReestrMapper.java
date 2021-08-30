package uz.depos.app.service.mapper;

import org.springframework.stereotype.Service;
import uz.depos.app.domain.Member;
import uz.depos.app.domain.Reestr;
import uz.depos.app.service.dto.MemberDTO;
import uz.depos.app.service.dto.ReestrDTO;

/**
 * Mapper for the entity {@link Reestr} and its DTO called {@link ReestrDTO}.
 * <p>
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */

@Service
public class ReestrMapper {

    public ReestrDTO reestrToReestrDTO(Reestr reestr) {
        return new ReestrDTO(reestr);
    }
}
