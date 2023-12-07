package ru.softplat.stats.server.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.softplat.stats.server.dto.SellerReportEntry;
import ru.softplat.stats.server.model.SellerReport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;

public class ApachePOI {

    public void createFile(SellerReport sellerReport) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Лист1");
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);

        Row rowHeader = sheet.createRow(0); //создание строки
        Cell cell = rowHeader.createCell(0); //создание столбца
        Cell cell1 = rowHeader.createCell(1);
        Cell cell2 = rowHeader.createCell(2);
        Cell cell3 = rowHeader.createCell(3);

        cell.setCellValue("Программа");
        cell1.setCellValue("Скачали демо");
        cell2.setCellValue("Купили(количество покупок)");
        cell3.setCellValue("Выручка");

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
        cell1.setCellStyle(style);
        cell2.setCellStyle(style);
        cell3.setCellStyle(style);

        int rowCount = 1;
        for (SellerReportEntry sellerReportEntry : sellerReport.getSellerReportEntryList()) {
            Row rowData = sheet.createRow(rowCount);
            Cell cellProductName = rowData.createCell(0);
            Cell cellQuantity = rowData.createCell(2);
            Cell cellPrice = rowData.createCell(3);
            cellProductName.setCellValue(sellerReportEntry.getProductName());
            cellQuantity.setCellValue(sellerReportEntry.getQuantity());
            cellPrice.setCellValue(sellerReportEntry.getQuantity());
            rowCount++;
        }
        Row rowSumRevenue = sheet.createRow(rowCount + 1);
        Cell cellSumRevenue = rowSumRevenue.createCell(2);
        Cell cellSumRevenueValue = rowSumRevenue.createCell(3);
        cellSumRevenue.setCellValue("Итого за период:");
        cellSumRevenueValue.setCellValue(sellerReport.getSumRevenue());
        cellSumRevenue.setCellStyle(style);
        cellSumRevenueValue.setCellStyle(style);

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
}
