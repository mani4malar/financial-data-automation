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
import java.math.BigDecimal;
import java.util.List;

public class AggregationTest {

    @Test
    public void shouldValidateReportAggregations() throws IOException {
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

        BigDecimal expectedTotalQuantity = expectedReports.stream()
                .map(PositionReport::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal actualTotalQuantity = actualReports.stream()
                .map(PositionReport::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expectedTotalPrice = expectedReports.stream()
                .map(PositionReport::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal actualTotalPrice = actualReports.stream()
                .map(PositionReport::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Assert.assertEquals(
                actualTotalQuantity,
                expectedTotalQuantity,
                "Total Quantity should match expected value"
        );

        Assert.assertEquals(
                actualTotalPrice,
                expectedTotalPrice,
                "Total Price should match expected value"
        );
    }
}