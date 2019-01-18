/**
 * Copyright (C) 2013, Easiio, Inc.
 * All Rights Reserved.
 */
package com.ccaaii.base.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.SystemClock;
import android.text.format.Time;

import com.ccaaii.shenghuotong.R;

public class DateTimeUtils {
	
    private static String TAG = "[CCAAII] DateUtils";
    private static String TIME_FORMAT = "h:mm a";
    private static String DATE_FORMAT = "yyyy-MM-dd";
    private static String DATE_TIME_FORMAT = "yyyy-MM-dd h:mm a";
    
    public static String getDateTimeLabel(long time) {
    	SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT);
        return formatter.format(new Date(time));
    }

    public static String getUTCandRelativeDateFromElapsedTime(long elapsedTime) {
        try {
        	SimpleDateFormat formatter = new SimpleDateFormat("dd/HH:mm:ss");
            int flags = android.text.format.DateUtils.FORMAT_ABBREV_RELATIVE;
            long curTime = System.currentTimeMillis();
            long time = curTime - (SystemClock.elapsedRealtime() - elapsedTime);
            StringBuffer sb = new StringBuffer();
            sb.append(formatter.format(new Date(time)));
            if (time <= curTime) {
                sb.append('{');
                sb.append(android.text.format.DateUtils.getRelativeTimeSpanString(time, curTime, android.text.format.DateUtils.MINUTE_IN_MILLIS,
                        flags).toString());
                sb.append('}');
            }
            return sb.toString();
        } catch (Throwable th) {
            return "TIME_ERROR";
        }
    }
    
    public static String getRelativeDateLabel(long time) {
		long curTime = System.currentTimeMillis();
		long startTimeOfToday = getStartTimeOfDay(curTime);
		
		Date happenedDay = new Date(time);
		
		if (time > curTime) {
			return getDateLabel(happenedDay);
		} else if (time > startTimeOfToday) {
			return getTimeLabel(time);
		} else{
			return getDateLabel(happenedDay);
		} 
		
	}
    
    private static long getStartTimeOfDay(long timeInLong){
		Time time = new Time(); 
		time.set(timeInLong);
		
		Time dayStartTime = new Time(); 
		dayStartTime.year = time.year;
		dayStartTime.month = time.month;
		dayStartTime.monthDay = time.monthDay;
		
		return dayStartTime.toMillis(true);
	}
    
    public static String getDateLabel(Date date) {
    	SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
    }
    
    public static String getTimeLabel(long date) {
    	SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
        return sdf.format(date);
    }
    
    private static final String PARSE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String getCurrentTimeStr(){
    	SimpleDateFormat dateFormat = new SimpleDateFormat(PARSE_DATE_FORMAT);
    	Date d = new Date(System.currentTimeMillis());
    	return dateFormat.format(d);
    }

    public static String getCurrentDateStr(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Date d = new Date(System.currentTimeMillis());
        return dateFormat.format(d);
    }
    
    public static String getFormatTimeStr(long time){
    	SimpleDateFormat dateFormat = new SimpleDateFormat(PARSE_DATE_FORMAT);
    	Date d = new Date(time);
    	return dateFormat.format(d);
    }
    
    public static long getTimeByStr(String str){
    	SimpleDateFormat sdf= new SimpleDateFormat(PARSE_DATE_FORMAT);
    	Date date;
		try {
			date = sdf.parse(str);
			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return System.currentTimeMillis();
		}
    	
    }

    public static String converToString(Date date)
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        return df.format(date);
    }

    public static Date converToDate(String strDate) throws Exception
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.parse(strDate);
    }

    public static int getWeekdayByStrDate(String strDate){
        try {
            Date date = converToDate(strDate);
            return dateToWeekStringId(date);
        } catch (Exception ex){
            return WEEK[0];
        }
    }

    public static final int WEEKDAYS = 7;

    public static int[] WEEK = {
            R.string.weekday_0,
            R.string.weekday_1,
            R.string.weekday_2,
            R.string.weekday_3,
            R.string.weekday_4,
            R.string.weekday_5,
            R.string.weekday_6
    };

    public static int dateToWeekStringId(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayIndex < 1 || dayIndex > WEEKDAYS) {
            return WEEK[0];
        }

        return WEEK[dayIndex - 1];
    }


}
