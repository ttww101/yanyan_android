package com.ccaaii.shenghuotong.provider;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.LinkedHashMap;

public class CcaaiiDataStore {
	
	private CcaaiiDataStore() {};

    public static final int DB_VERSION = 11;
    static final String DB_FILE = "ccaaiishenghuotong.db";
    
    public interface CcaaiiColumns {
        public static final String USER_ID = "UserId";
        public static final String DEFAULT_SORT_ORDER = "_ID ASC";
    }
    
    public static final class CurrentUserTable extends CcaaiiDbTable implements BaseColumns, CcaaiiColumns {

        private CurrentUserTable() {};
        private static final CurrentUserTable sInstance = new CurrentUserTable();
        public static CurrentUserTable getInstance() {
            return sInstance;
        }

        public static final String TABLE_NAME = "CurrentUserTab";
        
        public static final String USER_ID_NONE = "0";
        
        private static final String CREATE_TABLE_STMT =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
          + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
          + USER_ID + " TEXT "
          + ");";

        private static final String INIT_TABLE_STMT =
            "INSERT INTO " + TABLE_NAME
          + " (" 
          	+  USER_ID
          +  ") "
          + "VALUES (" 
          	+ USER_ID_NONE
          + ")";


        @Override
        String getName() {
            return TABLE_NAME;
        }
        
        @Override
        void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_STMT);
            db.execSQL(INIT_TABLE_STMT);
        }
    }

    public static final class CityTable extends CcaaiiDbTable implements BaseColumns, CcaaiiColumns {

        private CityTable() {};
        private static final CityTable sInstance = new CityTable();
        public static CityTable getInstance() {
            return sInstance;
        }

        public static final String TABLE_NAME 		= "CityTab";

        public static final String CITY_ID = "City_ID";
        public static final String CITY = "City";
        public static final String PROVINCE = "Province";
        public static final String CITY_PY = "City_PY";
        public static final String CITY_PY_HEAD = "City_PY_Head";


        private static final String CREATE_TABLE_STMT =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                        + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + USER_ID + " TEXT, "
                        + CITY_ID + " TEXT UNIQUE ON CONFLICT REPLACE, "
                        + CITY + " TEXT, "
                        + PROVINCE + " TEXT, "
                        + CITY_PY + " TEXT, "
                        + CITY_PY_HEAD + " TEXT "
                        + ");";

        @Override
        String getName() {
            return TABLE_NAME;
        }

        @Override
        void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_STMT);
        }
    }

    public static final class WeatherTable extends CcaaiiDbTable implements BaseColumns, CcaaiiColumns {

        private WeatherTable() {};
        private static final WeatherTable sInstance = new WeatherTable();
        public static WeatherTable getInstance() {
            return sInstance;
        }

        public static final String TABLE_NAME 		= "WeatherTab";

        public static final String CITY_ID = "City_ID";
        public static final String CITY = "City";
        public static final String PROVINCE = "Province";
        public static final String WEATHER = "Weather";
        public static final String IS_CURRENT = "Is_Current";
        public static final String UPDATE_TIME = "Update_Time";


        private static final String CREATE_TABLE_STMT =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                        + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + USER_ID + " TEXT, "
                        + CITY_ID + " TEXT UNIQUE ON CONFLICT REPLACE, "
                        + CITY + " TEXT, "
                        + PROVINCE + " TEXT, "
                        + WEATHER + " TEXT, "
                        + IS_CURRENT + " INTEGER, "
                        + UPDATE_TIME + " INTEGER "
                        + ");";

        @Override
        String getName() {
            return TABLE_NAME;
        }

        @Override
        void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_STMT);
        }
    }

    public static final class DocumentTable extends CcaaiiDbTable implements BaseColumns, CcaaiiColumns {

        private DocumentTable() {};
        private static final DocumentTable sInstance = new DocumentTable();
        public static DocumentTable getInstance() {
            return sInstance;
        }

        public static final String TABLE_NAME 		= "DocumentTab";

        public static final String OBJECT_ID = "Object_ID";
        public static final String NAME = "Name";
        public static final String IS_HOT = "Is_Hot";
        public static final String IS_NEW = "Is_New";
        public static final String DESCRIPTION = "Description";
        public static final String CONTENT_FILE = "Content_File";
        public static final String CATEGORY = "Category";
        public static final String PLAY_COUNT = "Play_Count";
        public static final String CREATE_TIME = "Create_Time";


        private static final String CREATE_TABLE_STMT =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                        + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + USER_ID + " TEXT, "
                        + OBJECT_ID + " TEXT UNIQUE ON CONFLICT REPLACE, "
                        + NAME + " TEXT, "
                        + IS_HOT + " INTEGER, "
                        + IS_NEW + " INTEGER, "
                        + DESCRIPTION + " TEXT, "
                        + CONTENT_FILE + " TEXT, "
                        + CATEGORY + " INTEGER, "
                        + PLAY_COUNT + " INTEGER, "
                        + CREATE_TIME + " INTEGER "
                        + ");";

        @Override
        String getName() {
            return TABLE_NAME;
        }

        @Override
        void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_STMT);
        }
    }

    static LinkedHashMap<String, CcaaiiDbTable> sFSADbTables = new LinkedHashMap<String, CcaaiiDbTable>();
    
    static {
    	sFSADbTables.put(CurrentUserTable.getInstance().getName(), CurrentUserTable.getInstance());
        sFSADbTables.put(CityTable.getInstance().getName(), CityTable.getInstance());
        sFSADbTables.put(WeatherTable.getInstance().getName(), WeatherTable.getInstance());
        sFSADbTables.put(DocumentTable.getInstance().getName(), DocumentTable.getInstance());
    }
}
