package com.ccaaii.shenghuotong.document;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.ccaaii.base.log.MarketLog;
import com.ccaaii.shenghuotong.bean.DocumentBean;
import com.ccaaii.shenghuotong.provider.CcaaiiDataStore;
import com.ccaaii.shenghuotong.provider.CcaaiiProvider;
import com.ccaaii.shenghuotong.provider.UriHelper;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.category;

/**
 */
public class DocumentDAO {

    private static final String TAG = "[CCAAII]DocumentDAO";

    /***
     * Save document list
     *
     */
    public static void saveDocumentList(Context context, List<DocumentBean> list){
        MarketLog.i(TAG, "saveDocumentList");
        clearDocument(context);
        try {
            if (list == null || list.size() <= 0){
                MarketLog.w(TAG, "saveDocumentList failed, list is empty.");
                return;
            }
            Uri uri = UriHelper.getUri(CcaaiiProvider.DOCUMENT_TABLE);
            ContentValues[] values = new ContentValues[list.size()];
            for (int i = 0; i < list.size(); i ++){
                values[i] = new ContentValues();
                DocumentBean document = list.get(i);
                values[i].put(CcaaiiDataStore.DocumentTable.OBJECT_ID, document.getId());
                values[i].put(CcaaiiDataStore.DocumentTable.NAME, document.getName());
                values[i].put(CcaaiiDataStore.DocumentTable.IS_HOT, document.isIsHot() ? 1 : 0);
                values[i].put(CcaaiiDataStore.DocumentTable.IS_NEW, document.isIsNew() ? 1 : 0);
                values[i].put(CcaaiiDataStore.DocumentTable.DESCRIPTION, document.getDescription());
                values[i].put(CcaaiiDataStore.DocumentTable.CATEGORY, document.getCategory());
                values[i].put(CcaaiiDataStore.DocumentTable.PLAY_COUNT, document.getPlaycount());
                values[i].put(CcaaiiDataStore.DocumentTable.CREATE_TIME, System.currentTimeMillis());

            }

            int insertCount = context.getContentResolver().bulkInsert(uri, values);
            MarketLog.w(TAG, "saveDocumentList insertCount = " + insertCount);
        } catch (Exception ex){
            MarketLog.e(TAG, "saveDocumentList failed ex: " + ex.getLocalizedMessage().toString());
        }
    }


