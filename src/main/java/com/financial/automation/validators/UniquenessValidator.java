package com.financial.automation.validators;

import com.financial.automation.models.InstrumentDetails;
import com.financial.automation.models.PositionDetails;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class UniquenessValidator {

    private UniquenessValidator() {
    }

    public static boolean areInstrumentIdsUnique(
            List<InstrumentDetails> instruments) {

        return areUnique(instruments, InstrumentDetails::getId);
    }

    public static boolean areIsinsUnique(
            List<InstrumentDetails> instruments) {

        return areUnique(instruments, InstrumentDetails::getIsin);
    }

    public static boolean arePositionIdsUnique(
            List<PositionDetails> positions) {

        return areUnique(positions, PositionDetails::getId);
    }

    private static <T> boolean areUnique(
            List<T> records,
            Function<T, String> keyExtractor) {

        Set<String> uniqueValues = records.stream()
                .map(keyExtractor)
                .collect(Collectors.toSet());

        return uniqueValues.size() == records.size();
    }
}