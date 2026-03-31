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
    String filePath = System.getProperty("user.dir") + "/src/test/resources/TestData.xlsx";

    Map<String, List<HashMap<String, String>>> testData =
            Excel.loadWorkbook(filePath);


    // ✅ Use SAME path here (FIXED)
    private static final String excelPath =
            System.getProperty("user.dir") + "/src/test/resources/TestData.xlsx";



    public static synchronized void clearResults() {
        try (FileInputStream fis = new FileInputStream(excelPath)) {

            Workbook workbook = new XSSFWorkbook(fis);

            Sheet sheet = workbook.getSheet("Results");
            if (sheet != null) {
                int lastRow = sheet.getLastRowNum();
                for (int i = lastRow; i >= 0; i--) {
                    Row row = sheet.getRow(i);
                    if (row != null) sheet.removeRow(row);
                }
            } else {
                sheet = workbook.createSheet("Results");
            }

            // Header
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("TestCaseName");
            header.createCell(1).setCellValue("StepName");
            header.createCell(2).setCellValue("Status");
            header.createCell(3).setCellValue("Timestamp");

            try (FileOutputStream fos = new FileOutputStream(excelPath)) {
                workbook.write(fos);
            }

            workbook.close(); // ✅ IMPORTANT FIX

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // =========================================================
// Updates results
// =========================================================
    public static synchronized void updateResult(
            String sheetName,
            String testCaseName,
            String stepName,
            String status,
            IndexedColors color) {

        try (FileInputStream fis = new FileInputStream(excelPath)) {

            Workbook workbook = new XSSFWorkbook(fis);

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                sheet = workbook.createSheet(sheetName);

                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue("TestCaseName");
                header.createCell(1).setCellValue("StepName");
                header.createCell(2).setCellValue("Status");
                header.createCell(3).setCellValue("Timestamp");
            }

            int rowNum = sheet.getPhysicalNumberOfRows();
            Row row = sheet.createRow(rowNum);

            row.createCell(0).setCellValue(testCaseName);
            row.createCell(1).setCellValue(stepName);
            row.createCell(2).setCellValue(status);

            // Style
            Cell statusCell = row.getCell(2);
            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(color.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            statusCell.setCellStyle(style);

            // Timestamp
            Cell timeCell = row.createCell(3);
            CreationHelper createHelper = workbook.getCreationHelper();
            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat(
                    createHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));
            timeCell.setCellStyle(dateStyle);
            timeCell.setCellValue(new Date());

            try (FileOutputStream fos = new FileOutputStream(excelPath)) {
                workbook.write(fos);
            }

            workbook.close(); // ✅ IMPORTANT FIX

        } catch (Exception e) {
            e.printStackTrace();
        }
    }}







