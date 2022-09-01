// =============================================================================
// Copyright/Security Notice
//
// Licensed Materials - Property of Qnamic AG
// (C) Copyright Qnamic AG 2003-2022
// All rights reserved
//
// End Copyright
// =============================================================================
package com.darku.generator.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Catalin on 01.09.2022
 */

@Getter
@Setter
@Builder
@ToString
public class TimeReportSheetInput {

    private static final int NO_OF_COLS = 3;
    private String month;
    private int year;

    private List<TimeReportDayRow> rows;

    private String owner;

    public String getTitle() {
        return month + " " + year;
    }

    public int getNoOfCols() {
        return NO_OF_COLS;
    }

    @Getter
    @Setter
    @Builder
    @ToString
    public static class TimeReportDayRow {

        private String col1;
        private String col2;
        private String col3;

        private boolean header;
        private boolean weekend;
        private boolean shouldBold;
        private String formula;

        public String getValueAtIndex(int idx) {
            return switch (idx) {
                case 0 -> col1;
                case 1 -> col2;
                case 2 -> col3;
                default -> "";
            };
        }

    }
}
