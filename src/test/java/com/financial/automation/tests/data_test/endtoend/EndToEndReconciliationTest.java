package com.financial.automation.tests.data_test.endtoend;

import com.financial.automation.config.ConfigReader;
import com.financial.automation.models.InstrumentDetails;
import com.financial.automation.models.PositionDetails;
import com.financial.automation.models.PositionReport;
import com.financial.automation.readers.CsvReader;
import com.financial.automation.utils.ExpectedReportGenerator;
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

    CsvReader reader = new CsvReader();

    List<InstrumentDetails> instruments =
            reader.readInstrumentDetails(
                    ConfigReader.get("instrument.file"));

    List<PositionDetails> positions =
            reader.readPositionDetails(
                    ConfigReader.get("position.file"));

    List<PositionReport> actualReports =
            reader.readPositionReport(
                    ConfigReader.get("report.file"));

    Assert.assertTrue(
            InstrumentCompletenessValidator.areComplete(instruments),
            "Instrument data should be complete");

    Assert.assertTrue(
            InstrumentNumericValidator.hasValidUnitPrices(instruments),
            "All Unit Prices should be positive");

    Assert.assertTrue(
            PositionNumericValidator.hasValidQuantities(positions),
            "All Quantities should be positive");

    Assert.assertTrue(
            ReferentialIntegrityValidator.isValid(
                    instruments,
                    positions),
            "All PositionDetails InstrumentIDs should exist");

    Assert.assertTrue(
            UniquenessValidator.areInstrumentIdsUnique(instruments),
            "Instrument IDs should be unique");

    Assert.assertTrue(
            UniquenessValidator.areIsinsUnique(instruments),
            "Instrument ISINs should be unique");

    Assert.assertTrue(
            UniquenessValidator.arePositionIdsUnique(positions),
            "Position IDs should be unique");

    List<PositionReport> expectedReports =
            ExpectedReportGenerator.generate(
                    instruments,
                    positions);

    /*
     * Generate data reconciliation evidence.
     *
     * The report is generated from:
     * - source position count
     * - independently generated expected records
     * - actual output records
     */
    ReconciliationReportGenerator.generate(
            positions,
            expectedReports,
            actualReports
    );

    Assert.assertTrue(
            OutputValidator.hasExpectedRecords(
                    actualReports,
                    expectedReports),
            "Actual report should contain exactly the expected PositionIDs");
}
}
