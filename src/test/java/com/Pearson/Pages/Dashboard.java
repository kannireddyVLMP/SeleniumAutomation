// ...existing code...
// This class was moved from src/main/java/com/Pearson/Pages/ to src/test/java/com/Pearson/Pages/
// ...existing code...
package com.Pearson.Pages;

import com.Pearson.Base.Base;
import com.Pearson.CommonMethods.CommonMethods;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import static com.Pearson.CommonMethods.CommonMethods.webDriverWait;


public class Dashboard extends Base
{
    WebDriver driver;
    private static final Logger logger = LogManager.getLogger(Dashboard.class);

    // Locators

    private final static  By viewButton = By.xpath(".//button[contains(text(),'View')]");
    private final static By addToCartButton = By.xpath(".//button[contains(text(),'Add To Cart')]");
    private final static By navHome = By.xpath("//button[contains(text(),'HOME')]");
    private  final static By navOrders = By.xpath("//button[contains(text(),'ORDERS')]");
    private final static By navCart = By.xpath("//ul//button[contains(text(),'Cart')]");


     Dashboard(WebDriver driver) {
        this.driver = driver;

    }


    public void addProductToCart(String productNameToAdd) throws InterruptedException {
        try {
            List<WebElement> productCards = driver.findElements(By.xpath("//div[@class='card']/following::div[@class='card-body']"));

            boolean productFound = false;

            for (WebElement card : productCards) {
                String rawName = card.findElement(By.xpath(".//h5/b")).getText();

        // Clean up: remove price suffix and normalize spaces
                String name = rawName.split("==")[0].trim().replaceAll("\\s+", " ").toLowerCase();
                System.out.println("Checking product: " + name);
                String target = productNameToAdd.trim().replaceAll("\\s+", " ").toLowerCase();

                System.out.println("Comparing: [" + name + "] with [" + target + "]");
                if (name.equals(target)) {
                    productFound = true;
                    WebElement addBtn = card.findElement(By.xpath(".//button[contains(text(),'Add To Cart')]"));
                    try {
                        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
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
                    Thread.sleep(2000);
                    break;
                }
           }
            if (!productFound) {
                logger.info("Product Not Found " + productNameToAdd);
                throw new NoSuchElementException("Product not found: " + productNameToAdd);

            }
        } catch (Exception e)
        {
            // Take screenshot on failure
            logger.error("Error adding product to cart: " + e.getMessage());

            throw e;

        }
    }

    public void isProductAddedToastDisplayed() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        try {
            WebElement toast = wait.until(ExpectedConditions
                    .presenceOfElementLocated(By.id("toast-container")));
            logger.info("Toast Message is Viewed");
            Boolean b =  toast.isDisplayed();
                Assert.assertTrue(b, "Toast message is not displayed!");
        }
        catch (TimeoutException e)
        {
              // Take screenshot on failure
                logger.error("Error adding product to cart: " + e.getMessage());


                Assert.fail("Toast message is not displayed: " + e.getMessage());


        }
        }

    public void isCartCountCorrect(String expectedCount) throws InterruptedException {
        try {
            WebElement cartElement = webDriverWait(By.xpath("//button[@routerlink='/dashboard/cart']/label[contains(text(),'" + expectedCount + "')]"), 10);
            String cartText = cartElement.getText().trim();
            System.out.println("Cart text: " + cartText);
            logger.info("Cart text is: " + cartText + " Cart added successfully");
            Thread.sleep(2000);
            Assert.assertEquals(cartText, expectedCount);
        } catch (NoSuchElementException e) {
            // Take screenshot on failure
            logger.error("Cart count is not correct: " + e.getMessage());

            Assert.fail("Cart count is not correct: " + e.getMessage());
        }
    }
        public void goToCartPage() throws InterruptedException {
            try
            {
                Actions a = new Actions(driver);
                a.moveToElement(driver.findElement(By.xpath("//ul//button[contains(text(),'Cart')]"))).build().perform();

                Thread.sleep(1000);
                WebElement cartBtn = driver.findElement(By.xpath("//ul//button[contains(text(),'Cart')]"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", cartBtn);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cartBtn);

                logger.info("Navigated to Cart Page Successfully");

                Thread.sleep(2000);
            }
            catch (Exception e)
            {
                // Take screenshot on failure
                logger.error("Error navigating to cart page: " + e.getMessage());
                 throw e;
            }
        }



}



// ...existing code...
