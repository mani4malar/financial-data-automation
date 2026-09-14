package com.financial.automation.ui;

import com.financial.automation.config.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;

import java.time.Duration;

/** Creates and tracks the browser driver for the current test thread. */
public final class DriverFactory {
    private static final ThreadLocal<WebDriver> CURRENT_DRIVER = new ThreadLocal<>();

    private DriverFactory() {
    }

    /** Creates a configured Chrome, Firefox, or Edge driver. */
    public static WebDriver create() {
        WebDriver driver = switch (ConfigReader.get("browser")) {
            case "firefox" -> new FirefoxDriver();
            case "edge" -> new EdgeDriver();
            case "chrome" -> new ChromeDriver();
            default -> throw new IllegalArgumentException("Unsupported browser: " + (ConfigReader.get("browser")));
        };
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().window().maximize();
        CURRENT_DRIVER.set(driver);
        return driver;
    }

    /** Returns the current thread's driver, or null when no browser is active. */
    public static WebDriver currentDriver() {
        return CURRENT_DRIVER.get();
    }

    /** Clears the current thread's driver reference. */
    public static void clearCurrentDriver() {
        CURRENT_DRIVER.remove();
    }
}
