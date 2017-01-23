package com.liyuncong.application.commontools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static final String YYYY_MM_DD_HH_MM_SS_ONE = "yyyy-MM-dd HH:mm:ss";
	
	private DateUtil() {
	}
	
	public static Date toDate(String date, String format) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.parse(date);
	}
	
	public static String toString(Date date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}
	
	public static Date monthAgo(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -1);    //得到前一个月 
		return calendar.getTime();
	}
	
	public static boolean date1AfterDate2(Date date1, Date date2) {
		long long1 = date1.getTime();
		long long2 = date2.getTime();
		return long1 > long2;
	}
	
	public static long date1MinusDate2(Date date1, Date date2) {
		long long1 = date1.getTime();
		long long2 = date2.getTime();
		return long1 - long2;
	}
}
