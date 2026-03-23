package com.Pearson.Pages;

import com.Pearson.Base.Base;
import com.Pearson.CommonMethods.CommonMethods;
import com.Pearson.Screenshot.TakesScreenShot;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends Base
{


   WebDriver driver;
   String screenshotFolder;
    CommonMethods cm = new CommonMethods();

    LoginPage(WebDriver driver, String screenshotFolder) {
        this.driver = driver;
        this.screenshotFolder = screenshotFolder;
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
        } catch (Exception e) {
            // Take screenshot on failure
            String screenshotPath = screenshotFolder + "/page_title_validation_failed.png";
            TakesScreenShot.takeScreenshot(driver, screenshotPath);
            System.out.println("Screenshot saved (failure): " + screenshotPath);
            throw e;
        }
    }

     public void login() throws InterruptedException {
        try {
            cm.sendKeys(usernameField, uname, 1000);
            cm.sendKeys(passwordField, pwd, 1000);
            cm.click(loginButton, 1000);
            Thread.sleep(5000);
            logger.info("Login Successful with username: " + uname);
        } catch (Exception e) {
            // Take screenshot on failure
            String screenshotPath = screenshotFolder + "/login_failed.png";
            TakesScreenShot.takeScreenshot(driver, screenshotPath);
            System.out.println("Screenshot saved (failure): " + screenshotPath);
            throw e;
        }
    }



}