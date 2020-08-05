package com.omak.omakhelpers;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;

import com.google.android.material.datepicker.CalendarConstraints;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * App_Theme_Utils class used in MDC-111 application.
 */
public abstract class DateHelpers {

    /**
     * Unix to date with Long value
     * @param time
     * @return
     */
    public static String convertUnix2Date(Long time) {
        return DateFormat.format("dd-MM-yyyy hh:mm:ss", time).toString();
    }

    /**
     * Get Date in YYYY-mm-dd format
     * @return
     */
    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        Integer year = calendar.get(Calendar.YEAR);
        Integer month = calendar.get(Calendar.MONTH);
        Integer dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        Integer cur_month = month + 1;
        String combinedDate = "" + year + "-" + Utils.leftPad(cur_month) + "-" + Utils.leftPad(dayOfMonth);
        return combinedDate;
    }

    /**
     * Get full formatted date as per the format
     * @param format
     * @return
     */
    public String getCurrentDate(String format) {
        if(format==null) format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        String date = simpleDateFormat.format(new Date());
        return date;
    }

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


    /*
       Limit selectable Date range
     */
    public static CalendarConstraints.Builder limitRange(Integer year, Integer startMonth, Integer startDate) {

        CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();

        Calendar calendarStart = Calendar.getInstance();
        Calendar calendarEnd = Calendar.getInstance();

        calendarStart.set(year, startMonth - 1, startDate - 1);
        //calendarEnd.set(year, endMonth - 1, endDate);

        long minDate = calendarStart.getTimeInMillis();
        long maxDate = calendarEnd.getTimeInMillis();

        constraintsBuilderRange.setStart(minDate);
        constraintsBuilderRange.setEnd(maxDate);
        constraintsBuilderRange.setValidator(new RangeValidator(minDate, maxDate));

        return constraintsBuilderRange;
    }

    static class RangeValidator implements CalendarConstraints.DateValidator {

        public static final Parcelable.Creator<RangeValidator> CREATOR = new Parcelable.Creator<RangeValidator>() {

            @Override
            public RangeValidator createFromParcel(Parcel parcel) {
                return new RangeValidator(parcel);
            }

            @Override
            public RangeValidator[] newArray(int size) {
                return new RangeValidator[size];
            }
        };
        long minDate, maxDate;

        RangeValidator(long minDate, long maxDate) {
            this.minDate = minDate;
            this.maxDate = maxDate;
        }

        RangeValidator(Parcel parcel) {
            minDate = parcel.readLong();
            maxDate = parcel.readLong();
        }

        @Override
        public boolean isValid(long date) {
            return !(minDate > date || maxDate < date);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(minDate);
            dest.writeLong(maxDate);
        }
    }

}
