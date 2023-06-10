package com.chahel.ch0623.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
}
