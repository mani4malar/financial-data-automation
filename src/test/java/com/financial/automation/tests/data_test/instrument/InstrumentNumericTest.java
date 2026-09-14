package com.financial.automation.tests.data_test.instrument;

import com.financial.automation.config.ConfigReader;
import com.financial.automation.models.InstrumentDetails;
import com.financial.automation.readers.CsvReader;
import com.financial.automation.validators.InstrumentNumericValidator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class InstrumentNumericTest {

    private final CsvReader csvReader = new CsvReader();

    @Test
    public void shouldValidateInstrumentUnitPrices() throws IOException {

        String filePath = ConfigReader.get("instrument.file");

        List<InstrumentDetails> instruments =
                csvReader.readInstrumentDetails(filePath);

        Assert.assertTrue(
                InstrumentNumericValidator.hasValidUnitPrices(instruments),
                "InstrumentDetails contains invalid Unit Price values"
        );
    }
}