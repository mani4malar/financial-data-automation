package com.financial.automation.tests.data_test.unit;

import com.financial.automation.validators.CompletenessValidator;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CompletenessValidatorTest {

    @Test
    public void shouldAcceptNonBlankValue() {

        Assert.assertTrue(
                CompletenessValidator.isNotBlank("value")
        );
    }

    @Test
    public void shouldRejectNullValue() {

        Assert.assertFalse(
                CompletenessValidator.isNotBlank(null)
        );
    }

    @Test
    public void shouldRejectEmptyValue() {

        Assert.assertFalse(
                CompletenessValidator.isNotBlank("")
        );
    }

    @Test
    public void shouldRejectWhitespaceValue() {

        Assert.assertFalse(
                CompletenessValidator.isNotBlank("   ")
        );
    }
}