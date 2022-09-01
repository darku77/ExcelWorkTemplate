// =============================================================================
// Copyright/Security Notice
//
// Licensed Materials - Property of Qnamic AG
// (C) Copyright Qnamic AG 2003-2022
// All rights reserved
//
// End Copyright
// =============================================================================
package com.darku.generator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.darku.generator.model.TimeReportSheetInput;

import lombok.extern.log4j.Log4j2;

/**
 * @author Catalin on 01.09.2022
 */

@Service
@Log4j2
public class WorkReportExcelGenerator {

    public void generate(TimeReportSheetInput data, String owner, Path pathToGenerate) {
        log.info("generate report");
        try (var workbook = new XSSFWorkbook()) {

            generateExcelBins(data, workbook);

            dump(workbook, data, owner, pathToGenerate);

        } catch (Exception e) {
            log.error("Error while generating", e);
        }

    }

    private void generateExcelBins(TimeReportSheetInput data, XSSFWorkbook workbook) {
        final XSSFFont headerFont = getHeaderFont(workbook);
        final XSSFCellStyle weekendStyle = getWeekendCellStyle(workbook);
        final XSSFCellStyle headerCellStyle = getHeaderCellStyle(workbook, headerFont);

        final var sheet = workbook.createSheet(data.getTitle());
        //day
        sheet.setColumnWidth(0, 2300);
        // Task description
        sheet.setColumnWidth(1, 20000);
        // hours
        sheet.setColumnWidth(2, 4000);

        final var idx = new AtomicInteger(0);
        data.getRows().forEach(row -> {

            final var xlsRow = sheet.createRow(idx.getAndIncrement());

            // set row values
            for (int j = 0; j < data.getNoOfCols(); j++) {
                final var cell = xlsRow.createCell(j);
                cell.setCellValue(row.getValueAtIndex(j));
            }

            if (row.isHeader() || row.isShouldBold()) {
                for (int j = 0; j < data.getNoOfCols(); j++) {
                    final var headerCell = xlsRow.getCell(j);
                    headerCell.setCellStyle(headerCellStyle);
                }
            }

            if (row.isWeekend()) {
                for (int j = 0; j < data.getNoOfCols() - 1; j++) {
                    final var cell = xlsRow.getCell(j);
                    cell.setCellStyle(weekendStyle);
                    cell.setCellValue("");
                }
            }

            if (Objects.nonNull(row.getFormula())){
                final var cell = xlsRow.getCell(2);
                cell.setCellFormula(row.getFormula());
            }

        });
    }

    private static XSSFFont getHeaderFont(XSSFWorkbook workbook) {
        final var headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints((short) 11);
        headerFont.setFontName("Calibri");
        headerFont.setColor(IndexedColors.BLACK.getIndex());
        headerFont.setBold(true);
        headerFont.setItalic(false);
        return headerFont;
    }

    private XSSFCellStyle getWeekendCellStyle(XSSFWorkbook workbook) {
        final var weekendStyle = workbook.createCellStyle();
        weekendStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        weekendStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return weekendStyle;
    }

    private XSSFCellStyle getHeaderCellStyle(XSSFWorkbook workbook, XSSFFont headerFont) {
        final var style = workbook.createCellStyle();
        style.setFont(headerFont);
        return style;
    }

    private static void dump(XSSFWorkbook workbook, TimeReportSheetInput data, String owner, Path pathToGenerate) throws IOException {
        final var s = pathToGenerate.toString();
        final var fileLocation = s +"\\"+ owner + " " + data.getTitle() + ".xlsx";

        final var outputStream = new FileOutputStream(fileLocation);
        workbook.write(outputStream);
    }
}
