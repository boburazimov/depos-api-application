package uz.depos.app.web.api;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import uz.depos.app.domain.Meeting;
import uz.depos.app.domain.enums.MeetingSearchFieldEnum;
import uz.depos.app.domain.enums.MeetingStatusEnum;
import uz.depos.app.repository.CompanyRepository;
import uz.depos.app.repository.MeetingRepository;
import uz.depos.app.service.MeetingLoggingService;
import uz.depos.app.service.MeetingService;
import uz.depos.app.service.dto.MeetingDTO;
import uz.depos.app.service.dto.MeetingFilterDTO;
import uz.depos.app.service.view.View;
import uz.depos.app.web.rest.errors.LoginAlreadyUsedException;
import uz.depos.app.web.rest.errors.MeetingWithStartDateAlreadyCreatedException;

@RestController
@RequestMapping(path = "/api/meeting")
@Api(tags = "Meeting")
public class MeetingResource {

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Logger log = LoggerFactory.getLogger(MeetingResource.class);

    final MeetingService meetingService;
    final MeetingRepository meetingRepository;
    final CompanyRepository companyRepository;
    final MeetingLoggingService meetingLoggingService;
    final SimpMessageSendingOperations messageTemplate;

    public MeetingResource(
        MeetingService meetingService,
        MeetingRepository meetingRepository,
        CompanyRepository companyRepository,
        MeetingLoggingService meetingLoggingService,
        SimpMessageSendingOperations messageTemplate
    ) {
        this.meetingService = meetingService;
        this.meetingRepository = meetingRepository;
        this.companyRepository = companyRepository;
        this.meetingLoggingService = meetingLoggingService;
        this.messageTemplate = messageTemplate;
    }

