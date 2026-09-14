package com.financial.automation.validators;

import com.financial.automation.models.PositionReport;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class OutputValidator {

    private OutputValidator() {
    }

    public static boolean hasExpectedRecords(
            List<PositionReport> actualReports,
            List<PositionReport> expectedReports) {

        Set<String> actualPositionIds = actualReports.stream()
                .map(PositionReport::getPositionId)
                .collect(Collectors.toSet());

        Set<String> expectedPositionIds = expectedReports.stream()
                .map(PositionReport::getPositionId)
                .collect(Collectors.toSet());

        return actualPositionIds.equals(expectedPositionIds);
    }
}