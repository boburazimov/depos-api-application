package uz.depos.app.service.mapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.depos.app.domain.Member;
import uz.depos.app.repository.UserRepository;
import uz.depos.app.web.rest.errors.BadRequestAlertException;

/**
 * Apache POI classes such as: Workbook, Sheet, Row, Cell.
 * create Workbook from InputStream
 * create Sheet using Workbook.getSheet() method
 * iterate over Rows by Iterator with Sheet.iterator() and Iterator.hasNext()
 * from each Row, iterate over Cells
 * with each Cell, use getNumericCellValue(), getStringCellValue()… methods to read and parse the content.
 */
@Service
public class ExcelHelpers {

    private final Logger log = LoggerFactory.getLogger(ExcelHelpers.class);

    final String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    final String SHEET = "Лист1";
    final String[] HEADER = { "hld_num", "hld_name", "hld_PINFL", "hld_it", "hld_pass", "hld_phone", "hld_email", "position" };

    private final UserRepository userRepository;

    public ExcelHelpers(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Check if a file has Excel format or not.
     *
     * @param file file to check
     * @return true if it's equals to office format, or false if the format file is not office-document type.
     */
    public boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    /**
     * @param sheet           sheet to check
     * @param columnNumber    which column need to check
     * @param cellType        type of cell for check to type
     * @param lengthValueCell length of cell (pinfl length must be 14 char.)
     * @return List of cells
     */
    public List<String> CheckColumn(Sheet sheet, int columnNumber, CellType cellType, int lengthValueCell) {
        List<String> listOfCells = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header.
            Cell cell = row.getCell(columnNumber); // Get column.
            if (cell != null && formatter.formatCellValue(cell).length() != 0) { // Check to null.
                if (cell.getCellType() == cellType) { // Check to type cell.
                    if (lengthValueCell == 0 || (formatter.formatCellValue(cell).length() == lengthValueCell)) { // Check to Length of chars.
                        listOfCells.add(formatter.formatCellValue(cell)); // Collect to list for return.
                    } else {
                        throw new BadRequestAlertException(
                            "Length must be " + lengthValueCell + " characters",
                            "reestrManagement",
                            "lengthError"
                        );
                    }
                } else {
                    throw new BadRequestAlertException("Cell must be " + cellType.toString() + " type", "reestrManagement", "typeError");
                }
            } else {
                throw new BadRequestAlertException("Cell must not be contains null", "reestrManagement", "NullPointer");
            }
        }
        log.debug("Checked Information for Reestr Column: {}", listOfCells);
        return listOfCells;
    }

    /**
     * @param listOfCells list of cells for check to contains duplicates
     * @return String duplicate if exixst.
     */
    public String DuplicateCells(List<String> listOfCells) {
        Map<String, Integer> nameAndCount = new HashMap<>();
        // build hash table with count
        for (String cellValue : listOfCells) {
            Integer count = nameAndCount.get(cellValue);
            if (count == null) {
                nameAndCount.put(cellValue, 1);
            } else {
                nameAndCount.put(cellValue, ++count);
            }
        }
        Entry<String, Integer> duplicateEntry = nameAndCount
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue() > 1)
            .findFirst()
            .orElse(null);
        if (duplicateEntry != null && duplicateEntry.getKey() != null && (!duplicateEntry.getKey().isEmpty())) {
            log.debug("Checked Information for Reestr Column Duplicate: {}", duplicateEntry.getKey());
            return duplicateEntry.getKey();
        }
        log.debug("Checked Information for Reestr Column Duplicate: {}", "null");
        return null;
    }

    /**
     * @param members
     * @return ByteArrayInputStream
     */
    public ByteArrayInputStream MembersToExcelReestr(List<Member> members) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);

            // Header
            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < HEADER.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADER[col]);
            }

            int rowIdx = 1;
            for (Member member : members) {
                Row row = sheet.createRow(rowIdx++);

                userRepository
                    .findById(member.getUser().getId())
                    .ifPresent(
                        user -> {
                            row.createCell(0).setCellValue(member.getId()); // hld_num
                            row.createCell(1).setCellValue(user.getFullName()); // hld_name
                            row.createCell(2).setCellValue(user.getPinfl()); // hld_PINFL
                            row.createCell(3).setCellValue(member.getHldIt()); // hld_it
                            row.createCell(4).setCellValue(user.getPassport()); // hld_pass
                            row.createCell(5).setCellValue(user.getPhoneNumber()); // hld_phone
                            row.createCell(6).setCellValue(user.getEmail()); // hld_email
                            row.createCell(7).setCellValue(member.getPosition()); // position
                        }
                    );
            }
            workbook.write(byteArrayOutputStream);
            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }
}
