package uz.depos.app.web.api;

import com.fasterxml.jackson.annotation.JsonView;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import uz.depos.app.repository.AgendaRepository;
import uz.depos.app.service.AgendaService;
import uz.depos.app.service.dto.AgendaAndOptionsDTO;
import uz.depos.app.service.dto.AgendaDTO;
import uz.depos.app.service.dto.AgendaEditDTO;
import uz.depos.app.service.view.View;
import uz.depos.app.web.rest.errors.AgendaSubjectAlreadyUsedException;
import uz.depos.app.web.rest.errors.BadRequestAlertException;

@RestController
@RequestMapping("/api/agenda")
@Api(tags = "Agenda")
public class AgendaResource {

    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections.unmodifiableList(
        Arrays.asList("id", "meetingId", "subject", "speakerId", "speakTimeEnum", "debateEnum", "isActive")
    );

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Logger log = LoggerFactory.getLogger(AgendaResource.class);

    final AgendaRepository agendaRepository;
    final AgendaService agendaService;

    public AgendaResource(AgendaRepository agendaRepository, AgendaService agendaService) {
        this.agendaRepository = agendaRepository;
        this.agendaService = agendaService;
    }

    /**
     * {@code POST  /agenda} : register the Agenda.
     *
     * @param agendaDTO the managed agenda View Model.
     * @return AgendaDTO with status {@code 201 (Created)}
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the id is already used.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MODERATOR + "\")")
    @ApiOperation(value = "Create agenda", notes = "This method creates a new agenda")
    public ResponseEntity<AgendaAndOptionsDTO> createAgenda(
        @Valid @RequestBody @JsonView(value = View.ModelView.Post.class) AgendaDTO agendaDTO
    ) throws URISyntaxException {
        log.debug("REST request to create Agenda : {}", agendaDTO);

        AgendaAndOptionsDTO agendaAndOptionsDTO = agendaService.createAgenda(agendaDTO);
        return ResponseEntity
            .created(new URI("/api/agenda/" + agendaAndOptionsDTO.getId()))
            .headers(HeaderUtil.createAlert(applicationName, "agendaManagement.created", agendaAndOptionsDTO.getSubject()))
            .body(agendaAndOptionsDTO);
    }

    /**
     * {@code GET /agenda/:ID} : get the "id" agenda.
     *
     * @param id the id of the agenda to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "ID" agenda, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MODERATOR + "\")")
    @ApiOperation(value = "Get agenda", notes = "This method to get one agenda by ID")
    public ResponseEntity<AgendaEditDTO> getAgenda(@PathVariable Long id) {
        log.debug("REST request to get Agenda : {}", id);
        return ResponseUtil.wrapOrNotFound(agendaService.getAgendaDTO(id));
    }

    /**
     * {@code GET /agenda} : get all agendas with all the details.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all agendas.
     */
    @GetMapping
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MODERATOR + "\")")
    @ApiOperation(value = "Get agendas", notes = "This method to get all agendas in pageable")
    public ResponseEntity<List<AgendaDTO>> getAllAgendas(Pageable pageable) {
        log.debug("REST request to get all Agendas");
        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest().build();
        }
        final Page<AgendaDTO> page = agendaService.getAllAgendas(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code GET /agendas} : get agendas by the meeting with all the details - calling this are only allowed for the administrators.
     *
     * @param meetingId the member ID for search by him.
     * @param pageable  the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all agendas by the meeting.
     */
    @GetMapping("/by-meeting")
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @ApiOperation(value = "Get agendas by meeting", notes = "This method to get agendas by meeting ID")
    public ResponseEntity<List<AgendaAndOptionsDTO>> getAgendasByMeeting(@RequestParam Long meetingId, Pageable pageable) {
        log.debug("REST request to get Agendas by Meeting ID: " + meetingId);
        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest().build();
        }
        final Page<AgendaAndOptionsDTO> page = agendaService.getAgendasByMeeting(meetingId, pageable);
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
     * {@code PUT /agenda} : Updates an existing agenda.
     *
     * @param agendaDTO the agenda to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agenda.
     * @throws AgendaSubjectAlreadyUsedException {@code 400 (Bad Request)} if the subject is already use in this meeting.
     */
    @PutMapping
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @ApiOperation(value = "Update agenda", notes = "This method to update agenda fields")
    public ResponseEntity<AgendaEditDTO> updateAgenda(
        @Valid @RequestBody @JsonView(value = View.ModelView.PUT.class) AgendaEditDTO agendaDTO
    ) {
        log.debug("REST request to update Agenda : {}", agendaDTO);

        Optional<AgendaEditDTO> updatedAgendaDTO = agendaService.updateAgenda(agendaDTO);
        return ResponseUtil.wrapOrNotFound(
            updatedAgendaDTO,
            HeaderUtil.createAlert(applicationName, "agendaManagement.edited", agendaDTO.getSubject())
        );
    }

    /**
     * {@code PATCH /agenda} : Switch to status an existing agenda with adding comment.
     *
     * @param agendaDTO the agenda to switch.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agenda.
     * @throws AgendaSubjectAlreadyUsedException {@code 400 (Bad Request)} if the subject is already use in this meeting.
     */
    @PatchMapping
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @ApiOperation(value = "Switch status", notes = "This method to switch agenda status with additional comment")
    public ResponseEntity<AgendaDTO> switchAgendaStatus(
        @Valid @RequestBody @JsonView(value = View.ModelView.PATCH.class) AgendaDTO agendaDTO
    ) {
        log.debug("REST request to switch agenda status : {}", agendaDTO);

        Optional<AgendaDTO> updatedAgendaDTO = agendaService.switchAgendaStatus(agendaDTO);
        return ResponseUtil.wrapOrNotFound(
            updatedAgendaDTO,
            HeaderUtil.createAlert(applicationName, "agendaManagement.edited", agendaDTO.getId().toString())
        );
    }

    /**
     * {@code DELETE /agenda/:id} : delete the "id" Agenda.
     *
     * @param id the id of the agenda to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MODERATOR + "\")")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.debug("REST request to delete Agenda: {}", id);

        agendaService.deleteAgenda(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createAlert(applicationName, "agendaManagement.deleted", id.toString()))
            .build();
    }
}
