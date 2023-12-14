package ru.softplat.stats.server.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.softplat.stats.server.dto.SellerReportEntryAdmin;
import ru.softplat.stats.server.model.SellerReport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;

public class ApachePOI {

    public void createFileAdmin(SellerReport sellerReport) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Лист1");
        Row rowHeader = sheet.createRow(0);
        String[] headerTitles = {
                "№",
                "Дата",
                "Название",
                "Артикул",
                "Продавец",
                "Купили, шт",
                "Выручка продавцов, руб",
                "Прибыль, руб"};

        CellStyle style = styleFile(workbook);

        for (int i = 0; i < headerTitles.length; i++) {
            Cell cell = rowHeader.createCell(i);
            cell.setCellValue(headerTitles[i]);
            cell.setCellStyle(style);
        }

        int rowCount = 1;
        for (SellerReportEntryAdmin sellerReportEntry : sellerReport.getSellerReportEntryList()) {
            Row rowData = sheet.createRow(rowCount);
            String[] rowDataValues = {
                    String.valueOf(rowCount),
                    sellerReportEntry.getDate().toLocalDate().toString(),
                    sellerReportEntry.getProductName(),
                    String.valueOf(sellerReportEntry.getArticleNumber()),
                    sellerReportEntry.getSellerName(),
                    String.valueOf(sellerReportEntry.getQuantity()),
                    String.valueOf(sellerReportEntry.getRevenue()),
                    String.valueOf(sellerReportEntry.getReceiveAmountAdmin())};

            for (int i = 0; i < rowDataValues.length; i++) {
                Cell cell = rowData.createCell(i);
                cell.setCellValue(rowDataValues[i]);
            }
            rowCount++;
        }
        Row rowSumRevenue = sheet.createRow(rowCount + 1);
        String[] sumRevenueValues = {
                "Итого за период:",
                String.valueOf(sellerReport.getSumRevenue()),
                String.valueOf(sellerReport.getReceiveAmount())};
        for (int i = 0; i < sumRevenueValues.length; i++) {
            Cell cell = rowSumRevenue.createCell(5 + i);
            cell.setCellValue(sumRevenueValues[i]);
            cell.setCellStyle(style);
        }

        for (int i = 0; i <= headerTitles.length; i++) {
            sheet.autoSizeColumn(i);
        }
        FileOutputStream fileOutputStream = getFileOutputStream();
        workbook.write(fileOutputStream);
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
