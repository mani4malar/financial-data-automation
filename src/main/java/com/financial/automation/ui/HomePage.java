package com.financial.automation.ui;

import com.financial.automation.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/** Page Object for the configured homepage. */
public final class HomePage extends BasePage {
    private final By body = By.tagName("body");
    private final By userNotFoundEndpoint = By.xpath("//button[contains(normalize-space(), 'User not found')]");
    private final By sendRequestButton = By.xpath("//button[normalize-space()='Send request']");
    private final By requestPath = By.xpath("//*[normalize-space()='/api/users/23']");
    private final By responseStatus = By.xpath("//*[normalize-space()='404']");
    private final By responseBody = By.xpath("//*[text()='{}']");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    /** Opens the homepage and waits for its body to be visible. */
    public HomePage open() {
        driver.get(ConfigReader.get("ui.base.url"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(body));
        return this;
    }

    /** Returns visible text from the page body. */
    public String pageText() {
        return driver.findElement(body).getText();
    }

    /** Returns whether the homepage exposes the required user endpoint choices. */
    public boolean hasRequiredEndpointViews() {
        return driver.findElement(By.xpath("//button[contains(normalize-space(), 'List users')]")).isDisplayed()
                && driver.findElement(By.xpath("//button[contains(normalize-space(), 'Single user')]")).isDisplayed()
                && driver.findElement(userNotFoundEndpoint).isDisplayed();
    }

    /** Selects the GET user-not-found endpoint. */
    public HomePage selectUserNotFound() {
        clickVisible(userNotFoundEndpoint);
        wait.until(ExpectedConditions.visibilityOfElementLocated(requestPath));
        return this;
    }

    /** Sends the currently selected endpoint request. */
    public HomePage sendRequest() {
        clickVisible(sendRequestButton);
        wait.until(ExpectedConditions.visibilityOfElementLocated(responseStatus));
        return this;
    }

    /** Returns the selected request path. */
    public String selectedRequestPath() {
        return driver.findElement(requestPath).getText();
    }

    /** Returns the rendered response status. */
    public String responseStatus() {
        return driver.findElement(responseStatus).getText();
    }

    /** Returns the rendered response body. */
    public String responseBody() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(responseBody)).getText();
    }

    private void clickVisible(By locator) {
        var element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", element);
        wait.until(ExpectedConditions.elementToBeClickable(element));
        try {
            element.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException exception) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }
}
