package com.ccaaii.shenghuotong.weather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.ccaaii.shenghuotong.CcaaiiApp;
import com.ccaaii.shenghuotong.bean.City;
import com.ccaaii.shenghuotong.bean.Weather;
import com.ccaaii.shenghuotong.provider.CcaaiiDataStore;
import com.ccaaii.shenghuotong.provider.CcaaiiProvider;
import com.ccaaii.shenghuotong.provider.UriHelper;
import com.ccaaii.base.log.DevLog;
import com.ccaaii.base.log.LogLevel;

/**
 */
public class WeatherDAO {

    private static final String TAG = "[CCAAII]WeatherDAO";

    public static void saveWeather(Context context, City city, String weather, boolean isCurrent){
        if (LogLevel.DEV){
            DevLog.d(TAG, "saveWeahter");
        }

        if (city == null || TextUtils.isEmpty(city.id)){
            return;
        }

        if (LogLevel.DEV){
            DevLog.d(TAG, "saveWeahter, city = " + city.city + ", weather = " + weather);
        }
        try {

            Uri uri = UriHelper.getUri(CcaaiiProvider.WEATHER_TABLE);
            if (isCurrent){
                ContentValues valueInit = new ContentValues();
                valueInit.put(CcaaiiDataStore.WeatherTable.IS_CURRENT, 0);
                context.getContentResolver().update(uri, valueInit, null, null);
            }

            ContentValues value = new ContentValues();
            value.put(CcaaiiDataStore.WeatherTable.CITY_ID, city.id);
            value.put(CcaaiiDataStore.WeatherTable.CITY, city.city);
            value.put(CcaaiiDataStore.WeatherTable.PROVINCE, city.prov);
            value.put(CcaaiiDataStore.WeatherTable.WEATHER, TextUtils.isEmpty(weather) ? "" : weather);
            value.put(CcaaiiDataStore.WeatherTable.IS_CURRENT, isCurrent ? 1 : 0);
            value.put(CcaaiiDataStore.WeatherTable.UPDATE_TIME, System.currentTimeMillis());
            context.getContentResolver().insert(uri, value);
        } catch (Exception ex){

        }
    }

    public static Weather getCurrentWeather(Context context){
        Cursor cursor = null;
        try {
            Uri uri = UriHelper.getUri(CcaaiiProvider.WEATHER_TABLE);
            String where = CcaaiiDataStore.WeatherTable.IS_CURRENT + " = '1'";
            cursor = context.getContentResolver().query(uri, WeatherProjection.SUMMARY_PROJECTION, where, null, null);
            if (cursor == null || !cursor.moveToFirst()){
                return null;
            }

            String weatherStr = cursor.getString(WeatherProjection.WEATHER_INDEX);
            Weather weather = CcaaiiApp.getGson().fromJson(weatherStr,  new TypeToken<Weather>() {}.getType());
            if (weather == null){
                weather = new Weather();
                weather.weather = "";
            }
            weather.weather = weatherStr;
            weather.cityId = cursor.getString(WeatherProjection.CITY_ID_INDEX);
            weather.city = cursor.getString(WeatherProjection.CITY_INDEX);
            weather.province = cursor.getString(WeatherProjection.PROVINCE_INDEX);
            weather.updateTime = cursor.getLong(WeatherProjection.UPDATE_TIME_INDEX);

            return weather;
        } catch (Exception ex){
            return null;
        } finally {
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
                cursor = null;
            }
        }


    }
}

