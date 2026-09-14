package com.financial.automation.tests.data_test.instrument;

import com.financial.automation.models.InstrumentDetails;
import com.financial.automation.readers.CsvReader;
import com.financial.automation.utils.TestDataPath;
import com.financial.automation.validators.InstrumentNumericValidator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class NegativeInstrumentNumericTest {

    private final CsvReader csvReader = new CsvReader();

    @Test
    public void shouldDetectZeroUnitPrice() throws IOException {

        String filePath =
                TestDataPath.negativeInstrument("invalid_unit_price.csv");

        List<InstrumentDetails> instruments =
                csvReader.readInstrumentDetails(filePath);

        Assert.assertFalse(
                InstrumentNumericValidator.hasValidUnitPrices(instruments),
                "invalid Unit Price should be detected"
        );
    }
}