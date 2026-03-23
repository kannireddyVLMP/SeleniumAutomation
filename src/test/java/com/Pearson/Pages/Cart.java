package com.Pearson.Pages;

import com.Pearson.Base.Base;
import com.Pearson.CommonMethods.CommonMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class Cart  extends Base
{
    CommonMethods cm = new CommonMethods();
    WebDriver driver;
    String screenshotFolder;

    // Locators
    private By cartProductName = By.cssSelector(".cartSection h3");
    private By checkoutButton = By.xpath("//button[contains(text(),'Checkout')]");
    Cart(WebDriver driver, String screenshotFolder) {
        this.driver = driver;
        this.screenshotFolder = screenshotFolder;
        CommonMethods.driver = driver;
    }

    // Validate product name in cart
    public void isProductInCart(String expectedProductName) {
        WebElement productElement = driver.findElement(cartProductName);
        String actualName = productElement.getText().trim();
        System.out.println("Cart page product: " + actualName);
         Assert.assertEquals(actualName,expectedProductName);
        logger.info("Product in cart matches expected: " + expectedProductName);
    }
    public void goToCheckout() throws InterruptedException {
        cm.click(checkoutButton, 1000);
        Thread.sleep(2000);
         logger.info("Navigated to Payment Page.");

    }
}