    /**
     * {@code POST  /meeting/users}  : Creates a new meeting.
     * <p>
     * Creates a new meeting if the company is not empty, and return meetingDTO
     *
     * @param meetingDTO the meeting to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new meeting, or with status {@code 400 (Bad Request)} if the company empty
     * and have incorrect id.
     * @throws NullPointerException      if the company is Empty.
     * @throws ResourceNotFoundException if the company id is not correct - Not found).
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create meeting", notes = "This method creates a new meeting")
    public ResponseEntity<MeetingDTO> createMeeting(@Valid @RequestBody MeetingDTO meetingDTO) throws URISyntaxException {
        log.debug("REST request to create Meeting : {}", meetingDTO);

        MeetingDTO meeting = meetingService.createMeeting(meetingDTO);
        return ResponseEntity
            .created(new URI("/api/meeting/" + meeting.getId()))
            .headers(HeaderUtil.createAlert(applicationName, "meetingManagement.created", meeting.getDescription()))
            .body(meeting);
    }

    /**
     * {@code PUT /meeting} : Updates an existing Meeting.
     *
     * @param meetingDTO the meeting to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated meeting.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already in use.
     */
    @PutMapping
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MODERATOR + "\")")
    @ApiOperation(value = "Update meeting", notes = "This method updated a meeting")
    public ResponseEntity<MeetingDTO> updateMeeting(@Valid @RequestBody MeetingDTO meetingDTO) {
        log.debug("REST request to update Meeting : {}", meetingDTO);

        Optional<Meeting> existingMeeting = meetingRepository.findOneByStartDateAndCompanyId(
            meetingDTO.getStartDate(),
            meetingDTO.getCompanyId()
        );
        if (existingMeeting.isPresent() && (!existingMeeting.get().getId().equals(meetingDTO.getId()))) {
            throw new MeetingWithStartDateAlreadyCreatedException();
        } else if (ObjectUtils.isEmpty(meetingDTO.getCompanyId())) {
            throw new NullPointerException("Meeting have must a company");
        } else if (!companyRepository.existsById(meetingDTO.getCompanyId())) throw new ResourceNotFoundException(
            "Company not found by ID: " + meetingDTO.getCompanyId()
        );

        Optional<MeetingDTO> updatedMeeting = meetingService.updateMeeting(meetingDTO);
        return ResponseUtil.wrapOrNotFound(
            updatedMeeting,
            HeaderUtil.createAlert(applicationName, "meetingManagement.updated", meetingDTO.getDescription())
        );
    }

    /**
     * {@code GET /meeting/:id} : get the "id" meeting.
     *
     * @param id the id of the meeting to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "id" meeting, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MODERATOR + "\")")
    @ApiOperation(value = "Get meeting", notes = "This method get one meeting")
    public ResponseEntity<MeetingDTO> getMeeting(@PathVariable Long id) {
        log.debug("REST request to get Meeting : {}", id);
        return ResponseUtil.wrapOrNotFound(meetingService.getMeeting(id).map(MeetingDTO::new));
    }

    /**
     * {@code GET /meeting} : get all meetings with all the details - calling this are only allowed for the moderator.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all meetings.
     */
    @GetMapping
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MODERATOR + "\")")
    @ApiOperation(value = "Get meetings", notes = "This method get all meetings")
    public ResponseEntity<List<MeetingDTO>> getAllMeetings(Pageable pageable) {
        log.debug("REST request to get all Meeting for an moderator");

        final Page<MeetingDTO> page = meetingService.getAllMeetings(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code DELETE /meeting/:id} : delete the "id" User.
     *
     * @param id the id of the meeting to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MODERATOR + "\")")
    @ApiOperation(value = "Delete meeting", notes = "This method to delete meeting")
    public ResponseEntity<Void> deleteMeeting(@PathVariable Long id) {
        log.debug("REST request to delete Meeting: {}", id);
        meetingService.deleteMeeting(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createAlert(applicationName, "meetingManagement.deleted", id.toString()))
            .build();
    }

    /**
     * Filters for header table Meeting.
     *
     * @param field    - Column in the table (entity).
     * @param value    - fragment of word to search by him.
     * @param pageable - params for pageable.
     * @return - List of MeetingDTO.
     */
    @GetMapping("/filter")
    @ApiOperation(value = "Filter meeting", notes = "This method to filter meetings by field and search-value in pageable form.")
    public ResponseEntity<List<MeetingDTO>> filterMeeting(
        @RequestParam MeetingSearchFieldEnum field,
        @RequestParam String value,
        Pageable pageable
    ) {
        log.debug("REST request to filter Meetings field : {}", field);

        final Page<MeetingDTO> page = meetingService.filterMeeting(field, value, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code GET /meetings} : get meetings by the company with all the details.
     *
     * @param companyId the company ID for search by him.
     * @param pageable  the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all meetings by the company.
     */
    @GetMapping("/by-company")
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @ApiOperation(value = "Get meetings by company", notes = "This method to get meetings by company ID")
    public ResponseEntity<List<MeetingDTO>> getMeetingsByCompany(@RequestParam Long companyId, Pageable pageable) {
        log.debug("REST request to get meetings by company ID: " + companyId);

        final Page<MeetingDTO> page = meetingService.getMeetingsByCompany(companyId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code GET /meetings} : get meetings by the company and user with all the details.
     *
     * @param companyId the company ID for search by him.
     * @param userId    the user ID for search by him.
     * @param pageable  the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all meetings by the company and user.
     */
    @GetMapping("/by-user")
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @ApiOperation(value = "Get meetings by company and user", notes = "This method to get meetings by company ID and user ID")
    public ResponseEntity<List<MeetingDTO>> getMeetingsByCompanyAndUser(
        @RequestParam Long userId,
        @RequestParam Long companyId,
        Pageable pageable
    ) {
        log.debug("REST request to get meetings by company ID: " + companyId + " and user ID: " + userId);

        final Page<MeetingDTO> page = meetingService.getMeetingsByCompanyAndUser(userId, companyId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PatchMapping("/meeting-status")
    @ApiOperation(value = "Change status of meeting", notes = "This method to set status by Meeting ID and statusEnum")
    public ResponseEntity<MeetingDTO> changeMeetingStatus(@RequestParam Long meetingId, @RequestParam MeetingStatusEnum statusEnum) {
        log.debug("REST request to set active status for meeting by ID: " + meetingId);
        MeetingDTO meetingDTO = meetingService.changeMeetingStatus(meetingId, statusEnum);
        messageTemplate.convertAndSend("/topic/meeting-status/" + meetingDTO.getId(), meetingDTO);
        return ResponseEntity.status(HttpStatus.OK).body(meetingDTO);
    }

    /**
     * {@code POST /meeting/spec-filter} : get all meetings Filtered for header table by Specification interface.
     *
     * @param meetingDTO - Column in the table (entity).
     * @param pageable   - params for pageable.
     * @return - List of MeetingFilterDTO.
     */
    @PostMapping("/spec-filter")
    @ApiOperation(value = "Filter meetings", notes = "This method to get Meetings by Specification filter")
    public ResponseEntity<List<MeetingFilterDTO>> filterMeetings(
        @RequestBody @JsonView(value = View.ModelView.Post.class) MeetingFilterDTO meetingDTO,
        Pageable pageable
    ) {
        log.debug("REST request to filter Meetings by Specification interface : {}", meetingDTO);

        final Page<MeetingFilterDTO> page = meetingService.getFilteredMeetings(meetingDTO, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
