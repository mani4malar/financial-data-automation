package com.financial.automation.tests;

import com.financial.automation.validators.NumericValidator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;

public class NumericValidatorTest {

    @Test
    public void shouldAcceptPositiveValue() {

        Assert.assertTrue(
                NumericValidator.isPositive(
                        new BigDecimal("100.00")
                )
        );
    }

    @Test
    public void shouldRejectZero() {

        Assert.assertFalse(
                NumericValidator.isPositive(
                        BigDecimal.ZERO
                )
        );
    }

    @Test
    public void shouldRejectNegativeValue() {

        Assert.assertFalse(
                NumericValidator.isPositive(
                        new BigDecimal("-10.00")
                )
        );
    }

    @Test
    public void shouldRejectNull() {

        Assert.assertFalse(
                NumericValidator.isPositive(null)
        );
    }
}