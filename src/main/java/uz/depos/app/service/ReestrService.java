package uz.depos.app.service;

import io.undertow.util.BadRequestException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.depos.app.domain.Meeting;
import uz.depos.app.domain.Member;
import uz.depos.app.domain.User;
import uz.depos.app.domain.enums.MemberTypeEnum;
import uz.depos.app.domain.enums.UserAuthTypeEnum;
import uz.depos.app.domain.enums.UserGroupEnum;
import uz.depos.app.repository.*;
import uz.depos.app.service.dto.AttachReestrDTO;
import uz.depos.app.service.dto.DeposUserDTO;
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
    private final CompanyRepository companyRepository;
    private final FilesStorageService filesStorageService;
    private final MailService mailService;

    public ReestrService(
        MeetingRepository meetingRepository,
        UserRepository userRepository,
        MemberRepository memberRepository,
        ExcelHelpers excelHelpers,
        UserService userService,
        CompanyRepository companyRepository,
        FilesStorageService filesStorageService,
        MailService mailService
    ) {
        this.meetingRepository = meetingRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.excelHelpers = excelHelpers;
        this.userService = userService;
        this.companyRepository = companyRepository;
        this.filesStorageService = filesStorageService;
        this.mailService = mailService;
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

    public AttachReestrDTO parse(MultipartFile file, Long meetingId) throws IOException, BadRequestException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0); // Get first sheet from book.

        checkerReestrColumn(sheet, 2, CellType.NUMERIC, 14); // PINFL
        checkerReestrColumn(sheet, 6, CellType.STRING, 0); // EMAIL

        Iterator<Row> rows = sheet.iterator();
        List<Member> memberList = new ArrayList<Member>();
        DataFormatter formatter = new DataFormatter();

        String chairmanPinfl = Objects
            .requireNonNull(
                meetingRepository
                    .findById(meetingId)
                    .flatMap(meeting -> companyRepository.findById(meeting.getCompany().getId()))
                    .orElse(null)
            )
            .getChairman()
            .getPinfl();
        boolean hasChairmen = false;

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
            boolean isNew = !userRepository.findOneByPinfl(currentRowPinfl).isPresent();
            User oldUser = !isNew ? userRepository.findOneByPinfl(currentRowPinfl).get() : new User();

            DeposUserDTO user = new DeposUserDTO();

            String password = RandomStringUtils.randomAlphanumeric(6);

            // set ID
            if (!isNew) userRepository.findOneByPinfl(currentRowPinfl).ifPresent(user1 -> user.setId(user1.getId()));
            // set Login
            if (isNew) {
                user.setLogin(userService.generateLogin(currentRowPinfl));
            } else {
                user.setLogin(oldUser.getLogin());
            }
            // set Password
            if (isNew) {
                user.setPassword(password);
            } else {
                user.setPassword(oldUser.getPassword());
            }
            // set Email
            user.setEmail(currentRow.getCell(6).getStringCellValue());
            // set Activated
            if (isNew) {
                user.setActivated(true);
            } else {
                user.setActivated(oldUser.isActivated());
            }
            // set FullName
            user.setFullName(currentRow.getCell(1).getStringCellValue());
            // set Passport
            user.setPassport(currentRow.getCell(4).getStringCellValue());
            // set PINFL
            user.setPinfl(currentRowPinfl);
            // set GroupEnum
            if (isNew) {
                user.setGroupEnum(UserGroupEnum.INDIVIDUAL);
            } else {
                user.setGroupEnum(oldUser.getGroupEnum());
            }
            // set AuthTypeEnum
            if (isNew) {
                user.setAuthTypeEnum(UserAuthTypeEnum.ANY);
            } else {
                user.setAuthTypeEnum(oldUser.getAuthTypeEnum());
            }
            // set Resident
            if (isNew) {
                user.setResident(true);
            } else {
                user.setResident(oldUser.isResident());
            }
            // set INN
            if (!isNew) user.setInn(oldUser.getInn());
            // set Phone-number
            if (isNew) {
                user.setPhoneNumber(currentRow.getCell(5).getStringCellValue());
            } else {
                user.setPhoneNumber(oldUser.getPhoneNumber());
            }

            User savedUser;
            if (isNew) {
                savedUser = userService.createDeposUser(user);
            } else {
                savedUser = userService.editUser(user).get();
            }
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
            if (chairmanPinfl.equals(currentRowPinfl)) {
                member.setMemberTypeEnum(MemberTypeEnum.CHAIRMAN);
                hasChairmen = true;
            } else {
                member.setMemberTypeEnum(MemberTypeEnum.SIMPLE);
            }
            member.setHldIt(currentRow.getCell(3).getStringCellValue());
            member.setPosition(currentRow.getCell(7).getStringCellValue());
            member.setFromReestr(true);
            Member savedMember = memberRepository.saveAndFlush(member);

            if (meetingRepository.findById(meetingId).isPresent() && savedUser != null) {
                Meeting meeting = meetingRepository.findById(meetingId).get();
                Runnable runnable = () -> mailService.sendInvitationEmail(savedUser, meeting, isNew ? password : "");
                runnable.run();
            }
            memberList.add(savedMember);
        }
        workbook.close();
        //        if (!hasChairmen) throw new BadRequestAlertException(
        //            "From this Reestr don't have Chairmen by Company",
        //            "reestrManagement",
        //            "chairmenError"
        //        );
        AttachReestrDTO savedReestr = filesStorageService.uploadReestrExcel(file, meetingId);
        savedReestr.setExtraInfo("By this Reestr uploaded members: " + memberList.size());
        log.debug("Parsed Information for Reestr: {}", workbook);
        return savedReestr;
    }

    /**
     * Get Excel Reestr by Meeting ID.
     *
     * @param meetingId for get Excel by meeting ID
     * @return ByteArrayInputStream
     */
    public ByteArrayInputStream getExcelReestr(Long meetingId) {
        Optional<List<Member>> allByMeetingIdAndFromReestrTrue = memberRepository.findAllByMeetingIdAndFromReestrTrue(meetingId);
        List<Member> members = new ArrayList<>();
        allByMeetingIdAndFromReestrTrue.ifPresent(members::addAll);
        return excelHelpers.MembersToExcelReestr(members);
    }
}
