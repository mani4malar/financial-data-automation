package com.financial.automation.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class FinancialCalculationUtils {

    private static final int MONEY_SCALE = 2;

    private FinancialCalculationUtils() {
    }

    public static BigDecimal calculateTotalPrice(
            BigDecimal quantity,
            BigDecimal unitPrice) {

        return quantity
                .multiply(unitPrice)
                .setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }
}