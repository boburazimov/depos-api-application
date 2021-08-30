package uz.depos.app.web.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.depos.app.domain.Member;
import uz.depos.app.repository.MeetingRepository;
import uz.depos.app.repository.MemberRepository;
import uz.depos.app.repository.UserRepository;
import uz.depos.app.service.ReestrService;
import uz.depos.app.service.dto.ReestrDTO;
import uz.depos.app.service.mapper.ExcelHelpers;
import uz.depos.app.service.mapper.MemberMapper;
import uz.depos.app.web.rest.errors.BadRequestAlertException;

@RestController
@RequestMapping(path = "/api/reestr")
@Api(tags = "Reestr")
public class ReestrResource {

    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections.unmodifiableList(
        Arrays.asList(
            "id",
            "meetingId",
            "memberId",
            "isChairmen",
            "hldNum",
            "hldName",
            "hldPinfl",
            "hldIt",
            "hldPass",
            "hldPhone",
            "hldEmail",
            "position"
        )
    );

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Logger log = LoggerFactory.getLogger(ReestrResource.class);

    final ReestrService reestrService;
    final MeetingRepository meetingRepository;
    final UserRepository userRepository;
    final MemberRepository memberRepository;
    final MemberMapper memberMapper;
    final ExcelHelpers excelHelpers;

    public ReestrResource(
        ReestrService reestrService,
        MeetingRepository meetingRepository,
        UserRepository userRepository,
        MemberRepository memberRepository,
        MemberMapper memberMapper,
        ExcelHelpers excelHelpers
    ) {
        this.reestrService = reestrService;
        this.meetingRepository = meetingRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
        this.excelHelpers = excelHelpers;
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
    public ResponseEntity<ReestrDTO> parseReestr(@RequestPart MultipartFile file, @RequestParam("meetingId") Long meetingId)
        throws URISyntaxException, IOException {
        log.debug("REST request to create Reestr : {}", file);

        if (!excelHelpers.hasExcelFormat(file)) {
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
        List<Member> members = reestrService.parse(file, meetingId);
        //
        //        memberMapper.memberToMemberDTO(members)
        //
        //        // TODO - Нужно сделать проверку, есть ли member с ID user и с ID meeting при загрузке реестра.
        //        // нужно взять в один массив пинфл реестра и проверить на повторяющийся элементы
        //        return ResponseEntity
        //            .created(new URI("/api/reestr/" + savedReestrDTO.getId()))
        //            .headers(HeaderUtil.createAlert(applicationName, "reestrManagement.created", savedReestrDTO.getId().toString()))
        //            .body(savedReestrDTO);
        return null;
    }
    //    /**
    //     * {@code PUT /reestr} : Updates an existing Reestr.
    //     *
    //     * @param reestrDTO the user to update.
    //     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reestr.
    //     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already in use.
    //     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already in use.
    //     */
    //    @PutMapping
    //    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    //    @ApiOperation(value = "Update reestr", notes = "This method is update exist reestr")
    //    public ResponseEntity<ReestrDTO> updateReestr(@Valid @RequestBody ReestrDTO reestrDTO) {
    //        log.debug("REST request to update Reestr : {}", reestrDTO);
    //        Optional<ReestrDTO> savedReestrDTO = reestrService.updateReestr(reestrDTO);
    //
    //        return ResponseUtil.wrapOrNotFound(
    //            savedReestrDTO,
    //            HeaderUtil.createAlert(applicationName, "reestrManagement.updated", reestrDTO.getId().toString())
    //        );
    //    }
    //
    //    /**
    //     * {@code GET /reestr/:id} : get the "id" reestr.
    //     *
    //     * @param id the id of the reestr to find.
    //     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "id" reestr, or with status {@code 404 (Not Found)}.
    //     */
    //    @GetMapping("/{id}")
    //    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    //    @ApiOperation(value = "Get reestr", notes = "This method to get exist reestr")
    //    public ResponseEntity<ReestrDTO> getReestr(@PathVariable Long id) {
    //        log.debug("REST request to get Reestr : {}", id);
    //        return ResponseUtil.wrapOrNotFound(reestrService.getReestr(id).map(ReestrDTO::new));
    //    }
    //
    //    /**
    //     * {@code GET /reestrs} : get all reestrs with all the details - calling this are only allowed for the administrators.
    //     *
    //     * @param pageable the pagination information.
    //     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all reestrs.
    //     */
    //    @GetMapping
    //    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    //    @ApiOperation(value = "Get reestrs", notes = "This method to get all reestrs")
    //    public ResponseEntity<List<ReestrDTO>> getAllReestrs(Pageable pageable) {
    //        log.debug("REST request to get all Reestrs");
    //        if (!onlyContainsAllowedProperties(pageable)) {
    //            return ResponseEntity.badRequest().build();
    //        }
    //
    //        final Page<ReestrDTO> page = reestrService.getAllReestrs(pageable);
    //        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
    //        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    //    }
    //
    //    /**
    //     * {@code CHECK :pageable} : check the "pageable" Reestr.
    //     *
    //     * @param pageable the pageable of the reestrs for check sort variable, is contains in Allowed properties.
    //     * @return the {@link Pageable} is sort var is contain}.
    //     */
    //    private boolean onlyContainsAllowedProperties(Pageable pageable) {
    //        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    //    }
    //
    //    /**
    //     * {@code DELETE /reestr/:id} : delete the "id" Reestr.
    //     *
    //     * @param id the id of the reestr to delete.
    //     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
    //     */
    //    @DeleteMapping("/{id}")
    //    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    //    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    //        log.debug("REST request to delete Reestr: {}", id);
    //        reestrService.deleteReestr(id);
    //        return ResponseEntity
    //            .noContent()
    //            .headers(HeaderUtil.createAlert(applicationName, "reestrManagement.deleted", id.toString()))
    //            .build();
    //    }
}
