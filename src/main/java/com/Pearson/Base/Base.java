package com.Pearson.Base;

import com.Pearson.Screenshot.TakesScreenShot;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Base
{
    static Properties prop;
    public static WebDriver driver;
    public static String uname;
    public static String pwd;

   public  static Logger logger = Logger.getLogger(Base.class);
    static {
        PropertyConfigurator.configure("src/test/resources/log4j.properties");
    }

    public static void browserIntialzationAndLauchURL(String screenshotFolder) throws InterruptedException, IOException {
        com.Pearson.CommonMethods.CommonMethods.screenshotFolder = screenshotFolder;
        prop = new Properties();
        FileInputStream fis = new FileInputStream("src/test/resources/Config.properties");
        prop.load(fis);
        //env = System.getProperty("env","QA");
        String browser = prop.getProperty("Browser");
        String url = prop.getProperty("URL");
        uname=prop.getProperty("UserName");
        pwd = prop.getProperty("Password");

        switch(browser)
        {
            case "Chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions opt = new ChromeOptions();
                opt.setAcceptInsecureCerts(true);
                driver = new ChromeDriver(opt);
                driver.manage().window().maximize();
                logger.info("Running tests on Chrome browser");
                break;
            case "Edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions opt1 = new EdgeOptions();
                opt1.setAcceptInsecureCerts(true);
                driver = new EdgeDriver(opt1);
                driver.manage().window().maximize();
                logger.info("Running tests on Edge browser");
                break;
            default:
                logger.error("Browser not supported, please check the configuration.");
        }

        if(url == null|| uname == null || pwd == null)
        {
            logger.error("No URL or credentials found for env");
            throw new RuntimeException("No URL found for env: ");
        }
        //loadConfig();
        logger.info("Loading URL: " + url);

        try {
            driver.get(url);
            logger.info("Page loaded successfully!");
            logger.info("Current URL: " + driver.getCurrentUrl());
        } catch (Exception e) {
            String screenshotPath = screenshotFolder + "/driver_get_failed_" + System.currentTimeMillis() + ".png";
            TakesScreenShot.takeScreenshot(driver, screenshotPath);
            logger.error("driver.get() failed. Screenshot: " + screenshotPath, e);
            throw e;
        }
        // Take screenshot after loading URL in the provided folder
        String screenshotPath = screenshotFolder + "/initial_page.png";
        TakesScreenShot.takeScreenshot(driver, screenshotPath);
        logger.info("Screenshot saved: " + screenshotPath);

        // Keep browser open for 10 seconds to see the result
        Thread.sleep(2000);
    }
    public static void tearDown()
    {
        driver.quit();
    }


}
