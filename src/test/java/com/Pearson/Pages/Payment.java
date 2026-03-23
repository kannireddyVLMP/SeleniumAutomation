package com.Pearson.Pages;

import com.Pearson.Base.Base;
import com.Pearson.CommonMethods.CommonMethods;
import com.Pearson.Screenshot.TakesScreenShot;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

public class Payment extends Base {
    private WebDriver driver;
    private CommonMethods cm;
    private String screenshotFolder;

    // Locators
    private By creditCardNumber = By.xpath("//div[contains(text(),'Credit Card Number')]/parent::div/input");
    private By monthDropdown    = By.xpath("(//select[@class='input ddl'])[1]"); // month
    private By dateDropdown     = By.xpath("(//select[@class='input ddl'])[2]"); // date
    private By cvvCode          = By.xpath("//div[@class='title' and contains(text(),'CVV') ]/parent::div/input");
    private By nameOnCard       = By.xpath("//div[@class='title' and contains(text(),'Name') ]/parent::div/input");
    private By emailField       = By.xpath("//div[@class='user__name mt-5']//label/following-sibling::input");
    private By countryField     = By.cssSelector("input[placeholder='Select Country']");
    private By countryDropdownOptions = By.cssSelector(".ta-results.list-group span");
    private By placeOrderButton = By.xpath("//a[contains(text(),'Place Order ')]");

    // Constructor
    public Payment(WebDriver driver, String screenshotFolder) {
        this.driver = driver;
        this.cm = new CommonMethods();
        CommonMethods.driver = driver;
        this.screenshotFolder = screenshotFolder;
    }

    // Method 1: Fill Personal Info
    public void fillPersonalInfo(String cardNumber, String month, String date,
                                 String cvv, String name) {
        try {
            logger.info("Filling personal info...");
            WebElement ele = cm.webDriverWait(creditCardNumber, 5);
            ele.sendKeys(cardNumber);

            WebElement monthElement = CommonMethods.webDriverWait(monthDropdown, 5);
            new Select(monthElement).selectByVisibleText(month);
            logger.info("Selected expiry month: " + month);

            WebElement dateElement = CommonMethods.webDriverWait(dateDropdown, 5);
            new Select(dateElement).selectByVisibleText(date);
            logger.info("Selected expiry date: " + date);

            cm.sendKeys(cvvCode, cvv, 5);
            cm.sendKeys(nameOnCard, name, 5);
            logger.info("Personal info filled successfully.");

        } catch (Exception e) {
            String screenshotPath = screenshotFolder + "/personal_info_failed.png";
            TakesScreenShot.takeScreenshot(driver, screenshotPath);
            logger.error("Personal info failed. Screenshot saved: " + screenshotPath, e);
            throw e;
        }
    }

    // Method 2: Validate Shipping Info (email auto-filled)
    public boolean validateShippingInfo(String loginEmail, String country) throws InterruptedException {
        try {
            logger.info("Validating shipping info...");
            String enteredEmail = CommonMethods.driver.findElement(emailField).getAttribute("value").trim();
            boolean emailMatches = enteredEmail.equalsIgnoreCase(loginEmail);
            logger.info("Auto-filled email: " + enteredEmail + " | Expected: " + loginEmail);

            // Type country letters one by one
            for (char c : country.toCharArray()) {
                cm.sendKeys(countryField, String.valueOf(c), 5);
                Thread.sleep(500);
            }
            logger.info("Typed country letters: " + country);

            // Select country from dropdown
            for (WebElement option : CommonMethods.driver.findElements(countryDropdownOptions)) {
                if (option.getText().equalsIgnoreCase(country)) {
                    option.click();
                    logger.info("Selected country: " + country);
                    break;
                }
            }

            return emailMatches;

        } catch (Exception e) {
            String screenshotPath = screenshotFolder + "/shipping_info_failed.png";
            TakesScreenShot.takeScreenshot(driver, screenshotPath);
            logger.error("Shipping info failed. Screenshot saved: " + screenshotPath, e);
            throw e;
        }
    }

    // Method 3: Place Order
    public void placeOrder() throws InterruptedException {
        try {
            logger.info("Placing order...");
            WebElement placeOrderBtn = driver.findElement(placeOrderButton);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", placeOrderBtn);
            Thread.sleep(3000);
            logger.info("Order placed successfully.");
        } catch (Exception e) {
            String screenshotPath = screenshotFolder + "/place_order_failed.png";
            TakesScreenShot.takeScreenshot(driver, screenshotPath);
            logger.error("Place order failed. Screenshot saved: " + screenshotPath, e);
            throw e;
        }
    }
}
