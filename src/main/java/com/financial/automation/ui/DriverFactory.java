package com.financial.automation.ui;

import com.financial.automation.config.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.time.Duration;

/** Creates and tracks the browser driver for the current test thread. */
public final class DriverFactory {

    private static final ThreadLocal<WebDriver> CURRENT_DRIVER =
            new ThreadLocal<>();

    private DriverFactory() {
    }

    /** Creates a configured Chrome, Firefox, or Edge driver. */
    public static WebDriver create() {

        String browser =
                ConfigReader.get("browser").toLowerCase();

        boolean headless =
                Boolean.parseBoolean(
                        ConfigReader.get("browser.headless")
                );

        WebDriver driver = switch (browser) {

            case "firefox" -> createFirefoxDriver(headless);

            case "edge" -> createEdgeDriver(headless);

            case "chrome" -> createChromeDriver(headless);

            default ->
                    throw new IllegalArgumentException(
                            "Unsupported browser: " + browser
                    );
        };

        driver.manage()
                .timeouts()
                .implicitlyWait(Duration.ofSeconds(2));

        driver.manage()
                .timeouts()
                .pageLoadTimeout(Duration.ofSeconds(30));

        if (!headless) {
            driver.manage().window().maximize();
        }

        CURRENT_DRIVER.set(driver);

        return driver;
    }

    private static WebDriver createChromeDriver(
            boolean headless) {

        ChromeOptions options =
                new ChromeOptions();

        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        }

        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver(
            boolean headless) {

        FirefoxOptions options =
                new FirefoxOptions();

        if (headless) {
            options.addArguments("-headless");
            options.addArguments("--width=1920");
            options.addArguments("--height=1080");
        }

        return new FirefoxDriver(options);
    }

    private static WebDriver createEdgeDriver(
            boolean headless) {

        EdgeOptions options =
                new EdgeOptions();

        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        }

        return new EdgeDriver(options);
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