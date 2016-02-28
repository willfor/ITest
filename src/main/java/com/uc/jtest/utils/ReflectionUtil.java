package com.uc.jtest.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class ReflectionUtil {

    public static void setFieldValue(Class className, Object ddlInstance, String attributeName,
            Object attributeValue) {
        try {
            Field field = className.getDeclaredField(attributeName);
            field.setAccessible(true);
            field.set(ddlInstance, attributeValue);
        } catch (Exception e) {
            System.out.println("exception throws when set value to method!" + e.getMessage());
        }
    }

    public static Object getValue(Class className, String fieldName, Object object) {
        try {
            String getterMethodName = "get"  + JTestStringUtils.upperFirstLetter(fieldName);
            Method meth = className.getDeclaredMethod(getterMethodName);
            return meth.invoke(object);
        } catch (Exception e) {
            return null;
        }
    }

   

    public static Map<String, String> getFiledName(Object o) {
        Map<String, String> fieldNameAndTypeMap = new HashMap<String, String>();
        Field[] fields = o.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fieldNameAndTypeMap.put(fields[i].getName(), fields[i].getType().toString());
        }
        return fieldNameAndTypeMap;
    }

    public static void randomSetAllFieldValue(Object o, Class className) {
        Map<String, String> fieldNameAndTypeMap = getFiledName(o);
        for (String fieldName : fieldNameAndTypeMap.keySet()) {
            setFieldValue(className, o, fieldName,
                    getDefaultValue(fieldName,fieldNameAndTypeMap.get(fieldName)));
        }
    }

    public static Object getDefaultValue(String fieldName,String type) {
        return "aaaa";
    }

    public static Object getMethodValue(String defaultMethod) {
        Object methodValue = null;
        try {
            int lastDotIndex = defaultMethod.lastIndexOf(".");
            int leftBracketIndex = defaultMethod.indexOf("(");
            int rightBracketIndex = defaultMethod.indexOf(")");
            String className = defaultMethod.substring(0, lastDotIndex);
            String methodName = defaultMethod.substring(lastDotIndex + 1, leftBracketIndex);
            String type = defaultMethod.substring(leftBracketIndex + 1, rightBracketIndex);
            if (JTestStringUtils.isNotEmpty(type)) {
                type = type.trim();
                String[] dataInBracket = type.split(" ");
                String actualType = dataInBracket[0].trim();
                String actualValue = dataInBracket[1].trim();
                Class c1 = Class.forName(className);
                if ("int".equals(actualType)) {
                    Method m2 = c1.getMethod(methodName, new Class[] { int.class });
                    methodValue = m2.invoke(c1, new Object[] { Integer.valueOf(actualValue)
                            .intValue() });
                } else if ("Integer".equals(actualType)) {
                    Method m2 = c1.getMethod(methodName, new Class[] { Integer.class });
                    methodValue = m2.invoke(c1, new Object[] { Integer.valueOf(actualValue) });
                } else if ("String".equals(actualType)) {
                    Method m2 = c1.getMethod(methodName, new Class[] { String.class });
                    methodValue = m2.invoke(c1, new Object[] { actualValue });
                }

            } else {
                Class c1 = Class.forName(className);
                Method m2 = c1.getMethod(methodName);
                methodValue = m2.invoke(c1);
            }
        } catch (Exception e) {
            PrintUtil.print("Exception throws when reflect method!" + e.getMessage(),Level.SEVERE);
        }
        return methodValue;
    }

}
