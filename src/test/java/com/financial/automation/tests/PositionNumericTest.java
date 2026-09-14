package com.financial.automation.tests;

import com.financial.automation.config.ConfigReader;
import com.financial.automation.models.PositionDetails;
import com.financial.automation.readers.CsvReader;
import com.financial.automation.validators.PositionNumericValidator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class PositionNumericTest {

    @Test
    public void shouldValidatePositiveQuantities() throws IOException {
        CsvReader reader = new CsvReader();

        List<PositionDetails> positions =
                reader.readPositionDetails(ConfigReader.get("position.file"));

        Assert.assertTrue(
                PositionNumericValidator.hasValidQuantities(positions),
                "All quantities should be greater than zero"
        );
    }
}