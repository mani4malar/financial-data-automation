package com.financial.automation.validators;

import com.financial.automation.models.PositionDetails;

import java.util.List;

public final class PositionCompletenessValidator {

    private PositionCompletenessValidator() {
    }

    public static boolean areComplete(List<PositionDetails> positions) {
        return positions.stream()
                .allMatch(position ->
                        CompletenessValidator.isNotBlank(position.getId())
                                && CompletenessValidator.isNotBlank(position.getInstrumentId())
                                && position.getQuantity() != null
                );
    }
}