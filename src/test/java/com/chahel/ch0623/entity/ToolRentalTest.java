package com.chahel.ch0623.entity;

import org.junit.jupiter.api.Test;

import com.chahel.ch0623.entity.EnumsForTools.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ToolRentalTest {

    // 2023 JAN 13 SUN
    private LocalDate dateFirst = LocalDate.of(LocalDate.now().getYear(), 1, 13);

    // 2023 JUL 03 MON
    private LocalDate dateSecond = LocalDate.of(LocalDate.now().getYear(), 7, 3);

    // 2023 SEP 01 FRI
    private LocalDate dateThird = LocalDate.of(LocalDate.now().getYear(), 9, 1);

    // 2023 AUG 27 SUN
    private LocalDate dateFourth = LocalDate.of(LocalDate.now().getYear(), 8, 27);

    @Test
    void scratch() {
        System.out.println(DayOfWeek.MONDAY.getValue());
        System.out.println(dateSecond.format(DateTimeFormatter.ofPattern("MM/dd/YY")));

        ToolRental rental01 = new ToolRental(Integer.toUnsignedLong(1), ToolType.LADDER.getTypeCode(),
                ToolBrand.RIDGID.getBrandCode(), dateSecond, 5, 0, 9.95);
        System.out.println((rental01.processRentalAgreement()));
    }

    @Test
    void checkValidRentalsReturnCorrectFinalCharge() {
        ToolRental rental01 = new ToolRental(Integer.toUnsignedLong(1), ToolType.LADDER.getTypeCode(),
                ToolBrand.RIDGID.getBrandCode(), dateFirst, 5, 0, 9.95);
        ToolRental rental02 = new ToolRental(Integer.toUnsignedLong(1), ToolType.LADDER.getTypeCode(),
                ToolBrand.RIDGID.getBrandCode(), dateFirst, 3, 0, 5.97);

        double result01 = rental01.calculateFinalCharge(5, rental01.getDiscount());
        double result02 = rental02.calculateFinalCharge(3, rental02.getDiscount());

        assertEquals(rental01.getFinalCharge(), result01);
        assertEquals(rental02.getFinalCharge(), result02);
    }

    @Test
    void checkFixedHolidaysCorrectlyCalculated() {
        ToolRental rental01 = new ToolRental(Integer.toUnsignedLong(2), ToolType.JACKHAMMER.getTypeCode(),
                ToolBrand.DEWALT.getBrandCode(), dateSecond, 4, 0, 14.95);
        ToolRental rental02 = new ToolRental(Integer.toUnsignedLong(3), ToolType.LADDER.getTypeCode(),
                ToolBrand.WERNER.getBrandCode(), dateThird, 9, 0, 99.0);

        int holidays01 = rental01.calculateHolidays();
        int holidays02 = rental02.calculateHolidays();

        assertEquals(1, holidays01);
        assertEquals(1, holidays02);
    }

    @Test
    void checkWeekendsCorrectlyCalculated() {
        ToolRental rental = new ToolRental(Integer.toUnsignedLong(1), ToolType.LADDER.getTypeCode(),
                ToolBrand.RIDGID.getBrandCode(), dateFirst,  11, 0, 9.95);

        int weekends = rental.calculateWeekends(dateFirst.getDayOfWeek());

        assertEquals(4, weekends);
    }

    @Test
    void checkChargeableDaysCorrectlyCalculated() {
        ToolRental rental = new ToolRental(Integer.toUnsignedLong(10), ToolType.LADDER.getTypeCode(),
                ToolBrand.DEWALT.getBrandCode(), dateSecond, 8, 0, 17.94);

        int result01 = rental.determineChargeableDays(5, 2, 1);
        int result02 = rental.determineChargeableDays(0, 2, 1);
        int result03 = rental.determineChargeableDays(5, 0, 1);
        int result04 = rental.determineChargeableDays(5, 2, 0);

        rental.setToolCode(ToolType.CHAINSAW.getTypeCode());

        int result05 = rental.determineChargeableDays(5, 2, 1);
        int result06 = rental.determineChargeableDays(0, 2, 1);
        int result07 = rental.determineChargeableDays(5, 0, 1);
        int result08 = rental.determineChargeableDays(5, 2, 0);

        rental.setToolCode(ToolType.JACKHAMMER.getTypeCode());

        int result09 = rental.determineChargeableDays(5, 2, 1);
        int result10 = rental.determineChargeableDays(0, 2, 1);
        int result11 = rental.determineChargeableDays(5, 0, 1);
        int result12 = rental.determineChargeableDays(5, 2, 0);

        assertEquals(7, result01);
        assertEquals(2, result02);
        assertEquals(5, result03);
        assertEquals(7, result04);

        assertEquals(6, result05);
        assertEquals(1, result06);
        assertEquals(6, result07);
        assertEquals(5, result08);

        assertEquals(5, result09);
        assertEquals(0, result10);
        assertEquals(5, result11);
        assertEquals(5, result12);
    }
}
