package com.Pearson.Pages;

import com.Pearson.Base.Base;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.testng.annotations.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.Pearson.Base.Base.*;


public class EndtoEndTest extends Base
{

 public static String screenshotRunFolder;
 private static final Logger logger = LogManager.getLogger(EndtoEndTest.class);
 @Test
 public void E2ETest()
    {

        try {

            Login lp = new Login(driver);
            Dashboard dashboard = new Dashboard(driver);
            Cart cart = new Cart(driver);
            Payment payment = new Payment(driver);
            Order order = new Order(driver);

            //login
            lp.pageTitleValidation();
            lp.login();
            String productName = "iphone 13 pro"; // This can be parameterized

            //dashboard
            dashboard.addProductToCart(productName);
            dashboard.isCartCountCorrect("1");
            dashboard.goToCartPage();

            //cart
            cart.isProductInCart(productName);
            cart.goToCheckout();

            //payment
            payment.fillPersonalInfo("1234567890987654","12","31",
                    "274" ,"Sathish Reddy");
            payment.validateShippingInfo(uName,"India");
            payment.placeOrder();

            //order
            order.CheckOrderConfirmed();
            order.validateProductName(productName);
            order.validateProductQty(1);

             } catch (Exception e) {
            System.out.println("Test failed: " + e.getMessage());
        }
    }
}
