package uz.depos.app.service.mapper;

import org.springframework.stereotype.Service;
import uz.depos.app.domain.Member;
import uz.depos.app.service.dto.MemberDTO;

/**
 * Mapper for the entity {@link Member} and its DTO called {@link MemberDTO}.
 * <p>
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */

@Service
public class MemberMapper {

    public MemberDTO memberToMemberDTO(Member member) {
        return new MemberDTO(member);
    }
}
