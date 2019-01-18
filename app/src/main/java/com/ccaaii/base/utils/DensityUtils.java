package com.ccaaii.base.utils;

import com.ccaaii.shenghuotong.CcaaiiApp;

public class DensityUtils {
	
	public static int dp_px(int dpValue) {
        final float scale = CcaaiiApp.getCcaaiiContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);  
    } 
	
	public static int px_dp(int pxValue) {
        final float scale = CcaaiiApp.getCcaaiiContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);  
    } 

}
