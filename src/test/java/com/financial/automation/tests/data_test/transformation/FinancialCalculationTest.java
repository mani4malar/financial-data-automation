package com.financial.automation.tests.data_test.transformation;

import com.financial.automation.utils.FinancialCalculationUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;

public class FinancialCalculationTest {

    @Test
    public void shouldRoundTotalPriceToTwoDecimalPlaces() {

        BigDecimal quantity = new BigDecimal("3");
        BigDecimal unitPrice = new BigDecimal("10.125");

        BigDecimal actual =
                FinancialCalculationUtils.calculateTotalPrice(
                        quantity,
                        unitPrice);

        BigDecimal expected = new BigDecimal("30.38");

        Assert.assertEquals(
                actual,
                expected,
                "Total Price should be rounded to 2 decimal places using HALF_UP"
        );
    }
}