package com.Pearson.Utilities;

import com.Pearson.Base.Base;
import com.Pearson.TestData.Excel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.Pearson.Utilities.FailureContext;
import static org.testng.internal.invokers.ITestInvoker.*;

public class TestListener implements ITestListener {

    private static final Logger logger = LogManager.getLogger(TestListener.class);
    public static String screenshotFolder = "target/screenshots/com.Pearson.Pages.EndtoEndTest/";

    @Override
    public void onStart(ITestContext context) {
        Excel.clearResults();
        logger.info("Previous results cleared.");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        try {

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String stepName = FailureContext.getFailureLocation(result.getThrowable());
            String screenshotPath = Base.screenshotRunFolder + "/" + stepName + "_" + timestamp + ".png";
            String folderPath = Base.screenshotRunFolder + "/" + stepName.split("\\.")[0];
            new File(folderPath).mkdirs();
            TakesScreenShot.takeScreenshot(Base.driver, screenshotPath);
            logger.info("Screenshot captured on failure: " + screenshotPath);
            Excel.updateResult(
                    "Results",
                    result.getMethod().getMethodName(),
                    stepName,
                    "FAIL",
                    IndexedColors.RED
            );
        }
        catch (Exception e) {
            logger.error("Failed to capture screenshot on test failure: ", e);
        }
    }


    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test Passed: " + result.getMethod().getMethodName());
        Excel.updateResult(
                "Results",
                result.getMethod().getMethodName(),   // TestCaseName
                "Completed Execution",                          // StepName (you can customize)
                "PASS",                               // Status
                IndexedColors.GREEN                   // Color
        );
        System.out.println("Test Passed: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test Skipped: " + result.getMethod().getMethodName());
        Excel.updateResult(
                "Results",
                result.getMethod().getMethodName(),
                "Skipped Execution",
                "SKIP",
                IndexedColors.YELLOW
        );
        System.out.println("Test Skipped: " + result.getMethod().getMethodName());
    }
}



