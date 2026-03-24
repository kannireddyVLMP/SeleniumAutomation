package com.Pearson.Pages;

import com.Pearson.Base.Base;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.testng.annotations.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.Pearson.Base.Base.*;

@Test
public class EndtoEndTest extends Base{
    public static String screenshotRunFolder;

    private static final Logger logger = LogManager.getLogger(EndtoEndTest.class);

    public void EndToEndtest()
    {
        String dateStr = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        screenshotRunFolder = "target/screenshots/com.Pearson.Pages.EndtoEndTest/" + dateStr;
        new java.io.File(screenshotRunFolder).mkdirs();
        try {

            Login lp = new Login(driver, screenshotRunFolder);
            lp.pageTitleValidation();
            lp.login();
            Dashboard dashboard = new Dashboard(driver,screenshotRunFolder);
            String productName = "iphone 13 pro"; // This can be parameterized

            dashboard.addProductToCart(productName);

            //Assert.assertTrue(dashboard.isProductAddedToastDisplayed(), "Product added toast not displayed!");
           dashboard.isCartCountCorrect("1");
            dashboard.goToCartPage();
            Cart cart = new Cart(driver, screenshotRunFolder);
            cart.isProductInCart(productName);
            cart.goToCheckout();
            Payment payment = new Payment(driver, screenshotRunFolder);
            payment.fillPersonalInfo("1234567890987654","12","31",
                    "274" ,"Sathish Reddy");

            payment.validateShippingInfo(uName,"India");
            payment.placeOrder();
            Order order = new Order(driver, screenshotRunFolder);
            order.CheckOrderConfirmed();
            order.validateProductName(productName);
            order.validateProductQty(1);

            // If you want to combine screenshots, call the combiner here, or let a utility handle it.
        } catch (Exception e) {
            System.out.println("Test failed: " + e.getMessage());
        } finally {// ...existing code...
        }
    }
}
