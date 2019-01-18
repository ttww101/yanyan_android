package com.ccaaii.shenghuotong.city;

import com.ccaaii.shenghuotong.CcaaiiApp;
import com.google.gson.reflect.TypeToken;
import com.ccaaii.shenghuotong.bean.City;
import com.ccaaii.shenghuotong.bean.HotCity;
import com.ccaaii.base.log.DevLog;
import com.ccaaii.base.log.LogLevel;
import com.ccaaii.base.log.MarketLog;
import com.ccaaii.base.utils.CharUtils;

import java.util.List;

/**
 */
public class CityUtils {

    private static final String TAG = "[CCAAII]CityUtils";

    public static List<City> getHotCityList(){
        if (LogLevel.DEV){
            DevLog.d(TAG, "getHotCityList...");
        }
        try {
            String cityInfoStr = CharUtils.getFromAssets(CcaaiiApp.getCcaaiiContext(), "hotcity.json");
            HotCity hotCity = CcaaiiApp.getGson().fromJson(cityInfoStr,  new TypeToken<HotCity>() {}.getType());
            return hotCity.hot_city;
        } catch (Exception ex){
            MarketLog.e(TAG, "Get hot city failed, ex : " + ex.getLocalizedMessage());
            return null;
        }

    }
}
