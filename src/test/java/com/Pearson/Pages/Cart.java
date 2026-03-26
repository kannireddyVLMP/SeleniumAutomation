package com.Pearson.Pages;

import com.Pearson.Base.Base;
import com.Pearson.CommonMethods.CommonMethods;
import com.Pearson.Utilities.ExtentLogger;
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
    private static final Logger logger = LogManager.getLogger(Cart.class);
    private WebDriver driver;
    private CommonMethods cm;

    public Cart(WebDriver driver) {
        this.driver = driver;
        this.cm = new CommonMethods(driver); // ✅ pass driver
    }
    private final static By cartProductName = By.cssSelector(".cartSection h3");
    private final static  By checkoutButton = By.xpath("//button[contains(text(),'Checkout')]");

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
        ExtentLogger.info("Cart validation successful: " + actualProductName);

    }

    public void goToCheckout() throws InterruptedException
    {

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.elementToBeClickable(checkoutButton));
             CommonMethods.click(checkoutButton);
        } catch (ElementClickInterceptedException e) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", checkoutButton);
        }

        Thread.sleep(2000);


    }
}


