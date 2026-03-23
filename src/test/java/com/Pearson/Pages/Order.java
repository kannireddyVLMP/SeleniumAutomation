package com.Pearson.Pages;

import com.Pearson.Base.Base;
import com.Pearson.CommonMethods.CommonMethods;
import com.Pearson.Screenshot.TakesScreenShot;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class Order extends Base
{
    private WebDriver driver;
    private CommonMethods cm;
    private String screenshotFolder;

    // Locators
    private By confirmationMessage = By.xpath("//h1[normalize-space()='Thankyou for the order.']");

    public Order(WebDriver driver, String screenshotFolder) {
        this.driver = driver;
        this.cm = new CommonMethods();
        CommonMethods.driver = driver;
        this.screenshotFolder = screenshotFolder;
    }

    // Validate confirmation message
    public void CheckOrderConfirmed() {
        try {
            logger.info("Validating order confirmation message...");
            String message = cm.getText(confirmationMessage, 5).trim();
            logger.info("Confirmation message found: " + message);
            Assert.assertEquals(message,"THANKYOU FOR THE ORDER.");
        } catch (Exception e) {
            String screenshotPath = screenshotFolder + "/order_confirmation_failed.png";
            TakesScreenShot.takeScreenshot(driver, screenshotPath);
            logger.error("Order confirmation validation failed. Screenshot saved: " + screenshotPath, e);
            throw e;
        }
    }

    // Validate product name
    public void validateProductName(String expectedProduct) {
        try {
            logger.info("Validating product name on order page...");
             By productName = By.xpath("//div[@class='title' and contains(text(),'"+expectedProduct+"')]");   // adjust if table structure differs

            String actualProduct = cm.getText(productName, 5).trim();
            logger.info("Product name found: " + actualProduct + " | Expected: " + expectedProduct);
            Assert.assertEquals(actualProduct,expectedProduct);
        } catch (Exception e) {
            String screenshotPath = screenshotFolder + "/product_name_failed.png";
            TakesScreenShot.takeScreenshot(driver, screenshotPath);
            logger.error("Product name validation failed. Screenshot saved: " + screenshotPath, e);
            throw e;
        }
    }

    // Validate product quantity
    public void validateProductQty(int expectedQty) {
        try {
            logger.info("Validating product quantity on order page...");

             By productQty  = By.xpath("//div[@class='sub' and contains(text(),'1')]");   // adjust if table structure differs

            String qtyText = cm.getText(productQty, 5).replace("Qty:", "").trim();
            int actualQty = Integer.parseInt(qtyText);
            logger.info("Product quantity found: " + actualQty + " | Expected: " + expectedQty);
            Assert.assertEquals(actualQty,expectedQty);
        } catch (Exception e) {
            String screenshotPath = screenshotFolder + "/product_qty_failed.png";
            TakesScreenShot.takeScreenshot(driver, screenshotPath);
            logger.error("Product quantity validation failed. Screenshot saved: " + screenshotPath, e);
            throw e;
        }
    }
}
