package uz.depos.app.web.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.undertow.util.BadRequestException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.depos.app.domain.Meeting;
import uz.depos.app.domain.Member;
import uz.depos.app.domain.User;
import uz.depos.app.repository.CompanyRepository;
import uz.depos.app.repository.MeetingRepository;
import uz.depos.app.repository.MemberRepository;
import uz.depos.app.repository.UserRepository;
import uz.depos.app.service.FilesStorageService;
import uz.depos.app.service.MemberService;
import uz.depos.app.service.ReestrService;
import uz.depos.app.service.dto.AttachReestrDTO;
import uz.depos.app.service.mapper.ExcelHelpers;
import uz.depos.app.service.mapper.MemberMapper;
import uz.depos.app.web.rest.errors.BadRequestAlertException;

@RestController
@RequestMapping(path = "/api/reestr")
@Api(tags = "Reestr")
public class ReestrResource {

    private final Logger log = LoggerFactory.getLogger(ReestrResource.class);

    final ReestrService reestrService;
    final MeetingRepository meetingRepository;
    final UserRepository userRepository;
    final MemberRepository memberRepository;
    final MemberMapper memberMapper;
    final ExcelHelpers excelHelpers;
    final FilesStorageService filesStorageService;
    final MemberService memberService;
    final CompanyRepository companyRepository;

    public ReestrResource(
        ReestrService reestrService,
        MeetingRepository meetingRepository,
        UserRepository userRepository,
        MemberRepository memberRepository,
        MemberMapper memberMapper,
        ExcelHelpers excelHelpers,
        FilesStorageService filesStorageService,
        MemberService memberService,
        CompanyRepository companyRepository
    ) {
        this.reestrService = reestrService;
        this.meetingRepository = meetingRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
        this.excelHelpers = excelHelpers;
        this.filesStorageService = filesStorageService;
        this.memberService = memberService;
        this.companyRepository = companyRepository;
    }

    /**
     * {@code POST  /reestr}  : Creates a new reestr.
     * <p>
     * Creates a new reestr if the meeting ID and member ID is not empty, and return reestrDTO
     *
     * @param file, @param meetingId the reestr to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reestr, or with status {@code 400 (Bad Request)} if the company empty
     * and have incorrect id.
     * @throws NullPointerException      if the reestr is Empty.
     * @throws ResourceNotFoundException if the reestr id is not correct - Not found).
     */
    @PostMapping(value = "/parse", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "Parse reestr", notes = "This method parsed rows from reestr to member")
    public ResponseEntity<?> parseReestr(@RequestPart MultipartFile file, @RequestParam("meetingId") Long meetingId)
        throws URISyntaxException, IOException, BadRequestException {
        log.debug("REST request to create Reestr : {}", file);

        try {
            // Clear previous members from current Meeting before parse.
            memberService.deleteAllByMeetingId(meetingId);
            // Remove previous Reestr Excel file from DB and static folder.
            filesStorageService.deleteReestrFile(meetingId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!excelHelpers.hasExcelFormat(file)) { // Check to excel format
            throw new BadRequestAlertException("File has not Excel format!", "reestrManagement", "excelFormat");
        } else if (file.isEmpty()) {
            throw new NullPointerException();
        } else if (meetingId == null || meetingId == 0) {
            throw new BadRequestAlertException("MeetingID must NOT be null and zero", "reestrManagement", "meetingIDNull");
        } else if (!meetingRepository.findById(meetingId).isPresent()) throw new BadRequestAlertException(
            "Meeting not found by ID: " + meetingId,
            "reestrManagement",
            "notFound"
        );

        AttachReestrDTO attachReestrDTO = reestrService.parse(file, meetingId);
        if (attachReestrDTO != null) {
            //            memberRepository.findAllByMeetingIdAndFromReestrTrue(meetingId).ifPresent(
            //                members -> {
            //                    members.forEach(member -> {
            //                        if (meetingRepository.findById(meetingId).isPresent() && userRepository.findById(member.getUser().getId()).isPresent()) {
            //                            Meeting meeting = meetingRepository.findById(meetingId).get();
            //                            User user = userRepository.findById(member.getUser().getId()).get();
            //                            mailService.sendInvitationEmail(user, meeting, password);
            //                        }
            //                    });
            //            });
            log.debug("Changed Information for Reestr: {}", attachReestrDTO);
            return ResponseEntity.status(HttpStatus.OK).body(attachReestrDTO);
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(meetingId);
        }
    }

    /**
     * {@code GET  /download} : Download reestr by Meeting ID.
     *
     * @param meetingId for search members by him.
     * @return Excel file contains members by one meeting.
     */
    @GetMapping("/download")
    @ApiOperation(value = "Download reestr", notes = "This method download members by Meeting ID")
    public ResponseEntity<Resource> getFile(@RequestParam("meetingId") Long meetingId) {
        log.debug("REST request to download Reestr by Meeting ID : {}", meetingId);
        String filename = "Reestr (MeetingID: " + meetingId + ").xlsx";
        InputStreamResource file = new InputStreamResource(reestrService.getExcelReestr(meetingId));

        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
            .body(file);
    }
}
