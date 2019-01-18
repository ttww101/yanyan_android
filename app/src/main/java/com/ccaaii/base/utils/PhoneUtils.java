package com.ccaaii.base.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneUtils {

	public static boolean isMobilePhone(String user){
		String str = "^(1[0-9]{10})|(\\d{10})|(\\+1[0-9]{10})|(861[0-9]{10})|(\\d{11})|(\\+861[0-9]{10})$";
		
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(user);

		return m.matches();
	}
	
}
