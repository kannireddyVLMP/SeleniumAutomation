package com.Pearson.Pages;

import com.Pearson.Base.Base;
import com.Pearson.CommonMethods.CommonMethods;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

import static com.Pearson.CommonMethods.CommonMethods.click;

public class Cart extends Base
{

    WebDriver driver;
    String screenshotFolder;
    private static final Logger logger = LogManager.getLogger(Cart.class);
    // Locators
    private final static By cartProductName = By.cssSelector(".cartSection h3");
    private final static  By checkoutButton = By.xpath("//button[contains(text(),'Checkout')]");
    Cart(WebDriver driver)
    {
        this.driver = driver;

        CommonMethods.driver = driver;
    }

    // Validate product name in cart
    public void isProductInCart(String expectedProductName)
    {
        WebElement productElement = driver.findElement(cartProductName);
        String actualProductName = productElement.getText().trim();
        System.out.println("Cart page product: " + actualProductName);
        Assert.assertEquals(
                actualProductName.toLowerCase().trim(),
                expectedProductName.toLowerCase().trim(),
                "Cart validation failed!"
        );
            logger.info("Cart validation successful: " + actualProductName);

        }

    public void goToCheckout() throws InterruptedException
    {

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.elementToBeClickable(checkoutButton));
             CommonMethods.click(checkoutButton);
        } catch (ElementClickInterceptedException e) {
            logger.warn("Click intercepted, using JS click");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", checkoutButton);
        }

        Thread.sleep(2000);
         logger.info("Navigated to Payment Page.");

    }
}


