package com.financial.automation.tests;

import com.financial.automation.config.ConfigReader;
import com.financial.automation.models.InstrumentDetails;
import com.financial.automation.models.PositionDetails;
import com.financial.automation.readers.CsvReader;
import com.financial.automation.validators.UniquenessValidator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class UniquenessTest {

    @Test
    public void shouldValidateUniqueIdentifiers() throws IOException {
        CsvReader reader = new CsvReader();

        List<InstrumentDetails> instruments =
                reader.readInstrumentDetails(
                        ConfigReader.get("instrument.file"));

        List<PositionDetails> positions =
                reader.readPositionDetails(
                        ConfigReader.get("position.file"));

        Assert.assertTrue(
                UniquenessValidator.areInstrumentIdsUnique(instruments),
                "Instrument IDs should be unique"
        );

        Assert.assertTrue(
                UniquenessValidator.areIsinsUnique(instruments),
                "Instrument ISINs should be unique"
        );

        Assert.assertTrue(
                UniquenessValidator.arePositionIdsUnique(positions),
                "Position IDs should be unique"
        );
    }
}