/*===============================================
 *Copyright(c) 2014 POSCO/POSDATA
 *
 *@ProcessChain   : FEMS
 *@File           : DateUtil.java
 *@FileName       : DateUtil
 *
 *Change history
 *@LastModifier : JM
 *@수정 날짜; SCR_NO; 수정자; 수정내용
 * 2014-04-10; JM; Initial Version
 ===============================================*/

package com.poscoict.posledger.assets.util;

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

	public static String formatDateToString(String dateString, String format, String returnFormat) {
		return formatDate(stringToDate(dateString, format), returnFormat);
	}

	public static int daysDiff(String earlierDate, String laterDate, String format) {
		if (earlierDate == null || laterDate == null)
			return 0;

		Date d1 = stringToDate(earlierDate, format);
		Date d2 = stringToDate(laterDate, format);
		return (int) ((d2.getTime() - d1.getTime()) / (24 * 60 * 60 * 1000));
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

	public static String dueDatetime(String startDate, int addedTime, String format, String strField) {
		if (startDate == null)
			return null;

		Date d1 = stringToDate(startDate, format);
		// create a GregorianCalendar
		int field = 0;
		if (strField.equalsIgnoreCase("SECOND")) {
			field = Calendar.SECOND;
		} else if (strField.equalsIgnoreCase("MINUTE")) {
			field = Calendar.MINUTE;
		} else if (strField.equalsIgnoreCase("HOUR")) {
			field = Calendar.HOUR;
		} else if (strField.equalsIgnoreCase("DATE")) {
			field = Calendar.DATE;
		} else if (strField.equalsIgnoreCase("MONTH")) {
			field = Calendar.MONTH;
		}
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(d1);
		calendar.add(field, addedTime);

		return formatDate(calendar.getTime(), format);
	}

	public static String dueDatetime(String startDate, int addedTime, String format) {
		if (startDate == null)
			return null;

		Date d1 = stringToDate(startDate, format);

		long chgtime = d1.getTime() + (addedTime * (60 * 60 * 1000));

		d1.setTime(chgtime);

		return formatDate(d1, format);
	}

	public static long getTimeInMillis() {
		GregorianCalendar gc = new GregorianCalendar();
		SimpleTimeZone kstZone = new SimpleTimeZone(9 * 60 * 60 * 1000, "Asia/Seoul");
		gc.setTimeZone(kstZone);
		return gc.getTimeInMillis();
	}

	public static Date getDateObject() {
		GregorianCalendar gc = new GregorianCalendar();
		SimpleTimeZone kstZone = new SimpleTimeZone(9 * 60 * 60 * 1000, "Asia/Seoul");
		gc.setTimeZone(kstZone);
		return gc.getTime();
	}

	public static String getDateString(String format) {
		GregorianCalendar gc = new GregorianCalendar();
		SimpleTimeZone kstZone = new SimpleTimeZone(9 * 60 * 60 * 1000, "Asia/Seoul");
		gc.setTimeZone(kstZone);

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		cal.setTime(gc.getTime());
		return sdf.format(cal.getTime());
	}

	public static String getLastDateString(String dateString, String format) {
		if (dateString == null) {
			return null;
		}
		Date d1 = stringToDate(dateString, format);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d1);
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
		return formatDate(cal.getTime(), format);
	}

	public static String getLastDateString(String dateString, String format, String returnFormat) {
		if (dateString == null) {
			return null;
		}
		Date d1 = stringToDate(dateString, format);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d1);
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
		return formatDate(cal.getTime(), returnFormat);
	}

	public static long getFromToDay(String fromDate, String toDate) {
		if (fromDate == null || toDate == null)
			return 0;

		Date d1 = stringToDate(fromDate, "yyyyMMdd");
		Date d2 = stringToDate(toDate, "yyyyMMdd");

		return (d2.getTime() - d1.getTime()) / (24 * 60 * 60 * 1000);
	}
}
