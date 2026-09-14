package com.financial.automation.utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtils {

    private static final String SCREENSHOT_DIRECTORY =
            "target/screenshots/";

    private ScreenshotUtils() {
    }

    public static String capture(WebDriver driver, String screenshotName) {

        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        String filePath =
                SCREENSHOT_DIRECTORY
                        + screenshotName
                        + "_"
                        + timestamp
                        + ".png";

        File source =
                ((TakesScreenshot) driver)
                        .getScreenshotAs(OutputType.FILE);

        File destination = new File(filePath);

        try {
            FileUtils.copyFile(source, destination);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to save screenshot: " + filePath,
                    e
            );
        }

        return filePath;
    }
}