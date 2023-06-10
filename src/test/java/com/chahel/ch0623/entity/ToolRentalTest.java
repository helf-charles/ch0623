package com.chahel.ch0623.entity;

import org.junit.jupiter.api.Test;

import com.chahel.ch0623.entity.EnumsForTools.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ToolRentalTest {

    private LocalDate dateFirst = LocalDate.of(2023, 1, 13);
    private LocalDate dateSecond = LocalDate.of(2023, 1, 23);
    private LocalDate dateThird = LocalDate.of(2023, 2, 9);
    private ToolRental sampleValidOneDay = new ToolRental (Integer.toUnsignedLong(0),
            ToolType.CHAINSAW.getTypeCode(), ToolBrand.STIHL.getBrandCode(), dateFirst,
            1, 0.0, 1.49);
    private ToolRental sampleValidManyDays = new ToolRental (Integer.toUnsignedLong(0),
            ToolType.LADDER.getTypeCode(), ToolBrand.RIDGID.getBrandCode(), dateSecond,
            5, 0.0, 9.95);


    @Test
    void demoJUnit() {
        assertTrue(true);
    }

    @Test
    void scratchWork() {
        EnumsForTools.ToolType toolType = EnumsForTools.ToolType.valueOf("CHAINSAW");
        assert (toolType == EnumsForTools.ToolType.CHAINSAW);
    }

    @Test
    void checkValidRentalsReturnCorrectFinalCharge() {
        double resultFirst = ToolRental.calculateFinalCharge(sampleValidOneDay.getToolCode(),
                sampleValidOneDay.getRentalDays(), sampleValidOneDay.getDiscount());
        double resultSecond = ToolRental.calculateFinalCharge(sampleValidManyDays.getToolCode(),
                sampleValidManyDays.getRentalDays(), sampleValidManyDays.getDiscount());
        assert (resultFirst == sampleValidOneDay.getFinalCharge());
        assert (resultSecond == sampleValidManyDays.getFinalCharge());
    }
}
