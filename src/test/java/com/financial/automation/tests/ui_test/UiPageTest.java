package com.financial.automation.tests.ui_test;
import com.financial.automation.ui.DriverFactory;
import com.financial.automation.ui.HomePage;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.WebDriver;

/** Covers the configured homepage and support page journeys. */
public class UiPageTest {
    private WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    /** Creates a browser before each UI test. */
    public void startBrowser() {
        driver = DriverFactory.create();
    }

    @AfterMethod(alwaysRun = true)
    /** Closes the browser after each UI test. */
    public void stopBrowser() {
        if (driver != null) {
            driver.quit();
            DriverFactory.clearCurrentDriver();
        }
    }

    @Test(groups = {"ui", "smoke"})
    /** Verifies that the homepage has visible content. */
    public void homepageLoads() {
        HomePage homePage = new HomePage(driver).open();
        Assert.assertTrue(homePage.hasRequiredEndpointViews(), "Required endpoint views are missing");
    }

    @Test(groups = {"ui", "regression"})
    /** Verifies endpoint selection and displayed request/response details. */
    public void userNotFoundEndpointDisplaysRequestAndResponse() {
        HomePage homePage = new HomePage(driver).open().selectUserNotFound().sendRequest();
        Assert.assertEquals(homePage.selectedRequestPath(), "/api/users/23");
        Assert.assertEquals(homePage.responseStatus(), "404");
        Assert.assertEquals(homePage.responseBody(), "{}");
    }
}
