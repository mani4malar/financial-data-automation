package com.financial.automation.tests.data_test.transformation;

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
import java.util.Map;
import java.util.stream.Collectors;
import java.util.function.Function;

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
                        TestDataPath.negativeTransformation("incorrect_total_price.csv"));

        List<PositionReport> expectedReports =
                ExpectedReportGenerator.generate(
                        instruments,
                        positions);

        Map<String, PositionReport> actualReportMap =
        actualReports.stream()
                .collect(Collectors.toMap(
                        PositionReport::getPositionId,
                        Function.identity()
                ));

Map<String, PositionReport> expectedReportMap =
        expectedReports.stream()
                .collect(Collectors.toMap(
                        PositionReport::getPositionId,
                        Function.identity()
                ));

PositionReport actualReport = actualReportMap.get("P001");
PositionReport expectedReport = expectedReportMap.get("P001");

Assert.assertNotEquals(
        actualReport.getTotalPrice(),
        expectedReport.getTotalPrice(),
        "Incorrect Total Price should be detected for PositionID P001"
);
    }
}