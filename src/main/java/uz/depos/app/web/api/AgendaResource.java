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
import uz.depos.app.repository.AgendaRepository;
import uz.depos.app.service.AgendaService;
import uz.depos.app.service.dto.AgendaDTO;
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
    public ResponseEntity<AgendaDTO> createAgenda(@Valid @RequestBody AgendaDTO agendaDTO) throws URISyntaxException {
        log.debug("REST request to create Agenda : {}", agendaDTO);

        if (agendaDTO.getId() != null && agendaDTO.getId() > 0) {
            throw new BadRequestAlertException("A new agenda cannot already have an ID", "userManagement", "idexists");
        }

        agendaRepository
            .findOneBySubjectContainsAndMeetingId(agendaDTO.getSubject(), agendaDTO.getMeetingId())
            .ifPresent(
                agenda -> {
                    if (Objects.equals(agendaDTO.getSubject(), agenda.getSubject())) {
                        throw new AgendaSubjectAlreadyUsedException();
                    }
                }
            );

        AgendaDTO savedAgendaDTO = agendaService.createAgenda(agendaDTO);
        return ResponseEntity
            .created(new URI("/api/agenda/" + savedAgendaDTO.getId()))
            .headers(HeaderUtil.createAlert(applicationName, "agendaManagement.created", savedAgendaDTO.getSubject()))
            .body(savedAgendaDTO);
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
    public ResponseEntity<AgendaDTO> getAgenda(@PathVariable Long id) {
        log.debug("REST request to get Agenda : {}", id);
        return ResponseUtil.wrapOrNotFound(agendaService.getAgenda(id).map(AgendaDTO::new));
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
    public ResponseEntity<AgendaDTO> updateAgenda(@Valid @RequestBody AgendaDTO agendaDTO) {
        log.debug("REST request to update Agenda : {}", agendaDTO);

        agendaRepository
            .findOneBySubjectContainsAndMeetingId(agendaDTO.getSubject(), agendaDTO.getMeetingId())
            .ifPresent(
                agenda -> {
                    if (!agenda.getId().equals(agendaDTO.getId())) {
                        if (Objects.equals(agendaDTO.getSubject(), agenda.getSubject())) throw new AgendaSubjectAlreadyUsedException();
                    }
                }
            );

        Optional<AgendaDTO> updatedAgendaDTO = agendaService.updateAgenda(agendaDTO);

        return ResponseUtil.wrapOrNotFound(
            updatedAgendaDTO,
            HeaderUtil.createAlert(applicationName, "agendaManagement.edited", agendaDTO.getSubject())
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
