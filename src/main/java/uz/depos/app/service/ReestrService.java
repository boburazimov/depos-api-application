package uz.depos.app.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.security.RandomUtil;
import uz.depos.app.domain.Authority;
import uz.depos.app.domain.Member;
import uz.depos.app.domain.User;
import uz.depos.app.domain.enums.UserAuthTypeEnum;
import uz.depos.app.repository.*;
import uz.depos.app.security.AuthoritiesConstants;
import uz.depos.app.service.dto.AttachReestrDTO;
import uz.depos.app.service.mapper.ExcelHelpers;
import uz.depos.app.web.rest.errors.BadRequestAlertException;

/**
 * Service class for managing reestr.
 */
@Service
@Transactional
public class ReestrService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final ExcelHelpers excelHelpers;
    private final UserService userService;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyRepository companyRepository;
    private final FilesStorageService filesStorageService;

    public ReestrService(
        MeetingRepository meetingRepository,
        UserRepository userRepository,
        MemberRepository memberRepository,
        ExcelHelpers excelHelpers,
        UserService userService,
        AuthorityRepository authorityRepository,
        PasswordEncoder passwordEncoder,
        CompanyRepository companyRepository,
        FilesStorageService filesStorageService
    ) {
        this.meetingRepository = meetingRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.excelHelpers = excelHelpers;
        this.userService = userService;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
        this.companyRepository = companyRepository;
        this.filesStorageService = filesStorageService;
    }

    public void checkerReestrColumn(Sheet sheet, int columnNumber, CellType cellType, int lengthValueCell) {
        // Check column to: null, type and cellLength, then return List of cell values.
        List<String> listOfCells = excelHelpers.CheckColumn(sheet, columnNumber, cellType, lengthValueCell);

        // Check list of cell by one column to duplicate.
        String duplicate = excelHelpers.DuplicateCells(listOfCells);
        if (duplicate != null) {
            throw new BadRequestAlertException("Column contains duplicate value: " + duplicate, "reestrManagement", "duplicateExist");
        }
        log.debug("Checked Information for Reestr Sheet: {}", sheet);
    }

    public AttachReestrDTO parse(MultipartFile file, Long meetingId) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0); // Get first sheet from book.

        checkerReestrColumn(sheet, 2, CellType.NUMERIC, 14); // PINFL
        checkerReestrColumn(sheet, 6, CellType.STRING, 0); // EMAIL

        Iterator<Row> rows = sheet.iterator();
        List<Member> memberList = new ArrayList<Member>();
        DataFormatter formatter = new DataFormatter();

        int rowNumber = 0;
        while (rows.hasNext()) {
            Row currentRow = rows.next();
            // skip header
            if (rowNumber == 0) {
                rowNumber++;
                continue;
            }
            String currentRowPinfl = formatter.formatCellValue(currentRow.getCell(2)); // Get pinfl.

            // Try to get User by PINFL orElse create new User.
            User user = userRepository.findOneByPinfl(currentRowPinfl).isPresent()
                ? userRepository.findOneByPinfl(currentRowPinfl).get()
                : new User();

            if (user.getLogin() == null) user.setLogin(userService.generateLogin(currentRowPinfl)); // Set login
            user.setPassword(passwordEncoder.encode(RandomStringUtils.randomAlphanumeric(6))); // Set password
            user.setFullName(currentRow.getCell(1).getStringCellValue());
            user.setResident(true);
            user.setActivated(true);
            user.setActivationKey(RandomUtil.generateActivationKey());
            user.setResetKey(RandomUtil.generateResetKey());
            user.setResetDate(Instant.now());
            Set<Authority> authorities = new HashSet<>();
            authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
            user.setAuthorities(authorities);
            if (user.getId() == null) user.setPinfl(currentRowPinfl);
            user.setAuthTypeEnum(UserAuthTypeEnum.ANY);
            user.setPassport(currentRow.getCell(4).getStringCellValue());
            user.setPhoneNumber(currentRow.getCell(5).getStringCellValue());

            Optional<User> existingByEmail = userRepository.findOneByEmailIgnoreCase(currentRow.getCell(6).getStringCellValue());
            existingByEmail.ifPresent(
                user1 -> {
                    if (
                        user.getId() == null || !(user.getId() != null && user1.getId().equals(user.getId()))
                    ) throw new BadRequestAlertException(
                        "Email: " + user1.getEmail() + " already in use by User (PINFL): " + user1.getPinfl(),
                        "reestrManagement",
                        "emailExist"
                    );
                }
            );
            user.setEmail(currentRow.getCell(6).getStringCellValue());
            User savedUser = userRepository.save(user);

            Member member = new Member(); // Create member for fill from current row.
            meetingRepository
                .findOneById(meetingId)
                .ifPresent(
                    meeting -> {
                        member.setMeeting(meeting); // Set Meeting
                        if (meeting.getCompany() != null) {
                            companyRepository.findById(meeting.getCompany().getId()).ifPresent(member::setCompany); // Set Company
                        } else {
                            throw new NullPointerException("Company must not be null for meeting!");
                        }
                    }
                );
            member.setUser(savedUser);
            member.setRemotely(true);
            member.setConfirmed(false);
            member.setInvolved(false);
            member.setSpeaker(false);
            meetingRepository
                .findOneById(meetingId)
                .flatMap(meeting -> companyRepository.findById(meeting.getCompany().getId()))
                .ifPresent(
                    company -> {
                        if (company.getChairman() != null) {
                            if (company.getChairman().getPinfl().equals(currentRowPinfl)) {
                                member.setChairmen(true);
                            } else member.setChairmen(false);
                        }
                    }
                );
            member.setHldIt(currentRow.getCell(3).getStringCellValue());
            member.setPosition(currentRow.getCell(7).getStringCellValue());
            member.setFromReestr(true);
            Member savedMember = memberRepository.saveAndFlush(member);
            memberList.add(savedMember);
        }
        workbook.close();

        AttachReestrDTO savedReestr = filesStorageService.uploadReestrExcel(file, meetingId);
        savedReestr.setExtraInfo("By this Reestr uploaded members: " + memberList.size());
        log.debug("Parsed Information for Reestr: {}", workbook);
        return savedReestr;
    }

    /**
     * Get Excel Reestr by Meeting ID.
     *
     * @param meetingId
     * @return ByteArrayInputStream
     */
    public ByteArrayInputStream getExcelReestr(Long meetingId) {
        Optional<List<Member>> allByMeetingIdAndFromReestrTrue = memberRepository.findAllByMeetingIdAndFromReestrTrue(meetingId);
        List<Member> members = new ArrayList<>();
        allByMeetingIdAndFromReestrTrue.ifPresent(members::addAll);
        return excelHelpers.MembersToExcelReestr(members);
    }
}
