package com.Pearson.CommonMethods;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CommonMethods
{
   public static WebDriver driver;

   public static void waitForPageLoad() throws InterruptedException {
        Thread.sleep(2000); // Adjust the sleep time as needed
    }
    public static void sendKeys(By locator, String text, int seconds) {
        implicitlyWait(seconds);
        driver.findElement(locator).sendKeys(text);
    }
    public static void click(By locator,int seconds) {
        implicitlyWait(seconds);
        driver.findElement(locator).click();
    }
    public static String getText(By locator,int seconds) {
        return driver.findElement(locator).getText();
    }
    public static boolean isElementDisplayed(By locator,int seconds) {
        return driver.findElement(locator).isDisplayed();
    }
    public static boolean isElementEnabled(By locator, int seconds)
    {
       implicitlyWait(seconds);
       return driver.findElement(locator).isEnabled();
    }
    public static void implicitlyWait(int seconds) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));

    }
    public static WebElement webDriverWait(By locator, int seconds) {
        WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        WebElement ele = w.until(ExpectedConditions.visibilityOfElementLocated(locator));

        return ele;
    }
    public static void clear(By locator) {
        driver.findElement(locator).clear();
    }
    public static void jsExec(By locator)
    {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);",driver.findElement(locator));
    }

    public static void jsExec(WebElement element, int i)
    {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);",element);

    }
}
