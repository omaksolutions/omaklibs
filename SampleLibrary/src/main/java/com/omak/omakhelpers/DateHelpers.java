package com.omak.omakhelpers;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * App_Theme_Utils class used in MDC-111 application.
 */
public abstract class DateHelpers {

    /* GEt Date Method*/
    public static Date convertString2Date(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date convertString2Date(String dateString, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String formatDate(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static String primaryDateFormat(String date) {
        return DateHelpers.formatDate(DateHelpers.convertString2Date(date, "yyyy-MM-dd"), "d MMM, yyyy");
    }

    public static String dateFormatGlobalShort(String date) {
        return DateHelpers.formatDate(DateHelpers.convertString2Date(date), "d MMM, yyyy");
    }

    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        Integer year = calendar.get(Calendar.YEAR);
        Integer month = calendar.get(Calendar.MONTH);
        Integer dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        Integer cur_month = month + 1;
        String combinedDate = "" + year + "-" + Utils.leftPad(cur_month) + "-" + Utils.leftPad(dayOfMonth);
        return combinedDate;
    }

    public static String getFormattedDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        String dateString = formatter.format(date);

        return dateString;
    }

    public static String getFormattedDate(String dateString) {
        Date date = convertString2Date(dateString);
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, yyyy HH:mm:ss");
        return formatter.format(date);
    }

    public static String getFormattedDate(String dateString, String format) {
        Date date = convertString2Date(dateString);
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public String convertUnix2Date(Long time) {
        return DateFormat.format("dd-MM-yyyy hh:mm:ss", time).toString();
    }

    public String getDate() {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        return date;
    }

}
