package com.chahel.ch0623.entity;

import jakarta.persistence.Entity;
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
@Entity
public class ToolRental {

    @Id
    @GeneratedValue
    private Long id;
    private String toolBrandCode;
    private LocalDate checkoutDate;
    private int rentalDays;
    private int discount;
    private double finalCharge;

    public String processRentalAgreement() throws ToolRentalException {
        if (!isValidToolRentalObject()) {
            throw new ToolRentalException("Rental Agreement Processing failed");
        }
        double decimalDiscount = (double) discount / 100.0;


        int countHolidays = calculateHolidays();
        int countWeekends = calculateWeekends(checkoutDate.getDayOfWeek());
        int countWeekdays = rentalDays - (countHolidays + countWeekends);
        int chargeableDays = determineChargeableDays(countWeekdays, countWeekends, countHolidays);
        finalCharge = calculateFinalCharge(chargeableDays, decimalDiscount);

        String invoiceString = generateInvoiceString(chargeableDays);

        return invoiceString;
    }

    public String generateInvoiceString(int chargeDays) {
        ToolType toolType = ToolType.convertFromTypeCode(toolBrandCode.substring(0,3));
        double preDiscountCharge = chargeDays * toolType.getTypePrice();
        LocalDate returnDate = LocalDate.of(checkoutDate.getYear(), checkoutDate.getMonth(),
                checkoutDate.getDayOfMonth()).plusDays(rentalDays);

        return "Tool Code: " + toolBrandCode + "\n"
                + "Tool Type: " + toolType.getTypeString() + "\n"
                + "Tool Brand: " + ToolBrand.convertFromBrandCode(toolBrandCode.substring(3)).getBrandString() + "\n"
                + "RentalDays: " + rentalDays + "\n"
                + "Checkout Date: " + formatDate(checkoutDate) + "\n"
                + "Due Date: " + formatDate(returnDate) + "\n"
                + "Daily Rental Charge: " + formatCurrency(toolType.getTypePrice()) + "\n"
                + "Charge Days: " + chargeDays + "\n"
                + "Pre-Discount Charge: " + formatCurrency(preDiscountCharge) + "\n"
                + "Discount Percent: " + discount + "%\n"
                + "Discount Amount: " + formatCurrency(preDiscountCharge - finalCharge) + "\n"
                + "Final Charge: " + formatCurrency(finalCharge);
    }

    public boolean isValidToolRentalObject() {
        if (!isValidDiscountValue()) {
            throw new ToolRentalException("Discount value outside acceptable range (0 - 100%)");
        }
        if (!isValidRentalDaysValue()) {
            throw new ToolRentalException("RentalDays value outside acceptable range (1+)");
        }
        if (!isValidToolCodeValue()) {
            throw new ToolRentalException("ToolCode value not recognized");
        }
        if (!isValidBrandCodeValue()) {
            throw new ToolRentalException("BrandCode value not recognized");
        }
        if(!isValidToolBrandPairing()) {
            throw new ToolRentalException("ToolCode and BrandCode pair invalid");
        }
        if (!isValidCheckoutDate()) {
            throw new ToolRentalException("CheckoutDate value is before current date");
        }
        return true;
    }

    public String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("MM/dd/YY"));
    }

    public String formatCurrency(double value) {
        return NumberFormat.getCurrencyInstance().format(value);
    }

    public boolean isValidDiscountValue() {
        return ((0 <= discount) && (100 >= discount));
    }

    public boolean isValidRentalDaysValue() {
        return (rentalDays >= 1);
    }

    public boolean isValidToolCodeValue() {
        for (ToolType type : ToolType.values()) {
            if (toolBrandCode.substring(0,3).equals(type.getTypeCode())) {
                return true;
            }
        }
        return false;
    }

    public boolean isValidBrandCodeValue() {
        for (ToolBrand brand : ToolBrand.values()) {
            if (toolBrandCode.substring(3).equals(brand.getBrandCode())) {
                return true;
            }
        }
        return false;
    }

    public boolean isValidToolBrandPairing() {
        return ToolType.isValidBrandCode(ToolType.convertFromTypeCode(toolBrandCode.substring(0,3)), toolBrandCode.charAt(3));
    }

    public boolean isValidCheckoutDate() {
        return ((checkoutDate.isAfter(LocalDate.now())) || (checkoutDate.isEqual(LocalDate.now())));
    }

    public double calculateFinalCharge(int chargeableDays, double decimalDiscount) {
        ToolType toolType = ToolType.convertFromTypeCode(toolBrandCode.substring(0, 3));
        double price = toolType.getTypePrice();
        return ( ((double) chargeableDays * price) * (1.0 - decimalDiscount));
    }

    public int determineChargeableDays(int weekdays, int weekends, int holidays) {
        ToolType toolType = ToolType.convertFromTypeCode(toolBrandCode.substring(0, 3));
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
            counter ++;
        }
        if ((terminalWeekValue % 7) < checkoutDayOfWeek.getValue()) {
            counter++;
        }

        return counter;
    }

    public int calculateHolidays() {
        LocalDate returnDate = LocalDate.of(checkoutDate.getYear(), checkoutDate.getMonth(),
                checkoutDate.getDayOfMonth()).plusDays(rentalDays);
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
