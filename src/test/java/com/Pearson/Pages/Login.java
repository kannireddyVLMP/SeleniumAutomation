package com.Pearson.Pages;

import com.Pearson.Base.Base;
import com.Pearson.CommonMethods.CommonMethods;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Login extends Base
{


   WebDriver driver;

    CommonMethods cm = new CommonMethods();

    private static final Logger logger = LogManager.getLogger(Login.class);
    Login(WebDriver driver) {
        this.driver = driver;

        CommonMethods.driver = driver;
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
        catch (Exception e) {
            logger.info("Page title validation failed. Expected: " + "Let's Shop" + ", Actual: " + driver.getTitle());
            throw e;
        }
    }

     public void login() throws InterruptedException {
        try {

            cm.webDriverWait(usernameField,1000);
            cm.sendKeys(usernameField, uName);
            cm.sendKeys(passwordField, pwd);
            cm.click(loginButton);
            Thread.sleep(2000);
            logger.info("Login Successful with username: " + uName);
        } catch (Exception e) {
            // Take screenshot on failure
            logger.error("Login failed: " + e.getMessage());
            throw e;
        }
    }



}