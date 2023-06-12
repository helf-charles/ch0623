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
    void scratch() {
        System.out.println("Hello world!");
        for (ToolFixedHoliday holiday : ToolFixedHoliday.values())
        {
            System.out.println(holiday);
        }

    }

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
    void checkFixedHolidaysCorrectlyIdentified() {
        int holidays = ToolRental.countHolidays(dateSecond,
                dateSecond.plusDays(rentalJulyHoliday.getRentalDays()));
        assertEquals(1, holidays);
    }

    @Test
    void checkWeekendsCorrectlyIdentified() {
        int weekends = ToolRental.countWeekends(dateFirst.getDayOfWeek(), dateFirst.getDayOfMonth(),
                dateFirst.getDayOfMonth() + 11);
        assertEquals(4, weekends);
    }

    @Test
    void checkChargeableDaysCorrectlyCalculated() {

    }
}
