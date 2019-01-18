package com.ccaaii.base.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

public class CharUtils {

	public static String isoToUtf8(String str){
		try{
			return new String(str.getBytes(HTTP.ISO_8859_1), HTTP.UTF_8);
		} catch (Exception e){
			e.printStackTrace();
			return str;
		}
	}
	
	public static String utf8ToIso(String str){
		try{
			return new String(str.getBytes(HTTP.UTF_8), HTTP.ISO_8859_1);
		} catch (Exception e){
			e.printStackTrace();
			return str;
		}
	}
	
	public static String toBASE64Str(String str){
		return new String(Base64.encode(str.getBytes(), Base64.NO_WRAP));
	}
	
	public static String toNormalStrFromBase64(String str){
		return new String(Base64.decode(str.getBytes(), Base64.NO_WRAP));
	}
	
	public static boolean isNumeric(String str) {
	    Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
	}

	public static boolean numberMatch(String string, String s) {
		// TODO Auto-generated method stub
		if (null == string) return false;
		String dealStr = string.replace("-", "");
		dealStr = dealStr.replace(" ", "");
		if (dealStr.contains(s)) 
			return true;
		return false;
	}

	public static boolean alphaMatch(String name, String spell_all, String spell_first, String search){
		if((TextUtils.isEmpty(name) && TextUtils.isEmpty(spell_all)) || TextUtils.isEmpty(spell_first)){
			return false;
		}
		if(name.toLowerCase().contains(search) || name.toLowerCase().replaceAll(" ", "").contains(search)){
			return true;
		} 
		if(spell_all.startsWith(search) || spell_first.startsWith(search)){
			return true;
		}
		return false;
	}
	
	public static boolean t9StartMatch(String t9_all, String t9_first, String search){
		if(TextUtils.isEmpty(t9_all) || TextUtils.isEmpty(t9_first) || TextUtils.isEmpty(search)){
			return false;
		}
		if(t9_all.startsWith(search)){
			return true;
		} else if(t9_first.startsWith(search)){
			return true;
		}
		return false;
	}
	
	public static boolean t9ContainMatch(String t9_all, String t9_first, String search){
		if(TextUtils.isEmpty(t9_all) || TextUtils.isEmpty(t9_first) || TextUtils.isEmpty(search)){
			return false;
		}
		if(t9_all.contains(search)){
			return true;
		} else if(t9_first.contains(search)){
			return true;
		}
		return false;
	}
	
	public static boolean numberStartMatch(String contact, String search){
		if(TextUtils.isEmpty(contact) || TextUtils.isEmpty(search)){
			return false;
		}
		if(contact.startsWith(search)){
			return true;
		}
		return false;
	}
	
	public static boolean numberContainMatch(String contact, String search){
		if(TextUtils.isEmpty(contact) || TextUtils.isEmpty(search)){
			return false;
		}
		if(contact.contains(search)){
			return true;
		}
		return false;
	}
	
	public static String getPDFUrl(String str){
		try {
			if(str.indexOf(" ") < 0 && str.toLowerCase().endsWith(".pdf")){
				return str;
			}
			String[] strs = str.split(" ");
			for(int i = 0; i < strs.length; i ++){
				if(strs[i].toLowerCase().endsWith(".pdf")){
					return strs[i];
				}
			}
			
			return null;
		} catch (Exception e){
			
			return null;
		}
		
	}

	public static String inputStream2String(InputStream is){
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null){
				buffer.append(line);
			}
			return buffer.toString();
		} catch (IOException ex){
			return null;
		}

	}

	public static String getFromRaw(Context context, int rawId){
		try {
			InputStreamReader inputReader = new InputStreamReader( context.getResources().openRawResource(rawId));
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line="";
			String Result="";
			while((line = bufReader.readLine()) != null)
				Result += line;
			return Result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getFromAssets(Context context, String fileName){
		try {
			InputStreamReader inputReader = new InputStreamReader( context.getResources().getAssets().open(fileName) );
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line="";
			String Result="";
			while((line = bufReader.readLine()) != null)
				Result += line;
			return Result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getFromAssetsWithLine(Context context, String fileName){
		try {
			InputStreamReader inputReader = new InputStreamReader( context.getResources().getAssets().open(fileName) );
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line="";
			String Result="";
			while((line = bufReader.readLine()) != null)
				Result += line + "\n";
			return Result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
