package com.Pearson.Utilities;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.File;
import java.nio.file.Paths;

import static com.Pearson.Base.Base.getProp;

public class ExtentManager
{
    private static ExtentReports extent;
    public static ExtentReports getInstance() {

        if (extent == null) {

            String reportDir = Paths.get(System.getProperty("user.dir"), "target").toString();
            File dir = new File(reportDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String path = reportDir + File.separator + "ExtentReport.html";
            System.out.println("Report Path: " + path);

            ExtentSparkReporter reporter = new ExtentSparkReporter(path);
            reporter.config().setReportName("E2E Automation Results");
            reporter.config().setDocumentTitle("Test Report");

            extent = new ExtentReports();
            extent.attachReporter(reporter);
            extent.setSystemInfo("Tester", "Sathish");
            extent.setSystemInfo("Environment", getProp("Environment"));
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Framework", "Selenium + TestNG");
        }

        return extent;
    }
}