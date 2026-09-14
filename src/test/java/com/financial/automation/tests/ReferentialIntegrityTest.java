package com.financial.automation.tests;

import com.financial.automation.config.ConfigReader;
import com.financial.automation.models.InstrumentDetails;
import com.financial.automation.models.PositionDetails;
import com.financial.automation.readers.CsvReader;
import com.financial.automation.validators.ReferentialIntegrityValidator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class ReferentialIntegrityTest {

    @Test
    public void shouldValidatePositionInstrumentReferences() throws IOException {
        CsvReader reader = new CsvReader();

        List<InstrumentDetails> instruments =
                reader.readInstrumentDetails(
                        ConfigReader.get("instrument.file"));

        List<PositionDetails> positions =
                reader.readPositionDetails(
                        ConfigReader.get("position.file"));

        Assert.assertTrue(
                ReferentialIntegrityValidator.isValid(instruments, positions),
                "Every PositionDetails.InstrumentID should exist in InstrumentDetails.ID"
        );
    }
}