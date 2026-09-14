package com.financial.automation.validators;

import com.financial.automation.models.InstrumentDetails;

import java.util.List;

public final class InstrumentCompletenessValidator {

    private InstrumentCompletenessValidator() {
        // Utility class
    }

    public static boolean areComplete(
            List<InstrumentDetails> instruments) {

        return instruments.stream().allMatch(instrument ->
                CompletenessValidator.isNotBlank(instrument.getId())
                        && CompletenessValidator.isNotBlank(instrument.getName())
                        && CompletenessValidator.isNotBlank(instrument.getIsin())
                        && instrument.getUnitPrice() != null
        );
    }
}