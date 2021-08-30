package uz.depos.app.service.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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

    final String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

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
        if (
            duplicateEntry != null && duplicateEntry.getKey() != null && (!duplicateEntry.getKey().isEmpty())
        ) return duplicateEntry.getKey();
        return null;
    }
}
