package com.financial.automation.tests.data_test.file;

import com.financial.automation.utils.TestDataPath;
import com.financial.automation.validators.FileValidator;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MalformedRowValidationTest {

    @Test(groups = {"data-validation", "regression"})
    public void shouldDetectMalformedCsvRow() {

        String filePath =
                TestDataPath.negativeFile("malformed_row.csv");

        boolean hasValidRowStructure =
                FileValidator.hasValidRowStructure(
                        filePath,
                        3
                );

        Assert.assertFalse(
                hasValidRowStructure,
                "Validation should fail when a CSV row has an incorrect number of columns"
        );
    }
}