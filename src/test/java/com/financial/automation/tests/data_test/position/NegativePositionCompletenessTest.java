package com.financial.automation.tests.data_test.position;

import com.financial.automation.models.InstrumentDetails;
import com.financial.automation.models.PositionDetails;
import com.financial.automation.readers.CsvReader;
import com.financial.automation.utils.TestDataPath;
import com.financial.automation.validators.InstrumentCompletenessValidator;
import com.financial.automation.validators.PositionCompletenessValidator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class NegativePositionCompletenessTest {

    @Test
    public void shouldDetectIncompletePositionRecords() throws IOException {
        CsvReader reader = new CsvReader();

        List<PositionDetails> positions =
                reader.readPositionDetails(
                        TestDataPath.negativePosition(
                                "invalid_position_completeness.csv"));

        Assert.assertFalse(
                PositionCompletenessValidator.areComplete(positions),
                "Blank mandatory PositionDetails fields should be detected"
        );
    }


}