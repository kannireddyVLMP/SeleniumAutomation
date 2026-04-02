package com.Pearson.Base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Base {
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    public static Properties prop = new Properties();
    public static String screenshotRunFolder = "target/screenshots";
    public static final Logger logger = LogManager.getLogger(Base.class);

    // ✅ Load Config.properties from classpath
    static {
        try (InputStream input = Base.class.getClassLoader().getResourceAsStream("Config.properties")) {
            if (input == null) {
                throw new RuntimeException("Config.properties not found in classpath");
            }
            prop.load(input);
            logger.info("Properties loaded: " + prop);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProp(String key) {
        return prop.getProperty(key);
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    @BeforeMethod
    public void browserInitializationAndLaunchURL() {
        String browser = getProp("Browser");
        String url = getProp("URL");

        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions opt = new ChromeOptions();
                opt.setAcceptInsecureCerts(true);
                driver.set(new ChromeDriver(opt));
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions opt1 = new EdgeOptions();
                opt1.setAcceptInsecureCerts(true);
                driver.set(new EdgeDriver(opt1));
                break;
            default:
                throw new RuntimeException(browser +"Browser is not supported. Please check Config.properties");
        }

        getDriver().manage().window().maximize();
        getDriver().get(url);
        logger.info("Page loaded successfully: " + url);
    }

    @AfterMethod
    public void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
            driver.remove(); // ✅ prevents leaks in parallel runs
            logger.info("Browser closed successfully.");
        }
    }
}
