package com.Pearson.Pages;

import com.Pearson.Base.Base;
import com.Pearson.CommonMethods.CommonMethods;
import com.Pearson.Utilities.ExtentLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class Login extends Base {
    private static final Logger logger = LogManager.getLogger(Login.class);

    private WebDriver driver;
    private CommonMethods cm;

    public Login(WebDriver driver) {
        this.driver = driver;
        this.cm = new CommonMethods(Base.getDriver());
    }

    // Locators
    private static final By usernameField = By.id("userEmail");
    private static final By passwordField = By.id("userPassword");
    private static final By loginButton = By.id("login");
    private static final By ordersLink = By.cssSelector("button[routerlink='/dashboard/myorders']");

    public void login() {
        try {
            cm.waitForElement(usernameField, 10);
            String uName = getProp("UserName");
            String pwd = getProp("Password");

            logger.info("Fetched credentials: " + uName + " / " + pwd);

            cm.sendKeys(usernameField, uName);
            cm.sendKeys(passwordField, pwd);
            cm.click(loginButton);

            // ✅ Wait for dashboard element
            cm.waitForElement(ordersLink, 10);
            Assert.assertTrue(driver.findElement(ordersLink).isDisplayed(), "Login failed - Orders link not visible");

            logger.info("Login Successful with username: " + uName +"and Password: "+pwd);
            ExtentLogger.pass("Login Successful with username: " + uName +"and Password: "+pwd);
        } catch (Exception e) {
            logger.error("Login failed: " + e.getMessage());
            ExtentLogger.fail("Login failed: " + e.getMessage());
            throw new RuntimeException("Login failed: " + e.getMessage(), e);
        }
    }
}
