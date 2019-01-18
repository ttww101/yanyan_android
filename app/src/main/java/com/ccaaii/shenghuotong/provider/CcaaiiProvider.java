package com.ccaaii.shenghuotong.provider;

import java.util.ArrayList;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

import com.ccaaii.base.log.DevLog;
import com.ccaaii.base.log.LogLevel;
import com.ccaaii.base.log.MarketLog;
import com.ccaaii.shenghuotong.BuildConfig;
import com.ccaaii.shenghuotong.provider.CcaaiiDataStore.CcaaiiColumns;

/**
 */
public class CcaaiiProvider extends ContentProvider {

	private static final String TAG = "[CCAAII]CcaaiiProvider";

	/* URI authority string */
	public static final String AUTHORITY = BuildConfig.APPLICATION_ID+".ccaaiiprovider";

	/* URI paths names */
	public static final String USER_ID_CURRENT = "user_id_current";
	public static final String CITY_TABLE = "city_table";
	public static final String WEATHER_TABLE = "weather_table";
	public static final String DOCUMENT_TABLE = "document_table";

	/* UriMatcher codes */
	private static final int USER_ID_CURRENT_MATCH = 10;


	private static final int CITY_MATCH = 20;
	private static final int CITY_ID_MATCH = 21;

	private static final int WEATHER_MATCH = 22;
	private static final int WEATHER_ID_MATCH = 23;

	private static final int BAIKE_MATCH = 30;
	private static final int BAIKE_ID_MATCH = 31;


	private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

	static {
		sUriMatcher.addURI(AUTHORITY, USER_ID_CURRENT, USER_ID_CURRENT_MATCH);
		sUriMatcher.addURI(AUTHORITY, CITY_TABLE, CITY_MATCH);
		sUriMatcher.addURI(AUTHORITY, CITY_TABLE + "/#", CITY_ID_MATCH);
		sUriMatcher.addURI(AUTHORITY, WEATHER_TABLE, WEATHER_MATCH);
		sUriMatcher.addURI(AUTHORITY, WEATHER_TABLE + "/#", WEATHER_ID_MATCH);
		sUriMatcher.addURI(AUTHORITY, DOCUMENT_TABLE, BAIKE_MATCH);
		sUriMatcher.addURI(AUTHORITY, DOCUMENT_TABLE + "/#", BAIKE_ID_MATCH);
    }

	private CcaaiiDbHelper mDbHelper;

	@Override
	public boolean onCreate() {
		if (LogLevel.DEV) {
			DevLog.d(TAG, "CcaaiiProvider.onCreate()");
		}

		mDbHelper = CcaaiiDbHelper.getInstance(getContext());
		return true;
	}

