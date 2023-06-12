package com.chahel.ch0623.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import com.chahel.ch0623.entity.EnumsForTools.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnumsForToolsTest {

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
        String brandCodes = "";

        for (ToolBrand toolBrand : ToolBrand.values()) {
            brandCodes += toolBrand.getBrandCode();
        }

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

    @Test
    void checkValidBrandCodeAssignments() {
        boolean allSucceeded = true;
        int errorCount = 0;

        for (ToolType toolType : ToolType.values()) {
            String brandCodes = toolType.getTypeValidBrandPairings();
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
        }

        assertEquals(true, allSucceeded);
        assertEquals(1, errorCount);
    }

    @Test
    void checkToolCodeConversion() {
        boolean allSucceeded = true;
        int errorCount = 0;
        ArrayList<String> toolTypeCodes = new ArrayList();
        for (ToolType toolType : ToolType.values()) {
            toolTypeCodes.add(toolType.getTypeCode());
        }
        for (String typeCode : toolTypeCodes) {
            try {
                if (ToolType.convertFromTypeCode(typeCode) == ToolType.ERROR) {
                    errorCount++;
                };
            } catch (IllegalArgumentException e) {
                allSucceeded = false;
            }

        }
        assertEquals(true, allSucceeded);
        assertEquals(1, errorCount);
    }

    @Test
    void checkAllToolTypesHaveChargeDays() {
        boolean allSucceeded = true;

        for (ToolType toolType : ToolType.values()) {
            if (toolType == ToolType.ERROR) {
                continue;
            }
            for (ToolChargeDay chargeDay : ToolChargeDay.values()) {
                if (chargeDay.getChargeableTools().contains(toolType)) {
                    break;
                }
                allSucceeded = false;
            }
            if (!allSucceeded) {
                break;
            }
        }

        assertEquals(true, allSucceeded);
    }
}
