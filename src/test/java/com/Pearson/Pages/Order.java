package com.Pearson.Pages;

import com.Pearson.Base.Base;
import com.Pearson.CommonMethods.CommonMethods;
import com.Pearson.Utilities.FailureContext;
import com.Pearson.Utilities.TakesScreenShot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Order extends Base
{
    private WebDriver driver;
    private CommonMethods cm;

    private static final Logger logger = LogManager.getLogger(Order.class);
    // Locators
    private By confirmationMessage = By.xpath("//h1[normalize-space()='Thankyou for the order.']");

    public Order(WebDriver driver) {
        this.driver = driver;

    }

    // Validate confirmation message
    public void CheckOrderConfirmed() {
        try {
            logger.info("Validating order confirmation message...");
            String message = cm.getText(confirmationMessage).trim();
            logger.info("Confirmation message found: " + message);
            Assert.assertEquals(message,"THANKYOU FOR THE ORDER.");
        } catch (Exception e) {

            logger.error("Order confirmation validation failed.  " + e);
            throw e;
        }
    }

    // Validate product name
    public void validateProductName(String expectedProduct) {
        try {
            logger.info("Validating product name on order page...");
             By productName = By.xpath("//div[@class='title' and contains(text(),'"+expectedProduct+"')]");   // adjust if table structure differs

            String actualProduct = cm.getText(productName).trim();
            logger.info("Product name found: " + actualProduct + " | Expected: " + expectedProduct);
            Assert.assertEquals(actualProduct,expectedProduct);
        } catch (Exception e) {

            logger.error("Product name validation failed. Screenshot saved: " + e);
            throw e;
        }
    }

    // Validate product quantity
    public void validateProductQty(int expectedQty) {
        try {
            logger.info("Validating product quantity on order page...");

             By productQty  = By.xpath("//div[@class='sub' and contains(text(),'1')]");   // adjust if table structure differs

            String qyText = cm.getText(productQty).replace("Qty:", "").trim();
            int actualQty = Integer.parseInt(qyText);
            logger.info("Product quantity found: " + actualQty + " | Expected: " + expectedQty);
            Assert.assertEquals(actualQty,expectedQty);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            // Get failure location dynamically from stack trace


            // Build screenshot path
            String screenshotPath = Base.screenshotRunFolder + "/CompleteOrder" + timestamp +".png";

            // Ensure directory exists


            TakesScreenShot.takeScreenshot(Base.driver, screenshotPath);
        } catch (Exception e) {

            logger.error("Product quantity validation failed. "+e);
            throw e;
        }
    }
}
