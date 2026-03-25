package com.Pearson.Utilities;

import com.Pearson.Base.Base;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.Pearson.Utilities.FailureContext;
import static org.testng.internal.invokers.ITestInvoker.*;

public class TestListener implements ITestListener {

    private static final Logger logger = LogManager.getLogger(TestListener.class);
    public static String screenshotFolder= "target/screenshots/com.Pearson.Pages.EndtoEndTest/";
    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test Passed: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            // Get failure location dynamically from stack trace
            String stepName = FailureContext.getFailureLocation(result.getThrowable());


            // Build screenshot path
            String screenshotPath = Base.screenshotRunFolder + "/" + stepName + "_" + timestamp + ".png";

            // Ensure directory exists
            String folderPath = Base.screenshotRunFolder + "/" + stepName.split("\\.")[0];
            new File(folderPath).mkdirs();
            TakesScreenShot.takeScreenshot(Base.driver, screenshotPath);
            logger.info("Screenshot captured on failure: " + screenshotPath);
        } catch (Exception e) {
            logger.error("Failed to capture screenshot on test failure: ", e);
        }
    }



    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test Skipped: " + result.getMethod().getMethodName());
    }

}
