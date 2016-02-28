package com.uc.jtest.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static final String date_formate = "yyyy-MM-dd HH:mm:ss";

    public static String getCurrentDate() {
        return getDateBeforeCurrentDayString(0);
    }

    public static String getDateBeforeCurrentDayString(int day) {
        SimpleDateFormat time = new SimpleDateFormat(date_formate);
        return time.format(getDateBeforeCurrent(day));
    }

    public static Date getDateBeforeCurrent(int day) {
        Date nowTime = new Date();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(nowTime.getTime() - day * 24 * 60 * 60 * 1000);
        return c.getTime();
    }

    public static Date getDateByTimeInMillins(long milseconds) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(milseconds);
        return c.getTime();
    }
    
    public static String formatDate(Date date, String pattern) {
        DateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }
    
    public static String formatCurrentDate(String pattern) {
        return formatDate(new Date(), pattern);
    }

}
