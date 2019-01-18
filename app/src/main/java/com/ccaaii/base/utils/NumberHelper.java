package com.ccaaii.base.utils;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class NumberHelper {

	public static String LeftPad_Tow_Zero(int str) {
		java.text.DecimalFormat format = new java.text.DecimalFormat("00");
		return format.format(str);
	}
	
	public static String LeftPad_Tow_Zero(long str) {
		java.text.DecimalFormat format = new java.text.DecimalFormat("00");
		return format.format(str);
	}
	
	public static String parseTime(long second) {
		long m = second / 60;
		long s = second % 60;
		return LeftPad_Tow_Zero(m) + ":" + LeftPad_Tow_Zero(s);
	}

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	public static String formatNumber(String str){
		return str.replaceAll("\\D+", "");
	}
	
	public static String formatPhoneNumber(String str){
		return str.replaceAll("[\\s\\+\\-\\(\\)]", "");
	}

	public static double formatDoubleNumber(double f, int point){
		BigDecimal b = new BigDecimal(f);
		return b.setScale(point, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

}
