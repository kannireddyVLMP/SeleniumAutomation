package com.Pearson.Pages;

import com.Pearson.Base.Base;
import com.Pearson.TestData.Excel;
import com.Pearson.Utilities.ExtentLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.*;

public class EndtoEndTest extends Base {
    private static final Logger logger = LogManager.getLogger(EndtoEndTest.class);

    @Test(dataProvider = "excelDataProvider")
    public void E2ETest(String testCaseName) {
        try {
            Map<String, List<HashMap<String, String>>> testData =
                    Excel.loadWorkbook("src/test/resources/TestData.xlsx");

            Login login = new Login(getDriver());
            Dashboard dashboard = new Dashboard(getDriver());
            Cart cart = new Cart(getDriver());
            Payment payment = new Payment(getDriver());
            Order order = new Order(getDriver());

            logger.info("Running TestCase: " + testCaseName);
            ExtentLogger.info("Running TestCase: " + testCaseName);

            HashMap<String, String> dashboardRow = getRow(testData.get("Dashboard"), testCaseName);
            HashMap<String, String> cartRow = getRow(testData.get("Cart"), testCaseName);
            HashMap<String, String> paymentRow = getRow(testData.get("Payment"), testCaseName);
            HashMap<String, String> orderRow = getRow(testData.get("Order"), testCaseName);

            // Login
            login.login();

            // Dashboard
           dashboard.addProductToCart(dashboardRow.get("ProductName"));
          dashboard.goToCartPage();

            // Cart
            cart.isProductInCart(cartRow.get("ProductName"));
           cart.goToCheckout();

            // Payment
            payment.fillPersonalInfo(
                    paymentRow.get("CardNumber"),
                    paymentRow.get("Month"),
                    paymentRow.get("Date"),
                    paymentRow.get("CVV"),
                    paymentRow.get("Name")
            );
            payment.validateShippingInfo(getProp("UserName"), paymentRow.get("Country"));
            payment.placeOrder();

            // Order
            order.CheckOrderConfirmed();
            order.validateProductName(orderRow.get("ProductName"));
            order.validateProductQty(Integer.parseInt(orderRow.get("ExpectedQty")));

            ExtentLogger.pass("TestCase Passed: " + testCaseName);
        } catch (Exception e) {
            logger.error("❌ TestCase Failed: " + testCaseName + " - " + e.getMessage());
            ExtentLogger.fail("❌ TestCase Failed: " + testCaseName + " - " + e.getMessage());
            Assert.fail("❌ TestCase Failed: " + testCaseName + " - " + e.getMessage());
        }
    }

    @DataProvider(name = "excelDataProvider", parallel = true)
    public Object[][] getData() {
        Map<String, List<HashMap<String, String>>> testData =
                Excel.loadWorkbook("src/test/resources/TestData.xlsx");

        List<HashMap<String, String>> dashboardData = testData.get("Dashboard");
        List<Object[]> filtered = new ArrayList<>();

        String indexProp = System.getProperty("datasetIndex");

        if (indexProp != null && !indexProp.trim().isEmpty()) {
            int targetIndex = Integer.parseInt(indexProp.trim());
            if (targetIndex >= 0 && targetIndex < dashboardData.size()) {
                HashMap<String, String> row = dashboardData.get(targetIndex);
                if (row != null && !row.isEmpty() && "Y".equalsIgnoreCase(row.get("Run"))) {
                    filtered.add(new Object[]{row.get("TestCaseName")});
                }
            }
        } else {
            for (HashMap<String, String> row : dashboardData) {
                if (row != null && !row.isEmpty() && "Y".equalsIgnoreCase(row.get("Run"))) {
                    filtered.add(new Object[]{row.get("TestCaseName")});
                }
            }
        }

        Object[][] data = new Object[filtered.size()][1];
        for (int i = 0; i < filtered.size(); i++) {
            data[i][0] = filtered.get(i)[0];
        }
        return data;
    }

    private HashMap<String, String> getRow(List<HashMap<String, String>> data, String testCaseName) {
        for (HashMap<String, String> row : data) {
            if (testCaseName.equalsIgnoreCase(row.get("TestCaseName"))) {
                return row;
            }
        }
        throw new RuntimeException("❌ No data found for TestCaseName: " + testCaseName);
    }
}