	@Override
	public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
		synchronized (mDbHelper) {
			SQLiteDatabase db = mDbHelper.getWritableDatabase();
			db.beginTransaction();
			try {
				ContentProviderResult[] results = super.applyBatch(operations);
				db.setTransactionSuccessful();
				return results;
			} finally {
				db.endTransaction();
			}
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		if (LogLevel.DEV) {
			DevLog.d(TAG, "query(" + uri + ",...)");
		}
		int match = sUriMatcher.match(uri);
		if (match == UriMatcher.NO_MATCH) {
			if (LogLevel.MARKET) {
				MarketLog.e(TAG, "query(): Wrong URI");
			}
			throw new IllegalArgumentException("Wrong URI: " + uri);
		}

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		int where_append_count = 0;

		if (userIdIdRequired(match)) {
			String user_id_string = uri.getQueryParameter(CcaaiiColumns.USER_ID);
			if (user_id_string == null) {
				if (LogLevel.MARKET) {
					MarketLog.e(TAG, "query(): Wrong URI: no userID: " + uri);
				}
				throw new IllegalArgumentException("Wrong URI: no userID: " + uri);
			}

			String current_user_id = CcaaiiProviderHelper.getCurrentUserId(getContext());
			if (!user_id_string.equals(current_user_id)) {
				if (LogLevel.MARKET) {
					MarketLog.i(TAG, "query(): userID mis-match: " + user_id_string + ". Current userID: " + current_user_id);
				}
				return null;
			}

			qb.appendWhere((where_append_count++ == 0 ? "" : " AND ") + (CcaaiiColumns.USER_ID + " = '" + user_id_string + "'"));
		}

		qb.setTables(tableName(match));
		if (uriWithID(match)) {
			qb.appendWhere((where_append_count++ == 0 ? "" : " AND ") + (BaseColumns._ID + "=" + uri.getLastPathSegment()));
		}

		if (projection == null) {
			projection = defaultProjection(match);
		}

		if (TextUtils.isEmpty(sortOrder)) {
			sortOrder = CcaaiiColumns.DEFAULT_SORT_ORDER;
		}

		SQLiteDatabase db;
		try {
			db = mDbHelper.getReadableDatabase();
		} catch (SQLiteException e) {
			if (LogLevel.MARKET) {
				MarketLog.e(TAG, "query(): Error opening readable database", e);
			}

			throw e;
		}

		Cursor cursor;
		synchronized (mDbHelper) {
			try {
				cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
			} catch (Throwable e) {
				if (LogLevel.MARKET) {
					DevLog.e(TAG, "query(): Exception at db query", e);
				}
				throw new RuntimeException("Exception at db query: " + e.getMessage());
			}
		}

		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		if (LogLevel.DEV) {
			DevLog.d(TAG, "query(): Cursor has " + cursor.getCount() + " rows");
		}
		return cursor;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if (LogLevel.DEV) {
			DevLog.d(TAG, "insert(" + uri + ", ...)");
		}

		int match = sUriMatcher.match(uri);

		if (match == UriMatcher.NO_MATCH) {
			if (LogLevel.MARKET) {
				MarketLog.e(TAG, "insert(): Wrong URI: " + uri);
			}
			throw new IllegalArgumentException("Wrong URI: " + uri);
		}

		if (uriWithID(match)) {
			if (LogLevel.MARKET) {
				MarketLog.e(TAG, "insert(): Insert not allowed for this URI: " + uri);
			}
			throw new IllegalArgumentException("Insert not allowed for this URI: " + uri);
		}

		if (LogLevel.MARKET) {
			if (uri.getQueryParameter(CcaaiiColumns.USER_ID) != null) {
				if (LogLevel.MARKET) {
					MarketLog.e(TAG, "insert(): Insert not allowed for this URI: " + uri);
				}
				throw new IllegalArgumentException("Insert not allowed for this URI: " + uri);
			}
		}

		SQLiteDatabase db;
		long rowId;
		try {
			db = mDbHelper.getWritableDatabase();
		} catch (SQLiteException e) {
			if (LogLevel.MARKET) {
				MarketLog.e(TAG, "insert(): Error opening writeable database", e);
			}
			throw e;
		}

		synchronized (mDbHelper) {
			try {
				rowId = db.insert(tableName(match), null, values);
			} catch (SQLException e) {
				if (LogLevel.MARKET) {
					MarketLog.e(TAG, "Insert() failed", e);
				}
				throw e;
			}
		}

		if (rowId <= 0) {
			if (LogLevel.MARKET) {
				MarketLog.e(TAG, "insert(): Error: insert() returned " + rowId);
			}
			throw new RuntimeException("DB insert failed");
		}

		uri = ContentUris.withAppendedId(UriHelper.removeQuery(uri), rowId);
		if (LogLevel.DEV) {
			DevLog.d(TAG, "insert(): new uri with rowId: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);

		if (LogLevel.DEV) {
			DevLog.d(TAG, "insert(): return " + uri);
		}
		return uri;
	}

	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		if (LogLevel.DEV) {
			DevLog.d(TAG, "bulkInsert(" + uri + ", ...)");
		}
		int match = sUriMatcher.match(uri);

		if (match == UriMatcher.NO_MATCH) {
			if (LogLevel.MARKET) {
				MarketLog.e(TAG, "bulkInsert(): Wrong URI: " + uri);
			}
			throw new IllegalArgumentException("Wrong URI: " + uri);
		}

		if (uriWithID(match)) {
			if (LogLevel.MARKET) {
				MarketLog.e(TAG, "bulkInsert(): Insert not allowed for this URI: " + uri);
			}
			throw new IllegalArgumentException("Insert not allowed for this URI: " + uri);
		}

		if (LogLevel.MARKET) {
			if (uri.getQueryParameter(CcaaiiColumns.USER_ID) != null) {
				if (LogLevel.MARKET) {
					MarketLog.e(TAG, "bulkInsert(): Insert not allowed for this URI: " + uri);
				}
				throw new IllegalArgumentException("Insert not allowed for this URI: " + uri);
			}
		}

		SQLiteDatabase db;
		try {
			db = mDbHelper.getWritableDatabase();
		} catch (SQLiteException e) {
			if (LogLevel.MARKET) {
				MarketLog.e(TAG, "bulkInsert(): Error opening writable database", e);
			}
			throw e;
		}

		int added = 0;
		long rowId = 0;
		String table = tableName(match);

		synchronized (mDbHelper) {
			try {
				db.beginTransaction();
				for (int i = 0; i < values.length; i++) {
					try {
						rowId = db.insert(table, null, values[i]);
					} catch (SQLException e) {
						if (LogLevel.MARKET) {
							MarketLog.e(TAG, "bulkInsert() P1", e);
						}
						continue;
					}

					if (rowId <= 0) {
						if (LogLevel.MARKET) {
							MarketLog.e(TAG, "bulkInsert() P2: " + rowId);
						}
						continue;
					}
					added = added + 1;
				}
				db.setTransactionSuccessful();
			} catch (SQLException e) {
				if (LogLevel.MARKET) {
					MarketLog.e(TAG, "bulkInsert() P3", e);
				}

				throw new RuntimeException("bulkInsert(): DB insert failed: " + e.getMessage());
			} finally {
				db.endTransaction();
			}
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return added;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		if (LogLevel.DEV) {
			DevLog.d(TAG, "update(" + uri + ", ...)");
		}
		int match = sUriMatcher.match(uri);
		if (match == UriMatcher.NO_MATCH) {
			if (LogLevel.MARKET) {
				MarketLog.e(TAG, "update(): Wrong URI: " + uri);
			}
			throw new IllegalArgumentException("Wrong URI: " + uri);
		}

		if (uriWithID(match)) {
			selection = BaseColumns._ID + "=" + uri.getLastPathSegment() + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
		}

		String user_id_string = uri.getQueryParameter(CcaaiiColumns.USER_ID);
		if (!TextUtils.isEmpty(user_id_string)) {
			selection = CcaaiiColumns.USER_ID + " = '" + user_id_string + "' " + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
		}

		SQLiteDatabase db;
		try {
			db = mDbHelper.getWritableDatabase();
		} catch (SQLiteException e) {
			if (LogLevel.MARKET) {
				MarketLog.e(TAG, "update(): Error opening writable database", e);
			}
			throw e;
		}

		int count;
		synchronized (mDbHelper) {
			try {
				count = db.update(tableName(match), values, selection, selectionArgs);
			} catch (SQLException e) {
				if (LogLevel.MARKET) {
					MarketLog.e(TAG, "update() failed", e);
				}
				throw e;
			}
		}

		if (LogLevel.DEV) {
			DevLog.d(TAG, "update(): " + count + " rows updated");
		}

		if (count > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return count;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		if (LogLevel.DEV) {
			DevLog.d(TAG, "delete(" + uri + ", ...)");
		}
		int match = sUriMatcher.match(uri);
		if (match == UriMatcher.NO_MATCH) {
			if (LogLevel.MARKET) {
				MarketLog.e(TAG, "delete(): Wrong URI: " + uri);
			}
			throw new IllegalArgumentException("Wrong URI: " + uri);
		}

		if (uriDerived(match)) {
			if (LogLevel.MARKET) {
				MarketLog.e(TAG, "delete(): Row delete not allowed for this URI: " + uri);
			}
			throw new IllegalArgumentException("Row delete not allowed for this URI: " + uri);
		}

		if (uriWithID(match)) {
			selection = BaseColumns._ID + "=" + uri.getLastPathSegment() + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
		}

		String user_id_string = uri.getQueryParameter(CcaaiiColumns.USER_ID);
		if (!TextUtils.isEmpty(user_id_string)) {
			selection = CcaaiiColumns.USER_ID + "= '" + user_id_string + "' " + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
		}

		SQLiteDatabase db;
		try {
			db = mDbHelper.getWritableDatabase();
		} catch (SQLiteException e) {
			// TODO Implement proper error handling
			if (LogLevel.MARKET) {
				MarketLog.e(TAG, "delete(): Error opening writable database", e);
			}

			throw e;
		}

		int count;
		synchronized (mDbHelper) {
			try {
				count = db.delete(tableName(match), selection, selectionArgs);
			} catch (SQLException e) {
				// TODO Implement proper error handling
				if (LogLevel.MARKET) {
					MarketLog.e(TAG, "delete(): DB rows delete error", e);
				}

				throw e;
			}
		}

		if (LogLevel.DEV) {
			DevLog.d(TAG, "delete(), " + uri.toString() + "; " + count + " rows deleted");
		}

		if (count > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return count;
	}

	@Override
	public String getType(Uri uri) {
		if (LogLevel.DEV) {
			DevLog.d(TAG, "getType(" + uri + ')');
		}

		return null;
	}

	private boolean uriWithID(int uri_match) {
		switch (uri_match) {
			case CITY_ID_MATCH:
			case WEATHER_ID_MATCH:
			case BAIKE_ID_MATCH:
			return true;
		default:
			return false;
		}
	}

	private boolean userIdIdRequired(int uri_match) {
		if (uriWithID(uri_match)) {
			return false;
		}

		switch (uri_match) {
            case USER_ID_CURRENT_MATCH:
			case CITY_MATCH:
			case WEATHER_MATCH:
			case BAIKE_MATCH:
			return false;
		default:
			return true;
		}
	}

	private boolean uriDerived(int uri_match) {
		switch (uri_match) {
		case USER_ID_CURRENT_MATCH:
			return true;
		default:
			return false;
		}
	}

	private String[] defaultProjection(int uri_matcher) {
		switch (uri_matcher) {
		default:
			return null;
		}
	}

	private String tableName(int uri_match) {
		switch (uri_match) {

            case USER_ID_CURRENT_MATCH:
                return CcaaiiDataStore.CurrentUserTable.getInstance().getName();
			case CITY_MATCH:
			case CITY_ID_MATCH:
				return CcaaiiDataStore.CityTable.getInstance().getName();
			case WEATHER_MATCH:
			case WEATHER_ID_MATCH:
				return CcaaiiDataStore.WeatherTable.getInstance().getName();
			case BAIKE_MATCH:
			case BAIKE_ID_MATCH:
				return CcaaiiDataStore.DocumentTable.getInstance().getName();
		default:
			throw new Error(TAG + " No table defined for #" + uri_match);
		}
	}
}
