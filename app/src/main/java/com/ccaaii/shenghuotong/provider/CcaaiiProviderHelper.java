package com.ccaaii.shenghuotong.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.ccaaii.base.log.DevLog;
import com.ccaaii.base.log.LogLevel;
import com.ccaaii.base.log.MarketLog;
import com.ccaaii.shenghuotong.provider.CcaaiiDataStore.CurrentUserTable;
import com.ccaaii.shenghuotong.provider.CcaaiiDataStore.CcaaiiColumns;

public class CcaaiiProviderHelper {

	private static final String TAG = "[CCAAII] CcaaiiProviderHelper";
	
	public static void setCurrentUserId(Context context, String user_id) {
    	String user_id_current = getCurrentUserId(context);
        if (!user_id_current.equals(user_id)) {
            if (LogLevel.MARKET) {
                MarketLog.i(TAG, "setCurrentUserId(): " + user_id);
            }
            updateSingleValue(context, CcaaiiProvider.USER_ID_CURRENT, CurrentUserTable.USER_ID, String.valueOf(user_id), null);
        }
    }
	
	public static String getCurrentUserId(Context context) {
        String id = CurrentUserTable.USER_ID_NONE;
        
        Cursor c = null;
        try {
        	c = context.getContentResolver().query(UriHelper.getUri(CcaaiiProvider.USER_ID_CURRENT),
        			new String[]{CurrentUserTable.USER_ID}, null, null, null);
        	if (c != null && c.moveToNext()) {
        		id = c.getString(0);
        	}
        } finally {
        	try {
        		if (c != null && !c.isClosed()) {
        			c.close();
        		}
        	} catch (Exception e) {
        	}
        }
        
        return id;
    }
	
	public static int updateSingleValue(Context context, String uri_path, String column, String value, String where) {
        Uri uri = UriHelper.getUri(uri_path);
        ContentValues values = new ContentValues();
        values.put(column, value);
        return context.getContentResolver().update(uri, values, where, null);   //returns the number of updated rows
    }
	
	public static int updateSingleValueWithUserId(Context context, String uri_path, String column, String value, String where) {
        String user_id = getCurrentUserId(context);
        String where_where = CcaaiiColumns.USER_ID + '=' + user_id;

        if (!TextUtils.isEmpty(where)) {
            where_where += " AND " + where;
        }
        
        return updateSingleValue(context, uri_path, column, value, where_where);
    }
	
	public static void updateOrInsertSingleValueWithUserId(Context context, String uri_path, String column, String value) {
        String user_id = getCurrentUserId(context);
        String where = CcaaiiColumns.USER_ID + '=' + user_id;
        
        if (updateSingleValue(context, uri_path, column, value, where) <= 0) {
            insertSingleValueWithUserId(context, uri_path, column, value);
        }
        
    }
    public static void updateOrInsertSingleValueByUserId(Context context, long user_id, String uri_path, String column, String value) {
        String where = CcaaiiColumns.USER_ID + '=' + user_id;
        if (updateSingleValue(context, uri_path, column, value, where) <= 0) {
            insertSingleValueWithUserId(context, uri_path, column, value);
        }
    }

    private static void insertSingleValueWithUserId(Context context, String uri_path, String column, String value) {
        String user_id = getCurrentUserId(context);
        ContentValues values = new ContentValues();
        values.put(CcaaiiColumns.USER_ID, user_id);
        values.put(column, value);
        context.getContentResolver().insert(UriHelper.getUri(uri_path), values);
    }
    
    public static String simpleQueryWithUserId(Context context, String uri_path, String column) {
        String user_id = getCurrentUserId(context);
        Uri uri = UriHelper.getUri(uri_path, user_id);
        return simpleQuery(context, uri, column, null);
    }

    public static String simpleQueryByUserId(Context context, String userId, String uri_path, String column) {
        Uri uri = UriHelper.getUri(uri_path, userId);
        return simpleQuery(context, uri, column, null);
    }
    
    public static String simpleQuery(Context context, String uri_path, String column ) {
        Uri uri = UriHelper.getUri(uri_path);
        return simpleQuery(context, uri, column, null);
    }

    private static String simpleQuery(Context context, Uri uri, String column, String selection) {
    	if (context == null) {
    		return "";    		
    	}
    	
        if (LogLevel.DEV) {
        	DevLog.d(TAG, "simpleQuery(" + uri + ", " + column + ") " + (selection == null ? "" : " selection: " + selection));
        }

        try{
        	Cursor cursor = context.getContentResolver().query(uri, new String[]{column}, selection, null, null);
            if (cursor == null) {
                if (LogLevel.DEV) {
                	DevLog.d(TAG, "simpleQuery(): null cursor received; return \"\"");
                }
                return "";
            }

            if (!cursor.moveToFirst()) {
                if (LogLevel.DEV) {
                    DevLog.d(TAG, "simpleQuery(): empty cursor received; return \"\", count: " + cursor.getCount());
                }
                cursor.close();
                return "";
            }

            String result = cursor.getString(0);
            if (result == null) {
                if (LogLevel.DEV){
                	DevLog.d(TAG, "simpleQuery(): cursor returned null; return \"\"");
                }
                result = "";
            }

            cursor.close();
            return result;
        } catch(Exception e){
        	if(LogLevel.DEV){
        		DevLog.e(TAG, "simpleQuery ERROR : " + e.toString());
        	}
        	return null;
        }
        
    }
    
    private static int simpleQueryWithUserIdInt(Context context, String uri_path, String column, int def_value ) {
        String result = simpleQueryWithUserId(context, uri_path, column );
        int value = TextUtils.isEmpty(result) ? def_value : Integer.valueOf(result);

        if (LogLevel.DEV){
        	DevLog.d(TAG, "column: " + column + " result:" + value);
        }
        
        return value;
    }
    
    public static String getLimitedOrderBy(int offset, int queryCount){
		return " LIMIT " + offset + "," + queryCount;
	}


}
