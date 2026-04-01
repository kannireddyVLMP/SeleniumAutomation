package com.Pearson.Pages;

import com.Pearson.Base.Base;
import com.Pearson.CommonMethods.CommonMethods;
import com.Pearson.Utilities.ExtentLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import java.util.List;

public class Payment extends Base {
    private WebDriver driver;
    private CommonMethods cm;

    public Payment(WebDriver driver) {
        this.driver = driver;
        this.cm = new CommonMethods(driver); // ✅ instance driver
    }

    private static final Logger logger = LogManager.getLogger(Payment.class);

    // Locators
    private static final By creditCardNumber = By.xpath("//div[contains(text(),'Credit Card Number')]/parent::div/input");
    private static final By monthDropdown    = By.xpath("(//select[@class='input ddl'])[1]");
    private static final By dateDropdown     = By.xpath("(//select[@class='input ddl'])[2]");
    private static final By cvvCode          = By.xpath("//div[@class='title' and contains(text(),'CVV')]/parent::div/input");
    private static final By nameOnCard       = By.xpath("//div[@class='title' and contains(text(),'Name')]/parent::div/input");
    private static final By emailField       = By.xpath("//div[@class='user__name mt-5']//label/following-sibling::input");
    private static final By countryField     = By.cssSelector("input[placeholder='Select Country']");
    private static final By countryDropdownOptions = By.cssSelector(".ta-results.list-group span");
    private static final By placeOrderButton = By.xpath("//a[contains(text(),'Place Order ')]");

    // Fill Personal Info
    public void fillPersonalInfo(String cardNumber, String month, String date,
                                 String cvv, String name) {
        try {
            logger.info("Filling personal info...");
            ExtentLogger.info("Filling personal info...");

            WebElement cardInput = cm.waitForElement(creditCardNumber, 10);
            cardInput.clear();
            cardInput.sendKeys(cardNumber);

            WebElement monthElement = cm.waitForElement(monthDropdown, 10);
            new Select(monthElement).selectByVisibleText(month);
            logger.info("Selected expiry month: " + month);
            ExtentLogger.info("Selected expiry month: " + month);

            WebElement dateElement = cm.waitForElement(dateDropdown, 10);
            new Select(dateElement).selectByVisibleText(date);
            logger.info("Selected expiry date: " + date);
            ExtentLogger.info("Selected expiry date: " + date);

            cm.sendKeys(cvvCode, cvv);
            cm.sendKeys(nameOnCard, name);

            logger.info("Personal info filled successfully.");
            ExtentLogger.pass("Personal info filled successfully.");
        } catch (Exception e) {
            logger.error("Personal info entry failed: " + e.getMessage());
            ExtentLogger.fail("Personal info entry failed: " + e.getMessage());
            Assert.fail("Personal info entry failed: " + e.getMessage());
        }
    }

    // Validate Shipping Info
    public void validateShippingInfo(String loginEmail, String country) {
        try {
            logger.info("Validating shipping info...");
            ExtentLogger.info("Validating shipping info...");

            String enteredEmail = cm.waitForElement(emailField, 10).getAttribute("value").trim();
            Assert.assertEquals(enteredEmail.toLowerCase(), loginEmail.toLowerCase(), "Email mismatch!");
            logger.info("Auto-filled email validated: " + enteredEmail);
            ExtentLogger.pass("Auto-filled email validated: " + enteredEmail);

            // Type country letters one by one
            for (char c : country.toCharArray()) {
                cm.sendKeysWithoutClear(countryField, String.valueOf(c));
                Thread.sleep(300);
            }

            logger.info("Typed country letters: " + country);
            ExtentLogger.info("Typed country letters: " + country);

            // Select country from dropdown
            List<WebElement> options = driver.findElements(countryDropdownOptions);
            for (WebElement option : options) {
                if (option.getText().equalsIgnoreCase(country)) {
                    option.click();
                    logger.info("Selected country: " + country);
                    ExtentLogger.pass("Selected country: " + country);
                    break;
                }
            }
        } catch (Exception e) {
            logger.error("Shipping info validation failed: " + e.getMessage());
            ExtentLogger.fail("Shipping info validation failed: " + e.getMessage());
            Assert.fail("Shipping info validation failed: " + e.getMessage());
        }
    }

    // Place Order
    public void placeOrder() {
        try {
            logger.info("Placing order...");
            ExtentLogger.info("Placing order...");

            WebElement placeOrderBtn = cm.waitForElement(placeOrderButton, 10);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", placeOrderBtn);

            logger.info("Order placed successfully.");
            ExtentLogger.pass("Order placed successfully.");
        } catch (Exception e) {
            logger.error("Placing order failed: " + e.getMessage());
            ExtentLogger.fail("Placing order failed: " + e.getMessage());
            Assert.fail("Placing order failed: " + e.getMessage());
        }
    }
}
