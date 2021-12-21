package uz.depos.app.service.mapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import uz.depos.app.domain.Company;
import uz.depos.app.domain.Member;
import uz.depos.app.service.dto.CompanyNameDTO;
import uz.depos.app.service.dto.MemberDTO;
import uz.depos.app.service.dto.MemberManagersDTO;

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

    public MemberManagersDTO memberToMemberManagersDTO(Member member) {
        return new MemberManagersDTO(member);
    }

    public List<MemberDTO> membersToMemberDTOs(List<Member> members) {
        return members.stream().filter(Objects::nonNull).map(this::memberToMemberDTO).collect(Collectors.toList());
    }
}
