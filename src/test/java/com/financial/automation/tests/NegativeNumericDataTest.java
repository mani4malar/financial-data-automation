package com.financial.automation.tests;

import com.financial.automation.models.InstrumentDetails;
import com.financial.automation.models.PositionDetails;
import com.financial.automation.readers.CsvReader;
import com.financial.automation.utils.TestDataPath;
import com.financial.automation.validators.InstrumentNumericValidator;
import com.financial.automation.validators.PositionNumericValidator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class NegativeNumericDataTest {

    @Test
    public void shouldDetectNonNumericValues() throws IOException {
        CsvReader reader = new CsvReader();

        List<InstrumentDetails> instruments =
                reader.readInstrumentDetails(
                        TestDataPath.negative("non_numeric_unit_price.csv"));

        List<PositionDetails> positions =
                reader.readPositionDetails(
                        TestDataPath.negative("non_numeric_quantity.csv"));

        Assert.assertFalse(
                InstrumentNumericValidator.hasValidUnitPrices(instruments),
                "Non-numeric Unit Price should be detected"
        );

        Assert.assertFalse(
                PositionNumericValidator.hasValidQuantities(positions),
                "Non-numeric Quantity should be detected"
        );
    }
}