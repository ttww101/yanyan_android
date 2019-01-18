package com.ccaaii.shenghuotong.provider;

import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;

import com.ccaaii.base.log.DevLog;
import com.ccaaii.base.log.LogLevel;

public class UriHelper {

    private static String TAG = "[CCAAII]UriHelper";

    public static Uri getUri(String path) {
        if (LogLevel.DEV){
        	DevLog.d(TAG, "getUri(" + path + ')');
        }
        Uri uri = prepare(path).build();
        
        if (LogLevel.DEV){
        	DevLog.d(TAG, "uri: " + uri);
        }
        return uri;
    }
    
    public static Uri getUri(String path, String user_id) {
        if (LogLevel.DEV){
        	DevLog.d(TAG, "getUri(" + path + ", " + user_id + ')');
        }
        
        Uri uri = prepare(path, user_id).build();
        
        if (LogLevel.DEV){
        	DevLog.d(TAG, "uri: " + uri);
        }

        return uri;
    }
    
    public static Uri getUri(String path, String user_id, long item_id) {
        if (LogLevel.DEV){
        	DevLog.d(TAG, "getUri(" + path + ", " + user_id + ", " + item_id + ')');
        }
        
        Uri uri = prepare(path, user_id).appendPath(String.valueOf(item_id)).build();

        if (LogLevel.DEV){
        	DevLog.d(TAG, "uri: " + uri);
        }

        return uri;
    }
    
    static Uri removeQuery(Uri uri) {
        if (LogLevel.DEV)
            DevLog.d(TAG, "removeQuery(" + uri + ")");

        Uri newUri = uri.buildUpon().query("").build();

        if (LogLevel.DEV)
            DevLog.d(TAG, "new uri: " + newUri);
        
        return newUri;
    }
    
    private static Builder prepare(String path) {
        return new Builder().scheme("content")
                                .authority(CcaaiiProvider.AUTHORITY)
                                .path(path)
                                .query("")                                          
                                .fragment("");  
                                                
    }
    
    private static Builder prepare(String path, String user_id) {
    	Builder builder = prepare(path);
    	if(!TextUtils.isEmpty(user_id)) {
    		builder.appendQueryParameter(CcaaiiDataStore.CcaaiiColumns.USER_ID, user_id);
    	}
    	return builder;
    }
}
