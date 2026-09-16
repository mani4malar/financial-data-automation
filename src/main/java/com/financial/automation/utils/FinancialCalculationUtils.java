package com.financial.automation.utils;

import com.financial.automation.config.ConfigReader;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class FinancialCalculationUtils {

    private FinancialCalculationUtils() {
    }

    /**
     * Calculates Total Price using the configured money scale
     * and rounding mode.
     *
     * Formula:
     *
     * Total Price = Quantity × Unit Price
     *
     * The final result is rounded according to:
     * - money.scale
     * - rounding.mode
     *
     * configured in config.properties.
     */
    public static BigDecimal calculateTotalPrice(
            BigDecimal quantity,
            BigDecimal unitPrice) {

        int moneyScale = getMoneyScale();
        RoundingMode roundingMode = getRoundingMode();

        return quantity
                .multiply(unitPrice)
                .setScale(moneyScale, roundingMode);
    }

    /**
     * Reads the configured monetary scale.
     */
    private static int getMoneyScale() {

        String scale =
                ConfigReader.get("money.scale");

        if (scale == null || scale.isBlank()) {
            throw new IllegalArgumentException(
                    "Configuration 'money.scale' is missing or empty"
            );
        }

        try {
            int value = Integer.parseInt(scale);

            if (value < 0) {
                throw new IllegalArgumentException(
                        "Configuration 'money.scale' must be >= 0. Actual="
                                + value
                );
            }

            return value;

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    "Configuration 'money.scale' must be a valid integer. Actual="
                            + scale,
                    e
            );
        }
    }

    /**
     * Reads the configured rounding mode.
     */
    private static RoundingMode getRoundingMode() {

        String roundingMode =
                ConfigReader.get("rounding.mode");

        if (roundingMode == null || roundingMode.isBlank()) {
            throw new IllegalArgumentException(
                    "Configuration 'rounding.mode' is missing or empty"
            );
        }

        try {

            return RoundingMode.valueOf(
                    roundingMode.trim().toUpperCase()
            );

        } catch (IllegalArgumentException e) {

            throw new IllegalArgumentException(
                    "Configuration 'rounding.mode' is invalid. Actual="
                            + roundingMode,
                    e
            );
        }
    }
}