package com.Pearson.Pages;

import com.Pearson.Base.Base;
import com.Pearson.CommonMethods.CommonMethods;
import com.Pearson.Utilities.ExtentLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Login extends Base
{

    private static final Logger logger = LogManager.getLogger(Login.class);

    private WebDriver driver;
    private CommonMethods cm;

    public Login(WebDriver driver) {
        this.driver = driver;
        this.cm = new CommonMethods(driver); // ✅ pass driver
    }

    private final static By usernameField = By.id("userEmail");
    private final static By passwordField = By.id("userPassword");
    private final static By loginButton = By.id("login");

     public boolean pageTitleValidation() {
        try {
            String expectedTitle = "Let's Shop";
            String actualTitle = driver.getTitle();
            return actualTitle.equals(expectedTitle);


        }
        catch (Exception e)
        {
            logger.info("Page title validation failed. Expected: " + "Let's Shop" + ", Actual: " + driver.getTitle());
            ExtentLogger.info("Page title validation failed. Expected: " + "Let's Shop" + ", Actual: " + driver.getTitle());
            throw e;
        }
    }

        public void login() throws InterruptedException {
        try {

            cm.webDriverWait(usernameField,5000);
            String uName = getProp("UserName");
            String pwd = getProp("Password");
            cm.sendKeys(usernameField, uName);
            cm.sendKeys(passwordField, pwd);
            cm.click(loginButton);
            Thread.sleep(2000);
            logger.info("Login Successful with username: " + uName);
            ExtentLogger.info("Login Successful with username: " + uName);
        } catch (Exception e) {
            // Take screenshot on failure
            logger.error("Login failed: " + e.getMessage());
           // ExtentLogger.info("Login failed: " + e.getMessage());
            throw e;
        }
    }



}