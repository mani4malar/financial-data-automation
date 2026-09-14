package com.financial.automation.tests.data_test.integrity;

import com.financial.automation.config.ConfigReader;
import com.financial.automation.models.InstrumentDetails;
import com.financial.automation.models.PositionDetails;
import com.financial.automation.readers.CsvReader;
import com.financial.automation.utils.TestDataPath;
import com.financial.automation.validators.ReferentialIntegrityValidator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class NegativeReferentialIntegrityTest {

    @Test
    public void shouldDetectInvalidInstrumentReferences() throws IOException {
        CsvReader reader = new CsvReader();

        List<InstrumentDetails> instruments =
                reader.readInstrumentDetails(
                        ConfigReader.get("instrument.file"));

        List<PositionDetails> positions =
                reader.readPositionDetails(
                        TestDataPath.negativeIntegrity("invalid_instrument_reference.csv"));

        Assert.assertFalse(
                ReferentialIntegrityValidator.isValid(instruments, positions),
                "PositionDetails should not reference a nonexistent InstrumentID"
        );
    }
}