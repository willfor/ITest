package com.uc.jtest.utils;

public class JTestStringUtils {

    public static boolean isNotEmpty(String src) {
        return src != null && src != "" && src.trim().length() > 0;
    }

    public static boolean isEmpty(String src) {
        return src == null || src == "";
    }
    
    public static String upperFirstLetter(String fieldName) {
        String firstLetter = fieldName.substring(0, 1).toUpperCase();
        return firstLetter + fieldName.substring(1);
    }

    /*
     * Input 格式如下： citest.apitest.resource.app.CheckPiratedTest.testSignatureIsNotSameAndLegal(CheckPiratedTest.java:93)
     * 
     * Output: CheckPiratedTest_testSignatureIsNotSameAndLegal.properties
     */
    public static String constructResponsePropertiesFileName(String methodName1) {
        int firstBrack = methodName1.indexOf("(");
        methodName1 = methodName1.substring(0, firstBrack);
        int lastComma = methodName1.lastIndexOf(".");
        String testCaseName = methodName1.substring(lastComma + 1, methodName1.length());
        methodName1 = methodName1.substring(0, lastComma);
        int lastComma2 = methodName1.lastIndexOf(".");
        String testCaseName2 = methodName1.substring(lastComma2 + 1, methodName1.length());
        return testCaseName2 + "_" + testCaseName + ".properties";
    }

    /*
     * Input 格式如下： citest.apitest.resource.app.CheckPiratedTest.testSignatureIsNotSameAndLegal(CheckPiratedTest.java:93) Output: testSignatureIsNotSameAndLegal.properties
     */
    public static String constructResponsePropertiesFileNameV2(String methodName, String postFix) {
        int firstBrack = methodName.indexOf("(");
        methodName = methodName.substring(0, firstBrack);
        int lastComma = methodName.lastIndexOf(".");
        String testCaseName = methodName.substring(lastComma + 1, methodName.length());
        String fileName = testCaseName + "_" + postFix + ".txt";
        methodName = methodName.substring(0, lastComma);
        methodName = methodName.replace(".", "//");
        return methodName + "//" + fileName;
    }

    public static String getGeneratedFileName(String methodName) {
        return constructResponsePropertiesFileNameV2(methodName, "generated");
    }

    public static String getExpectedFileName(String methodName) {
        return constructResponsePropertiesFileNameV2(methodName, "expected");
    }

}
