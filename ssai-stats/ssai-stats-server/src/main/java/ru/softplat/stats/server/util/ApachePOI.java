package ru.softplat.stats.server.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;

public class ApachePOI {

    public void createFile() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        Cell cell = createHeading(sheet, 4);

        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());

        CellStyle style = workbook.createCellStyle();
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.BLUE1.getIndex());
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(font);
        cell.setCellStyle(style);

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);

        String userHome = System.getProperty("user.home");
        String separator = System.getProperty("file.separator");
        LocalDate currentDate = LocalDate.now();
        String baseName = "stats_" + currentDate + ".xlsx";
        String fullName = userHome + separator + "Downloads" + separator + baseName;
        File file = new File(fullName);
        int count = 1;
        while (file.exists()) {
            String newName = userHome +
                    separator +
                    "Downloads" +
                    separator +
                    "stats_" +
                    currentDate +
                    " (" + count + ").xlsx";
            file = new File(newName);
            count++;
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        workbook.write(fileOutputStream);

        fileOutputStream.close();
    }

    private static Cell createHeading(Sheet sheet, int countCell) {
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        Cell cell1 = row.createCell(1);
        Cell cell2 = row.createCell(2);
        Cell cell3 = row.createCell(3);

        cell.setCellValue("Программа");
        cell1.setCellValue("Скачали демо");
        cell2.setCellValue("Купили(количество покупок)");
        cell3.setCellValue("Выручка");
        return cell;
    }
}
