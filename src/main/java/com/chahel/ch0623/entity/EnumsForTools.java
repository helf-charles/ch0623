package com.chahel.ch0623.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public class EnumsForTools {

    /**
     * This Enum contains all valid Tools, their Prices, their valid Brand Code Pairings, and their Tool Codes,
     * as well as an Error Code
     */
    @Getter
    @AllArgsConstructor
    public enum ToolType {
        CHAINSAW("Chainsaw", "CHN", "S", 1.49),
        LADDER("Ladder", "LAD", "W", 1.99),
        JACKHAMMER("Jackhammer", "JAK", "DR", 2.99),
        ERROR("Error", "ERR", "!", 0.00);

        private String typeString;
        private String typeCode;
        private String typeValidBrandPairings;
        private double typePrice;

        public static ToolType convertFromTypeCode(String typeCode) {
            switch (typeCode) {
                case "CHN":
                    return ToolType.CHAINSAW;
                case "LAD":
                    return ToolType.LADDER;
                case "JAK":
                    return ToolType.JACKHAMMER;
                default:
                    return ToolType.ERROR;
            }
        }

        public static boolean isValidBrandCode(ToolType toolType, char toolCode) {
            for (char c : toolType.typeValidBrandPairings.toCharArray()) {
                if (c == toolCode) {
                    return true;
                }
            }
            return false;
        }

        public String toString() {
            return typeString + " " + typeCode + " " + typePrice;
        }
    }

    /**
     * This Enum contains all valid Brands and their Brand Codes, as well as an Error Code
     */
    @Getter
    @AllArgsConstructor
    public enum ToolBrand {
        STIHL("Stihl", "S"),
        WERNER("Werner", "W"),
        DEWALT("De Walt", "D"),
        RIDGID("Ridgid", "R"),
        ERROR("Error", "!");

        private String brandString;
        private String brandCode;

        public static ToolBrand convertFromBrandCode(String brandCode) {
            switch (brandCode) {
                case "S":
                    return ToolBrand.STIHL;
                case "W":
                    return ToolBrand.WERNER;
                case "D":
                    return ToolBrand.DEWALT;
                case "R":
                    return ToolBrand.RIDGID;
                default:
                    return ToolBrand.ERROR;
            }
        }

        public String toString() {
            return brandString + " " + brandCode;
        }
    }

    /**
     * This Enum contains all associations between the types of Tools and what days they can be charged a rental fee
     */
    @Getter
    @AllArgsConstructor
    public enum ToolChargeDay {
        WEEKDAY("Weekday", new ArrayList(List.of(ToolType.LADDER, ToolType.CHAINSAW, ToolType.JACKHAMMER))),
        WEEKEND("Weekend", new ArrayList(List.of(ToolType.LADDER))),
        HOLIDAY("Holiday", new ArrayList(List.of(ToolType.CHAINSAW)));

        private String toolChargeDay;
        private ArrayList<ToolType> chargeableTools;

        public String toString() {
            return toolChargeDay + " " + chargeableTools;
        }
    }

    /**
     * This Enum holds all Fixed value Holidays in a way that can be very easily compared to other dates
     */
    @Getter
    @AllArgsConstructor
    public enum ToolFixedHoliday {
        INDEPENDENCE_DAY("Independence Day", calculateCurrentFixedHoliday( 7, 4)),
        LABOR_DAY("Labor Day", convertFloatingHolidayToFixed(ToolFloatingHoliday.LABOR_DAY));

        private String holidayString;
        private LocalDate date;

        public static LocalDate calculateCurrentFixedHoliday(int month, int day) {
            LocalDate date = LocalDate.of(LocalDate.now().getYear(), month, day);
            if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
                date = date.minusDays(1);
            } else if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                date = date.plusDays(1);
            }
            // We want THIS year's Holiday value
            return date;
        }

        public static LocalDate convertFloatingHolidayToFixed(ToolFloatingHoliday floatingHoliday) {

            // First, set the result Year to this year and result Month to the Holiday's Month
            LocalDate result = LocalDate.of(LocalDate.now().getYear(),
                    floatingHoliday.getMonth(), 1);

            // Adjust the result DayOfMonth to point to the first DayOfWeek that this Holiday falls on
            result = result.with(TemporalAdjusters.firstInMonth(floatingHoliday.getDayOfWeek()));

            // If the Holiday falls on something other than the First DayOfWeek in the month, adjust accordingly
            if (floatingHoliday.getDatePattern() > 1) {
                int adjustmentDays = 7 * (floatingHoliday.getDatePattern() - 1);
                result = result.plusDays(adjustmentDays);
            }
            return result;
        }

        public String toString() {
            return "| " + holidayString + " | " + date + " |";
        }
    }

    /**
     * This Enum holds all Floating Holidays that need to be converted to fixed (i.e. "The 3rd Thursday of the month")
     */
    @Getter
    @AllArgsConstructor
    public enum ToolFloatingHoliday {
        LABOR_DAY("Labor Day", Month.SEPTEMBER, DayOfWeek.MONDAY, 1);

        private String holidayString;
        private Month month;
        private DayOfWeek dayOfWeek;
        private int datePattern;
    }
}
