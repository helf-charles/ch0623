package com.chahel.ch0623.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.SQLOutput;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToolRental {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private Long id;

    private String toolCode;
    private String brandCode;
    private LocalDate rentalDate;
    private int rentalDays;
    private double discount;
    private double finalCharge;

    public static double calculateFinalCharge(String toolCode, int rentalDays, double discount) {
        EnumsForTools.ToolType toolType = EnumsForTools.ToolType.convertFromTypeCode(toolCode);
        double price = toolType.getTypePrice();
        return ( ((double) rentalDays * price) * (1.0 - discount));
    }

    public static ArrayList<Integer> countNonWeekdays(LocalDate toolCheckoutDate, int toolCheckoutDuration) {
        int countWeekends = 0;
        int countHolidays = 0;
        ArrayList<Integer> result = new ArrayList();

        Month checkoutMonth = toolCheckoutDate.getMonth();
        int checkoutDay = toolCheckoutDate.getDayOfMonth();
        int returnDay = checkoutDay + toolCheckoutDuration;

        countHolidays += countFixedHolidays(checkoutMonth, checkoutDay, returnDay);
        int holidayDate = findFloatingHolidayDate(toolCheckoutDate);
        countHolidays += countFloatingHolidays(checkoutMonth, checkoutDay, returnDay, holidayDate);
        countWeekends += countWeekends(toolCheckoutDate.getDayOfWeek(), checkoutDay, returnDay);

        result.add(countWeekends);
        result.add(countHolidays);
        return result;
    }

    public static int countWeekends(DayOfWeek checkoutDayOfWeek, int checkoutDay, int returnDay) {
        int counter = 0;
        int dayOfWeekInt = checkoutDayOfWeek.getValue();

        for (int i = checkoutDay; i < returnDay; i++) {
            if ((checkoutDayOfWeek == DayOfWeek.SATURDAY) || (checkoutDayOfWeek == DayOfWeek.SUNDAY)) {
                counter++;
            }
            dayOfWeekInt++;
            if (dayOfWeekInt > 7) {
                dayOfWeekInt %= 7;
            }
            checkoutDayOfWeek = checkoutDayOfWeek.of(dayOfWeekInt);
        }

        return counter;
    }

    public static int countFixedHolidays(Month checkoutMonth, int checkoutDay, int returnDay) {
        int counter = 0;
        for (EnumsForTools.ToolFixedHoliday holiday : EnumsForTools.ToolFixedHoliday.values()) {
            if (holiday.getMonth() == checkoutMonth) {
                if ((holiday.getDay() >= checkoutDay) && (holiday.getDay() <= returnDay)){
                    counter++;
                }
            }
        }
        return counter;
    }

    public static int countFloatingHolidays(Month checkoutMonth, int checkoutDay, int returnDay, int holidayDate) {
        int counter = 0;
        for (EnumsForTools.ToolFloatingHoliday holiday : EnumsForTools.ToolFloatingHoliday.values()) {
            if (holiday.getMonth() == checkoutMonth) {
                System.out.println("Holiday month is " + holiday.getMonth());
                System.out.println("Holiday date is " + holidayDate);
                System.out.println("Holiday pattern is " + holiday.getDatePattern());
                if (holiday.getDatePattern() > 1) {
                    holidayDate = holidayDate + (7 * (holiday.getDatePattern() - 1));
                }
                if ((holidayDate >= checkoutDay) && (holidayDate <= returnDay)){
                    counter++;
                }
            }
        }
        return counter;
    }

    public static int findFloatingHolidayDate(LocalDate targetDate) {
        return targetDate.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY)).getDayOfMonth();
    }
}
