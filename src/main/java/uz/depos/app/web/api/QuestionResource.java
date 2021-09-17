package uz.depos.app.web.api;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import uz.depos.app.repository.MeetingRepository;
import uz.depos.app.repository.MemberRepository;
import uz.depos.app.repository.UserRepository;
import uz.depos.app.service.MeetingService;
import uz.depos.app.service.QuestionService;
import uz.depos.app.service.dto.MeetingLoggingDTO;
import uz.depos.app.service.dto.QuestionDTO;
import uz.depos.app.service.view.View;

@RestController
@RequestMapping(path = "/api/question")
@Api(tags = "Question")
public class QuestionResource {

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Logger log = LoggerFactory.getLogger(QuestionResource.class);

    final MeetingService meetingService;
    final MeetingRepository meetingRepository;
    final UserRepository userRepository;
    final MemberRepository memberRepository;
    final QuestionService questionService;

    public QuestionResource(
        MeetingService meetingService,
        MeetingRepository meetingRepository,
        UserRepository userRepository,
        MemberRepository memberRepository,
        QuestionService questionService
    ) {
        this.meetingService = meetingService;
        this.meetingRepository = meetingRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.questionService = questionService;
    }

    /**
     * {@code POST  /question}  : Creates a new question.
     * Creates a new question if the meeting and user is not empty, and return QuestionDTO
     *
     * @param questionDTO the question to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new  question, or with status {@code 400 (Bad Request)} if the required fields empty.
     * and have incorrect id.
     * @throws URISyntaxException if the required fields is Empty.
     */
    @PostMapping
    @ApiOperation(value = "Create Question", notes = "This method to add question text for current meeting by the member")
    public ResponseEntity<QuestionDTO> addQuestion(
        @Valid @RequestBody @JsonView(value = View.ModelView.Post.class) QuestionDTO questionDTO
    ) throws URISyntaxException {
        log.debug("REST request to add Question to Meeting ID : {}", questionDTO.getMeetingId());
        QuestionDTO savedQuestionDTO = questionService.addQuestionByMember(questionDTO);
        return ResponseEntity
            .created(new URI("/api/meeting/question"))
            .headers(HeaderUtil.createAlert(applicationName, "questionManagement.created", questionDTO.getQuestionText()))
            .body(savedQuestionDTO);
    }

    /**
     * {@code PUT /question} : Updates an existing Question.
     *
     * @param questionDTO the id to update question.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated question.
     */
    @PutMapping
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MODERATOR + "\")")
    @ApiOperation(value = "Update question", notes = "This method will update a question answer by current user")
    public ResponseEntity<QuestionDTO> updateQuestionAnswer(
        @Valid @RequestBody @JsonView(value = View.ModelView.PUT.class) QuestionDTO questionDTO
    ) {
        log.debug("REST request to update question answer : {}", questionDTO.getQuestionAnswer());

        Optional<QuestionDTO> savedQuestionDTO = questionService.updateQuestionAnswer(questionDTO);
        return ResponseUtil.wrapOrNotFound(
            savedQuestionDTO,
            HeaderUtil.createAlert(applicationName, "questionManagement.updated", questionDTO.getId().toString())
        );
    }

    /**
     * {@code PUT /question/:id} : Switch to an existing question status.
     *
     * @param id the id to switch question status.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the status switched question.
     */
    @PatchMapping("/{id}")
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MODERATOR + "\")")
    @ApiOperation(value = "Switch status", notes = "This method will to switch a question status")
    public ResponseEntity<QuestionDTO> switchQuestionStatus(@PathVariable Long id) {
        log.debug("REST request to switch status of Question ID : {}", id.toString());

        Optional<QuestionDTO> questionDTO = questionService.switchQuestionStatus(id);
        return ResponseUtil.wrapOrNotFound(
            questionDTO,
            HeaderUtil.createAlert(applicationName, "questionManagement.updated", id.toString())
        );
    }

    /**
     * {@code GET /question/:id} : get the "id" question.
     *
     * @param id the id of the question to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "id" question, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MODERATOR + "\")")
    @ApiOperation(value = "Get one question", notes = "This method get one question")
    public ResponseEntity<QuestionDTO> getQuestion(@PathVariable Long id) {
        log.debug("REST request to get Question : {}", id);
        return ResponseUtil.wrapOrNotFound(questionService.getQuestion(id).map(QuestionDTO::new));
    }

    /**
     * {@code GET /question} : get all questions with all the details - calling this are only allowed for the moderator.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all questions.
     */
    @GetMapping("/by-meeting")
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MODERATOR + "\")")
    @ApiOperation(value = "Get questions by meeting", notes = "This method get all questions by Meeting")
    public ResponseEntity<List<QuestionDTO>> getAllQuestionsByMeeting(@RequestParam Long meetingId, Pageable pageable) {
        log.debug("REST request to get all Questions by Meeting ID: " + meetingId);

        final Page<QuestionDTO> page = questionService.getAllQuestionsByMeeting(meetingId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code GET /question} : get all questions with all the details - calling this are only allowed for the moderator.
     *
     * @param id the member id for search questions.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all questions.
     */
    @GetMapping("/by-member/{id}")
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MODERATOR + "\")")
    @ApiOperation(value = "Get question", notes = "This method get all questions by Member")
    public ResponseEntity<List<QuestionDTO>> getAllQuestionsByMember(@PathVariable Long id) {
        log.debug("REST request to get all Questions by Member ID: " + id);

        List<QuestionDTO> questions = questionService.getAllQuestionsByMember(id);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    /**
     * {@code DELETE /question/:id} : delete the "id" question.
     *
     * @param id the id of the question to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    //  @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MODERATOR + "\")")
    @ApiOperation(value = "Delete question", notes = "This method to delete question")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        log.debug("REST request to delete Question: {}", id);
        questionService.deleteQuestion(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createAlert(applicationName, "questionManagement.deleted", id.toString()))
            .build();
    }
}
