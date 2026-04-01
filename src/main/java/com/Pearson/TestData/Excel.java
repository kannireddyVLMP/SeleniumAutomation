package com.Pearson.TestData;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
        try (FileInputStream fis = new FileInputStream(excelPath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("Results");
            if (sheet == null) {
                sheet = workbook.createSheet("Results");
            }

            DataFormatter formatter = new DataFormatter();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String today = sdf.format(new Date());

            // Collect rows to keep (only today’s)
            List<List<String>> rowsToKeep = new ArrayList<>();
            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
                Row headerRow = rowIterator.next(); // skip header
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Cell tsCell = row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String timestamp = formatter.formatCellValue(tsCell);

                    if (timestamp.startsWith(today)) {
                        List<String> rowData = new ArrayList<>();
                        for (int i = 0; i < 4; i++) {
                            Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                            rowData.add(formatter.formatCellValue(cell));
                        }
                        rowsToKeep.add(rowData);
                    }
                }
            }

            // Clear sheet completely
            int lastRow = sheet.getLastRowNum();
            for (int i = lastRow; i >= 0; i--) {
                Row row = sheet.getRow(i);
                if (row != null) sheet.removeRow(row);
            }

            // Recreate header
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("TestCaseName");
            header.createCell(1).setCellValue("StepName");
            header.createCell(2).setCellValue("Status");
            header.createCell(3).setCellValue("Timestamp");

            // Write back only today’s rows
            int rowNum = 1;
            for (List<String> rowData : rowsToKeep) {
                Row row = sheet.createRow(rowNum++);
                for (int i = 0; i < rowData.size(); i++) {
                    row.createCell(i).setCellValue(rowData.get(i));
                }
            }

            try (FileOutputStream fos = new FileOutputStream(excelPath)) {
                workbook.write(fos);
            }
            workbook.close();

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






