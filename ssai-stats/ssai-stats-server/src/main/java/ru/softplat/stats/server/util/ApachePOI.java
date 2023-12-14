package ru.softplat.stats.server.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.softplat.stats.server.dto.SellerReportEntryAdmin;
import ru.softplat.stats.server.model.SellerReport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;

public class ApachePOI {

    public void createFileAdmin(SellerReport sellerReport) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Лист1");
        Row rowHeader = sheet.createRow(0); //создание строки
        Cell cell = rowHeader.createCell(0); //создание столбца
        Cell cell1 = rowHeader.createCell(1);
        Cell cell2 = rowHeader.createCell(2);
        Cell cell3 = rowHeader.createCell(3);
        Cell cell4 = rowHeader.createCell(4);
        Cell cell5 = rowHeader.createCell(5);
        Cell cell6 = rowHeader.createCell(6);
        Cell cell7 = rowHeader.createCell(7);

        cell.setCellValue("№");
        cell1.setCellValue("Дата");
        cell2.setCellValue("Название");
        cell3.setCellValue("Артикул");
        cell4.setCellValue("Продавец");
        cell5.setCellValue("Купили, шт");
        cell6.setCellValue("Выручка продавцов, руб");
        cell7.setCellValue("Прибыль, руб");

        CellStyle style = styleFile(workbook);

        cell.setCellStyle(style);
        cell1.setCellStyle(style);
        cell2.setCellStyle(style);
        cell3.setCellStyle(style);
        cell4.setCellStyle(style);
        cell5.setCellStyle(style);
        cell6.setCellStyle(style);
        cell7.setCellStyle(style);

        int rowCount = 1;
        for (SellerReportEntryAdmin sellerReportEntry : sellerReport.getSellerReportEntryList()) {
            Row rowData = sheet.createRow(rowCount);
            Cell cellNumber = rowData.createCell(0);
            Cell cellDate = rowData.createCell(1);
            Cell cellProductName = rowData.createCell(2);
            Cell cellArticleNumber = rowData.createCell(3);
            Cell cellSellerName = rowData.createCell(4);
            Cell cellQuantity = rowData.createCell(5);
            Cell cellRevenue = rowData.createCell(6);
            Cell cellReceiveAmountAdmin = rowData.createCell(7);

            cellNumber.setCellValue(rowCount);
            cellDate.setCellValue(sellerReportEntry.getDate().toLocalDate().toString());
            cellProductName.setCellValue(sellerReportEntry.getProductName());
            cellArticleNumber.setCellValue(sellerReportEntry.getArticleNumber());
            cellSellerName.setCellValue(sellerReportEntry.getSellerName());
            cellQuantity.setCellValue(sellerReportEntry.getQuantity());
            cellRevenue.setCellValue(sellerReportEntry.getRevenue());
            cellReceiveAmountAdmin.setCellValue(sellerReportEntry.getReceiveAmountAdmin());
            rowCount++;
        }
        Row rowSumRevenue = sheet.createRow(rowCount + 1);
        Cell cellSumRevenue = rowSumRevenue.createCell(5);
        Cell cellSumRevenueValue = rowSumRevenue.createCell(6);
        Cell cellSumRevenueAdminValue = rowSumRevenue.createCell(7);

        cellSumRevenue.setCellValue("Итого за период:");
        cellSumRevenueValue.setCellValue(sellerReport.getSumRevenue());
        cellSumRevenueAdminValue.setCellValue(sellerReport.getReceiveAmount());

        cellSumRevenue.setCellStyle(style);
        cellSumRevenueValue.setCellStyle(style);
        cellSumRevenueAdminValue.setCellStyle(style);

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);
        sheet.autoSizeColumn(6);
        sheet.autoSizeColumn(7);

        FileOutputStream fileOutputStream = getFileOutputStream();
        workbook.write(fileOutputStream);
        fileOutputStream.close();
    }

    private CellStyle styleFile(Workbook workbook) {
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());

        CellStyle style = workbook.createCellStyle();
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.BLUE1.getIndex());
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private FileOutputStream getFileOutputStream() throws IOException {
        String userHome = System.getProperty("user.home");
        String separator = System.getProperty("file.separator");
        LocalDate currentDate = LocalDate.now();
        String baseName = "report_" + currentDate + ".xlsx";
        String fullName = userHome + separator + "Downloads" + separator + baseName;
        File file = new File(fullName);
        int count = 1;
        while (file.exists()) {
            String newName = userHome +
                    separator +
                    "Downloads" +
                    separator +
                    "report_" +
                    currentDate +
                    " (" + count + ").xlsx";
            file = new File(newName);
            count++;
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        return fileOutputStream;
    }
}
