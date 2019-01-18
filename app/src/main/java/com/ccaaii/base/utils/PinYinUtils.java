package com.ccaaii.base.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

public class PinYinUtils {

	public static String getPinYin(String src){
		return getPinYin(src, false);
	}
	
	public static String getPinYin(String src, boolean needSpace) {
		if(src == null){
    		return "";
    	}
		if(!needSpace){
			src = src.replaceAll(" ", "");
		}
        char[] t1 = null;
        t1 = src.toCharArray();
        String[] t2 = new String[t1.length];
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4 = "";
        int t0 = t1 != null ? t1.length : 0;
        try {
            for (int i = 0; i < t0; i++) {
                if (Character.toString(t1[i]) != null && Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
                    t4 += t2[0];
                } else {
                    t4 += Character.toString(t1[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t4.toLowerCase();
    }
	
	/**
     * get first char
     *
     * @param str
     * @return
     */
	public static String getPinYinHeadChar(String str){
		return getPinYinHeadChar(str, false);
	}
	
    public static String getPinYinHeadChar(String str, boolean needSpace) {
    	if(str == null){
    		return "";
    	}
    	String convert = "";
    	if(!needSpace){
    		str = str.replaceAll(" ", "");
    	}
        boolean isFirst = true;
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
                isFirst = false;
            } else {
            	if(needSpace){
            		if(isFirst){
                		convert += word;
                		isFirst = false;
                	}
                	if(" ".equals(word + "")){
                		convert += word;
                		isFirst = true;
                	}
            	} else {
            		convert += word;
            	}
            	
            }
        }
        return convert.toLowerCase();
    }
 
    /**
     * to ascii
     *
     * @param cnStr
     * @return
     */
    public static String getCnASCII(String cnStr) {
        StringBuffer strBuf = new StringBuffer();
        byte[] bGBK = cnStr.getBytes();
        for (int i = 0; i < bGBK.length; i++) {
            strBuf.append(Integer.toHexString(bGBK[i] & 0xff));
        }
        return strBuf.toString();
    }
    
    public static String getPinYinArrayStr(String src) {
		if(src == null){
    		return "";
    	}
        char[] t1 = null;
        t1 = src.toCharArray();
        String[] t2 = new String[t1.length];
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4 = "";
        int t0 = t1 != null ? t1.length : 0;
        try {
            for (int i = 0; i < t0; i++) {
            	String charStr = Character.toString(t1[i]);
                if (charStr != null && charStr.matches("[\\u4E00-\\u9FA5]+")) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
                    t4 += t2[0] + "|";
                } else {
                    t4 += Character.toString(t1[i]) + "|";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t4.toLowerCase();
    }
}
