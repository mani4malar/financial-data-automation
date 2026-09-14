package com.financial.automation.tests;

import com.financial.automation.config.ConfigReader;
import com.financial.automation.models.InstrumentDetails;
import com.financial.automation.models.PositionDetails;
import com.financial.automation.models.PositionReport;
import com.financial.automation.readers.CsvReader;
import com.financial.automation.utils.ExpectedReportGenerator;
import com.financial.automation.utils.TestDataPath;
import com.financial.automation.validators.OutputValidator;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class NegativeOutputIntegrityTest {

    @Test
    public void shouldDetectMissingAndExtraOutputRecords() throws IOException {
        CsvReader reader = new CsvReader();

        List<InstrumentDetails> instruments =
                reader.readInstrumentDetails(
                        ConfigReader.get("instrument.file"));

        List<PositionDetails> positions =
                reader.readPositionDetails(
                        ConfigReader.get("position.file"));

        List<PositionReport> expectedReports =
                ExpectedReportGenerator.generate(
                        instruments,
                        positions);

        List<PositionReport> missingReports =
                reader.readPositionReport(
                        TestDataPath.negative("missing_output_record.csv"));

        List<PositionReport> extraReports =
                reader.readPositionReport(
                        TestDataPath.negative("extra_output_record.csv"));
       Assert.assertFalse(
        OutputValidator.hasExpectedRecords(
                missingReports,
                expectedReports),
        "Missing output records should be detected"
);

Assert.assertFalse(
        OutputValidator.hasExpectedRecords(
                extraReports,
                expectedReports),
        "Unexpected output records should be detected"
);
    }
}