package com.financial.automation.tests;

import com.financial.automation.models.InstrumentDetails;
import com.financial.automation.models.PositionDetails;
import com.financial.automation.readers.CsvReader;
import com.financial.automation.utils.TestDataPath;
import com.financial.automation.validators.UniquenessValidator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class NegativeUniquenessTest {

    @Test
    public void shouldDetectDuplicateIdentifiers() throws IOException {
        CsvReader reader = new CsvReader();

        List<InstrumentDetails> instruments =
                reader.readInstrumentDetails(
                        TestDataPath.negative("duplicate_instrument_data.csv"));

        List<PositionDetails> positions =
                reader.readPositionDetails(
                        TestDataPath.negative("duplicate_position_id.csv"));

        Assert.assertFalse(
                UniquenessValidator.areInstrumentIdsUnique(instruments),
                "Duplicate Instrument IDs should be detected"
        );

        Assert.assertFalse(
                UniquenessValidator.areIsinsUnique(instruments),
                "Duplicate ISINs should be detected"
        );

        Assert.assertFalse(
                UniquenessValidator.arePositionIdsUnique(positions),
                "Duplicate Position IDs should be detected"
        );
    }
}