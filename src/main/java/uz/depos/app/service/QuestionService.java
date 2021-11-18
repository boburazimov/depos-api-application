package uz.depos.app.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.depos.app.domain.Question;
import uz.depos.app.repository.MeetingRepository;
import uz.depos.app.repository.MemberRepository;
import uz.depos.app.repository.QuestionRepository;
import uz.depos.app.repository.UserRepository;
import uz.depos.app.service.dto.QuestionDTO;
import uz.depos.app.service.mapper.MeetingMapper;
import uz.depos.app.web.rest.errors.BadRequestAlertException;

/**
 * Service class for managing question.
 */
@Service
@Transactional
public class QuestionService {

    private final Logger log = LoggerFactory.getLogger(QuestionService.class);

    final MeetingRepository meetingRepository;
    final UserRepository userRepository;
    final MemberRepository memberRepository;
    final MeetingMapper meetingMapper;
    final QuestionRepository questionRepository;

    public QuestionService(
        MeetingRepository meetingRepository,
        UserRepository userRepository,
        MemberRepository memberRepository,
        MeetingMapper meetingMapper,
        QuestionRepository questionRepository
    ) {
        this.meetingRepository = meetingRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.meetingMapper = meetingMapper;
        this.questionRepository = questionRepository;
    }

    public QuestionDTO addQuestionByMember(QuestionDTO questionDTO) {
        if (!meetingRepository.findById(questionDTO.getMeetingId()).isPresent()) {
            throw new ResourceNotFoundException("Meeting not found by ID: " + questionDTO.getMeetingId());
        } else if (StringUtils.isBlank(questionDTO.getQuestionText())) {
            throw new ResourceNotFoundException("Question text must not be Empty!");
        } else if (!memberRepository.findById(questionDTO.getMemberId()).isPresent()) {
            throw new ResourceNotFoundException("Member not found by ID: " + questionDTO.getMemberId());
        } else if (
            questionRepository.findOneByMeetingIdAndQuestionText(questionDTO.getMeetingId(), questionDTO.getQuestionText()).isPresent()
        ) throw new BadRequestAlertException("Question text already in use by this meeting", "questionManagement", "textExist");

        Question question = new Question();
        meetingRepository.findById(questionDTO.getMeetingId()).ifPresent(question::setMeeting);
        memberRepository.findById(questionDTO.getMemberId()).ifPresent(question::setMember);
        question.setQuestionText(questionDTO.getQuestionText());
        question.setActive(false);
        Question savedQuestion = questionRepository.saveAndFlush(question);
        log.debug("Created Information for Question: {}", savedQuestion);
        return meetingMapper.questionToQuestionDTO(savedQuestion);
    }

    @Transactional(readOnly = true)
    public Optional<Question> getQuestion(Long id) {
        return questionRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<QuestionDTO> getAllQuestionsByMeeting(Long meetingId, Pageable pageable) {
        return questionRepository.findAllByMeetingId(meetingId, pageable).map(QuestionDTO::new);
    }

    @Transactional(readOnly = true)
    public List<QuestionDTO> getQuestionsByMeetingId(Long meetingId) {
        return questionRepository.findAllByMeetingId(meetingId).stream().map(QuestionDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<QuestionDTO> getAllQuestionsByMember(Long memberId) {
        List<Question> allByMemberId = questionRepository.findAllByMemberId(memberId);
        if (allByMemberId.size() > 0) {
            return meetingMapper.questionsToQuestionDTOs(allByMemberId);
        } else {
            throw new ResourceNotFoundException("Questions not found by Member ID: " + memberId);
        }
    }

    public Optional<QuestionDTO> updateQuestionAnswer(QuestionDTO questionDTO) {
        if (questionDTO.getId() == null || (!questionRepository.findById(questionDTO.getId()).isPresent())) {
            throw new ResourceNotFoundException("Question not found by ID: " + questionDTO.getId());
        } else if (!userRepository.findById(questionDTO.getUserId()).isPresent()) {
            throw new ResourceNotFoundException("User not found by ID: " + questionDTO.getUserId());
        } else if (StringUtils.isBlank(questionDTO.getQuestionAnswer())) {
            throw new NullPointerException("Question answer must not be null or empty");
        }
        return Optional
            .of(questionRepository.findById(questionDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(
                question -> {
                    userRepository.findById(questionDTO.getUserId()).ifPresent(question::setUser);
                    question.setQuestionAnswer(questionDTO.getQuestionAnswer());
                    log.debug("Updating Answer Information for Question: {}", question);
                    return questionRepository.saveAndFlush(question);
                }
            )
            .map(QuestionDTO::new);
    }

    public Optional<QuestionDTO> switchQuestionStatus(Long id) {
        return Optional
            .of(questionRepository.findById(id))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(
                question -> {
                    question.setActive(!question.getActive());
                    log.debug("Changed Information for Question: {}", question);
                    return questionRepository.saveAndFlush(question);
                }
            )
            .map(QuestionDTO::new);
    }

    public void deleteQuestion(Long id) {
        questionRepository
            .findOneById(id)
            .ifPresent(
                question -> {
                    questionRepository.delete(question);
                    log.debug("Deleted Question: {}", question);
                }
            );
    }
}
