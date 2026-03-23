// ...existing code...
// This class was moved from src/main/java/com/Pearson/Pages/ to src/test/java/com/Pearson/Pages/
// ...existing code...
package com.Pearson.Pages;

import com.Pearson.Base.Base;
import com.Pearson.CommonMethods.CommonMethods;
import com.Pearson.Screenshot.TakesScreenShot;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import static com.Pearson.Base.Base.*;
import static com.Pearson.Base.Base.driver;
import static com.Pearson.CommonMethods.CommonMethods.screenshotFolder;
import static com.Pearson.CommonMethods.CommonMethods.webDriverWait;


public class Dashboard extends Base
{
    CommonMethods cm = new CommonMethods();
    WebDriver driver;
    String screenshotFolder;



    // Locators
    private By productCards = By.cssSelector(".card-body");
    private By productName = By.cssSelector(".card-body b");
    private By productPrice = By.cssSelector(".card-body label");
    private By viewButton = By.xpath(".//button[contains(text(),'View')]");
    private By addToCartButton = By.xpath(".//button[contains(text(),'Add To Cart')]");
    private By navHome = By.xpath("//button[contains(text(),'HOME')]");
    private By navOrders = By.xpath("//button[contains(text(),'ORDERS')]");
    private By navCart = By.xpath("//ul//button[contains(text(),'Cart')]");


     Dashboard(WebDriver driver, String screenshotFolder) {
        this.driver = driver;
        this.screenshotFolder = screenshotFolder;
        CommonMethods.driver = driver;
    }


    public void addProductToCart(String productNameToAdd) throws InterruptedException {
        try {
            List<WebElement> productCards = driver.findElements(By.cssSelector(".card-body"));

            boolean productFound = false;

            for (WebElement card : productCards) {
                String name = card.findElement(By.cssSelector("b")).getText().trim();

                if (name.equalsIgnoreCase(productNameToAdd)) {
                    productFound = true;

                    // Click Add To Cart button inside this card
                    card.findElement(By.xpath(".//button[contains(text(),'Add To Cart')]")).click();
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
            String screenshotPath = screenshotFolder + "/addProducToCart_failed.png";
            TakesScreenShot.takeScreenshot(driver, screenshotPath);
            System.out.println("Screenshot saved (failure): " + screenshotPath);
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
                String screenshotPath = screenshotFolder + "/addProducToCart_failed.png";
                TakesScreenShot.takeScreenshot(driver, screenshotPath);
                System.out.println("Screenshot saved (failure): " + screenshotPath);
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
            String screenshotPath = screenshotFolder + "/cartCount_failed.png";
            TakesScreenShot.takeScreenshot(driver, screenshotPath);
            System.out.println("Screenshot saved (failure): " + screenshotPath);
            Assert.fail("Cart count is not correct: " + e.getMessage());
        }
    }
        public void goToCartPage() throws InterruptedException {
            try
            {
                cm.click(navCart, 10);
                logger.info("Navigated to Cart Page Successfully");
                Thread.sleep(2000);
            }
            catch (Exception e)
            {
                // Take screenshot on failure
                logger.error("Error navigating to cart page: " + e.getMessage());
                String screenshotPath = screenshotFolder + "/navigateToCart_failed.png";
                TakesScreenShot.takeScreenshot(driver, screenshotPath);
                System.out.println("Screenshot saved (failure): " + screenshotPath);
                throw e;
            }
        }



}



// ...existing code...
