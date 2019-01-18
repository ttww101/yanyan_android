package com.ccaaii.shenghuotong.city;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.ccaaii.shenghuotong.bean.City;
import com.ccaaii.shenghuotong.bean.CityInfo;
import com.ccaaii.shenghuotong.provider.CcaaiiDataStore;
import com.ccaaii.shenghuotong.provider.CcaaiiProvider;
import com.ccaaii.shenghuotong.provider.UriHelper;
import com.ccaaii.base.log.MarketLog;
import com.ccaaii.base.utils.PinYinUtils;

/**
 */
public class CityDAO {

    private static final String TAG = "[CCAAII]CityDAO";

    public static void saveCity(Context context, CityInfo info){
        MarketLog.w(TAG, "saveCity");
        if (info == null || info.city_info == null || info.city_info.size() <= 0){
            return;
        }

        try {
            Uri uri = UriHelper.getUri(CcaaiiProvider.CITY_TABLE);
            context.getContentResolver().delete(uri, null, null);

            ContentValues values[] = new ContentValues[info.city_info.size()];

            for (int i = 0; i < info.city_info.size(); i ++){
                City city = info.city_info.get(i);
                values[i] = new ContentValues();
                values[i].put(CcaaiiDataStore.CityTable.CITY_ID, city.id);
                values[i].put(CcaaiiDataStore.CityTable.CITY, city.city);
                values[i].put(CcaaiiDataStore.CityTable.PROVINCE, city.prov);
                values[i].put(CcaaiiDataStore.CityTable.CITY_PY, PinYinUtils.getPinYin(city.city));
                values[i].put(CcaaiiDataStore.CityTable.CITY_PY_HEAD, PinYinUtils.getPinYinHeadChar(city.city));
            }

            int insearCount = context.getContentResolver().bulkInsert(uri, values);
            MarketLog.w(TAG, "Save city count = " + insearCount);
        } catch (Exception ex){

        }
    }

    public static City readCityByCursor(Cursor cursor){
        if (cursor == null || cursor.isClosed()){
            return null;
        }

        try {
            City city = new City();
            city.id = cursor.getString(CityProjection.CITY_ID_INDEX);
            city.city = cursor.getString(CityProjection.CITY_INDEX);
            city.prov = cursor.getString(CityProjection.PROVINCE_INDEX);
            return city;
        } catch (Exception ex){
            return null;
        }
    }

    public static City getCityByCityAndProv(Context context, String prov, String city){
        MarketLog.w(TAG, "getCityByName prov = " + prov + ", city = " + city);
        if (TextUtils.isEmpty(prov) || TextUtils.isEmpty(city)){
            return null;
        }

        if (city.contains("市")){
            city = city.substring(0, city.length() - 1);
        }

        if (prov.contains("省")){
            prov = prov.substring(0, prov.length() - 1);
        }

        Cursor cursor = null;
        try {
            Uri cityUri = UriHelper.getUri(CcaaiiProvider.CITY_TABLE);
            String where = CcaaiiDataStore.CityTable.CITY  + " = '" + city + "' AND " + CcaaiiDataStore.CityTable.PROVINCE + " = '" + prov + "'";
            cursor = context.getContentResolver().query(cityUri, CityProjection.SUMMARY_PROJECTION, where, null, null);
            if (cursor == null || !cursor.moveToFirst()){
                MarketLog.w(TAG, "No city prov found.");
                return getCityByCityName(context, city);
            } else {
                if (cursor.getCount() >= 1){
                    return readCityByCursor(cursor);
                } else {
                    return getCityByCityName(context, city);
                }
            }
        } catch (Exception ex){
            return null;
        } finally {
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
                cursor = null;
            }
        }
    }

    public static City getCityByCityName(Context context, String city){
        MarketLog.w(TAG, "getCityByCityName city = " + city);
        if (TextUtils.isEmpty(city)){
            return null;
        }

        Cursor cursor = null;
        try {
            Uri cityUri = UriHelper.getUri(CcaaiiProvider.CITY_TABLE);
            String where = CcaaiiDataStore.CityTable.CITY  + " = '" + city + "'";
            cursor = context.getContentResolver().query(cityUri, CityProjection.SUMMARY_PROJECTION, where, null, null);
            if (cursor == null || !cursor.moveToFirst()){
                MarketLog.w(TAG, "No city found.");
                return null;
            } else {
                return readCityByCursor(cursor);
            }
        } catch (Exception ex){
            return null;
        } finally {
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
                cursor = null;
            }
        }
    }

    public static boolean hasSavedCity(Context context){
        MarketLog.w(TAG, "hasSavedCity");
        Cursor cursor = null;
        try {
            Uri cityUri = UriHelper.getUri(CcaaiiProvider.CITY_TABLE);
            cursor = context.getContentResolver().query(cityUri, CityProjection.SUMMARY_PROJECTION, null, null, null);
            if (cursor == null || !cursor.moveToFirst()){
                MarketLog.w(TAG, "No city found.");
                return false;
            } else {
                return cursor.getCount() > 0;
            }
        } catch (Exception ex){
            return false;
        } finally {
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
                cursor = null;
            }
        }
    }
}
