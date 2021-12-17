package uz.depos.app.service;

import io.undertow.util.BadRequestException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.depos.app.domain.Meeting;
import uz.depos.app.domain.Member;
import uz.depos.app.domain.User;
import uz.depos.app.domain.enums.MemberTypeEnum;
import uz.depos.app.domain.enums.UserAuthTypeEnum;
import uz.depos.app.domain.enums.UserGroupEnum;
import uz.depos.app.repository.CompanyRepository;
import uz.depos.app.repository.MeetingRepository;
import uz.depos.app.repository.MemberRepository;
import uz.depos.app.repository.UserRepository;
import uz.depos.app.service.dto.AttachReestrDTO;
import uz.depos.app.service.dto.DeposUserDTO;
import uz.depos.app.service.mapper.ExcelHelpers;
import uz.depos.app.service.utils.CheckElementInArray;
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
    private final FilesStorageService filesStorageService;
    private final MailService mailService;
    private final CheckElementInArray checkElementInArray;
    private final CompanyRepository companyRepository;

    public ReestrService(
        MeetingRepository meetingRepository,
        UserRepository userRepository,
        MemberRepository memberRepository,
        ExcelHelpers excelHelpers,
        UserService userService,
        FilesStorageService filesStorageService,
        MailService mailService,
        CheckElementInArray checkElementInArray,
        CompanyRepository companyRepository
    ) {
        this.meetingRepository = meetingRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.excelHelpers = excelHelpers;
        this.userService = userService;
        this.filesStorageService = filesStorageService;
        this.mailService = mailService;
        this.checkElementInArray = checkElementInArray;
        this.companyRepository = companyRepository;
    }

    public void checkerReestrColumn(Sheet sheet, int columnNumber, CellType cellType, int lengthValueCell, String chairmanPinfl) {
        // Check column to: null, type and cellLength, then return List of cell values.
        List<String> listOfCells = excelHelpers.CheckColumn(sheet, columnNumber, cellType, lengthValueCell);

        if (chairmanPinfl != null) {
            Boolean present = checkElementInArray.checkStr(listOfCells, chairmanPinfl);
            //            boolean hasChairman = listOfCells.stream().allMatch(Predicate.isEqual(chairmanPinfl));
            if (!present) {
                throw new BadRequestAlertException(
                    "Do not find a chairman from the list, which elect in Company",
                    "reestrManagement",
                    "notFoundChairman"
                );
            }
        }

        // Check list of cell by one column to duplicate.
        String duplicate = excelHelpers.DuplicateCells(listOfCells);

        if (duplicate != null) {
            throw new BadRequestAlertException("Column contains duplicate value: " + duplicate, "reestrManagement", "duplicateExist");
        }
        log.debug("Checked Information for Reestr Sheet: {}", sheet);
    }

    private void parseThread(Workbook workbook, Sheet sheet, Meeting meeting) throws BadRequestException, IOException {
        Iterator<Row> rows = sheet.iterator();
        DataFormatter formatter = new DataFormatter();

        String chairmanPinfl = meeting.getCompany().getChairman() != null ? meeting.getCompany().getChairman().getPinfl() : "";

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

            member.setCompany(meeting.getCompany());
            member.setMeeting(meeting);
            member.setUser(savedUser);
            member.setRemotely(true);
            member.setConfirmed(false);
            member.setInvolved(false);
            if (chairmanPinfl.equals(currentRowPinfl)) {
                member.setMemberTypeEnum(MemberTypeEnum.CHAIRMAN);
                if (meeting.getCompany().getChairman() != null) {
                    boolean present = userRepository.findById(meeting.getCompany().getChairman().getId()).isPresent();
                    boolean present1 = memberRepository
                        .findByMemberTypeEnumAndMeetingIdAndFromReestrFalse(MemberTypeEnum.CHAIRMAN, meeting.getId())
                        .isPresent();
                    if (present && !present1) memberRepository.save(
                        new Member(
                            meeting,
                            meeting.getCompany(),
                            savedUser,
                            true,
                            false,
                            false,
                            MemberTypeEnum.CHAIRMAN,
                            currentRow.getCell(3).getStringCellValue(),
                            currentRow.getCell(7).getStringCellValue(),
                            false
                        )
                    );
                }
            } else {
                member.setMemberTypeEnum(MemberTypeEnum.SIMPLE);
            }
            member.setHldIt(currentRow.getCell(3).getStringCellValue());
            member.setPosition(currentRow.getCell(7).getStringCellValue());
            member.setFromReestr(true);
            Member savedMember = memberRepository.save(member);

            new Thread(() -> mailService.sendInvitationEmail(savedUser, meeting, savedMember, isNew ? password : "")).start();
        }
        workbook.close();
    }

    public AttachReestrDTO parse(MultipartFile file, Long meetingId) throws IOException, BadRequestException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0); // Get first sheet from book.

        meetingRepository
            .findById(meetingId)
            .ifPresent(
                meeting -> {
                    if (meeting.getCompany() != null && meeting.getCompany().getChairman() != null) {
                        String chairmanPinfl = meeting.getCompany().getChairman().getPinfl();
                        checkerReestrColumn(sheet, 2, CellType.NUMERIC, 14, chairmanPinfl); // PINFL
                    } else {
                        checkerReestrColumn(sheet, 2, CellType.NUMERIC, 14, null); // PINFL
                    }
                    checkerReestrColumn(sheet, 6, CellType.STRING, 0, null); // EMAIL
                }
            );

        meetingRepository
            .findById(meetingId)
            .ifPresent(
                meeting -> {
                    try {
                        parseThread(workbook, sheet, meeting);
                    } catch (BadRequestException | IOException e) {
                        e.printStackTrace();
                    }
                }
            );

        AttachReestrDTO savedReestr = filesStorageService.uploadReestrExcel(file, meetingId);
        savedReestr.setExtraInfo("Reestr uploaded success");
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
