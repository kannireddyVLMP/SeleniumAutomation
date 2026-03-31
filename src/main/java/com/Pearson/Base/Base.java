package com.Pearson.Base;

import com.Pearson.Utilities.ExtentLogger;
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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;


public class Base
{
    public static WebDriver driver;
    public static Properties prop = new Properties();

    public static String screenshotRunFolder = "target/screenshots";

    private static final Logger logger = LogManager.getLogger(Base.class);

    static {
        try {
            FileInputStream fis = new FileInputStream("src/test/resources/Config.properties");
            prop.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Helper method (use this everywhere)
    public static String getProp(String key) {
        return prop.getProperty(key);
    }


    @BeforeMethod
    public  void browserIntialzationAndLauchURL() throws InterruptedException, IOException
    {
        String browser = getProp("Browser");
        String url = getProp("URL");

        switch( browser)
        {
            case "Chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions opt = new ChromeOptions();
                opt.setAcceptInsecureCerts(true);
                driver = new ChromeDriver(opt);
                driver.manage().window().maximize();
                logger.info("Running tests on Chrome Browser");

                break;
            case "Edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions optt = new EdgeOptions();
                optt.setAcceptInsecureCerts(true);
                driver = new EdgeDriver(optt);
                driver.manage().window().maximize();
                logger.info("Running tests on Edge browser");

                break;
            default:
                logger.error("Browser not supported, please check the configuration.");
                    throw new RuntimeException("Browser not supported, please check the configuration.");
        }



            driver.get(url);
            logger.info("Page loaded successfully!");
            logger.info("Current URL: " + driver.getCurrentUrl());

    }
    @AfterMethod
    public  void tearDown()
    {
        driver.quit();
        logger.info("Browser closed successfully.");


    }


}
