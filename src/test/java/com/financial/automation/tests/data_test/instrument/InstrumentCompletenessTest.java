package com.financial.automation.tests.data_test.instrument;

import com.financial.automation.config.ConfigReader;
import com.financial.automation.models.InstrumentDetails;
import com.financial.automation.readers.CsvReader;
import com.financial.automation.validators.InstrumentCompletenessValidator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class InstrumentCompletenessTest {

    private final CsvReader csvReader = new CsvReader();

    @Test
    public void shouldValidateInstrumentCompleteness() throws IOException {

        String filePath = ConfigReader.get("instrument.file");

        List<InstrumentDetails> instruments =
                csvReader.readInstrumentDetails(filePath);

        Assert.assertTrue(
                InstrumentCompletenessValidator.areComplete(instruments),
                "InstrumentDetails contains incomplete records"
        );
    }
}