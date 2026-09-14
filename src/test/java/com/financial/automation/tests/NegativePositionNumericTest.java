package com.financial.automation.tests;

import com.financial.automation.models.PositionDetails;
import com.financial.automation.readers.CsvReader;
import com.financial.automation.utils.TestDataPath;
import com.financial.automation.validators.PositionNumericValidator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class NegativePositionNumericTest {

    @Test
    public void shouldDetectInvalidQuantities() throws IOException {
        CsvReader reader = new CsvReader();

        List<PositionDetails> positions =
                reader.readPositionDetails(
                        TestDataPath.negative("invalid_quantity.csv"));

        Assert.assertFalse(
                PositionNumericValidator.hasValidQuantities(positions),
                "Zero or negative quantities should be rejected"
        );
    }
}