package com.uc.jtest.utils;

import org.apache.commons.lang.StringUtils;


public class RandomUtil {
    private static final String LEFT_SQUARE_BRACKET = "[";
    private static final  String RIGHT_SQUARE_BRACKET = "]";
    private static final  String RANGE_CHARACTER = "~";
    private static final String COMMA = ",";
    

    private static long getLongRandom(int min, int max) {
        return Math.round(Math.random() * (max - min) + min);
    }

    public static int getIntRandom(int min, int max) {
        return Long.valueOf(getLongRandom(min, max)).intValue();
    }

    /*
     * 目前支持的几种方式： 起始范围和数据范围
     */
    public static void main(String[] args) {
        int min = 1, max = 5;
        for (int i = min; i <= max; i++) {
            System.out.println(getRandomValues("0-1000"));
            System.out.println(getRandomValues("1,2,3,4"));
        }

    }

    public static String getRandomValues(String randomValue) {
        int min = 0, max = 0;
        String[] values = null;
        if (randomValue.contains(",")) {
            values = randomValue.split(",");
            max = values.length - 1;
            return values[getIntRandom(min, max)];
        } else if (randomValue.contains("-")) {
            values = randomValue.split("-");
            int start = Integer.valueOf(values[0]).intValue();
            int end = Integer.valueOf(values[1]).intValue();
            min = start;
            max = end;
            return String.valueOf(getIntRandom(min, max));
        }
        return randomValue;
    }
    

    /*
     * 如果想用这种的话，需要用[]包住：[2015-01-17 10:49:40,2015-01-17 10:49:41,2015-01-17 10:49:39,2015-01-17 10:49:42,2015-01-17 10:49:40]
     */
    public static String getRangeValueBySequenceWithStartAndEndFlag(String value, int i) {
        if (JTestStringUtils.isNotEmpty(value)) {
            if (value.startsWith(LEFT_SQUARE_BRACKET) && value.endsWith(RIGHT_SQUARE_BRACKET)) {
                value = value.replace(LEFT_SQUARE_BRACKET, "").replace(RIGHT_SQUARE_BRACKET, "");
                if (value.contains(COMMA)) {
                    String[] stringValue = value.split(COMMA);
                    value = stringValue[i % stringValue.length];
                }
                else if (value.contains(RANGE_CHARACTER)) {
                    String[] values = value.split(RANGE_CHARACTER);
                    try {
                        int start = Integer.valueOf(values[0]).intValue();
                        value = String.valueOf(start + i);
                    } catch (NumberFormatException e) {
                        throw new UnsupportedOperationException("传入的：" + value + " 不是数值类型，不支持范围操作！");
                    }
                } 
            }
        }
        return value;
    }
    
  

    public static String getRangeValueBySenquence(String value, int i) {
        if (value!=null) {
            if (value.contains("-")) {
                    String[] values = value.split("-");
                    int start = Integer.valueOf(values[0]).intValue();
                    value = String.valueOf(start + i);
            } 
            if (value.contains(",")) {
                String[] stringValue = value.split(",");
                value = stringValue[i % stringValue.length];
            }
        }
        return value;
    }

}
