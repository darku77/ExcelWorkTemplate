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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

/**
 * @author Catalin on 01.09.2022
 */

@Service

@Log4j2
public class WorkReportGenerator {

    public void generate() {
        log.info("generate report");
        try (var workbook = new XSSFWorkbook()) {

            final Calendar cal = Calendar.getInstance();
            final String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
            final var sheet = workbook.createSheet(month);

            //day
            sheet.setColumnWidth(0, 4000);
            // Task description
            sheet.setColumnWidth(1, 20000);
            // hours
            sheet.setColumnWidth(2, 4000);

            final var header = sheet.createRow(0);

            final var headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            var headerCell = header.createCell(0);
            headerCell.setCellValue("Day");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(1);
            headerCell.setCellValue("Task Description");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(2);
            headerCell.setCellValue("8hrs measure");
            headerCell.setCellStyle(headerStyle);


            dump(workbook);

        } catch (Exception e) {
            log.error("Error while generating", e);
        }

    }

    private static void dump(XSSFWorkbook workbook) throws IOException {
        final var currDir = new File(".");
        final var path = currDir.getAbsolutePath();
        final var fileLocation = path.substring(0, path.length() - 1) + "temp.xlsx";

        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        workbook.write(outputStream);
    }
}
