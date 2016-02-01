package com.liyuncong.application.commontools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	/**
	 * yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	public static String ymdhms(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat
				("yyyy-MM-dd HH:mm:ss");
		return formatter.format(date);
	}
	
	public static Date monthAgo(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -1);    //得到前一个月 
		Date monthAgo = calendar.getTime();
		return monthAgo;
	}
}
