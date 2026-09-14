package com.financial.automation.tests.data_test.instrument;

import com.financial.automation.models.InstrumentDetails;
import com.financial.automation.readers.CsvReader;
import com.financial.automation.utils.TestDataPath;
import com.financial.automation.validators.InstrumentCompletenessValidator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class NegativeInstrumentCompletenessTest {

    private final CsvReader csvReader = new CsvReader();

    @Test
    public void shouldDetectBlankIsin() throws IOException {

        String filePath =
                TestDataPath.negativeInstrument("blank_isin.csv");

        List<InstrumentDetails> instruments =
                csvReader.readInstrumentDetails(filePath);

        Assert.assertFalse(
                InstrumentCompletenessValidator.areComplete(instruments),
                "Blank ISIN should be detected"
        );
    }

 
}