package com.chahel.ch0623.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public class EnumsForTools {

    @Getter
    @AllArgsConstructor
    public enum ToolType {
        CHAINSAW("Chainsaw", "CHN", 1.49),
        LADDER("Ladder", "LAD", 1.99),
        JACKHAMMER("Jackhammer", "JAK", 2.99),
        ERROR("Error", "ERR", 0.00);

        private String typeString;
        private String typeCode;
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

        public List<ToolChargeDay> findChargeableDays() {
            ArrayList<ToolChargeDay> result = new ArrayList();
            result.add(ToolChargeDay.WEEKDAY);
            switch (this) {
                case CHAINSAW:
                    result.add(ToolChargeDay.HOLIDAY);
                    break;
                case LADDER:
                    result.add(ToolChargeDay.WEEKEND);
                    break;
            }
            return result;
        }

        public String toString() {
            return typeString + " " + typeCode + " " + typePrice;
        }
    }

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

    @Getter
    @AllArgsConstructor
    public enum ToolBrandPairings {
        CHAINSAW("S"),
        LADDER("W"),
        JACKHAMMER("DR");

        private String brandListString;
    }

    @Getter
    @AllArgsConstructor
    public enum ToolChargeDay {
        WEEKDAY("Weekday", new ArrayList(List.of(ToolType.LADDER, ToolType.CHAINSAW, ToolType.JACKHAMMER))),
        WEEKEND("Weekend", new ArrayList(List.of(ToolType.LADDER))),
        HOLIDAY("Holiday", new ArrayList(List.of(ToolType.JACKHAMMER)));

        private String toolChargeDay;
        private ArrayList<ToolType> chargeableTools;

        public String toString() {
            return toolChargeDay + " " + chargeableTools;
        }
    }

    @Getter
    @AllArgsConstructor
    public enum ToolFixedHoliday {
        INDEPENDENCE_DAY("Independence Day", calculateCurrentFixedHoliday( 7, 4)),
        LABOR_DAY("Labor Day", convertFloatingHolidayToFixed(ToolFloatingHoliday.LABOR_DAY));

        private String holidayString;
        private LocalDate date;

        private static LocalDate calculateCurrentFixedHoliday(int month, int day) {
            return LocalDate.of(LocalDate.now().getYear(), month, day);
        }
        private static LocalDate convertFloatingHolidayToFixed(ToolFloatingHoliday floatingHoliday) {
            LocalDate result = LocalDate.of(LocalDate.now().getYear(),
                    floatingHoliday.getMonth(), 1);
            result = result.with(TemporalAdjusters.firstInMonth(floatingHoliday.getDayOfWeek()));
            if (floatingHoliday.getDatePattern() > 1) {
                int adjustmentDays = 7 * (floatingHoliday.getDatePattern() - 1);
                result.plusDays(adjustmentDays);
            }
            return result;
        }

        public String toString() {
            return "| " + holidayString + " | " + date + " |";
        }
    }

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
