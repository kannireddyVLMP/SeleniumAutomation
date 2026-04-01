
package com.Pearson.CommonMethods;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class CommonMethods {
    private WebDriver driver;

    public CommonMethods(WebDriver driver) {
        this.driver = driver; // ✅ instance driver, not static
    }

    public void waitForPageLoad() throws InterruptedException {
        Thread.sleep(2000); // simple pause, replace with smarter wait if needed
    }

    public void sendKeys(By locator, String text) {
        WebElement element = waitForElement(locator, 10);
        element.clear();
        element.sendKeys(text);
    }

    public void sendKeysWithoutClear(By locator, String text) {
        WebElement element = waitForElement(locator, 10);
        element.sendKeys(text);
    }

    public void click(By locator) {
        WebElement element = waitForElement(locator, 10);
        try {
            element.click();
        } catch (ElementClickInterceptedException e) {
            // fallback to JS click
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    public String getText(By locator) {
        return waitForElement(locator, 10).getText();
    }

    public boolean isElementDisplayed(By locator) {
        try {
            return waitForElement(locator, 5).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isElementEnabled(By locator) {
        try {
            return waitForElement(locator, 5).isEnabled();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void implicitlyWait(int seconds) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
    }

    public WebElement waitForElement(By locator, int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void clear(By locator) {
        waitForElement(locator, 5).clear();
    }

    public void jsScrollIntoView(By locator) {
        WebElement element = waitForElement(locator, 5);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void jsScrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }
    // Wait for list of elements to be visible
    public List<WebElement> waitForElements(By locator, int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    // Get list of elements without wait
    public List<WebElement> getElements(By locator) {
        return driver.findElements(locator);
    }

    // Get texts of all elements in a list
    public List<String> getElementsText(By locator, int seconds) {
        List<WebElement> elements = waitForElements(locator, seconds);
        List<String> texts = new ArrayList<>();
        for (WebElement el : elements) {
            texts.add(el.getText().trim());
        }
        return texts;
    }

}
