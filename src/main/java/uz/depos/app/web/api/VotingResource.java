package uz.depos.app.web.api;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import uz.depos.app.repository.VotingRepository;
import uz.depos.app.service.VotingService;
import uz.depos.app.service.dto.VotingDTO;
import uz.depos.app.service.dto.VotingEditDTO;
import uz.depos.app.service.view.View;
import uz.depos.app.web.rest.errors.BadRequestAlertException;
import uz.depos.app.web.rest.errors.VotinOptionTextAlreadyUsedException;

@RestController
@RequestMapping("/api/voting")
@Api(tags = "Voting")
public class VotingResource {

    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections.unmodifiableList(
        Arrays.asList("id", "votingText", "optionTypeEnum", "meetingId", "agendaId")
    );

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Logger log = LoggerFactory.getLogger(VotingResource.class);

    final VotingRepository votingRepository;
    final VotingService votingService;

    public VotingResource(VotingRepository votingRepository, VotingService votingService) {
        this.votingRepository = votingRepository;
        this.votingService = votingService;
    }

    /**
     * {@code POST  /voting} : register the VotingOption.
     *
     * @param votingDTO the managed agenda View Model.
     * @return VotingDTO with status {@code 201 (Created)}
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the id is already used.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MODERATOR + "\")")
    @ApiOperation(value = "Create voting", notes = "This method creates a new votingOption")
    public ResponseEntity<VotingDTO> createVoting(@Valid @RequestBody @JsonView(value = View.ModelView.Post.class) VotingDTO votingDTO)
        throws URISyntaxException {
        log.debug("REST request to create VotingOption : {}", votingDTO);

        if (votingDTO.getId() != null && votingDTO.getId() > 0) {
            throw new BadRequestAlertException("A new votingOption cannot already have an ID", "votingOptionManagement", "idexists");
        }

        votingRepository
            .findOneByVotingTextAndAgendaId(votingDTO.getVotingText(), votingDTO.getAgendaId())
            .ifPresent(
                votingOption -> {
                    if (Objects.equals(votingDTO.getVotingText(), votingOption.getVotingText())) {
                        throw new VotinOptionTextAlreadyUsedException();
                    }
                }
            );

        VotingDTO savedVotingDTO = votingService.createVotingOption(votingDTO);
        return ResponseEntity
            .created(new URI("/api/voting/" + savedVotingDTO.getId()))
            .headers(HeaderUtil.createAlert(applicationName, "votingOptionManagement.created", savedVotingDTO.getVotingText()))
            .body(savedVotingDTO);
    }

    /**
     * {@code GET /voting/:ID} : get the "id" votingOption.
     *
     * @param id the id of the votingOption to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "ID" votingOption, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MODERATOR + "\")")
    @ApiOperation(value = "Get one voting", notes = "This method to get one votingOption by ID")
    public ResponseEntity<VotingDTO> getVoting(@PathVariable Long id) {
        log.debug("REST request to get VotingOption : {}", id);
        return ResponseUtil.wrapOrNotFound(votingService.getVotingOption(id).map(VotingDTO::new));
    }

    /**
     * {@code GET /voting} : get all votingOptions with all the details.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all votingOptions.
     */
    @GetMapping
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MODERATOR + "\")")
    @ApiOperation(value = "Get all votings", notes = "This method to get all votingOptions in pageable")
    public ResponseEntity<List<VotingDTO>> getAllVotings(Pageable pageable) {
        log.debug("REST request to get all VotingOptions");

        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest().build();
        }

        final Page<VotingDTO> page = votingService.getAllVotingOptions(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code GET /votings} : get votings by the meeting with all the details.
     *
     * @param meetingId the meeting ID for search by him.
     * @param pageable  the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all votings by the meeting.
     */
    @GetMapping("/by-meeting")
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @ApiOperation(value = "Get votings by meeting", notes = "This method to get votings by meeting ID")
    public ResponseEntity<List<VotingDTO>> getVotingsByMeeting(@RequestParam Long meetingId, Pageable pageable) {
        log.debug("REST request to get Votings by Meeting ID: " + meetingId);

        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest().build();
        }

        final Page<VotingDTO> page = votingService.getVotingsByMeeting(meetingId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code GET /votings} : get votings by the agenda with all the details.
     *
     * @param agendaId the member ID for search by him.
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all votings by the agenda.
     */
    @GetMapping("/by-agenda")
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @ApiOperation(value = "Get votings by agenda", notes = "This method to get votings by agenda ID")
    public ResponseEntity<List<VotingDTO>> getVotingsByAgenda(@RequestParam Long agendaId, Pageable pageable) {
        log.debug("REST request to get Votings by Agenda ID: " + agendaId);

        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest().build();
        }

        final Page<VotingDTO> page = votingService.getVotingsByAgenda(agendaId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * @param pageable for check
     * @return Checked pageable for allowed ordered properties
     */
    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }

    /**
     * {@code PUT /voting} : Updates an existing votingOptions.
     *
     * @param votingDTO the votingOptions to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated votingOptions.
     * @throws VotinOptionTextAlreadyUsedException {@code 400 (Bad Request)} if the votingText is already use in this agenda.
     */
    @PutMapping
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @ApiOperation(value = "Update voting", notes = "This method to update votingOption fields")
    public ResponseEntity<VotingEditDTO> updateVoting(
        @Valid @RequestBody @JsonView(value = View.ModelView.PUT.class) VotingEditDTO votingDTO
    ) {
        log.debug("REST request to update Voting : {}", votingDTO);

        votingRepository
            .findById(votingDTO.getId())
            .flatMap(
                votingOption ->
                    votingRepository.findOneByVotingTextAndAgendaId(votingDTO.getVotingText(), votingOption.getMeeting().getId())
            )
            .ifPresent(
                votingOption1 -> {
                    if (!votingOption1.getId().equals(votingDTO.getId())) {
                        if (
                            Objects.equals(votingDTO.getVotingText(), votingOption1.getVotingText())
                        ) throw new VotinOptionTextAlreadyUsedException();
                    }
                }
            );

        Optional<VotingEditDTO> updatedVotingOptionDTO = votingService.updateVotingOption(votingDTO);

        return ResponseUtil.wrapOrNotFound(
            updatedVotingOptionDTO,
            HeaderUtil.createAlert(applicationName, "votingOptionManagement.edited", votingDTO.getVotingText())
        );
    }

    /**
     * {@code DELETE /voting/:id} : delete the "id" VotingOption.
     *
     * @param id the id of the VotingOption to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MODERATOR + "\")")
    public ResponseEntity<Void> deleteVotingOption(@PathVariable Long id) {
        log.debug("REST request to delete VotingOption: {}", id);

        votingService.deleteVotingOption(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createAlert(applicationName, "votingOptionManagement.deleted", id.toString()))
            .build();
    }
}
