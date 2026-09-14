package com.financial.automation.tests.data_test.file;

import com.financial.automation.utils.TestDataPath;
import com.financial.automation.validators.FileValidator;
import org.testng.Assert;
import org.testng.annotations.Test;

public class InvalidFileNameValidationTest {

    @Test(groups = {"data-validation", "regression"})
    public void shouldDetectUnexpectedFileName() {

        String filePath =
                TestDataPath.negativeFile("invalid_header.csv");

        boolean hasExpectedFileName =
                FileValidator.hasExpectedFileName(
                        filePath,
                        "InstrumentDetails.csv"
                );

        Assert.assertFalse(
                hasExpectedFileName,
                "Validation should fail when the file name is unexpected"
        );
    }
}