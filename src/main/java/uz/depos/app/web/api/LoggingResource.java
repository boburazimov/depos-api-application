package uz.depos.app.web.api;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import uz.depos.app.repository.CompanyRepository;
import uz.depos.app.repository.MeetingRepository;
import uz.depos.app.repository.MemberRepository;
import uz.depos.app.repository.UserRepository;
import uz.depos.app.service.MeetingLoggingService;
import uz.depos.app.service.MeetingService;
import uz.depos.app.service.dto.MeetingLoggingDTO;
import uz.depos.app.service.view.View;

@RestController
@RequestMapping(path = "/api/logging")
@Api(tags = "Logging")
public class LoggingResource {

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Logger log = LoggerFactory.getLogger(LoggingResource.class);

    final MeetingService meetingService;
    final MeetingRepository meetingRepository;
    final CompanyRepository companyRepository;
    final MeetingLoggingService meetingLoggingService;
    final UserRepository userRepository;
    final MemberRepository memberRepository;

    public LoggingResource(
        MeetingService meetingService,
        MeetingRepository meetingRepository,
        CompanyRepository companyRepository,
        MeetingLoggingService meetingLoggingService,
        UserRepository userRepository,
        MemberRepository memberRepository
    ) {
        this.meetingService = meetingService;
        this.meetingRepository = meetingRepository;
        this.companyRepository = companyRepository;
        this.meetingLoggingService = meetingLoggingService;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
    }

    /**
     * {@code POST  /logging}  : Creates a new Meeting-logging.
     * Creates a newMeeting-logging if the meeting and user is not empty, and return MeetingLoggingDTO
     *
     * @param loggingDTO the Meeting-logging to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new  Meeting-logging, or with status {@code 400 (Bad Request)} if the required fields empty.
     * and have incorrect id.
     * @throws URISyntaxException if the required fields is Empty.
     */
    @PostMapping
    @ApiOperation(value = "Create Meeting-logging", notes = "This method to add logging for current meeting.")
    public ResponseEntity<MeetingLoggingDTO> createLogging(
        @Valid @RequestBody @JsonView(value = View.ModelView.Post.class) MeetingLoggingDTO loggingDTO
    ) throws URISyntaxException {
        log.debug("REST request to add Logging to Meeting ID : {}", loggingDTO.getMeetingId());

        MeetingLoggingDTO meetingLoggingDTO = meetingLoggingService.addMeetingLogging(loggingDTO);
        return ResponseEntity
            .created(new URI("/api/meeting/logging"))
            .headers(HeaderUtil.createAlert(applicationName, "loggingManagement.created", meetingLoggingDTO.getLoggingText()))
            .body(meetingLoggingDTO);
    }

    /**
     * {@code PUT /logging} : Updates an existing Logging.
     *
     * @param id the id to update logging.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated logging.
     */
    @PatchMapping("/{id}")
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MODERATOR + "\")")
    @ApiOperation(value = "Switch status", notes = "This method will to switch a logging status")
    public ResponseEntity<MeetingLoggingDTO> switchLoggingStatus(@PathVariable Long id) {
        log.debug("REST request to update status Logging : {}", id.toString());

        Optional<MeetingLoggingDTO> loggingDTO = meetingLoggingService.switchLoggingStatus(id);
        return ResponseUtil.wrapOrNotFound(loggingDTO, HeaderUtil.createAlert(applicationName, "loggingManagement.updated", id.toString()));
    }

    /**
     * {@code GET /logging/:id} : get the "id" meeting-logging.
     *
     * @param id the id of the meeting-logging to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "id" meeting-logging, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MODERATOR + "\")")
    @ApiOperation(value = "Get logging", notes = "This method get one logging")
    public ResponseEntity<MeetingLoggingDTO> getMeetingLogging(@PathVariable Long id) {
        log.debug("REST request to get Meeting-logging : {}", id);
        return ResponseUtil.wrapOrNotFound(meetingLoggingService.getMeetingLogging(id).map(MeetingLoggingDTO::new));
    }

    /**
     * {@code GET /logging} : get all meeting-loggings with all the details - calling this are only allowed for the moderator.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all meeting-loggings.
     */
    @GetMapping("/by-meeting")
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MODERATOR + "\")")
    @ApiOperation(value = "Get loggings", notes = "This method get all loggings by Meeting")
    public ResponseEntity<List<MeetingLoggingDTO>> getAllLoggingsByMeeting(@RequestParam Long meetingId) {
        log.debug("REST request to get all Meeting-loggings.");
        List<MeetingLoggingDTO> loggingDTOS = meetingLoggingService.getAllLoggingsByMeeting(meetingId);
        return ResponseEntity.status(HttpStatus.OK).body(loggingDTOS);
        //        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        //        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code DELETE /logging/:id} : delete the "id" Logging.
     *
     * @param id the id of the meeting-logging to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    //  @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MODERATOR + "\")")
    @ApiOperation(value = "Delete logging", notes = "This method to delete logging")
    public ResponseEntity<Void> deleteMeetingLogging(@PathVariable Long id) {
        log.debug("REST request to delete Meeting-logging: {}", id);
        meetingLoggingService.deleteMeetingLogging(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createAlert(applicationName, "loggingManagement.deleted", id.toString()))
            .build();
    }
}
