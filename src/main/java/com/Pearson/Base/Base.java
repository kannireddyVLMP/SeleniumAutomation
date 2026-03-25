package com.Pearson.Base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;


public class Base
{
    static Properties prop;
    public static WebDriver driver;
    public static String uName;
    public static String pwd;

    private static final Logger logger = LogManager.getLogger(Base.class);
    public static String screenshotRunFolder = "target/screenshots";

    @BeforeMethod
    public  void browserIntialzationAndLauchURL() throws InterruptedException, IOException
    {
        prop = new Properties();
        FileInputStream fis = new FileInputStream("src/test/resources/Config.properties");
        prop.load(fis);

        //env = System.getProperty("env","QA");
        String browser = prop.getProperty("Browser");
        String url = prop.getProperty("URL");
        uName=prop.getProperty("UserName");
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

        if(url == null|| uName == null || pwd == null)
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
            logger.error("driver.get() failed. Error: " + e.getMessage());
            throw e;
        }
        Thread.sleep(2000);
    }
    @AfterMethod
    public  void tearDown()
    {
        driver.quit();
        logger.info("Browser closed successfully.");
    }


}
