package com.financial.automation.tests.data_test.file;

import com.financial.automation.config.ConfigReader;
import com.financial.automation.utils.TestDataPath;
import com.financial.automation.validators.FileValidator;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HeaderValidationTest {

    @Test
    public void shouldDetectInvalidHeaders() {

        String filePath =
        TestDataPath.negativeFile("invalid_header.csv");

        Assert.assertFalse(
                FileValidator.hasExpectedHeaders(
                        filePath,
                        "ID",
                        "Name",
                        "ISIN",
                        "Unit Price"
                ),
                "File should have invalid headers: " + filePath
        );
    }

    @Test
    public void shouldAcceptValidInstrumentHeaders() {

        String filePath = ConfigReader.get("instrument.file");

        Assert.assertTrue(
                FileValidator.hasExpectedHeaders(
                        filePath,
                        "ID",
                        "Name",
                        "ISIN",
                        "Unit Price"
                ),
                "InstrumentDetails headers should be valid"
        );
    }

    @Test
    public void shouldAcceptValidPositionHeaders() {

        String filePath = ConfigReader.get("position.file");

        Assert.assertTrue(
                FileValidator.hasExpectedHeaders(
                        filePath,
                        "ID",
                        "InstrumentID",
                        "Quantity"
                ),
                "PositionDetails headers should be valid"
        );
    }

    @Test
    public void shouldAcceptValidReportHeaders() {

        String filePath = ConfigReader.get("report.file");

        Assert.assertTrue(
                FileValidator.hasExpectedHeaders(
                        filePath,
                        "ID",
                        "PositionID",
                        "ISIN",
                        "Quantity",
                        "Total Price"
                ),
                "PositionReport headers should be valid"
        );
    }
}