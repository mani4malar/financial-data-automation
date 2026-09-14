package com.financial.automation.tests.data_test.file;

import com.financial.automation.utils.TestDataPath;
import com.financial.automation.validators.FileValidator;
import org.testng.Assert;
import org.testng.annotations.Test;

public class EmptyFileValidationTest {

    @Test
    public void shouldDetectEmptyFile() {

      String filePath = TestDataPath.negativeFile("empty_file.csv");

        Assert.assertTrue(
                FileValidator.isEmpty(filePath),
                "File should be detected as empty: " + filePath
        );
    }
}