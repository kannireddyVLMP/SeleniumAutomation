package com.Pearson.Utilities;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    private static ExtentReports extent;

    public static ExtentReports getInstance() {

        if (extent == null) {

            String path = System.getProperty("user.dir") + "/reports/ExtentReport.html";

            ExtentSparkReporter reporter = new ExtentSparkReporter(path);
            reporter.config().setReportName("E2E Automation Results");
            reporter.config().setDocumentTitle("Test Report");

            extent = new ExtentReports();
            extent.attachReporter(reporter);
            extent.setSystemInfo("Tester", "Sathish");
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Framework", "Selenium + TestNG");

        }

        return extent;
    }
}