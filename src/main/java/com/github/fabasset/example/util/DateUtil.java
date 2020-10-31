package com.github.fabasset.example.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;

/**
 * 날짜 관련 유틸리티 클래스
 *
 * @author JM
 */
public class DateUtil {

    public static String getTime(String format) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(cal.getTime());
    }

    public static Date stringToDate(String dateString, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        ParsePosition pos = new ParsePosition(0);
        return sdf.parse(dateString, pos);
    }

    public static String formatDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return sdf.format(cal.getTime());
    }

    public static String dueDate(String startDate, int day, String format) {
        if (startDate == null)
            return null;

        Date d1 = stringToDate(startDate, format);

        // create a GregorianCalendar
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(d1);
        calendar.add(Calendar.DATE, day);

        return formatDate(calendar.getTime(), format);
    }

    public static String dueDate(String startDate, int day, String format, String outputFormat) {
        if (startDate == null)
            return null;

        Date d1 = stringToDate(startDate, format);

        // create a GregorianCalendar
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(d1);
        calendar.add(Calendar.DATE, day);

        return formatDate(calendar.getTime(), outputFormat);
    }

    public static Date getDateObject() {
        GregorianCalendar gc = new GregorianCalendar();
        SimpleTimeZone kstZone = new SimpleTimeZone(9 * 60 * 60 * 1000, "Asia/Seoul");
        gc.setTimeZone(kstZone);
        return gc.getTime();
    }

}