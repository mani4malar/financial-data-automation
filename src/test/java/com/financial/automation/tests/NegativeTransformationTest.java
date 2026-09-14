package com.financial.automation.tests;

import com.financial.automation.config.ConfigReader;
import com.financial.automation.models.InstrumentDetails;
import com.financial.automation.models.PositionDetails;
import com.financial.automation.models.PositionReport;
import com.financial.automation.readers.CsvReader;
import com.financial.automation.utils.ExpectedReportGenerator;
import com.financial.automation.utils.TestDataPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class NegativeTransformationTest {

    @Test
    public void shouldDetectIncorrectTotalPrice() throws IOException {
        CsvReader reader = new CsvReader();

        List<InstrumentDetails> instruments =
                reader.readInstrumentDetails(
                        ConfigReader.get("instrument.file"));

        List<PositionDetails> positions =
                reader.readPositionDetails(
                        ConfigReader.get("position.file"));

        List<PositionReport> actualReports =
                reader.readPositionReport(
                        TestDataPath.negative("incorrect_total_price.csv"));

        List<PositionReport> expectedReports =
                ExpectedReportGenerator.generate(
                        instruments,
                        positions);

        Assert.assertNotEquals(
                actualReports.get(0).getTotalPrice(),
                expectedReports.get(0).getTotalPrice(),
                "Incorrect Total Price should be detected"
        );
    }
}