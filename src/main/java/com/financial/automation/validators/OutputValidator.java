package com.financial.automation.validators;

import com.financial.automation.models.PositionReport;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class OutputValidator {

    private OutputValidator() {
    }

    public static boolean hasExpectedRecords(
            List<PositionReport> actualReports,
            List<PositionReport> expectedReports) {

        if (actualReports.size() != expectedReports.size()) {
            return false;
        }

        Map<String, PositionReport> actualByPositionId =
                toUniquePositionMap(actualReports);

        Map<String, PositionReport> expectedByPositionId =
                toUniquePositionMap(expectedReports);

        if (actualByPositionId == null || expectedByPositionId == null) {
            return false;
        }

        if (actualByPositionId.size() != actualReports.size()) {
            return false;
        }

        if (expectedByPositionId.size() != expectedReports.size()) {
            return false;
        }

        if (!actualByPositionId.keySet().equals(expectedByPositionId.keySet())) {
            return false;
        }

        for (String positionId : expectedByPositionId.keySet()) {

            PositionReport expected = expectedByPositionId.get(positionId);
            PositionReport actual = actualByPositionId.get(positionId);

            if (!sameText(expected.getIsin(), actual.getIsin())) {
                return false;
            }

            if (!sameAmount(expected.getQuantity(), actual.getQuantity())) {
                return false;
            }

            if (!sameAmount(expected.getTotalPrice(), actual.getTotalPrice())) {
                return false;
            }
        }

        return true;
    }

    private static Map<String, PositionReport> toUniquePositionMap(
            List<PositionReport> reports) {

        Map<String, PositionReport> reportMap = new HashMap<>();

        for (PositionReport report : reports) {
            String positionId = report.getPositionId();

            if (positionId == null || reportMap.containsKey(positionId)) {
                return null;
            }

            reportMap.put(positionId, report);
        }

        return reportMap;
    }

    private static boolean sameText(String expected, String actual) {
        if (expected == null) {
            return actual == null;
        }

        return expected.equals(actual);
    }

    private static boolean sameAmount(BigDecimal expected, BigDecimal actual) {
        if (expected == null) {
            return actual == null;
        }

        return actual != null
                && expected.compareTo(actual) == 0;
    }
}