    public static List<DocumentBean> getDocumentList(Context context, int category){
        Cursor cursor = null;
        try {
            List<DocumentBean> list = new ArrayList<DocumentBean>();

            Uri uri = UriHelper.getUri(CcaaiiProvider.DOCUMENT_TABLE);
            String where = CcaaiiDataStore.DocumentTable.CATEGORY + " = '" + category + "'";
            cursor = context.getContentResolver().query(uri, DocumentProjection.SUMMARY_PROJECTION, where, null, DocumentProjection.SORT_ORDER);
            if (cursor == null || !cursor.moveToFirst()){
                return null;
            }

            while(!cursor.isAfterLast()){
                DocumentBean document = new DocumentBean();
                document.setId(cursor.getString(DocumentProjection.OBJECT_ID_INDEX));
                document.setName(cursor.getString(DocumentProjection.NAME_INDEX));
                document.setIsHot(cursor.getInt(DocumentProjection.IS_HOT_INDEX) == 1);
                document.setIsNew(cursor.getInt(DocumentProjection.IS_NEW_INDEX) == 1);
                document.setDescription(cursor.getString(DocumentProjection.DESCRIPTION_INDEX));
                document.setCategory(cursor.getInt(DocumentProjection.CATEGORY_INDEX));
                document.setPlaycount(cursor.getInt(DocumentProjection.PLAY_COUNT_INDEX));
                document.setCreaetTime(cursor.getLong(DocumentProjection.CREATE_TIME_INDEX));
                list.add(document);
                cursor.moveToNext();
            }

            return list;
        } catch (Exception ex){
            MarketLog.e(TAG, "getDocumentList failed ex: " + ex.getLocalizedMessage().toString());
            return null;
        } finally {
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
                cursor = null;
            }
        }
    }

    public static List<DocumentBean> getHotDocumentList(Context context){
        Cursor cursor = null;
        try {
            List<DocumentBean> list = new ArrayList<DocumentBean>();

            Uri uri = UriHelper.getUri(CcaaiiProvider.DOCUMENT_TABLE);
            String where = CcaaiiDataStore.DocumentTable.IS_HOT + " = '1'";
            cursor = context.getContentResolver().query(uri, DocumentProjection.SUMMARY_PROJECTION, where, null, DocumentProjection.SORT_ORDER);
            if (cursor == null || !cursor.moveToFirst()){
                return null;
            }

            while(!cursor.isAfterLast()){
                DocumentBean document = new DocumentBean();
                document.setId(cursor.getString(DocumentProjection.OBJECT_ID_INDEX));
                document.setName(cursor.getString(DocumentProjection.NAME_INDEX));
                document.setIsHot(cursor.getInt(DocumentProjection.IS_HOT_INDEX) == 1);
                document.setIsNew(cursor.getInt(DocumentProjection.IS_NEW_INDEX) == 1);
                document.setDescription(cursor.getString(DocumentProjection.DESCRIPTION_INDEX));
                document.setCategory(cursor.getInt(DocumentProjection.CATEGORY_INDEX));
                document.setPlaycount(cursor.getInt(DocumentProjection.PLAY_COUNT_INDEX));
                document.setCreaetTime(cursor.getLong(DocumentProjection.CREATE_TIME_INDEX));
                list.add(document);
                cursor.moveToNext();
            }

            return list;
        } catch (Exception ex){
            MarketLog.e(TAG, "getHotDocumentList failed ex: " + ex.getLocalizedMessage().toString());
            return null;
        } finally {
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
                cursor = null;
            }
        }
    }

    public static List<DocumentBean> getNewDocumentList(Context context){
        Cursor cursor = null;
        try {
            List<DocumentBean> list = new ArrayList<DocumentBean>();

            Uri uri = UriHelper.getUri(CcaaiiProvider.DOCUMENT_TABLE);
            String where = CcaaiiDataStore.DocumentTable.IS_NEW + " = '1'";
            cursor = context.getContentResolver().query(uri, DocumentProjection.SUMMARY_PROJECTION, where, null, DocumentProjection.SORT_ORDER);
            if (cursor == null || !cursor.moveToFirst()){
                return null;
            }

            while(!cursor.isAfterLast()){
                DocumentBean document = new DocumentBean();
                document.setId(cursor.getString(DocumentProjection.OBJECT_ID_INDEX));
                document.setName(cursor.getString(DocumentProjection.NAME_INDEX));
                document.setIsHot(cursor.getInt(DocumentProjection.IS_HOT_INDEX) == 1);
                document.setIsNew(cursor.getInt(DocumentProjection.IS_NEW_INDEX) == 1);
                document.setDescription(cursor.getString(DocumentProjection.DESCRIPTION_INDEX));
                document.setCategory(cursor.getInt(DocumentProjection.CATEGORY_INDEX));
                document.setPlaycount(cursor.getInt(DocumentProjection.PLAY_COUNT_INDEX));
                document.setCreaetTime(cursor.getLong(DocumentProjection.CREATE_TIME_INDEX));
                list.add(document);
                cursor.moveToNext();
            }

            return list;
        } catch (Exception ex){
            MarketLog.e(TAG, "getNewDocumentList failed ex: " + ex.getLocalizedMessage().toString());
            return null;
        } finally {
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
                cursor = null;
            }
        }
    }

    public static DocumentBean getDocument(Context context, String objectId){
        MarketLog.i(TAG, "getDocument object id = " + objectId);
        Cursor cursor = null;
        try {
            Uri uri = UriHelper.getUri(CcaaiiProvider.DOCUMENT_TABLE);
            String where = CcaaiiDataStore.DocumentTable.OBJECT_ID + " = '" + objectId + "'";
            cursor = context.getContentResolver().query(uri, DocumentProjection.SUMMARY_PROJECTION, where, null, DocumentProjection.SORT_ORDER);
            if (cursor == null || !cursor.moveToFirst()){
                return null;
            }

            return readDocumentCursor(cursor);

        } catch (Exception ex){
            MarketLog.e(TAG, "getDocument failed ex: " + ex.getLocalizedMessage().toString());
            return null;
        } finally {
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
                cursor = null;
            }
        }
    }

    public static DocumentBean readDocumentCursor(Cursor cursor){
        try {
            if (cursor == null || cursor.isClosed()){
                return null;
            }
            DocumentBean document = new DocumentBean();
            document.setId(cursor.getString(DocumentProjection.OBJECT_ID_INDEX));
            document.setName(cursor.getString(DocumentProjection.NAME_INDEX));
            document.setIsHot(cursor.getInt(DocumentProjection.IS_HOT_INDEX) == 1);
            document.setIsNew(cursor.getInt(DocumentProjection.IS_NEW_INDEX) == 1);
            document.setDescription(cursor.getString(DocumentProjection.DESCRIPTION_INDEX));
            document.setCategory(cursor.getInt(DocumentProjection.CATEGORY_INDEX));
            document.setPlaycount(cursor.getInt(DocumentProjection.PLAY_COUNT_INDEX));
            document.setCreaetTime(cursor.getLong(DocumentProjection.CREATE_TIME_INDEX));
            return document;
        } catch (Exception ex){
            return null;
        }
    }

    /**
     * Clear document
     *
     */
    public static void clearDocument(Context context){
        try {
            Uri uri = UriHelper.getUri(CcaaiiProvider.DOCUMENT_TABLE);
            int deleteCount = context.getContentResolver().delete(uri, null, null);
            MarketLog.w(TAG, "clearDocument deleteCount = " + deleteCount);
        } catch (Exception ex){
            MarketLog.e(TAG, "clearDocument failed ex: " + ex.getLocalizedMessage().toString());
        }
    }

}
