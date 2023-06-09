package com.chahel.ch0623.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;


public class ToolRental {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private Long id;

    private String toolCode;
    private String brandCode;
    private String rentalDate;
    private String rentalDuration;
    private double discount;
    private double finalCharge;

    public ToolRental(Long id, String toolCode, String brandCode, String rentalDate, String rentalDuration, double discount, double finalCharge) {
        this.id = id;
        this.toolCode = toolCode;
        this.brandCode = brandCode;
        this.rentalDate = rentalDate;
        this.rentalDuration = rentalDuration;
        this.discount = discount;
        this.finalCharge = finalCharge;
    }
}
