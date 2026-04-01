package com.Pearson.Pages;

import com.Pearson.Base.Base;
import com.Pearson.CommonMethods.CommonMethods;
import com.Pearson.Utilities.ExtentLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.testng.Assert;

import java.util.List;

public class Cart extends Base {
    private static final Logger logger = LogManager.getLogger(Cart.class);
    private WebDriver driver;
    private CommonMethods cm;

    public Cart(WebDriver driver) {
        this.driver = driver;
        this.cm = new CommonMethods(driver); // ✅ instance driver
    }

    // Locators
    private static final By cartProductName = By.cssSelector(".cartSection h3");
    private static final By checkoutButton = By.xpath("//button[contains(text(),'Checkout')]");

    // Validate product name in cart
    public void isProductInCart(String expectedProductName) {
        try {
            cm.waitForPageLoad();

// ✅ Wait for all product elements to be visible
            List<WebElement> productElements = cm.waitForElements(cartProductName, 10);

// ✅ Get texts of all products
            List<String> productNames = cm.getElementsText(cartProductName, 10);

            boolean productFound = false;
            String actualProductName = null;

            for (String name : productNames) {
                logger.info("Product in cart: " + name);
                ExtentLogger.info("Product in cart: " + name);

                if (name.equalsIgnoreCase(expectedProductName.trim())) {
                    productFound = true;
                    actualProductName = name;
                    break;
                }
            }

// ✅ Assert with expected vs actual
            Assert.assertTrue(productFound,
                    "Cart validation failed! Expected product: [" + expectedProductName + "] but found: " + productNames);

            logger.info("Cart validation successful. Expected: [" + expectedProductName + "] | Actual: [" + actualProductName + "]");
            ExtentLogger.pass("Cart validation successful. Expected: [" + expectedProductName + "] | Actual: [" + actualProductName + "]");

        } catch (Exception e) {
            logger.error("Cart validation failed: " + e.getMessage());
            ExtentLogger.fail("Cart validation failed: " + e.getMessage());
            Assert.fail("Cart validation failed: " + e.getMessage());
        }
    }

    public void goToCheckout() {
        try {
            cm.click(checkoutButton);
            logger.info("Navigated to Checkout successfully");
            ExtentLogger.pass("Navigated to Checkout successfully");
        } catch (ElementClickInterceptedException e) {
            logger.warn("Click intercepted, using JS click");
            WebElement checkoutBtn = cm.waitForElement(checkoutButton, 5);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkoutBtn);
        } catch (Exception e) {
            logger.error("Error navigating to Checkout: " + e.getMessage());
            ExtentLogger.fail("Error navigating to Checkout: " + e.getMessage());
            throw new RuntimeException("Error navigating to Checkout: " + e.getMessage(), e);
        }
    }
}
