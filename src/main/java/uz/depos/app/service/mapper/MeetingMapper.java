package uz.depos.app.service.mapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import uz.depos.app.domain.Meeting;
import uz.depos.app.domain.MeetingLogging;
import uz.depos.app.domain.Question;
import uz.depos.app.service.dto.MeetingByUserDTO;
import uz.depos.app.service.dto.MeetingDTO;
import uz.depos.app.service.dto.MeetingLoggingDTO;
import uz.depos.app.service.dto.QuestionDTO;

/**
 * Mapper for the entities {@link Meeting} {@link MeetingLogging} {@link Question}
 * and its DTOs called {@link MeetingDTO} {@link MeetingLoggingDTO} {@link QuestionDTO}.
 * <p>
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class MeetingMapper {

    public MeetingDTO meetingToMeetingDTO(Meeting meeting) {
        return new MeetingDTO(meeting);
    }

    public MeetingByUserDTO meetingToMeetingByUserDTO(Meeting meeting) {
        return new MeetingByUserDTO();
    }

    public MeetingLoggingDTO meetingLoggingToMeetingLoggingDTO(MeetingLogging logging) {
        return new MeetingLoggingDTO(logging);
    }

    public QuestionDTO questionToQuestionDTO(Question question) {
        return new QuestionDTO(question);
    }

    public List<QuestionDTO> questionsToQuestionDTOs(List<Question> questions) {
        return questions.stream().filter(Objects::nonNull).map(this::questionToQuestionDTO).collect(Collectors.toList());
    }
}
