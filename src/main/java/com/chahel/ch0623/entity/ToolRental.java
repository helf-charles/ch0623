package com.chahel.ch0623.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

import com.chahel.ch0623.entity.EnumsForTools.*;

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

    public static ArrayList<Integer> countNonWeekdays(LocalDate toolCheckoutDate, int toolCheckoutDuration) {
        int checkoutDay = toolCheckoutDate.getDayOfMonth();
        int returnDay = checkoutDay + toolCheckoutDuration;
        LocalDate toolReturnDate = LocalDate.of(toolCheckoutDate.getYear(),
                toolCheckoutDate.getMonth(), toolCheckoutDate.getDayOfMonth());
        toolReturnDate.plusDays(toolCheckoutDuration);

        int countHolidays = countHolidays(toolCheckoutDate, toolReturnDate);
        int countWeekends = countWeekends(toolCheckoutDate.getDayOfWeek(), checkoutDay, returnDay);

        ArrayList<Integer> result = new ArrayList();
        result.add(countWeekends);
        result.add(countHolidays);
        return result;
    }

    public static double calculateFinalCharge(String toolCode, int rentalDays, double discount) {
        EnumsForTools.ToolType toolType = EnumsForTools.ToolType.convertFromTypeCode(toolCode);
        double price = toolType.getTypePrice();
        return ( ((double) rentalDays * price) * (1.0 - discount));
    }

    public static int calculateChargeableDays(ToolType toolType, int weekdays, int weekends, int holidays) {
        int result = 0;
        if (ToolChargeDay.WEEKDAY.getChargeableTools().contains(toolType)) {
            result += weekdays;
        }
        if (ToolChargeDay.WEEKEND.getChargeableTools().contains(toolType)) {
            result += weekends;
        }
        if (ToolChargeDay.HOLIDAY.getChargeableTools().contains(toolType)) {
            result += holidays;
        }
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

    public static int countHolidays(LocalDate checkoutDate, LocalDate returnDate) {
        int counter = 0;

        for (EnumsForTools.ToolFixedHoliday holiday : EnumsForTools.ToolFixedHoliday.values()) {
            if (
                    (holiday.getDate().isAfter(checkoutDate) && (holiday.getDate().isBefore(returnDate)))
                    || (holiday.getDate().isEqual(checkoutDate))
                    || (holiday.getDate().isEqual(returnDate))
            )  {
                    counter++;
                }
            }
        return counter;
    }

    public static int findFloatingHolidayDate(LocalDate targetDate, DayOfWeek dayOfWeek) {
        return targetDate.with(TemporalAdjusters.firstInMonth(dayOfWeek)).getDayOfMonth();
    }
}
