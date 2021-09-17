package uz.depos.app.web.api;

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
import uz.depos.app.repository.VotingOptionRepository;
import uz.depos.app.service.VotingService;
import uz.depos.app.service.dto.VotingDTO;
import uz.depos.app.web.rest.errors.BadRequestAlertException;
import uz.depos.app.web.rest.errors.VotinOptionTextAlreadyUsedException;

@RestController
@RequestMapping("/api/voting")
@Api(tags = "Voting-option")
public class VotingOptionResource {

    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections.unmodifiableList(
        Arrays.asList("id", "votingText", "optionTypeEnum", "meetingId", "agendaId")
    );

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Logger log = LoggerFactory.getLogger(VotingOptionResource.class);

    final VotingOptionRepository votingOptionRepository;
    final VotingService votingService;

    public VotingOptionResource(VotingOptionRepository votingOptionRepository, VotingService votingService) {
        this.votingOptionRepository = votingOptionRepository;
        this.votingService = votingService;
    }

    /**
     * {@code POST  /voting_option} : register the VotingOption.
     *
     * @param votingDTO the managed agenda View Model.
     * @return VotingOptionDTO with status {@code 201 (Created)}
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the id is already used.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MODERATOR + "\")")
    @ApiOperation(value = "Create votingOption", notes = "This method creates a new votingOption")
    public ResponseEntity<VotingDTO> createVotingOption(@Valid @RequestBody VotingDTO votingDTO) throws URISyntaxException {
        log.debug("REST request to create VotingOption : {}", votingDTO);

        if (votingDTO.getId() != null && votingDTO.getId() > 0) {
            throw new BadRequestAlertException("A new votingOption cannot already have an ID", "votingOptionManagement", "idexists");
        }

        votingOptionRepository
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
     * {@code GET /voting_option/:ID} : get the "id" votingOption.
     *
     * @param id the id of the votingOption to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "ID" votingOption, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MODERATOR + "\")")
    @ApiOperation(value = "Get votingOption", notes = "This method to get one votingOption by ID")
    public ResponseEntity<VotingDTO> getVotingOption(@PathVariable Long id) {
        log.debug("REST request to get VotingOption : {}", id);
        return ResponseUtil.wrapOrNotFound(votingService.getVotingOption(id).map(VotingDTO::new));
    }

    /**
     * {@code GET /voting_option} : get all votingOptions with all the details.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all votingOptions.
     */
    @GetMapping
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MODERATOR + "\")")
    @ApiOperation(value = "Get votingOptions", notes = "This method to get all votingOptions in pageable")
    public ResponseEntity<List<VotingDTO>> getAllVotingOptions(Pageable pageable) {
        log.debug("REST request to get all VotingOptions");

        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest().build();
        }

        final Page<VotingDTO> page = votingService.getAllVotingOptions(pageable);
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
     * {@code PUT /voting_option} : Updates an existing votingOptions.
     *
     * @param votingDTO the votingOptions to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated votingOptions.
     * @throws VotinOptionTextAlreadyUsedException {@code 400 (Bad Request)} if the votingText is already use in this agenda.
     */
    @PutMapping
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @ApiOperation(value = "Update votingOption", notes = "This method to update votingOption fields")
    public ResponseEntity<VotingDTO> updateVotingOption(@Valid @RequestBody VotingDTO votingDTO) {
        log.debug("REST request to update VotingOption : {}", votingDTO);

        votingOptionRepository
            .findOneByVotingTextAndAgendaId(votingDTO.getVotingText(), votingDTO.getMeetingId())
            .ifPresent(
                votingOption -> {
                    if (!votingOption.getId().equals(votingDTO.getId())) {
                        if (
                            Objects.equals(votingDTO.getVotingText(), votingOption.getVotingText())
                        ) throw new VotinOptionTextAlreadyUsedException();
                    }
                }
            );

        Optional<VotingDTO> updatedVotingOptionDTO = votingService.updateVotingOption(votingDTO);

        return ResponseUtil.wrapOrNotFound(
            updatedVotingOptionDTO,
            HeaderUtil.createAlert(applicationName, "votingOptionManagement.edited", votingDTO.getVotingText())
        );
    }

    /**
     * {@code DELETE /voting_option/:id} : delete the "id" VotingOption.
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
