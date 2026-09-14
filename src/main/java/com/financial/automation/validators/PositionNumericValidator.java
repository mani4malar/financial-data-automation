package com.financial.automation.validators;

import com.financial.automation.models.PositionDetails;

import java.util.List;

public final class PositionNumericValidator {

    private PositionNumericValidator() {
    }

    public static boolean hasValidQuantities(List<PositionDetails> positions) {
        return positions.stream()
                .allMatch(position ->
                        NumericValidator.isPositive(position.getQuantity())
                );
    }
}