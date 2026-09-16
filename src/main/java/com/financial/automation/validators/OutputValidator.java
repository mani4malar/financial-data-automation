package com.financial.automation.validators;

import com.financial.automation.models.PositionReport;
import com.financial.automation.models.ValidationResult;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class OutputValidator {

    private OutputValidator() {
    }

    /**
     * Backward-compatible validation method.
     *
     * Existing tests using hasExpectedRecords() will continue to work.
     *
     * @param actualReports   actual output records
     * @param expectedReports expected output records
     * @return true when actual output matches expected output
     */
    public static boolean hasExpectedRecords(
            List<PositionReport> actualReports,
            List<PositionReport> expectedReports) {

        return validate(actualReports, expectedReports).isValid();
    }

    /**
     * Performs detailed output validation and collects all detected
     * validation errors.
     *
     * Validation includes:
     * - record count
     * - null PositionId
     * - duplicate PositionId
     * - missing PositionId
     * - unexpected PositionId
     * - ISIN mismatch
     * - Quantity mismatch
     * - Total Price mismatch
     *
     * @param actualReports   actual output records
     * @param expectedReports expected output records
     * @return detailed validation result
     */
    public static ValidationResult validate(
            List<PositionReport> actualReports,
            List<PositionReport> expectedReports) {

        ValidationResult result = new ValidationResult();

        /*
         * Null collection validation.
         */
        if (actualReports == null) {
            result.addError(
                    "Output validation failed | Field=Actual Reports | " +
                    "Expected=Non-null list | Actual=null"
            );

            return result;
        }

        if (expectedReports == null) {
            result.addError(
                    "Output validation failed | Field=Expected Reports | " +
                    "Expected=Non-null list | Actual=null"
            );

            return result;
        }

        /*
         * Record count validation.
         */
        if (actualReports.size() != expectedReports.size()) {

            result.addError(
                    "Record count mismatch | " +
                    "Field=Record Count | " +
                    "Expected=" + expectedReports.size() + " | " +
                    "Actual=" + actualReports.size()
            );
        }

        /*
         * Build maps while collecting duplicate/null PositionId errors.
         */
        Map<String, PositionReport> actualByPositionId =
                buildPositionMap(
                        actualReports,
                        "Actual",
                        result
                );

        Map<String, PositionReport> expectedByPositionId =
                buildPositionMap(
                        expectedReports,
                        "Expected",
                        result
                );

        /*
         * If either side contains no valid records, there is nothing
         * meaningful to compare at field level.
         */
        if (actualByPositionId.isEmpty() &&
                expectedByPositionId.isEmpty()) {

            return result;
        }

        /*
         * Find missing records.
         *
         * Expected record exists but actual record does not.
         */
        for (String expectedPositionId : expectedByPositionId.keySet()) {

            if (!actualByPositionId.containsKey(expectedPositionId)) {

                result.addError(
                        "Missing record | " +
                        "PositionId=" + expectedPositionId + " | " +
                        "Reason=Expected record is not present in actual output"
                );
            }
        }

        /*
         * Find unexpected/extra records.
         *
         * Actual record exists but expected record does not.
         */
        for (String actualPositionId : actualByPositionId.keySet()) {

            if (!expectedByPositionId.containsKey(actualPositionId)) {

                result.addError(
                        "Unexpected record | " +
                        "PositionId=" + actualPositionId + " | " +
                        "Reason=Actual record is not present in expected output"
                );
            }
        }

        /*
         * Compare fields for records that exist on both sides.
         */
        for (String positionId : expectedByPositionId.keySet()) {

            PositionReport expected =
                    expectedByPositionId.get(positionId);

            PositionReport actual =
                    actualByPositionId.get(positionId);

            /*
             * Missing actual record was already reported above.
             */
            if (actual == null) {
                continue;
            }

            validateIsin(
                    positionId,
                    expected,
                    actual,
                    result
            );

            validateQuantity(
                    positionId,
                    expected,
                    actual,
                    result
            );

            validateTotalPrice(
                    positionId,
                    expected,
                    actual,
                    result
            );
        }

        return result;
    }

    /**
     * Builds a PositionId -> PositionReport map.
     *
     * Duplicate and null PositionIds are reported rather than
     * causing validation to fail silently.
     */
    private static Map<String, PositionReport> buildPositionMap(
            List<PositionReport> reports,
            String source,
            ValidationResult result) {

        Map<String, PositionReport> reportMap = new HashMap<>();

        Set<String> duplicatePositionIds = new HashSet<>();

        for (PositionReport report : reports) {

            if (report == null) {

                result.addError(
                        "Null record | " +
                        "Source=" + source + " | " +
                        "Reason=PositionReport record is null"
                );

                continue;
            }

            String positionId = report.getPositionId();

            /*
             * Null PositionId.
             */
            if (positionId == null || positionId.isBlank()) {

                result.addError(
                        "Invalid PositionId | " +
                        "Source=" + source + " | " +
                        "Field=PositionId | " +
                        "Expected=Non-null and non-blank | " +
                        "Actual=" + positionId
                );

                continue;
            }

            /*
             * Duplicate PositionId.
             */
            if (reportMap.containsKey(positionId)) {

                if (duplicatePositionIds.add(positionId)) {

                    result.addError(
                            "Duplicate record | " +
                            "Source=" + source + " | " +
                            "PositionId=" + positionId + " | " +
                            "Reason=Multiple records found for the same PositionId"
                    );
                }

                continue;
            }

            reportMap.put(positionId, report);
        }

        return reportMap;
    }

    /**
     * Validates ISIN.
     */
    private static void validateIsin(
            String positionId,
            PositionReport expected,
            PositionReport actual,
            ValidationResult result) {

        if (!sameText(
                expected.getIsin(),
                actual.getIsin())) {

            result.addError(
                    "Field mismatch | " +
                    "PositionId=" + positionId + " | " +
                    "Field=ISIN | " +
                    "Expected=" + expected.getIsin() + " | " +
                    "Actual=" + actual.getIsin() + " | " +
                    "Reason=ISIN value does not match expected output"
            );
        }
    }

    /**
     * Validates Quantity.
     */
    private static void validateQuantity(
            String positionId,
            PositionReport expected,
            PositionReport actual,
            ValidationResult result) {

        if (!sameAmount(
                expected.getQuantity(),
                actual.getQuantity())) {

            result.addError(
                    "Field mismatch | " +
                    "PositionId=" + positionId + " | " +
                    "Field=Quantity | " +
                    "Expected=" + expected.getQuantity() + " | " +
                    "Actual=" + actual.getQuantity() + " | " +
                    "Reason=Quantity value does not match expected output"
            );
        }
    }

    /**
     * Validates Total Price.
     */
    private static void validateTotalPrice(
            String positionId,
            PositionReport expected,
            PositionReport actual,
            ValidationResult result) {

        if (!sameAmount(
                expected.getTotalPrice(),
                actual.getTotalPrice())) {

            result.addError(
                    "Field mismatch | " +
                    "PositionId=" + positionId + " | " +
                    "Field=Total Price | " +
                    "Expected=" + expected.getTotalPrice() + " | " +
                    "Actual=" + actual.getTotalPrice() + " | " +
                    "Reason=Total Price value does not match expected calculation"
            );
        }
    }

    /**
     * Compares text values.
     */
    private static boolean sameText(
            String expected,
            String actual) {

        if (expected == null) {
            return actual == null;
        }

        return expected.equals(actual);
    }

    /**
     * Compares numeric values using BigDecimal.compareTo().
     *
     * This intentionally treats values such as 10.0 and 10.00
     * as numerically equal.
     */
    private static boolean sameAmount(
            BigDecimal expected,
            BigDecimal actual) {

        if (expected == null) {
            return actual == null;
        }

        return actual != null
                && expected.compareTo(actual) == 0;
    }
}