package com.chahel.ch0623.entity;

import java.util.ArrayList;
import java.util.List;

public class EnumsForTools {
    public enum ToolBrand {
        STIHL("Stihl", "S"),
        WERNER("Werner", "W"),
        DEWALT("De Walt", "D"),
        RIDGID("Ridgid", "R");

        private String brandString;
        private String brandCode;

        ToolBrand(String brandString, String brandCode) {
            this.brandString = brandString;
            this.brandCode = brandCode;
        }

        public String toString() {
            return brandString + " " + brandCode;
        }
    }

    public enum ToolType {
        CHAINSAW("Chainsaw", "CHN"),
        LADDER("Ladder", "LAD"),
        JACKHAMMER("Jackhammer", "JAK");

        private String typeString;
        private String typeCode;

        ToolType(String typeString, String typeCode) {
            this.typeString = typeString;
            this.typeCode = typeCode;
        }

        public List<ToolChargeDay> FindChargeableDays() {
            ArrayList<ToolChargeDay> result = new ArrayList();
            result.add(ToolChargeDay.WEEKDAY);
            switch (this) {
                case CHAINSAW:
                    result.add(ToolChargeDay.HOLIDAY);
                    break;
                case LADDER:
                    result.add(ToolChargeDay.WEEKEND);
                    break;
            }
            return result;
        }

        public String toString() {
            return typeString + " " + typeCode;
        }
    }

    public enum ToolPrice {
        CHAINSAW(1.99), LADDER(1.49), JACKHAMMER(2.49);

        private double priceDbl;

        ToolPrice (double priceDbl) {
            this.priceDbl = priceDbl;
        }

        public String toString() {
            return String.valueOf(priceDbl);
        }
    }

    public enum ToolChargeDay {
        WEEKDAY("Weekday"), WEEKEND("Weekend"), HOLIDAY("Holiday");

        private String toolChargeDay;

        ToolChargeDay (String toolChargeDay) {
            this.toolChargeDay = toolChargeDay;
        }

        public String toString() {
            return toolChargeDay;
        }
    }
}
