package com.Pearson.Pages;

import com.Pearson.Base.Base;
import com.Pearson.CommonMethods.CommonMethods;
import com.Pearson.Utilities.ExtentLogger;
import com.Pearson.Utilities.TakesScreenShot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.testng.Assert;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Order extends Base {
    private WebDriver driver;
    private CommonMethods cm;

    public Order(WebDriver driver) {
        this.driver = driver;
        this.cm = new CommonMethods(driver); // ✅ instance driver
    }

    private static final Logger logger = LogManager.getLogger(Order.class);

    // Locators
    private static final By confirmationMessage = By.xpath("//h1[contains(text(),'Thankyou')]");

    // Validate confirmation message
    public void CheckOrderConfirmed() {
        try {
            WebElement messageElement = cm.waitForElement(confirmationMessage, 10);
            String message = messageElement.getText().trim();

            logger.info("Confirmation message found: " + message);
            ExtentLogger.info("Confirmation message found: " + message);

            Assert.assertEquals(message, "THANKYOU FOR THE ORDER.", "Order confirmation mismatch!");
            ExtentLogger.pass("Order confirmation validated successfully");
        } catch (Exception e) {
            logger.error("Order confirmation validation failed: " + e.getMessage());
            ExtentLogger.fail("Order confirmation validation failed: " + e.getMessage());
            Assert.fail("Order confirmation validation failed: " + e.getMessage());
        }
    }

    // Validate product name
    public void validateProductName(String expectedProduct) {
        try {
            logger.info("Validating product name on order page...");
            ExtentLogger.info("Validating product name on order page...");

            By productName = By.xpath("//div[@class='title' and contains(text(),'" + expectedProduct + "')]");
            String actualProduct = cm.getText(productName).trim();

            logger.info("Product name found: " + actualProduct + " | Expected: " + expectedProduct);
            ExtentLogger.info("Product name found: " + actualProduct + " | Expected: " + expectedProduct);

            Assert.assertEquals(actualProduct, expectedProduct, "Product name mismatch!");
            ExtentLogger.pass("Product name validated successfully");
        } catch (Exception e) {
            logger.error("Product name validation failed: " + e.getMessage());
            ExtentLogger.fail("Product name validation failed: " + e.getMessage());
            Assert.fail("Product name validation failed: " + e.getMessage());
        }
    }

    // Validate product quantity
    public void validateProductQty(int expectedQty) {
        try {
            logger.info("Validating product quantity on order page...");
            ExtentLogger.info("Validating product quantity on order page...");

            By productQty = By.xpath("//div[@class='sub' and contains(text(),'Qty')]");
            String qtyText = cm.getText(productQty).replace("Qty:", "").trim();
            int actualQty = Integer.parseInt(qtyText);

            logger.info("Product quantity found: " + actualQty + " | Expected: " + expectedQty);
            ExtentLogger.info("Product quantity found: " + actualQty + " | Expected: " + expectedQty);

            Assert.assertEquals(actualQty, expectedQty, "Product quantity mismatch!");
            ExtentLogger.pass("Product quantity validated successfully");

            // Take screenshot after successful validation
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String screenshotPath = Base.screenshotRunFolder + "/CompleteOrder_" + timestamp + ".png";
            TakesScreenShot.takeScreenshot(driver, screenshotPath);
        } catch (Exception e) {
            logger.error("Product quantity validation failed: " + e.getMessage());
            ExtentLogger.fail("Product quantity validation failed: " + e.getMessage());
            Assert.fail("Product quantity validation failed: " + e.getMessage());
        }
    }
}
