package com.ccaaii.shenghuotong.document;

import com.ccaaii.shenghuotong.provider.CcaaiiDataStore;

/**
 */
public class DocumentProjection {

    public static final String SORT_ORDER = CcaaiiDataStore.DocumentTable.CREATE_TIME + " DESC ";

    public static final String[] SUMMARY_PROJECTION = new String[]{
            CcaaiiDataStore.DocumentTable._ID,
            CcaaiiDataStore.DocumentTable.OBJECT_ID,
            CcaaiiDataStore.DocumentTable.NAME,
            CcaaiiDataStore.DocumentTable.IS_HOT,
            CcaaiiDataStore.DocumentTable.IS_NEW,
            CcaaiiDataStore.DocumentTable.DESCRIPTION,
            CcaaiiDataStore.DocumentTable.CONTENT_FILE,
            CcaaiiDataStore.DocumentTable.CATEGORY,
            CcaaiiDataStore.DocumentTable.PLAY_COUNT,
            CcaaiiDataStore.DocumentTable.CREATE_TIME
    };

    public static final int ID_INDEX = 0;
    public static final int OBJECT_ID_INDEX = 1;
    public static final int NAME_INDEX = 2;
    public static final int IS_HOT_INDEX = 3;
    public static final int IS_NEW_INDEX = 4;
    public static final int DESCRIPTION_INDEX = 5;
    public static final int CONTENT_FILE_INDEX = 6;
    public static final int CATEGORY_INDEX = 7;
    public static final int PLAY_COUNT_INDEX = 8;
    public static final int CREATE_TIME_INDEX = 9;

}
