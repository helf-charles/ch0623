package com.chahel.ch0623.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

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

    public static int countFixedHolidays(LocalDate toolCheckoutDate, int toolCheckoutDuration) {
        int counter = 0;
        Month targetMonth = toolCheckoutDate.getMonth();
        int targetDay = toolCheckoutDate.getDayOfMonth();
        int toolFinalDay = targetDay + toolCheckoutDuration;
        for (EnumsForTools.ToolFixedHoliday holiday : EnumsForTools.ToolFixedHoliday.values()) {
            if (holiday.getMonth() == targetMonth) {
                if ((holiday.getDay() >= targetDay) && (holiday.getDay() <= toolFinalDay)){
                    counter++;
                }
            }
        }
        return counter;
    }

    public static int countFloatingHolidays(LocalDate toolCheckoutDate, int toolCheckoutDuration) {
        int counter = 0;
        Month targetMonth = toolCheckoutDate.getMonth();
        int targetDay = toolCheckoutDate.getDayOfMonth();
        int toolFinalDay = targetDay + toolCheckoutDuration;
        for (EnumsForTools.ToolFloatingHoliday holiday : EnumsForTools.ToolFloatingHoliday.values()) {
            if (holiday.getMonth() == targetMonth) {
                int holidayDate = findFloatingHolidayDate(toolCheckoutDate);
                if (holiday.getDatePattern() > 1) {
                    holidayDate = holidayDate + (7 * (holiday.getDatePattern() - 1));
                }
                if ((holidayDate >= targetDay) && (holidayDate <= toolFinalDay)){
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
