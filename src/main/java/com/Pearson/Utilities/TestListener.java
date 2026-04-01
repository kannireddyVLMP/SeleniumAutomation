package com.Pearson.Utilities;

import com.Pearson.Base.Base;
import com.Pearson.TestData.Excel;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.Pearson.Utilities.FailureContext;

import static com.Pearson.TestData.Excel.*;
import static org.testng.internal.invokers.ITestInvoker.*;

public class TestListener implements ITestListener {

    private static final Logger logger = LogManager.getLogger(TestListener.class);
    public static String screenshotFolder = "target/screenshots/com.Pearson.Pages.EndtoEndTest/";
    ExtentReports extent = ExtentManager.getInstance();
    ExtentTest test;
    public static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        clearResults();
        logger.info("Previous results cleared.");

    }
    @Override
    public void onTestStart(ITestResult result) {

        Object[] params = result.getParameters();

        String testCaseName;
        if (params.length > 0) {
            testCaseName = params[0].toString();
            logger.info("TestCase Started: " + testCaseName);

            // 🔥 TC_01
        } else {
            testCaseName = result.getMethod().getMethodName();
        }

        ExtentTest test = extent.createTest(testCaseName);
        extentTest.set(test);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        try {

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String stepName = FailureContext.getFailureLocation(result.getThrowable());

            String screenshotPath = Base.screenshotRunFolder + "/" + stepName + "_" + timestamp + ".png";
            String folderPath = Base.screenshotRunFolder + "/" + stepName.split("\\.")[0];
            new File(folderPath).mkdirs();

            TakesScreenShot.takeScreenshot(Base.getDriver(), screenshotPath);
            logger.info("Screenshot captured on failure: " + screenshotPath);
            ExtentLogger.info("Screenshot captured on failure: " + screenshotPath);

            Object[] params = result.getParameters();
            String testCaseName = params[0].toString();

            // ✅ Excel update
            Excel.updateResult(
                    "Results",
                    testCaseName,
                    stepName,
                    "FAIL",
                    IndexedColors.RED
            );

            // 🔥 EXTENT REPORT PART (ADD THIS)


            extentTest.get().fail("Step Failed: " + stepName);
            extentTest.get().fail(result.getThrowable());

            // attach screenshot to report
            extentTest.get().addScreenCaptureFromPath(screenshotPath, stepName);
            logger.info("Test Failed: " + result.getMethod().getMethodName());
            ExtentLogger.info("Test Failed: " + result.getMethod().getMethodName());
            System.out.println("Test Failed: " + result.getMethod().getMethodName());

        }
        catch (Exception e) {
            logger.error("Failed to capture screenshot on test failure: ", e.getMessage());
            ExtentLogger.info("Failed to capture screenshot on test failure: " + e.getMessage());

        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        Object[] params = result.getParameters();
        String testCaseName = params[0].toString();
        Excel.updateResult(
                "Results",
                testCaseName,   // TestCaseName
                "Completed Execution",                          // StepName (you can customize)
                "PASS",                               // Status
                IndexedColors.GREEN                   // Color
        );
        extentTest.get().pass("Test Passed");
        logger.info("Test Passed: " + result.getMethod().getMethodName());
        ExtentLogger.info("Test Passed: " + result.getMethod().getMethodName());
        System.out.println("Test Passed: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        Object[] params = result.getParameters();
        String testCaseName = params[0].toString();
        Excel.updateResult(
                "Results",
                testCaseName,
                "Skipped Execution",
                "SKIP",
                IndexedColors.YELLOW
        );
        extentTest.get().skip("Test Skipped");
        logger.warn("Test Skipped: " + result.getMethod().getMethodName());
        ExtentLogger.info("Test Skipped: " + result.getMethod().getMethodName());
        System.out.println("Test Skipped: " + result.getMethod().getMethodName());
    }
    @Override
    public void onFinish(ITestContext context)
    {
        extent.flush(); // 🔥 generates report
        logger.info("Test execution finished. Report generated.");
        ExtentLogger.info("Test execution finished. Report generated.");
    }
}