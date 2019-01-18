package com.ccaaii.base.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Scanner;

import com.ccaaii.base.log.DevLog;
import com.ccaaii.base.log.LogLevel;
import com.ccaaii.base.log.MarketLog;
import com.ccaaii.shenghuotong.CcaaiiApp;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

public class DeviceUtils {

    private static final String TAG = "[CCAAII] DeviceUtils";
    
    private static final String CMD_CAT = "/system/bin/cat";
    public static final String SYS_FILE_MAX_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq";
    public static final String SYS_FILE_CURR_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq";

    private static final String NO_SIM_CARD = "No SIM";

    
    public static String getLine1Number(Context ctx) {
		return ((TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
	}

    
	public static int getDisplayOrientation(Context context) {
        int orientation = context.getResources().getConfiguration().orientation;
        if (LogLevel.DEV)
            DevLog.i(TAG, "getDisplayOrientation() #1: orientation = " + orientation);
        
        if (orientation == Configuration.ORIENTATION_UNDEFINED) {
            orientation = calcDisplayOrientation(context);
    
            if (LogLevel.DEV)
                DevLog.i(TAG, "getDisplayOrientation() #2: orientation = " + orientation);
        }
        return orientation;
    }

    public static int calcDisplayOrientation(Context context) {
        if (LogLevel.DEV)
            DevLog.i(TAG, "calcDisplayOrientation() #1");
    
        int orientation;
        DisplayMetrics disp_metrics = context.getResources().getDisplayMetrics();
        
        if (disp_metrics.heightPixels > disp_metrics.widthPixels)
            orientation = Configuration.ORIENTATION_PORTRAIT;
        else if (disp_metrics.heightPixels < disp_metrics.widthPixels)
            orientation = Configuration.ORIENTATION_LANDSCAPE;
        else
            orientation = Configuration.ORIENTATION_SQUARE;
    
        if (LogLevel.DEV)
            DevLog.i(TAG, "calcDisplayOrientation() #2: orientation = " + orientation);
        return orientation;
    }
    
    public static Integer getFrequencyKHz(String type) {
    	Integer result = null;
    	InputStream is = null;
    	String strCommandResult = null;
    	if(LogLevel.DEV) {
			DevLog.v(TAG, "getFrequency(), type : " + type);
		}
    	try {
    		final Process process = new ProcessBuilder(CMD_CAT, type).start();
    		is = process.getInputStream();
    		final StringBuilder sb = new StringBuilder();
    		
    		final Scanner sc = new Scanner(is);
    		while(sc.hasNextLine()) {
    			sb.append(sc.nextLine());
    		}
    		strCommandResult = sb.toString();	    		
    		result = Integer.parseInt(strCommandResult);
    	} catch (IOException ioEx) {
			if(LogLevel.DEV){
				DevLog.e(TAG, "getFrequency(), IOException ", ioEx);
			}
		} catch (NumberFormatException numEx) {
			if(LogLevel.DEV){
				DevLog.e(TAG, "getFrequency(), NumericEception, command : " + strCommandResult, numEx);
			}
			result = null;
		} finally {
			if(is != null) {
				try {
					is.close();
				} catch (IOException e) {
					if(LogLevel.DEV){
						DevLog.e(TAG, "getFrequency(), exception in finally");
					}
				}
				is = null;
			}
		}
		if(LogLevel.DEV) {
			DevLog.v(TAG, "getFrequency(), result : " + result);
		}
    	return result;
    }

    public static boolean isNecessaryUseTimeout() {
        return Compatibility.getApiLevel() <= 4;
    }
    
    public static boolean isHeadsetConnected() {
		boolean result = false;
		final AudioManager audioManager = (AudioManager) CcaaiiApp.getCcaaiiContext().getSystemService(Context.AUDIO_SERVICE);
		if (Compatibility.getApiLevel() >= 5) {
			try {
				Method isWidredHeadsetOnMethod = ReflectionHelper.getDeclaredMethod(audioManager.getClass(),"isWiredHeadsetOn");
				isWidredHeadsetOnMethod.setAccessible(true);
				Object o = isWidredHeadsetOnMethod.invoke(audioManager);
				result = (Boolean) o;
			} catch (Exception ex) {
				if (LogLevel.MARKET) {
					MarketLog.e(TAG, " isHeadsetConnected ", ex);
				}
				result = false;
			}
		}
		return result;
	}
    
	public static long getAvailableInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		
		return (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
	}
    
	public static long getAvailableExternalMemorySize() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			return (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
		} else {
			return -1;
		}
	}

	public static boolean isMotorolaX91AndHTCG1X() {
		try {
			String manuf = null, model = null;
			Class<?> execClass = Class.forName("android.os.Build");
			if (null != execClass) {
				Field manufacturerF = execClass.getField("MANUFACTURER");
				if (null != manufacturerF) {
					manuf = (String) manufacturerF.get(null);
				}
				Field modelF = execClass.getField("MODEL");
				if (null != modelF) {
					model = (String) modelF.get(null);
				}
			}
			if(TextUtils.isEmpty(manuf) || TextUtils.isEmpty(model)) { 
				return false;
			}
			
			if (0 == manuf.compareToIgnoreCase("Motorola") && ( model.contains("XT91") || model.contains("xt91")) ) {
				return true;
			} else if(0 == manuf.compareToIgnoreCase("HTC") && ( model.contains("HTC Desire HD A9191") || model.contains("HTC Incredible S") )) {
				return true;
			}
			
		} catch (Exception e) {
			if (LogLevel.MARKET) {
				MarketLog.e(TAG, " get deveice information error: " + e.toString());
			}
		}
		
		return false;
	}
	
	public static boolean isSamsungS3Device() {
		try {
			String manuf = null, model = null;
			Class<?> execClass = Class.forName("android.os.Build");
			if (null != execClass) {
				Field manufacturerF = execClass.getField("MANUFACTURER");

				if (null != manufacturerF) {
					manuf = (String) manufacturerF.get(null);
				}
				Field modelF = execClass.getField("MODEL");
				if (null != modelF) {
					model = (String) modelF.get(null);
				}
			}
			
			if (0 == manuf.compareToIgnoreCase("SAMSUNG") && 
					(model.contains("I9300") || model.contains("i9300") || model.contains("I9308") || model.contains("i9308") || 
					 model.contains("I939")  || model.contains("i939")  || model.contains("I535")  || model.contains("i535")  ||
					 model.contains("R530")  || model.contains("r530")  || model.contains("L710")  || model.contains("l710")) ) {
				return true;
			}
		} catch (Throwable th) {
			return false;
		}
		
		return false;
	}
	
	public static String getDeviceName() {
		String manuf = null, model = null;
		Class<?> execClass;
		try {
			execClass = Class.forName("android.os.Build");
			if (null != execClass) {
				Field manufacturerF = execClass.getField("MANUFACTURER");

				if (null != manufacturerF) {
					manuf = (String) manufacturerF.get(null);
				}
				Field modelF = execClass.getField("MODEL");
				if (null != modelF) {
					model = (String) modelF.get(null);
				}
				
				manuf = TextUtils.isEmpty(manuf) ? "" : manuf;
				model = TextUtils.isEmpty(model) ? "" : model;
				return manuf + " " + model;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String getAndroidLevel(){
		return String.valueOf(android.os.Build.VERSION.SDK_INT);
	}
	
	public static String getAndroidReleaseVersion(){
		return android.os.Build.VERSION.RELEASE;
	}
	
	public static String getAndroidSDK(){
		return android.os.Build.VERSION.SDK;
	}
	
	public static String getDeviceId(){
		Context context = CcaaiiApp.getCcaaiiContext();
		TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE); 
		String deviceId = TelephonyMgr.getDeviceId(); 
		
		WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE); 
		String wlanMac = wm.getConnectionInfo().getMacAddress();
		
		return deviceId + wlanMac;
	}
	
	public static boolean isChineseLangue() {  
	       Locale locale = Locale.getDefault();  
	       return locale.toString().equals(Locale.SIMPLIFIED_CHINESE.toString());
	}
	
	public static String getAppVersionName() {    
	    String versionName = "";    
	    try {    
	    	Context context = CcaaiiApp.getCcaaiiContext();
	        PackageManager pm = context.getPackageManager();    
	        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);    
	        versionName = pi.versionName;
	        if (versionName == null || versionName.length() <= 0) {    
	            return "";    
	        }    
	    } catch (Exception e) {    
	        MarketLog.e(TAG, "getAppVersionName Exception", e);    
	    }    
	    return versionName;    
	}   
	
	public static int getAppVersion() {    
	    try {    
	    	Context context = CcaaiiApp.getCcaaiiContext();
	        PackageManager pm = context.getPackageManager();    
	        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);    
	        return pi.versionCode;
	    } catch (Exception e) {    
	        MarketLog.e(TAG, "getAppVersion Exception", e);    
	    }    
	    return 0;    
	}   
	
	public static boolean checkCameraHardware(Context context) { 

	    if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){ 
	        return true; 
	    } else { 
	        return false; 

	    } 

	}
}
