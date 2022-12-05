package com.darku.generator;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.springframework.stereotype.Component;

import com.darku.generator.model.TimeReportSheetInput;

import lombok.extern.log4j.Log4j2;

/**
 * @author Catalin on 01.09.2022
 */
@Component
@Log4j2
public class WorkReportModelGenerator {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public TimeReportSheetInput buildModel() {
        final var cal = Calendar.getInstance();

        log.info("Cal instance is as for today {}", cal);

        final var month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        final var year = cal.get(Calendar.YEAR);
        final var monthInt = cal.get(Calendar.MONTH) + 1;

        final var data = TimeReportSheetInput.builder().rows(new ArrayList<>()).month(month).year(year).build();

        //header
        data.getRows()
                .add(TimeReportSheetInput.TimeReportDayRow.builder()
                        .col1("Day")
                        .col2("Task Description")
                        .col3("8hr measure")
                        .header(true)
                        .build());

        log.info("Identified month = {} ({}), year={}", month, monthInt, year);

        final var yearMonthObject = YearMonth.of(year, monthInt);
        final int daysInMonth = yearMonthObject.lengthOfMonth();

        final var startDate = LocalDate.of(year, Month.of(monthInt), 1);
        final var endDate = monthInt == 12 ? LocalDate.of(year + 1, 1, 1) : LocalDate.of(year, Month.of(monthInt + 1), 1);

        startDate.datesUntil(endDate).forEach(day -> {
            final var dayOfWeek = day.getDayOfWeek();
            log.info("Generating row for {} - {}", day, dayOfWeek);
            final boolean b = isWeekend(dayOfWeek);
            final String date = day.format(DATE_TIME_FORMATTER);
            data.getRows().add(TimeReportSheetInput.TimeReportDayRow.builder().col1(date).weekend(b).build());
        });
        final TimeReportSheetInput.TimeReportDayRow emptyRow = TimeReportSheetInput.TimeReportDayRow.builder()
                .weekend(false)
                .build();
        data.getRows().add(emptyRow);
        data.getRows().add(emptyRow);
        data.getRows().add(emptyRow);
        data.getRows()
                .add(TimeReportSheetInput.TimeReportDayRow.builder()
                        .shouldBold(true)
                        .col2("Total hrs")
                        // sum of all hours
                        .formula("SUM(C2:C" + data.getRows().size() + ")")
                        .build());
        data.getRows()
                .add(TimeReportSheetInput.TimeReportDayRow.builder()
                        .shouldBold(true)
                        .col2("Total days")
                        // dived sum of all hours computed above by 8 to count days
                        .formula("C" + data.getRows().size() + "/8")
                        .build());
        return data;
    }

    private static boolean isWeekend(DayOfWeek dayOfWeek) {
        return dayOfWeek.equals(DayOfWeek.SATURDAY) || dayOfWeek.equals(DayOfWeek.SUNDAY);
    }
}
