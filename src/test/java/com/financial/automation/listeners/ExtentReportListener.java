package com.financial.automation.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.financial.automation.ui.DriverFactory;
import com.financial.automation.utils.ScreenshotUtils;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.nio.file.Files;
import java.nio.file.Path;

public class ExtentReportListener implements ITestListener {

    private static final Path REPORT_PATH =
            Path.of(
                    "target",
                    "extent-reports",
                    "ExtentReport.html"
            );

    private static final ExtentReports EXTENT_REPORTS =
            createReport();

    private static final ThreadLocal<ExtentTest> EXTENT_TEST =
            new ThreadLocal<>();

    private static ExtentReports createReport() {

        try {
            Files.createDirectories(
                    REPORT_PATH.getParent()
            );
        } catch (Exception e) {
            throw new RuntimeException(
                    "Unable to create ExtentReports directory",
                    e
            );
        }

        ExtentSparkReporter sparkReporter =
                new ExtentSparkReporter(
                        REPORT_PATH.toString()
                );

        ExtentReports reports =
                new ExtentReports();

        reports.attachReporter(sparkReporter);

        reports.setSystemInfo(
                "Framework",
                "Financial Data Automation"
        );

        reports.setSystemInfo(
                "Java Version",
                System.getProperty("java.version")
        );

        reports.setSystemInfo(
                "Operating System",
                System.getProperty("os.name")
        );

        reports.setSystemInfo(
                "OS Version",
                System.getProperty("os.version")
        );

        reports.setSystemInfo(
                "User",
                System.getProperty("user.name")
        );

        return reports;
    }

    @Override
    public void onTestStart(ITestResult result) {

        String testName =
                result.getMethod().getMethodName();

        String className =
                result.getTestClass()
                        .getRealClass()
                        .getSimpleName();

        ExtentTest test =
                EXTENT_REPORTS.createTest(
                        className + " - " + testName
                );

        String[] groups =
                result.getMethod().getGroups();

        if (groups.length > 0) {
            test.assignCategory(groups);
        }

        EXTENT_TEST.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {

        ExtentTest test = EXTENT_TEST.get();

        if (test != null) {
            test.pass("Test passed");
        }
        captureScreenshot(result, test,"PASSED");
    }

    @Override
    public void onTestFailure(ITestResult result) {

        ExtentTest test = EXTENT_TEST.get();

        if (test == null) {
            return;
        }

        Throwable throwable =
                result.getThrowable();

        if (throwable != null) {

            test.fail(
                    throwable.getClass().getSimpleName()
                            + ": "
                            + throwable.getMessage()
            );

        } else {

            test.fail(
                    "Test failed without an exception message"
            );
        }

        captureScreenshot(result, test,"FAILED");
    }

    private void captureScreenshot(
        ITestResult result,
        ExtentTest test,
        String status) {

    try {

        WebDriver driver =
                DriverFactory.currentDriver();

        if (driver == null) {
            test.info(
                    "Screenshot not captured because "
                            + "WebDriver is not available."
            );
            return;
        }

        String testName =
                result.getMethod().getMethodName();

        String screenshotPath =
        ScreenshotUtils.capture(
                driver,
                testName + "_" + status
        );

Path screenshot =
        Path.of(screenshotPath).toAbsolutePath();

Path reportDirectory =
        REPORT_PATH.toAbsolutePath().getParent();

String relativeScreenshotPath =
        reportDirectory
                .relativize(screenshot)
                .toString()
                .replace("\\", "/");

test.addScreenCaptureFromPath(
        relativeScreenshotPath
);

    } catch (Exception e) {

        test.warning(
                "Unable to capture screenshot: "
                        + e.getMessage()
        );
    }
}
    @Override
    public void onTestSkipped(ITestResult result) {

        ExtentTest test = EXTENT_TEST.get();

        if (test != null) {
            test.skip("Test skipped");
        }
    }

    @Override
    public void onFinish(ITestContext context) {

        EXTENT_REPORTS.flush();

        EXTENT_TEST.remove();
    }
}