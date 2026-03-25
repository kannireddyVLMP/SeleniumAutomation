package com.Pearson.Utilities;

public class FailureContext
{
    public static String getFailureLocation(Throwable throwable)
    {
        if (throwable == null) {
            return "UnknownLocation";
        }
        for (StackTraceElement element : throwable.getStackTrace())
        {
            // Only consider your project package
            if (element.getClassName().startsWith("com.Pearson.Pages"))
            {
                String className = element.getClassName()
                        .substring(element.getClassName().lastIndexOf(".") + 1);
                String methodName = element.getMethodName();
                return className + "." + methodName;
            }
        }
        return "UnknownLocation";
    }
}
