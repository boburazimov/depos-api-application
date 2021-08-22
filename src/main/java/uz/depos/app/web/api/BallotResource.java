package uz.depos.app.web.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import uz.depos.app.repository.*;
import uz.depos.app.service.BallotService;
import uz.depos.app.service.dto.BallotDTO;
import uz.depos.app.web.rest.errors.BadRequestAlertException;
import uz.depos.app.web.rest.errors.EmailAlreadyUsedException;
import uz.depos.app.web.rest.errors.LoginAlreadyUsedException;

@RestController
@RequestMapping(path = "/api/ballot")
@Api(tags = "Ballot")
public class BallotResource {

    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections.unmodifiableList(
        Arrays.asList("id", "agendaId", "meetingId", "agendaId", "votingOptionId", "options")
    );

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Logger log = LoggerFactory.getLogger(BallotResource.class);

    final BallotRepository ballotRepository;
    final MeetingRepository meetingRepository;
    final MemberRepository memberRepository;
    final AgendaRepository agendaRepository;
    final VotingOptionRepository votingOptionRepository;
    final BallotService ballotService;

    public BallotResource(
        BallotRepository ballotRepository,
        MeetingRepository meetingRepository,
        MemberRepository memberRepository,
        AgendaRepository agendaRepository,
        VotingOptionRepository votingOptionRepository,
        BallotService ballotService
    ) {
        this.ballotRepository = ballotRepository;
        this.meetingRepository = meetingRepository;
        this.memberRepository = memberRepository;
        this.agendaRepository = agendaRepository;
        this.votingOptionRepository = votingOptionRepository;
        this.ballotService = ballotService;
    }

    /**
     * {@code POST  /ballot}  : Creates a new ballot.
     * <p>
     * Creates a new ballot if the meeting, agenda, member and voting-option is not empty, and return ballotDTO
     *
     * @param ballotDTO the ballot to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ballot, or with status {@code 400 (Bad Request)} if the others empty
     * and have incorrect id.
     * @throws NullPointerException      if the others is Empty.
     * @throws ResourceNotFoundException if the others id is not correct - Not found).
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create ballot", notes = "This method creates a new ballot")
    public ResponseEntity<BallotDTO> createBallot(@Valid @RequestBody BallotDTO ballotDTO) throws URISyntaxException {
        log.debug("REST request to create Ballot : {}", ballotDTO);

        if (ballotDTO.getId() != null && ballotDTO.getId() > 0) {
            throw new BadRequestAlertException("A new ballot cannot already have an ID", "ballotManagement", "idExists");
        } else if (ballotDTO.getMeetingId() == null || ballotDTO.getMeetingId() == 0) {
            throw new BadRequestAlertException("Meeting ID must NOT be null and zero", "ballotManagement", "meetingIDNull");
        } else if (ballotDTO.getMemberId() == null || ballotDTO.getMemberId() == 0) {
            throw new BadRequestAlertException("Member ID must NOT be null and zero", "ballotManagement", "memberIDNull");
        } else if (ballotDTO.getAgendaId() == null || ballotDTO.getAgendaId() == 0) {
            throw new BadRequestAlertException("Agenda ID must NOT be null and zero", "ballotManagement", "agendaIDNull");
        } else if (ballotDTO.getVotingOptionId() == null || ballotDTO.getVotingOptionId() == 0) {
            throw new BadRequestAlertException("Voting-option ID must NOT be null and zero", "ballotManagement", "votingOptionIDNull");
        } else if (!meetingRepository.existsById(ballotDTO.getMeetingId())) {
            throw new ResourceNotFoundException("Meeting not found by ID: " + ballotDTO.getMeetingId());
        } else if (!memberRepository.existsById(ballotDTO.getMemberId())) {
            throw new ResourceNotFoundException("Member not found by ID: " + ballotDTO.getMemberId());
        } else if (!agendaRepository.existsById(ballotDTO.getAgendaId())) {
            throw new ResourceNotFoundException("Agenda not found by ID: " + ballotDTO.getAgendaId());
        } else if (!votingOptionRepository.existsById(ballotDTO.getVotingOptionId())) {
            throw new ResourceNotFoundException("Voting-option not found by ID: " + ballotDTO.getVotingOptionId());
        }

        BallotDTO savedBallotDTO = ballotService.createBallot(ballotDTO);
        return ResponseEntity
            .created(new URI("/api/ballot/" + savedBallotDTO.getId()))
            .headers(HeaderUtil.createAlert(applicationName, "ballotManagement.created", savedBallotDTO.getId().toString()))
            .body(savedBallotDTO);
    }

    /**
     * {@code PUT /ballot} : Updates an existing Ballot.
     *
     * @param ballotDTO the user to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ballot.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already in use.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already in use.
     */
    @PutMapping("/users")
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @ApiOperation(value = "Update ballot", notes = "This method is update exist ballot")
    public ResponseEntity<BallotDTO> updateBallot(@Valid @RequestBody BallotDTO ballotDTO) {
        log.debug("REST request to update Ballot : {}", ballotDTO);
        Optional<BallotDTO> savedBallotDTO = ballotService.updateBallot(ballotDTO);

        return ResponseUtil.wrapOrNotFound(
            savedBallotDTO,
            HeaderUtil.createAlert(applicationName, "ballotManagement.updated", ballotDTO.getId().toString())
        );
    }

    /**
     * {@code GET /ballot/:id} : get the "id" ballot.
     *
     * @param id the id of the ballot to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "id" ballot, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @ApiOperation(value = "Get ballot", notes = "This method to get exist ballot")
    public ResponseEntity<BallotDTO> getBallot(@PathVariable Long id) {
        log.debug("REST request to get Ballot : {}", id);
        return ResponseUtil.wrapOrNotFound(ballotService.getBallot(id).map(BallotDTO::new));
    }

    /**
     * {@code GET /ballots} : get all ballots with all the details - calling this are only allowed for the administrators.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all ballots.
     */
    @GetMapping
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @ApiOperation(value = "Get ballots", notes = "This method to get all ballots")
    public ResponseEntity<List<BallotDTO>> getAllBallots(Pageable pageable) {
        log.debug("REST request to get all Ballots");
        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest().build();
        }

        final Page<BallotDTO> page = ballotService.getAllBallots(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code CHECK :pageable} : check the "pageable" Ballot.
     *
     * @param pageable the pageable of the ballots for check sort variable, is contains in Allowed properties.
     * @return the {@link Pageable} is sort var is contain}.
     */
    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }

    /**
     * {@code DELETE /ballot/:id} : delete the "id" Ballot.
     *
     * @param id the id of the ballot to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.debug("REST request to delete Ballot: {}", id);
        ballotService.deleteBallot(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createAlert(applicationName, "ballotManagement.deleted", id.toString()))
            .build();
    }
}
