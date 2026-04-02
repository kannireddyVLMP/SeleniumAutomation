package com.Pearson.Pages;

import com.Pearson.Base.Base;
import com.Pearson.CommonMethods.CommonMethods;
import com.Pearson.Utilities.ExtentLogger;
import com.Pearson.Utilities.TakesScreenShot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;

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
            System.out.println("Validating product name on order page...");
            System.out.println("Expected product: " + expectedProduct);
            // ✅ Wait for all product elements to be visible
            List<WebElement> productElements = cm.waitForElements(By.xpath("//td[@class='line-item product-info-column m-3']/div[@class='title']"), 10);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//td[@class='line-item product-info-column m-3']/div[@class='title']")));
            productElements.forEach(element -> logger.info("Order page product element: " + element.getText().trim()));
            Thread.sleep(5000);
            // ✅ Get texts of all products
            List<String> productNames = cm.getElementsText(By.xpath("//td[@class='line-item product-info-column m-3']/div[@class='title']"), 10);

            boolean productFound = false;
            String actualProductName = null;

            for (String name : productNames) {
                logger.info("Product on order page: " + name);
                ExtentLogger.info("Product on order page: " + name);

                if (name.toLowerCase().contains(expectedProduct.trim().toLowerCase())) {
                    productFound = true;
                    actualProductName = name;
                    break;
                }
            }

            // ✅ Assert with expected vs actual
            Assert.assertTrue(productFound,
                    "Order validation failed! Expected product containing: [" + expectedProduct + "] but found: " + productNames);

            logger.info("Order validation successful. Expected contains: [" + expectedProduct + "] | Actual: [" + actualProductName + "]");
            ExtentLogger.pass("Order validation successful. Expected contains: [" + expectedProduct + "] | Actual: [" + actualProductName + "]");

        } catch (Exception e) {
            logger.error("Order validation failed: " + e.getMessage());
            ExtentLogger.fail("Order validation failed: " + e.getMessage());
            Assert.fail("Order validation failed: " + e.getMessage());
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
