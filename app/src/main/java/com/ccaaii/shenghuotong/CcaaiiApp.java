package com.ccaaii.shenghuotong;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ccaaii.shenghuotong.bean.InitBean;
import com.flurry.android.FlurryAgent;
import com.flurry.android.FlurryAgentListener;
import com.google.gson.Gson;
import com.ccaaii.base.log.LogLevel;
import com.ccaaii.base.log.MarketLog;
import com.ccaaii.base.utils.LogcatFileManager;
import com.google.gson.reflect.TypeToken;

import cn.jpush.android.api.JPushInterface;

/**
 */
public class CcaaiiApp extends Application{

    private static final String TAG = "[CCAAII]CcaaiiApp";

    private static Context sApplicationContext = null;

    public static String mProvince;
    public static String mCity;
    public static String mCountry;
    public static String mAddr;

    public static void setCity(String city){
        mCity = city;
        SharedPreferences setting = getCcaaiiContext().getSharedPreferences(CcaaiiConstants.CCAAII_SHARED_PREFERENCES_CONSTANTS, 0);
    }

    public static String getCity(){
        return mCity;
    }

    public static void setProvince(String province){
        mProvince = province;
    }

    public static String getProvince(){
        return mProvince;
    }

    public static void setCountry(String country){
        mCountry = country;
    }

    public static String getCountry(){
        return mCountry;
    }

    private static Gson gson;
    public static Gson getGson() {
        if(gson==null){
            gson=new Gson();
        }
        return gson;
    }

    private static RequestQueue mRequestQueue;

    public static RequestQueue getQueue(){
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(sApplicationContext);
        }
        return mRequestQueue;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplicationContext = getApplicationContext();
        LogcatFileManager.getInstance().start();
        mRequestQueue = Volley.newRequestQueue(sApplicationContext);

        // 极光推送的初始化代码
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
        JPushInterface.setAlias(this,1000, BuildConfig.FLAVOR);

        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .withListener(new FlurryAgentListener() {
                    @Override
                    public void onSessionStarted() {
                        MarketLog.w(TAG, "Flurry agent on session started.");
                    }
                })
                .build(this, getString(R.string.flurry_key));

    }

    @Override
    public void onLowMemory(){
        super.onLowMemory();
        MarketLog.w(TAG, "onLowMemory");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        if (LogLevel.MARKET) {
            MarketLog.w(TAG, "onTerminate ");
        }

        sApplicationContext = null;
    }

    public static Context getCcaaiiContext(){
        return sApplicationContext;
    }


    public static String getAppChannel(){
        try {
            ApplicationInfo appInfo = getCcaaiiContext().getPackageManager()
                    .getApplicationInfo(getCcaaiiContext().getPackageName(),
                            PackageManager.GET_META_DATA);
            String channel = appInfo.metaData.getString("BaiduMobAd_CHANNEL");
            if (!TextUtils.isEmpty(channel)){
                return channel;
            } else {
                return "Default";
            }
        } catch (Exception ex){
            return "Default";
        }

    }

    public static void saveInitData(String initDataStr){
        SharedPreferences setting = getCcaaiiContext().getSharedPreferences(CcaaiiConstants.OOAAIIPP_SHARED_PREFERENCES_CONSTANTS, 0);
        setting.edit().putString(CcaaiiConstants.INIT_DATA, initDataStr).commit();
    }

    public static InitBean getInitBean(){
        SharedPreferences setting = getCcaaiiContext().getSharedPreferences(CcaaiiConstants.OOAAIIPP_SHARED_PREFERENCES_CONSTANTS, 0);
        String infoStr = setting.getString(CcaaiiConstants.INIT_DATA, null);
        if (!TextUtils.isEmpty(infoStr)){
            InitBean bean = getGson().fromJson(infoStr, new TypeToken<InitBean>(){}.getType());
            return bean;
        }
        return null;
    }
}
