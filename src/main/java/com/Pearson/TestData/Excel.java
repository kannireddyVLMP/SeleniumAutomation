package com.Pearson.TestData;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;


import java.io.*;
import java.util.*;

public class Excel {

    // ✅ Method 1: Load test data
    public static Map<String, List<HashMap<String, String>>> loadWorkbook(String filePath) {
        Map<String, List<HashMap<String, String>>> workbookData = new HashMap<>();
        DataFormatter formatter = new DataFormatter();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            for (Sheet sheet : workbook) {
                List<HashMap<String, String>> sheetData = new ArrayList<>();

                Iterator<Row> rowIterator = sheet.iterator();
                if (!rowIterator.hasNext()) continue;

                // First row = headers
                Row headerRow = rowIterator.next();
                List<String> headers = new ArrayList<>();
                for (Cell cell : headerRow) {
                    headers.add(formatter.formatCellValue(cell));
                }

                // Remaining rows = data
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    HashMap<String, String> dataMap = new HashMap<>();
                    for (int i = 0; i < headers.size(); i++) {
                        Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        dataMap.put(headers.get(i), formatter.formatCellValue(cell));
                    }
                    sheetData.add(dataMap);
                }

                workbookData.put(sheet.getSheetName(), sheetData);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return workbookData;
    }

    // ✅ Method 2: Update results with colors
    public static synchronized void updateResult(String testName, String stepName, IndexedColors color) {
        String excelPath = "src/test/resources/TestData.xlsx";

        try (FileInputStream fis = new FileInputStream(excelPath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("Results");
            if (sheet == null) {
                sheet = workbook.createSheet("Results");
            }

            int rowNum = sheet.getPhysicalNumberOfRows();
            Row row = sheet.createRow(rowNum);

            // Column A: Test Name
            Cell testCell = row.createCell(0);
            testCell.setCellValue(testName);

            // Column B: Page/Step
            Cell stepCell = row.createCell(1);
            stepCell.setCellValue(stepName);

            // Column C: Status (colored)
            Cell statusCell = row.createCell(2);
            statusCell.setCellValue("Status");
            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(color.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            statusCell.setCellStyle(style);

            try (FileOutputStream fos = new FileOutputStream(excelPath)) {
                workbook.write(fos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
