package com.financial.automation.validators;

import com.financial.automation.models.InstrumentDetails;

import java.util.List;

public final class InstrumentNumericValidator {

    private InstrumentNumericValidator() {
        // Utility class
    }

    public static boolean hasValidUnitPrices(
            List<InstrumentDetails> instruments) {

        return instruments.stream()
                .allMatch(instrument ->
                        NumericValidator.isPositive(
                                instrument.getUnitPrice()
                        )
                );
    }
}