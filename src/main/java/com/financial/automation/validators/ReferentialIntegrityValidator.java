package com.financial.automation.validators;

import com.financial.automation.models.InstrumentDetails;
import com.financial.automation.models.PositionDetails;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class ReferentialIntegrityValidator {

    private ReferentialIntegrityValidator() {
    }

    public static boolean isValid(
            List<InstrumentDetails> instruments,
            List<PositionDetails> positions) {

        Set<String> instrumentIds = instruments.stream()
                .map(InstrumentDetails::getId)
                .collect(Collectors.toSet());

        return positions.stream()
                .allMatch(position ->
                        instrumentIds.contains(position.getInstrumentId()));
    }
}