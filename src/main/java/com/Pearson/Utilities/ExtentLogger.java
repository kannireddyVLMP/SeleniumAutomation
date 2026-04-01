package com.Pearson.Utilities;

public class ExtentLogger {

    public static void info(String message) {
        if (TestListener.extentTest.get() != null) {
            TestListener.extentTest.get().info(message);
        }
    }

    public static void pass(String message) {
        if (TestListener.extentTest.get() != null) {
            TestListener.extentTest.get().pass(message);
        }
    }

    public static void fail(String message) {
        if (TestListener.extentTest.get() != null) {
            TestListener.extentTest.get().fail(message);
        }
    }

    public static void skip(String message) {
        if (TestListener.extentTest.get() != null) {
            TestListener.extentTest.get().skip(message);
        }
    }
}
