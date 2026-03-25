package com.Pearson.Pages;

import com.Pearson.Base.Base;
import com.Pearson.TestData.Excel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.*;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.Pearson.Base.Base.*;


public class EndtoEndTest extends Base
{


 private static final Logger logger = LogManager.getLogger(EndtoEndTest.class);



        @Test(dataProvider = "excelDataProvider")
        public void E2ETest(int datasetIndex) throws InterruptedException {
            Map<String, List<HashMap<String, String>>> testData =
                    Excel.loadWorkbook("src/test/resources/TestData.xlsx");

            Login login = new Login(driver);
            login.login();

            Dashboard dashboard = new Dashboard(driver);
            Cart cart = new Cart(driver);
            Payment payment = new Payment(driver);
            Order order = new Order(driver);

            // Dashboard dataset
            HashMap<String, String> dashboardRow = testData.get("Dashboard").get(datasetIndex);
            dashboard.addProductToCart(dashboardRow.get("ProductName"));
            dashboard.isCartCountCorrect(dashboardRow.get("ExpectedCartCount"));
            dashboard.goToCartPage();

            // Cart dataset
            HashMap<String, String> cartRow = testData.get("Cart").get(datasetIndex);
            cart.isProductInCart(cartRow.get("ProductName"));
            cart.goToCheckout();

            // Payment dataset
            HashMap<String, String> paymentRow = testData.get("Payment").get(datasetIndex);
            payment.fillPersonalInfo(
                    paymentRow.get("CardNumber"),
                    paymentRow.get("Month"),
                    paymentRow.get("Date"),
                    paymentRow.get("CVV"),
                    paymentRow.get("Name")
            );
            payment.validateShippingInfo(uName, paymentRow.get("Country"));
            payment.placeOrder();

            // Order dataset
            HashMap<String, String> orderRow = testData.get("Order").get(datasetIndex);
            order.CheckOrderConfirmed();
            order.validateProductName(orderRow.get("ProductName"));
            order.validateProductQty(Integer.parseInt(orderRow.get("ExpectedQty")));
        }

    @DataProvider(name = "excelDataProvider")
    public Object[][] getData() {
        Map<String, List<HashMap<String, String>>> testData =
                Excel.loadWorkbook("src/test/resources/TestData.xlsx");

        List<HashMap<String, String>> dashboardData = testData.get("Dashboard");
        List<Object[]> filtered = new ArrayList<Object[]>();

        // Read target index dynamically (system property or default to all)
        String indexProp = System.getProperty("datasetIndex"); // e.g. mvn test -DdatasetIndex=2

        if (indexProp != null && !indexProp.trim().isEmpty()) {
            int targetIndex = Integer.parseInt(indexProp.trim());

            if (targetIndex >= 0 && targetIndex < dashboardData.size()) {
                HashMap<String, String> row = dashboardData.get(targetIndex);

                if (row != null && !row.isEmpty()) {
                    boolean hasNonEmptyValue = false;
                    for (String val : row.values()) {
                        if (val != null && val.trim().length() > 0) {
                            hasNonEmptyValue = true;
                            break;
                        }
                    }
                    if (hasNonEmptyValue) {
                        filtered.add(new Object[]{targetIndex});
                    }
                }
            }
        } else {
            // No datasetIndex specified → run all valid rows
            for (int i = 0; i < dashboardData.size(); i++) {
                HashMap<String, String> row = dashboardData.get(i);

                if (row != null && !row.isEmpty()) {
                    boolean hasNonEmptyValue = false;
                    for (String val : row.values()) {
                        if (val != null && val.trim().length() > 0) {
                            hasNonEmptyValue = true;
                            break;
                        }
                    }
                    if (hasNonEmptyValue) {
                        filtered.add(new Object[]{i});
                    }
                }
            }
        }

        Object[][] data = new Object[filtered.size()][1];
        for (int i = 0; i < filtered.size(); i++) {
            data[i][0] = filtered.get(i)[0];
        }

        return data;
    }

}


