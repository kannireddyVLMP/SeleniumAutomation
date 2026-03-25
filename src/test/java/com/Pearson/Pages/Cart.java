package com.Pearson.Pages;

import com.Pearson.Base.Base;
import com.Pearson.CommonMethods.CommonMethods;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

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

        CommonMethods.click(checkoutButton, 1000);
        Thread.sleep(2000);
         logger.info("Navigated to Payment Page.");

    }
}


