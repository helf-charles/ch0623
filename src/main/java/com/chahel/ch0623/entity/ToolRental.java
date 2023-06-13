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
        try {
            isValidToolRentalObject();
        } catch (ToolRentalException e) {
            return e.getMessage();
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

        return "\n"
                + "Tool Code: " + toolBrandCode + "\n"
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
                + "Final Charge: " + formatCurrency(finalCharge) + "\n"
                + "--------------------\n";
    }

    public boolean isValidToolRentalObject() {
        if (!isValidDiscountValue()) {
            throw new ToolRentalException("ERROR - Discount value " + discount + " outside acceptable range (0 - 100%)");
        }
        if (!isValidRentalDaysValue()) {
            throw new ToolRentalException("ERROR - RentalDays value " + rentalDays + " outside acceptable range (1+)");
        }
        if (!isValidToolCodeValue()) {
            throw new ToolRentalException("ERROR - ToolCode value " + toolBrandCode.substring(0, 3) + " not recognized");
        }
        if (!isValidBrandCodeValue()) {
            throw new ToolRentalException("ERROR - BrandCode value " + toolBrandCode.substring(3) + " not recognized");
        }
        if(!isValidToolBrandPairing()) {
            throw new ToolRentalException("ERROR - ToolCode and BrandCode pair " + toolBrandCode + " invalid");
        }
        if (!isValidCheckoutDate()) {
            //throw new ToolRentalException("ERROR - CheckoutDate value " + checkoutDate.toString() + " is before current date");
            return true;
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

        // Every full week has two Weekend days
        counter += 2 * fullWeeks;

        // This performs a DayOfWeek integer calculus for the final non-full rental week
        for (int i = 0; i < remainderDays; i++) {
            checkoutDayOfWeek = checkoutDayOfWeek.plus(1);
            if ((checkoutDayOfWeek == DayOfWeek.SATURDAY) || (checkoutDayOfWeek == DayOfWeek.SUNDAY)) {
                counter++;
            }
        }

        return counter;
    }

    public int calculateHolidays() {
        LocalDate returnDate = LocalDate.of(checkoutDate.getYear(), checkoutDate.getMonth(),
                checkoutDate.getDayOfMonth()).plusDays(rentalDays);
        int counter = 0;
        int yearDifference = checkoutDate.getYear() - ToolFixedHoliday.INDEPENDENCE_DAY.getDate().getYear();
        LocalDate holidayDate;

        // As long as the Holiday occurs within the range of Checkout and Return, it applies to this rental
        for (ToolFixedHoliday holiday : ToolFixedHoliday.values()) {
            holidayDate = holiday.getDate().plusYears(yearDifference);
            System.out.println("\nTarget holiday date is " + holidayDate + "\n");
            if (
                    (holidayDate.isAfter(checkoutDate) && (holidayDate.isBefore(returnDate)))
                    || (holidayDate.isEqual(checkoutDate))
                    || (holidayDate.isEqual(returnDate))
            )
            {
                System.out.println("\nHOLIDAY FOUND!\n");
                counter++;
            }
        }

        return counter;
    }
}
