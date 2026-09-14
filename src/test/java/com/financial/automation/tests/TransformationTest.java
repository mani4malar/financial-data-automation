package com.financial.automation.tests;

import com.financial.automation.config.ConfigReader;
import com.financial.automation.models.InstrumentDetails;
import com.financial.automation.models.PositionDetails;
import com.financial.automation.models.PositionReport;
import com.financial.automation.readers.CsvReader;
import com.financial.automation.utils.ExpectedReportGenerator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransformationTest {

    @Test
    public void shouldValidatePositionReportTransformation() throws IOException {
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

        List<PositionReport> expectedReports =
                ExpectedReportGenerator.generate(
                        instruments,
                        positions);

        Assert.assertEquals(
                actualReports.size(),
                expectedReports.size(),
                "Actual and expected report record counts should match"
        );

       Map<String, PositionReport> actualByPositionId =
        actualReports.stream()
                .collect(Collectors.toMap(
                        PositionReport::getPositionId,
                        report -> report
                ));

Map<String, PositionReport> expectedByPositionId =
        expectedReports.stream()
                .collect(Collectors.toMap(
                        PositionReport::getPositionId,
                        report -> report
                ));

Assert.assertEquals(
        actualByPositionId.keySet(),
        expectedByPositionId.keySet(),
        "Actual and expected PositionIDs should match"
);

for (String positionId : expectedByPositionId.keySet()) {

    PositionReport expected = expectedByPositionId.get(positionId);
    PositionReport actual = actualByPositionId.get(positionId);

    Assert.assertEquals(
            actual.getIsin(),
            expected.getIsin(),
            "ISIN mismatch for PositionID " + positionId
    );

    Assert.assertEquals(
            actual.getQuantity(),
            expected.getQuantity(),
            "Quantity mismatch for PositionID " + positionId
    );

    Assert.assertEquals(
            actual.getTotalPrice(),
            expected.getTotalPrice(),
            "Total Price mismatch for PositionID " + positionId
    );
}
    }
}