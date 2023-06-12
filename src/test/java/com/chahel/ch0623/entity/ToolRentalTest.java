package com.chahel.ch0623.entity;

import org.junit.jupiter.api.Test;

import com.chahel.ch0623.entity.EnumsForTools.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ToolRentalTest {

    // 2023 JAN 13 SUN
    private LocalDate dateFirst = LocalDate.of(2023, 1, 13);

    // 2023 JUL 03 MON
    private LocalDate dateSecond = LocalDate.of(2023, 7, 3);

    // 2023 SEP 01 FRI
    private LocalDate dateThird = LocalDate.of(2023, 9, 1);

    // 2023 AUG 27 SUN
    private LocalDate dateFourth = LocalDate.of(2023, 8, 27);

    // JAN 13 2023 SUN - JAN 14 2023 MON
    private ToolRental rentalOneWeekday = new ToolRental(Integer.toUnsignedLong(0),
            ToolType.CHAINSAW.getTypeCode(), ToolBrand.STIHL.getBrandCode(), dateFirst,
            1, 0.0, 1.49);

    // JAN 13 2023 SUN - JAN 18 2023 FRI
    private ToolRental rentalManyWeekdays = new ToolRental(Integer.toUnsignedLong(1),
            ToolType.LADDER.getTypeCode(), ToolBrand.RIDGID.getBrandCode(), dateFirst,
            5, 0.0, 9.95);

    // JUL 03 2023 MON - JUL 07 2023 FRI
    private ToolRental rentalJulyHoliday = new ToolRental(Integer.toUnsignedLong(2),
            ToolType.JACKHAMMER.getTypeCode(), ToolBrand.DEWALT.getBrandCode(), dateSecond,
            4, 0.0, 14.95);

    // SEP 01 2023 FRI - SEP 05 2023 TUE
    private ToolRental rentalSeptemberHoliday = new ToolRental(Integer.toUnsignedLong(3),
            ToolType.JACKHAMMER.getTypeCode(), ToolBrand.DEWALT.getBrandCode(), dateThird,
            5, 0.0, 11.96);

    private ToolRental rentalJulySeptemberHoliday = new ToolRental(Integer.toUnsignedLong(4),
            ToolType.LADDER.getTypeCode(), ToolBrand.WERNER.getBrandCode(), dateFourth,
            9, 0.0, 11.96);

    @Test
    void checkValidRentalsReturnCorrectFinalCharge() {
        double resultFirst = ToolRental.calculateFinalCharge(rentalOneWeekday.getToolCode(),
                rentalOneWeekday.getRentalDays(), rentalOneWeekday.getDiscount());
        double resultSecond = ToolRental.calculateFinalCharge(rentalManyWeekdays.getToolCode(),
                rentalManyWeekdays.getRentalDays(), rentalManyWeekdays.getDiscount());
        assertEquals(rentalOneWeekday.getFinalCharge(), resultFirst);
        assertEquals(rentalManyWeekdays.getFinalCharge(), resultSecond);
    }

    @Test
    void checkFixedHolidaysCorrectlyCalculated() {
        int holidays = ToolRental.countHolidays(dateSecond,
                dateSecond.plusDays(rentalJulyHoliday.getRentalDays()));
        assertEquals(1, holidays);
    }

    @Test
    void checkWeekendsCorrectlyCalculated() {
        int weekends = ToolRental.countWeekends(dateFirst.getDayOfWeek(), dateFirst.getDayOfMonth(),
                dateFirst.getDayOfMonth() + 11);
        assertEquals(4, weekends);
    }

    @Test
    void checkChargeableDaysCorrectlyCalculated() {
        int result01 = ToolRental.calculateChargeableDays(ToolType.LADDER, 5, 2, 1);
        int result02 = ToolRental.calculateChargeableDays(ToolType.LADDER, 0, 2, 1);
        int result03 = ToolRental.calculateChargeableDays(ToolType.LADDER, 5, 0, 1);
        int result04 = ToolRental.calculateChargeableDays(ToolType.LADDER, 5, 2, 0);

        int result05 = ToolRental.calculateChargeableDays(ToolType.CHAINSAW, 5, 2, 1);
        int result06 = ToolRental.calculateChargeableDays(ToolType.CHAINSAW, 0, 2, 1);
        int result07 = ToolRental.calculateChargeableDays(ToolType.CHAINSAW, 5, 0, 1);
        int result08 = ToolRental.calculateChargeableDays(ToolType.CHAINSAW, 5, 2, 0);

        int result09 = ToolRental.calculateChargeableDays(ToolType.JACKHAMMER, 5, 2, 1);
        int result10 = ToolRental.calculateChargeableDays(ToolType.JACKHAMMER, 0, 2, 1);
        int result11 = ToolRental.calculateChargeableDays(ToolType.JACKHAMMER, 5, 0, 1);
        int result12 = ToolRental.calculateChargeableDays(ToolType.JACKHAMMER, 5, 2, 0);

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
