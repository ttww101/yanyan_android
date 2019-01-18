package com.ccaaii.shenghuotong.weather;

import com.ccaaii.shenghuotong.provider.CcaaiiDataStore;

/**
 */
public class WeatherProjection {

    public static final String [] SUMMARY_PROJECTION = new String[]{
            CcaaiiDataStore.WeatherTable._ID,
            CcaaiiDataStore.WeatherTable.CITY_ID,
            CcaaiiDataStore.WeatherTable.CITY,
            CcaaiiDataStore.WeatherTable.PROVINCE,
            CcaaiiDataStore.WeatherTable.WEATHER,
            CcaaiiDataStore.WeatherTable.IS_CURRENT,
            CcaaiiDataStore.WeatherTable.UPDATE_TIME
    };

    public static final int ID_INDEX = 0;
    public static final int CITY_ID_INDEX = 1;
    public static final int CITY_INDEX = 2;
    public static final int PROVINCE_INDEX = 3;
    public static final int WEATHER_INDEX = 4;
    public static final int IS_CURRENT_INDEX = 5;
    public static final int UPDATE_TIME_INDEX = 6;

}
