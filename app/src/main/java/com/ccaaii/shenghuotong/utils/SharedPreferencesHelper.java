package com.ccaaii.shenghuotong.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ccaaii.shenghuotong.bean.InitBean;


public class SharedPreferencesHelper {

    private static final String TAG=SharedPreferencesHelper.class.getSimpleName();

    public static void  saveTouzhuLink(Context context, InitBean initBean){
        if(initBean!=null&&context!=null){
            SharedPreferences init = context.getSharedPreferences(InitBean.TOUZHULINK, Context.MODE_PRIVATE);
            init.edit().putString(InitBean.TOUZHULINK, initBean.touzhulink).commit();
        }else{
            Log.e(TAG,"init bean or context is null in saveTouzhuLink()");
        }
    }

    public static String getTouzhuLink(Context context){
        if(context!=null){
            SharedPreferences init = context.getSharedPreferences(InitBean.TOUZHULINK, Context.MODE_PRIVATE);
            return init.getString(InitBean.TOUZHULINK,"");
        }else{
            Log.e(TAG,"init bean or context is null in getTouzhuLink()");
        }
        return "";
    }

    public static void  saveH5Page(Context context, InitBean initBean){
        if(initBean!=null&&context!=null){
            SharedPreferences init = context.getSharedPreferences(InitBean.H5PAGE, Context.MODE_PRIVATE);
            init.edit().putString(InitBean.H5PAGE, initBean.h5Page).commit();
        }else{
            Log.e(TAG,"init bean or context is null in saveH5Page()");
        }
    }

    public static String getH5page(Context context){
        if(context!=null){
            SharedPreferences init = context.getSharedPreferences(InitBean.H5PAGE, Context.MODE_PRIVATE);
            return init.getString(InitBean.H5PAGE,"");
        }else{
            Log.e(TAG,"init bean or context is null in getH5page()");
        }
        return "";
    }

    public static String getAndroidApkUrl(Context context){
        if(context!=null){
            SharedPreferences init = context.getSharedPreferences(InitBean.ANDROIDAPKURL, Context.MODE_PRIVATE);
            return init.getString(InitBean.ANDROIDAPKURL,"");
        }else{
            Log.e(TAG,"init bean or context is null in getH5page()");
        }
        return "";
    }

    public static void  saveAndroidApkUrl(Context context, InitBean initBean){
        if(initBean!=null&&context!=null){
            SharedPreferences init = context.getSharedPreferences(InitBean.ANDROIDAPKURL, Context.MODE_PRIVATE);
            init.edit().putString(InitBean.ANDROIDAPKURL, initBean.androidApkUrl).commit();
        }else{
            Log.e(TAG,"init bean or context is null in saveInitBean()");
        }
    }


}
