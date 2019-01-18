package com.ccaaii.shenghuotong.city;

import com.ccaaii.shenghuotong.provider.CcaaiiDataStore;

/**
 */
public class CityProjection {

    public static final String QUERY_CITY_PROJECTION =
                    CcaaiiDataStore.CityTable.CITY + " LIKE ? || '%'" + " OR " +
                    CcaaiiDataStore.CityTable.CITY + " LIKE '%' || ' ' || ? || '%'" + " OR " +
                    CcaaiiDataStore.CityTable.CITY_PY + " LIKE ? || '%'" + " OR " +
                    CcaaiiDataStore.CityTable.CITY_PY + " LIKE '%' || ' ' || ? || '%'" + " OR " +
                    CcaaiiDataStore.CityTable.CITY_PY_HEAD + " LIKE ? || '%'" + " OR " +
                    CcaaiiDataStore.CityTable.CITY_PY_HEAD + " LIKE '%' || ' ' || ? || '%'";

    public static final String[] SUMMARY_PROJECTION = new String[]{
            CcaaiiDataStore.CityTable._ID,
            CcaaiiDataStore.CityTable.CITY_ID,
            CcaaiiDataStore.CityTable.CITY,
            CcaaiiDataStore.CityTable.PROVINCE,
            CcaaiiDataStore.CityTable.CITY_PY,
            CcaaiiDataStore.CityTable.CITY_PY_HEAD
    };

    public static final int ID_INDEX = 0;
    public static final int CITY_ID_INDEX = 1;
    public static final int CITY_INDEX = 2;
    public static final int PROVINCE_INDEX = 3;
    public static final int CITY_PY_INDEX = 4;
    public static final int CITY_PY_HEAD_INDEX = 5;

}
