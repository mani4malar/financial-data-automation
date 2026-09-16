package com.financial.automation.tests.data_test.endtoend;

import com.financial.automation.config.ConfigReader;
import com.financial.automation.models.InstrumentDetails;
import com.financial.automation.models.PositionDetails;
import com.financial.automation.models.PositionReport;
import com.financial.automation.models.ValidationResult;
import com.financial.automation.readers.CsvReader;
import com.financial.automation.utils.ExpectedReportGenerator;
import com.financial.automation.utils.FrameworkLogger;
import com.financial.automation.utils.ReconciliationReportGenerator;
import com.financial.automation.validators.InstrumentCompletenessValidator;
import com.financial.automation.validators.InstrumentNumericValidator;
import com.financial.automation.validators.OutputValidator;
import com.financial.automation.validators.PositionNumericValidator;
import com.financial.automation.validators.ReferentialIntegrityValidator;
import com.financial.automation.validators.UniquenessValidator;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class EndToEndReconciliationTest {

    @Test(groups = {"end-to-end", "regression"})
    public void shouldValidateAndReconcilePositionReport()
            throws IOException {

        FrameworkLogger.info(
                "========== Financial Data Validation Started =========="
        );

        FrameworkLogger.info(
                "Instrument file: "
                        + ConfigReader.get("instrument.file")
        );

        FrameworkLogger.info(
                "Position file: "
                        + ConfigReader.get("position.file")
        );

        FrameworkLogger.info(
                "Report file: "
                        + ConfigReader.get("report.file")
        );

        CsvReader reader = new CsvReader();

        /*
         * Read InstrumentDetails.
         */
        List<InstrumentDetails> instruments =
                reader.readInstrumentDetails(
                        ConfigReader.get("instrument.file"));

        FrameworkLogger.info(
                "Instrument records loaded: "
                        + instruments.size()
        );

        /*
         * Read PositionDetails.
         */
        List<PositionDetails> positions =
                reader.readPositionDetails(
                        ConfigReader.get("position.file"));

        FrameworkLogger.info(
                "Position records loaded: "
                        + positions.size()
        );

        /*
         * Read actual PositionReport.
         */
        List<PositionReport> actualReports =
                reader.readPositionReport(
                        ConfigReader.get("report.file"));

        FrameworkLogger.info(
                "Actual report records loaded: "
                        + actualReports.size()
        );

        /*
         * Validate instrument completeness.
         */
        FrameworkLogger.info(
                "Validating instrument completeness..."
        );

        Assert.assertTrue(
                InstrumentCompletenessValidator.areComplete(instruments),
                "Instrument data should be complete"
        );

        FrameworkLogger.info(
                "Instrument completeness validation passed"
        );

        /*
         * Validate Unit Prices.
         */
        FrameworkLogger.info(
                "Validating instrument Unit Prices..."
        );

        Assert.assertTrue(
                InstrumentNumericValidator.hasValidUnitPrices(instruments),
                "All Unit Prices should be positive"
        );

        FrameworkLogger.info(
                "Instrument Unit Price validation passed"
        );

        /*
         * Validate Quantities.
         */
        FrameworkLogger.info(
                "Validating position Quantities..."
        );

        Assert.assertTrue(
                PositionNumericValidator.hasValidQuantities(positions),
                "All Quantities should be positive"
        );

        FrameworkLogger.info(
                "Position Quantity validation passed"
        );

        /*
         * Validate referential integrity.
         */
        FrameworkLogger.info(
                "Validating Position -> Instrument referential integrity..."
        );

        Assert.assertTrue(
                ReferentialIntegrityValidator.isValid(
                        instruments,
                        positions),
                "All PositionDetails InstrumentIDs should exist"
        );

        FrameworkLogger.info(
                "Referential integrity validation passed"
        );

        /*
         * Validate uniqueness.
         */
        FrameworkLogger.info(
                "Validating Instrument IDs uniqueness..."
        );

        Assert.assertTrue(
                UniquenessValidator.areInstrumentIdsUnique(instruments),
                "Instrument IDs should be unique"
        );

        FrameworkLogger.info(
                "Instrument ID uniqueness validation passed"
        );

        FrameworkLogger.info(
                "Validating ISIN uniqueness..."
        );

        Assert.assertTrue(
                UniquenessValidator.areIsinsUnique(instruments),
                "Instrument ISINs should be unique"
        );

        FrameworkLogger.info(
                "ISIN uniqueness validation passed"
        );

        FrameworkLogger.info(
                "Validating Position ID uniqueness..."
        );

        Assert.assertTrue(
                UniquenessValidator.arePositionIdsUnique(positions),
                "Position IDs should be unique"
        );

        FrameworkLogger.info(
                "Position ID uniqueness validation passed"
        );

        /*
         * Generate independently calculated expected report.
         */
        FrameworkLogger.info(
                "Generating expected PositionReport..."
        );

        List<PositionReport> expectedReports =
                ExpectedReportGenerator.generate(
                        instruments,
                        positions);

        FrameworkLogger.info(
                "Expected report records generated: "
                        + expectedReports.size()
        );

        /*
         * Generate reconciliation evidence.
         */
        FrameworkLogger.info(
                "Generating reconciliation report..."
        );

        ReconciliationReportGenerator.generate(
                positions,
                expectedReports,
                actualReports
        );

        FrameworkLogger.info(
                "Reconciliation report generated at: "
                        + "target/data-reports/reconciliation-summary.html"
        );

        /*
         * Perform detailed output validation.
         */
        FrameworkLogger.info(
                "Starting expected vs actual reconciliation..."
        );

        ValidationResult validationResult =
                OutputValidator.validate(
                        actualReports,
                        expectedReports
                );

        if (validationResult.isValid()) {

            FrameworkLogger.info(
                    "Expected vs actual reconciliation PASSED"
            );

        } else {

            FrameworkLogger.error(
                    "Expected vs actual reconciliation FAILED. "
                            + "Error count: "
                            + validationResult.getErrorCount()
            );

            validationResult.getErrors()
                    .forEach(error ->
                            FrameworkLogger.error(
                                    "Reconciliation error: " + error
                            )
                    );
        }

        Assert.assertTrue(
                validationResult.isValid(),
                validationResult.getErrorMessage()
        );

        FrameworkLogger.info(
                "========== Financial Data Validation Completed =========="
        );
    }
}