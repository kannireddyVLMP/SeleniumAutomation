package com.Pearson.Pages;

import com.Pearson.Base.Base;
import org.testng.Assert;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.Pearson.Base.Base.driver;
import static com.Pearson.Base.Base.pwd;

public class EndtoEndTest {
    public static String screenshotRunFolder;

    public static void main(String[] args) throws InterruptedException, IOException {
        String dateStr = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        screenshotRunFolder = "target/screenshots/com.Pearson.Pages.EndtoEndTest/" + dateStr;
        new java.io.File(screenshotRunFolder).mkdirs();
        try {
            Base.browserIntialzationAndLauchURL(screenshotRunFolder);
            LoginPage lp = new LoginPage(driver, screenshotRunFolder);
            lp.pageTitleValidation();
            lp.login();
            Dashboard dashboard = new Dashboard(driver,screenshotRunFolder);
            String productName = "ZARA COAT 3"; // This can be parameterized

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

            payment.validateShippingInfo(pwd,"India");
            payment.placeOrder();
            Order order = new Order(driver, screenshotRunFolder);
            order.CheckOrderConfirmed();
            order.validateProductName(productName);
            order.validateProductQty(1);
            Base.tearDown();

            // If you want to combine screenshots, call the combiner here, or let a utility handle it.
        } catch (Exception e) {
            System.out.println("Test failed: " + e.getMessage());
        } finally {// ...existing code...
        }
    }
}
