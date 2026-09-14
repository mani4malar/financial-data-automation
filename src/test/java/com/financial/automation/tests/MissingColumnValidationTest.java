package com.financial.automation.tests;

import com.financial.automation.utils.TestDataPath;
import com.financial.automation.validators.FileValidator;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MissingColumnValidationTest {

    @Test(groups = {"data-validation", "regression"})
    public void shouldDetectMissingRequiredColumn() {

        String filePath =
                TestDataPath.negative("missing_column.csv");

        boolean hasRequiredHeaders =
                FileValidator.hasRequiredHeaders(
                        filePath,
                        "ID",
                        "Name",
                        "ISIN",
                        "Unit Price"
                );

        Assert.assertFalse(
                hasRequiredHeaders,
                "Validation should fail when a required column is missing"
        );
    }
}