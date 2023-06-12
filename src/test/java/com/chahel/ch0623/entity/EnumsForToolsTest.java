package com.chahel.ch0623.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import com.chahel.ch0623.entity.EnumsForTools.*;

import javax.tools.Tool;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnumsForToolsTest {

    // 2023 JAN 13 SUN
    private LocalDate dateFirst = LocalDate.of(2023, 1, 13);

    // 2023 JUL 03 MON
    private LocalDate dateSecond = LocalDate.of(2023, 7, 3);

    // 2023 SEP 01 FRI
    private LocalDate dateThird = LocalDate.of(2023, 9, 1);

    // 2023 AUG 27 SUN
    private LocalDate dateFourth = LocalDate.of(2023, 8, 27);

    @Test
    void checkAllFloatingHolidaysSuccessfullyConvertedToFixedHolidays() {
        boolean allSucceeded = true;

        for (ToolFloatingHoliday holiday : ToolFloatingHoliday.values()) {
            try {
                ToolFixedHoliday.valueOf(holiday.name());
            } catch (IllegalArgumentException e) {
                allSucceeded = false;
            }
        }
        assertEquals(true, allSucceeded);

    }

    @Test
    void checkBrandCodeConversion() {
        boolean allSucceeded = true;
        int errorCount = 0;
        String brandCodes = "SWDRF";
        for (char c : brandCodes.toCharArray()) {
            String s = String.valueOf(c);
            try {
                if (ToolBrand.convertFromBrandCode(s) == ToolBrand.ERROR) {
                    errorCount++;
                };
            } catch (IllegalArgumentException e) {
                allSucceeded = false;
            }

        }
        assertEquals(true, allSucceeded);
        assertEquals(1, errorCount);
    }
}
