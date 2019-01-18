package com.ccaaii.base.utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.ccaaii.base.log.LogLevel;
import com.ccaaii.base.log.MarketLog;

public class DecriptUtils {

	private static final String TAG = "[CCAAII]DecriptUtils";
	
	private static final String HASH_ALGORITHM_MD5 = "MD5";
    private static final String HASH_ALGORITHM_SHA1 = "SHA-1";
    
	public static String SHA1(String decript) {
		try {
			MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM_SHA1);
			digest.update(decript.getBytes());
			byte messageDigest[] = digest.digest();
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			if(LogLevel.MARKET){
				MarketLog.e(TAG, "NoSuchAlgorithmException SHA1 E : " + e.toString());
			}
		}
		return "";
	}
	
	public static String MD5(String input) {
		try {
			MessageDigest mdInst = MessageDigest.getInstance(HASH_ALGORITHM_MD5);
			mdInst.update(input.getBytes());
			byte[] md = mdInst.digest();
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < md.length; i++) {
				String shaHex = Integer.toHexString(md[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

}
