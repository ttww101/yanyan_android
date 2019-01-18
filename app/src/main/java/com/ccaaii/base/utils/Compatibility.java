package com.ccaaii.base.utils;

import java.lang.reflect.Field;

import android.media.AudioManager;

import com.ccaaii.base.log.DevLog;
import com.ccaaii.base.log.LogLevel;
import com.ccaaii.base.log.MarketLog;

public class Compatibility {
    private static final String TAG = "[CCAAII]Compatibility";
    public static final int HTC_OR_REGULAR  = 0x00;
    public static final int MOTOROLA        = 0x01;   
    public static final int SAMSUNG         = 0x02;
    public static final int LGE				= 0x03;
    public static final int SONY_ERR		= 0x04;

    private static Integer sdkVersion = null;
    private static Integer manufacterCode = null;
    
    public static boolean isCompatible(int apiLevel) {
        return android.os.Build.VERSION.SDK_INT >= apiLevel;
    }
    
    /**
     * @return one of the INT values : {HTC_OR_REGULAR, MOTOROLA, SAMSUNG, LGE}
     */
    public static synchronized int getManufacturerCode() {
        if(null == manufacterCode) {
            int result = HTC_OR_REGULAR;
			
			try {
			    Class<?> execClass = Class.forName("android.os.Build");
			    if (null != execClass) {
			        Field manufacturerF = execClass.getField("MANUFACTURER");
			
			        if (null != manufacturerF) {
			            String manuf = null;
			            manuf = (String) manufacturerF.get(null);
			            if(LogLevel.DEV) {
			                DevLog.e(TAG, " >" + android.os.Build.DEVICE + "< >" + manuf + "< ");
			            }
			
			            // Searching of manufacturer name without first letter to avoid cases like 2 ways of 
			            // manufacturer writing, like Motorola & motorola, Samsung & samsung
			            if(manuf != null) {
			                if (manuf.contains("otorola")) {
			                    result = MOTOROLA;
			                } else if(manuf.contains("amsung")) {  /* "SGH-I997" "GT-I9000" */
			                    result = SAMSUNG;                            
			                } else if(manuf.toLowerCase().contains("lge")) {
			                	result = LGE;
			                } else if(manuf.toLowerCase().contains("ericsson") || manuf.toLowerCase().contains("sony")) {
			                	result = SONY_ERR;
			                }
			            }
			        }
			    }
			} catch (Throwable t) {
			    if(LogLevel.MARKET) {
			        MarketLog.v(TAG, "getManufacturerCode ", t);
			    }
			}
			manufacterCode = result;
        }
        
        return manufacterCode;
    }
    
    public static boolean isMotorolaDroidXT907() {
		String model = "";
		if (getManufacturerCode() == Compatibility.MOTOROLA) {
			try {
				Class<?> execClass = Class.forName("android.os.Build");
				if (null != execClass) {
					Field modelF = execClass.getField("MODEL");
					if (null != modelF) {
						model = (String) modelF.get(null);
						if ("XT907".equalsIgnoreCase(model)) {
							return true;
						}
					}
				}
			} catch (Exception e) {
			}
		}
		return false;
	}
	public static boolean isHtcOne() {
		String model = "";
		if (getManufacturerCode() == Compatibility.HTC_OR_REGULAR) {
			try {
				Class<?> execClass = Class.forName("android.os.Build");
				if (null != execClass) {
					Field modelF = execClass.getField("MODEL");
					if (null != modelF) {
						model = (String) modelF.get(null);
						if (model!=null&& model.toUpperCase().contains("ONE")&&!model.toUpperCase().contains("X")) {
							return true;
						}
					}
				}
			} catch (Exception e) {
			}
		}
		return false;
	}
    
    public static synchronized int getApiLevel() {
        if (sdkVersion == null) { 
            if (android.os.Build.VERSION.SDK.equalsIgnoreCase("3")) {
			    sdkVersion = 3;
			} else {
			    try {
			        Field f = android.os.Build.VERSION.class.getDeclaredField("SDK_INT");			        
			        if(null != f) { 
			            sdkVersion = (Integer) f.get(null);
			        }
			    } catch (Throwable t) {
			    	if (LogLevel.MARKET) {
			    		MarketLog.e(TAG, "getApiLevel(), exception", t);
			    	}
			    	sdkVersion = 0;
			    }
			}
        }
        
        return sdkVersion;
    }
    
    public static int getInCallStream() {
        //Archos has no voice call capabilities(AudioManager.STREAM_VOICE_CALL is not implemented)
        if (android.os.Build.BRAND.equalsIgnoreCase("archos")) {
            return AudioManager.STREAM_MUSIC;
        }
        return AudioManager.STREAM_VOICE_CALL;
    }
    
    public static boolean isCompatibleWith(int sdkVersion) {
        return getApiLevel() >= sdkVersion;
    }
    
}
