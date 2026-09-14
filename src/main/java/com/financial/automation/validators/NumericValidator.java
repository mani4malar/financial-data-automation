package com.financial.automation.validators;

import java.math.BigDecimal;

public final class NumericValidator {

    private NumericValidator() {
        // Utility class
    }

    public static boolean isPositive(BigDecimal value) {
        return value != null
                && value.compareTo(BigDecimal.ZERO) > 0;
    }
}