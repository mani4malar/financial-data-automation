package com.financial.automation.tests.data_test.file;

import com.financial.automation.config.ConfigReader;
import com.financial.automation.validators.FileValidator;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FileValidationTest {

    @Test
    public void shouldVerifyInstrumentDetailsFileExists() {

        String filePath =
                ConfigReader.get("instrument.file");

        Assert.assertTrue(
                FileValidator.fileExists(filePath),
                "InstrumentDetails.csv should exist: " + filePath
        );
    }

    @Test
    public void shouldVerifyPositionDetailsFileExists() {

        String filePath =
                ConfigReader.get("position.file");

        Assert.assertTrue(
                FileValidator.fileExists(filePath),
                "PositionDetails.csv should exist: " + filePath
        );
    }

    @Test
    public void shouldVerifyPositionReportFileExists() {

        String filePath =
                ConfigReader.get("report.file");

        Assert.assertTrue(
                FileValidator.fileExists(filePath),
                "PositionReport.csv should exist: " + filePath
        );
    }
}