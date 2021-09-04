package uz.depos.app.service.mapper;

import org.springframework.stereotype.Service;
import uz.depos.app.domain.Company;
import uz.depos.app.domain.Meeting;
import uz.depos.app.service.dto.CompanyDTO;
import uz.depos.app.service.dto.MeetingDTO;

/**
 * Mapper for the entity {@link Company} and its DTO called {@link CompanyDTO}.
 * <p>
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class MeetingMapper {

    public MeetingDTO meetingToMeetingDTO(Meeting meeting) {
        return new MeetingDTO(meeting);
    }
}