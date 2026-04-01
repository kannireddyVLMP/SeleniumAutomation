package com.Pearson.Pages;

import com.Pearson.Base.Base;
import com.Pearson.CommonMethods.CommonMethods;
import com.Pearson.Utilities.ExtentLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

public class Dashboard extends Base {
    private static final Logger logger = LogManager.getLogger(Dashboard.class);
    private WebDriver driver;
    private CommonMethods cm;

    public Dashboard(WebDriver driver) {
        this.driver = driver;
        this.cm = new CommonMethods(driver); // ✅ use instance driver
    }

    // Locators
    private static final By navHome = By.xpath("//button[contains(text(),'HOME')]");
    private static final By navOrders = By.xpath("//button[contains(text(),'ORDERS')]");
    private static final By navCart = By.cssSelector("button[routerlink='/dashboard/cart']");
    private static final By toastContainer = By.id("toast-container");

    public void addProductToCart(String productNameToAdd) throws InterruptedException {
        try {
            List<WebElement> productCards = driver.findElements(By.xpath("//div[@class='card']/following::div[@class='card-body']"));

            boolean productFound = false;

            for (WebElement card : productCards) {
                String rawName = card.findElement(By.xpath(".//h5/b")).getText();
                logger.info("Product found on page: " + rawName);
                ExtentLogger.info("Product found on page: " + rawName);
                // Clean up: remove price suffix and normalize spaces
                String name = rawName.split("==")[0].trim().replaceAll("\\s+", " ").toLowerCase();
                System.out.println("Checking product: " + name);
                String target = productNameToAdd.trim().replaceAll("\\s+", " ").toLowerCase();

                System.out.println("Comparing: [" + name + "] with [" + target + "]");
                if (name.equals(target)) {
                    productFound = true;
                    WebElement addBtn = card.findElement(By.xpath(".//button[contains(text(),'Add To Cart')]"));
                    try {
                        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
                        wait.until(ExpectedConditions.elementToBeClickable(addBtn));
                        JavascriptExecutor js = (JavascriptExecutor) driver;
                        js.executeScript("arguments[0].scrollIntoView(true);", addBtn);
                        addBtn.click();
                    } catch (ElementClickInterceptedException e) {
                        logger.warn("Click intercepted, using JS click");
                        JavascriptExecutor js = (JavascriptExecutor) driver;
                        js.executeScript("arguments[0].scrollIntoView(true);", addBtn);

                        js.executeScript("arguments[0].click();", addBtn);
                    }
                    logger.info("Product Added Successfully " + productNameToAdd);
                    ExtentLogger.info("Product Added Successfully " + productNameToAdd);

                    Thread.sleep(2000);
                    break;
                }
            }
            if (!productFound) {
                logger.info("Product Not Found " + productNameToAdd);
                ExtentLogger.info("Product Not Found " + productNameToAdd);
                throw new NoSuchElementException("Product not found: " + productNameToAdd);

            }
        } catch (Exception e)
        {
            // Take screenshot on failure
            logger.error("Error adding product to cart: " + e.getMessage());
            ExtentLogger.info("Error adding product to cart: " + e.getMessage());



        }
    }
    public void isProductAddedToastDisplayed() {
        try {
            WebElement toast = cm.waitForElement(toastContainer, 5);
            Assert.assertTrue(toast.isDisplayed(), "Toast message is not displayed!");
            logger.info("Toast Message displayed successfully");
            ExtentLogger.pass("Toast Message displayed successfully");
        } catch (Exception e) {
            logger.error("Toast message not displayed: " + e.getMessage());
            ExtentLogger.fail("Toast message not displayed: " + e.getMessage());
            Assert.fail("Toast message not displayed: " + e.getMessage());
        }
    }

    public void isCartCountCorrect(String expectedCount) {
        try {
            By cartCountLocator = By.xpath("//button[@routerlink='/dashboard/cart']/label[contains(text(),'" + expectedCount + "')]");
            WebElement cartElement = cm.waitForElement(cartCountLocator, 5);

            String cartText = cartElement.getText().trim();
            logger.info("Cart text is: " + cartText);
            ExtentLogger.pass("Cart count verified: " + cartText);
            Assert.assertEquals(cartText, expectedCount, "Cart count mismatch!");
        } catch (Exception e) {
            logger.error("Cart count verification failed: " + e.getMessage());
            ExtentLogger.fail("Cart count verification failed: " + e.getMessage());
            Assert.fail("Cart count verification failed: " + e.getMessage());
        }
    }

    public void goToCartPage() {
        try {
            // Wait for element to be visible
            WebElement cartBtn = cm.waitForElement(navCart, 5);

            // Scroll into view
            cm.jsScrollIntoView(cartBtn);

            try {
                // Primary click via JS
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cartBtn);
            } catch (Exception jsFail) {
                logger.warn("JS click failed, retrying with Actions + JS");
                try {
                    new Actions(driver).moveToElement(cartBtn).click().perform();
                } catch (Exception actionsFail) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cartBtn);
                }
            }

            // ✅ Wait until "My Cart" heading is visible
            logger.info("My Cart heading visibility check started");
            ExtentLogger.info("My Cart heading visibility check started");

            By myCartHeading = By.xpath("//h1[contains(text(),'My Cart')]");
            cm.waitForElement(myCartHeading, 5);

            logger.info("My Cart heading visibled successfully");
            ExtentLogger.info("My Cart heading visibled successfully");

            logger.info("Navigated to Cart Page successfully");
            ExtentLogger.pass("Navigated to Cart Page successfully");
        } catch (TimeoutException e) {
            logger.error("Cart page did not load within timeout: " + e.getMessage());
            ExtentLogger.fail("Cart page did not load within timeout: " + e.getMessage());
            throw new RuntimeException("Cart page did not load within timeout", e);
        } catch (Exception e) {
            logger.error("Error navigating to Cart Page: " + e.getMessage());
            ExtentLogger.fail("Error navigating to Cart Page: " + e.getMessage());
            throw new RuntimeException("Error navigating to Cart Page: " + e.getMessage(), e);
        }
    }
}
