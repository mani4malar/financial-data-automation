package com.financial.automation.ui;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/** Common Selenium state shared by all page objects. */
public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    /** Initializes the page with a driver and configured explicit wait. */
    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
}
