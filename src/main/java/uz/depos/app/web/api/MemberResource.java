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
import javax.validation.constraints.Pattern;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import uz.depos.app.config.Constants;
import uz.depos.app.repository.AuthorityRepository;
import uz.depos.app.repository.MeetingRepository;
import uz.depos.app.repository.MemberRepository;
import uz.depos.app.repository.UserRepository;
import uz.depos.app.security.AuthoritiesConstants;
import uz.depos.app.service.MemberService;
import uz.depos.app.service.dto.AdminUserDTO;
import uz.depos.app.service.dto.MemberDTO;
import uz.depos.app.web.rest.errors.BadRequestAlertException;
import uz.depos.app.web.rest.errors.EmailAlreadyUsedException;
import uz.depos.app.web.rest.errors.LoginAlreadyUsedException;

@RestController
@RequestMapping(path = "/api/member")
@Api(tags = "Member")
public class MemberResource {

    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections.unmodifiableList(
        Arrays.asList("id", "meetingId", "userId", "isRemotely", "isConfirmed", "isInvolved", "isSpeaker")
    );

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Logger log = LoggerFactory.getLogger(MemberResource.class);

    final MeetingRepository meetingRepository;
    final UserRepository userRepository;
    final MemberService memberService;
    final MemberRepository memberRepository;
    final AuthorityRepository authorityRepository;

    public MemberResource(
        MeetingRepository meetingRepository,
        UserRepository userRepository,
        MemberService memberService,
        MemberRepository memberRepository,
        AuthorityRepository authorityRepository
    ) {
        this.meetingRepository = meetingRepository;
        this.userRepository = userRepository;
        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.authorityRepository = authorityRepository;
    }

    /**
     * {@code POST  /member}  : Creates a new member.
     * <p>
     * Creates a new member if the meeting and user is not empty, and return memberDTO
     *
     * @param memberDTO the member to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new member, or with status {@code 400 (Bad Request)} if the company empty
     * and have incorrect id.
     * @throws NullPointerException      if the company is Empty.
     * @throws ResourceNotFoundException if the company id is not correct - Not found).
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create member", notes = "This method creates a new member")
    public ResponseEntity<MemberDTO> createMember(@Valid @RequestBody MemberDTO memberDTO) throws URISyntaxException {
        log.debug("REST request to create Member : {}", memberDTO);

        if (memberDTO.getId() != null && memberDTO.getId() > 0) {
            throw new BadRequestAlertException("A new member cannot already have an ID", "memberManagement", "idExists");
        } else if (memberDTO.getMeetingId() == null || memberDTO.getMeetingId() == 0) {
            throw new BadRequestAlertException("MeetingID must NOT be null and zero", "memberManagement", "meetingIDNull");
        } else if (memberDTO.getUserId() == null || memberDTO.getUserId() == 0) {
            throw new BadRequestAlertException("UserID must NOT be null and zero", "memberManagement", "userIDNull");
        } else if (!meetingRepository.existsById(memberDTO.getMeetingId())) {
            throw new ResourceNotFoundException("Meeting not found by ID: " + memberDTO.getMeetingId());
        } else if (!userRepository.existsById(memberDTO.getUserId())) {
            throw new ResourceNotFoundException("User not found by ID: " + memberDTO.getUserId());
        }

        // TODO - Нужно сделать проверку, есть ли member с ID user и с ID meeting при загрузке реестра.
        // нужно взять в один массив пинфл реестра и проверить на повторяющийся элементы

        MemberDTO savedMemberDTO = memberService.createMember(memberDTO);
        return ResponseEntity
            .created(new URI("/api/member/" + savedMemberDTO.getId()))
            .headers(HeaderUtil.createAlert(applicationName, "memberManagement.created", savedMemberDTO.getId().toString()))
            .body(savedMemberDTO);
    }

    /**
     * {@code PUT /member} : Updates an existing Member.
     *
     * @param memberDTO the user to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated member.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already in use.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already in use.
     */
    @PutMapping
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @ApiOperation(value = "Update member", notes = "This method is update exist member")
    public ResponseEntity<MemberDTO> updateMember(@Valid @RequestBody MemberDTO memberDTO) {
        log.debug("REST request to update Member : {}", memberDTO);
        Optional<MemberDTO> savedMemberDTO = memberService.updateMember(memberDTO);

        return ResponseUtil.wrapOrNotFound(
            savedMemberDTO,
            HeaderUtil.createAlert(applicationName, "memberManagement.updated", memberDTO.getId().toString())
        );
    }

    /**
     * {@code GET /member/:id} : get the "id" member.
     *
     * @param id the id of the member to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "id" member, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @ApiOperation(value = "Get member", notes = "This method to get exist member")
    public ResponseEntity<MemberDTO> getMember(@PathVariable Long id) {
        log.debug("REST request to get Member : {}", id);
        return ResponseUtil.wrapOrNotFound(memberService.getMember(id).map(MemberDTO::new));
    }

    /**
     * {@code GET /members} : get all members with all the details - calling this are only allowed for the administrators.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all members.
     */
    @GetMapping
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @ApiOperation(value = "Get members", notes = "This method to get all members")
    public ResponseEntity<List<MemberDTO>> getAllMembers(Pageable pageable) {
        log.debug("REST request to get all Members");
        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest().build();
        }

        final Page<MemberDTO> page = memberService.getAllMembers(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code CHECK :pageable} : check the "pageable" Member.
     *
     * @param pageable the pageable of the members for check sort variable, is contains in Allowed properties.
     * @return the {@link Pageable} is sort var is contain}.
     */
    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }

    /**
     * {@code DELETE /member/:id} : delete the "id" Member.
     *
     * @param id the id of the member to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.debug("REST request to delete Member: {}", id);
        memberService.deleteMember(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createAlert(applicationName, "memberManagement.deleted", id.toString()))
            .build();
    }
}
