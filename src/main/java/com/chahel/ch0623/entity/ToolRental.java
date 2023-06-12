package com.chahel.ch0623.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
    private LocalDate checkoutDate;
    private int rentalDays;
    private double discount;
    private double finalCharge;

    public String processRentalAgreement() {
        double decimalDiscount = (double) discount / 100.0;
        if (!isValidDiscountValue(decimalDiscount)) {
            return "ERROR - Discount amount outside acceptable range (0 - 100%)";
        }
        ToolType toolType = ToolType.convertFromTypeCode(toolCode);

        LocalDate returnDate = LocalDate.of(checkoutDate.getYear(), checkoutDate.getMonth(),
                checkoutDate.getDayOfMonth());
        returnDate = returnDate.plusDays(rentalDays);

        int countHolidays = calculateHolidays(returnDate);
        int countWeekends = calculateWeekends(checkoutDate.getDayOfWeek());
        int countWeekdays = rentalDays - (countHolidays + countWeekends);
        int chargeableDays = determineChargeableDays(countWeekdays, countWeekends, countHolidays);

        finalCharge = calculateFinalCharge(chargeableDays, decimalDiscount);

        String invoiceString = generateInvoiceString(returnDate, chargeableDays, finalCharge);

        return invoiceString;
    }

    public String generateInvoiceString(LocalDate returnDate, int chargeDays, double finalCharge) {
        ToolType toolType = ToolType.convertFromTypeCode(toolCode);
        double preDiscountCharge = chargeDays * toolType.getTypePrice();

        return "Tool Code: " + toolCode + brandCode + "\n"
                + "Tool Type: " + toolType.getTypeString() + "\n"
                + "Tool Brand: " + ToolBrand.convertFromBrandCode(brandCode).getBrandString() + "\n"
                + "RentalDays: " + rentalDays + "\n"
                + "Checkout Date: " + formatDate(checkoutDate) + "\n"
                + "Due Date: " + formatDate(returnDate) + "\n"
                + "Daily Rental Charge: " + formatCurrency(toolType.getTypePrice()) + "\n"
                + "Charge Days: " + chargeDays + "\n"
                + "Pre-Discount Charge: " + formatCurrency(preDiscountCharge) + "\n"
                + "Discount Percent: " + (int) discount + "%\n"
                + "Discount Amount: " + formatCurrency(preDiscountCharge - finalCharge) + "\n"
                + "Final Charge: " + formatCurrency(finalCharge);
    }

    public String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("MM/dd/YY"));
    }

    public String formatCurrency(double value) {
        return NumberFormat.getCurrencyInstance().format(value);
    }

    public boolean isValidDiscountValue(double decimalDiscount) {
        return ((0.0 <= decimalDiscount) && (1.0 >= decimalDiscount));
    }

    public double calculateFinalCharge(int chargeableDays, double decimalDiscount) {
        ToolType toolType = ToolType.convertFromTypeCode(toolCode);
        double price = toolType.getTypePrice();
        return ( ((double) chargeableDays * price) * (1.0 - decimalDiscount));
    }

    public int determineChargeableDays(int weekdays, int weekends, int holidays) {
        ToolType toolType = ToolType.convertFromTypeCode(toolCode);
        int result = 0;

        // The ToolChargeDay Enum dictates whether this Tool can be charged for these types of Days
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

    public int calculateWeekends(DayOfWeek checkoutDayOfWeek) {
        int counter = 0;

        int fullWeeks = rentalDays / 7;
        int remainderDays = rentalDays % 7;
        int terminalWeekValue = checkoutDayOfWeek.getValue() + remainderDays;

        // Every full week has two Weekend days
        counter += 2 * fullWeeks;

        // This performs a DayOfWeek integer calculus for the final non-full rental week
        if (terminalWeekValue >= 7) {
            counter += 2;
        } else if (terminalWeekValue == 6) {
            counter++;
        }

        return counter;
    }

    public int calculateHolidays(LocalDate returnDate) {
        int counter = 0;

        // As long as the Holiday occurs within the range of Checkout and Return, it applies to this rental
        for (EnumsForTools.ToolFixedHoliday holiday : EnumsForTools.ToolFixedHoliday.values()) {
            if (
                    (holiday.getDate().isAfter(checkoutDate) && (holiday.getDate().isBefore(returnDate)))
                    || (holiday.getDate().isEqual(checkoutDate))
                    || (holiday.getDate().isEqual(returnDate))
            )
            {
                    counter++;
            }
        }

        return counter;
    }
}
