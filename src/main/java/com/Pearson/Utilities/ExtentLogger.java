package com.Pearson.Utilities;

public class ExtentLogger {

    public static void info(String message) {
        TestListener.extentTest.get().info(message);
    }

    public static void pass(String message) {
        TestListener.extentTest.get().pass(message);
    }

    public static void fail(String message) {
        TestListener.extentTest.get().fail(message);
    }
}
