package com.asag.serial.data;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class PointRecordProvider extends ContentProvider {

	private static HashMap<String, String> sPersonsProjectionMap;  
	  
    private static final int POINT = 1;  
    private static final int POINT_ID = 2;  
  
    private static final UriMatcher sUriMatcher;  
  
    private DatabaseHelper mOpenHelper; 
    
	@Override
	public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext()); 
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();  
        qb.setTables(AsagProvider.PointRecord.TABLE_NAME);  
  
        switch (sUriMatcher.match(uri)) {  
        case POINT:  
            qb.setProjectionMap(sPersonsProjectionMap);  
            break;  
  
        case POINT_ID:  
            qb.setProjectionMap(sPersonsProjectionMap);  
            qb.appendWhere(AsagProvider.PointRecord._ID + "=" + uri.getPathSegments().get(1));  
            break;  
  
        default:  
            throw new IllegalArgumentException("Unknown URI " + uri);  
        }  
  
        // If no sort order is specified use the default  
        String orderBy;  
        if (TextUtils.isEmpty(sortOrder)) {  
            orderBy = AsagProvider.PointRecord.DEFAULT_SORT_ORDER;  
        } else {  
            orderBy = sortOrder;  
        }  
  
        // Get the database and run the query  
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();  
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);  
  
        // Tell the cursor what uri to watch, so it knows when its source data changes  
        c.setNotificationUri(getContext().getContentResolver(), uri);  
        return c;  
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {  
        case POINT:  
            return AsagProvider.CONTENT_TYPE;  
        case POINT_ID:  
            return AsagProvider.CONTENT_ITEM_TYPE;  
        default:  
            throw new IllegalArgumentException("Unknown URI " + uri);  
        }  
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		if (sUriMatcher.match(uri) != POINT) {  
            throw new IllegalArgumentException("Unknown URI " + uri);  
        }  
  
        ContentValues values;  
        if (initialValues != null) {  
            values = new ContentValues(initialValues);  
        } else {  
            values = new ContentValues();  
        }  
  
        // Make sure that the fields are all set  
        if (values.containsKey(AsagProvider.PointRecord.CHECKDATE) == false) {  
            values.put(AsagProvider.PointRecord.CHECKDATE, "");  
        }  
  
        if (values.containsKey(AsagProvider.PointRecord.CHECKTYPE) == false) {  
            values.put(AsagProvider.PointRecord.CHECKTYPE, "");  
        }
        
        if (values.containsKey(AsagProvider.PointRecord.COTWO) == false) {  
            values.put(AsagProvider.PointRecord.COTWO, "");  
        } 
  
        if (values.containsKey(AsagProvider.PointRecord.MMI) == false) {  
            values.put(AsagProvider.PointRecord.MMI, "");  
        }
        
        if (values.containsKey(AsagProvider.PointRecord.RHVALUE) == false) {  
            values.put(AsagProvider.PointRecord.RHVALUE, "");  
        } 
        
        if (values.containsKey(AsagProvider.PointRecord.SSI) == false) {  
            values.put(AsagProvider.PointRecord.SSI, "");  
        } 
        
        if (values.containsKey(AsagProvider.PointRecord.STATUS) == false) {  
            values.put(AsagProvider.PointRecord.STATUS, "");  
        } 
        
        if (values.containsKey(AsagProvider.PointRecord.TVALUE) == false) {  
            values.put(AsagProvider.PointRecord.TVALUE, "");  
        } 
        if (values.containsKey(AsagProvider.PointRecord.WAYNUMBER) == false) {  
            values.put(AsagProvider.PointRecord.WAYNUMBER, "");  
        } 
        if (values.containsKey(AsagProvider.PointRecord.OTWO) == false) {  
            values.put(AsagProvider.PointRecord.OTWO, "");  
        }
        if (values.containsKey(AsagProvider.PointRecord.PHVALUE) == false) {  
            values.put(AsagProvider.PointRecord.PHVALUE, "");  
        }
        
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();  
        long rowId = db.insert(AsagProvider.PointRecord.TABLE_NAME, AsagProvider.PointRecord.WAYNUMBER, values);  
        if (rowId > 0) {  
            Uri noteUri = ContentUris.withAppendedId(AsagProvider.PointRecord.CONTENT_URI, rowId);  
            getContext().getContentResolver().notifyChange(noteUri, null);  
            return noteUri;  
        }  
  
        throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();  
        int count;  
        switch (sUriMatcher.match(uri)) {  
        case POINT:  
            count = db.delete(AsagProvider.PointRecord.TABLE_NAME, selection, selectionArgs);  
            break;  
  
        case POINT_ID:  
            String noteId = uri.getPathSegments().get(1);  
            count = db.delete(AsagProvider.PointRecord.TABLE_NAME, AsagProvider.PointRecord._ID + "=" + noteId  
                    + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);  
            break;  
  
        default:  
            throw new IllegalArgumentException("Unknown URI " + uri);  
        }  
  
        getContext().getContentResolver().notifyChange(uri, null);  
        return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();  
        int count;  
        switch (sUriMatcher.match(uri)) {  
        case POINT:  
            count = db.update(AsagProvider.PointRecord.TABLE_NAME, values, selection, selectionArgs);
            break;  
  
        case POINT_ID:  
            String noteId = uri.getPathSegments().get(1);  
            count = db.update(AsagProvider.PointRecord.TABLE_NAME, values, AsagProvider.PointRecord._ID + "=" + noteId  
                    + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);  
            break;  
  
        default:  
            throw new IllegalArgumentException("Unknown URI " + uri);  
        }
        
        getContext().getContentResolver().notifyChange(uri, null);  
        return count; 
	}

	static {  
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);  
        sUriMatcher.addURI(AsagProvider.PointRecord.AUTHORITY, "point_record", POINT);  
        sUriMatcher.addURI(AsagProvider.PointRecord.AUTHORITY, "point_record/#", POINT_ID);  
  
        sPersonsProjectionMap = new HashMap<String, String>();  
        sPersonsProjectionMap.put(AsagProvider.PointRecord._ID, AsagProvider.PointRecord._ID);  
        sPersonsProjectionMap.put(AsagProvider.PointRecord.CHECKDATE, AsagProvider.PointRecord.CHECKDATE);  
        sPersonsProjectionMap.put(AsagProvider.PointRecord.CHECKTYPE, AsagProvider.PointRecord.CHECKTYPE);
        sPersonsProjectionMap.put(AsagProvider.PointRecord.COTWO, AsagProvider.PointRecord.COTWO);
        sPersonsProjectionMap.put(AsagProvider.PointRecord.MMI, AsagProvider.PointRecord.MMI);
        sPersonsProjectionMap.put(AsagProvider.PointRecord.RHVALUE, AsagProvider.PointRecord.RHVALUE);
        sPersonsProjectionMap.put(AsagProvider.PointRecord.SSI, AsagProvider.PointRecord.SSI);
        sPersonsProjectionMap.put(AsagProvider.PointRecord.STATUS, AsagProvider.PointRecord.STATUS);
        sPersonsProjectionMap.put(AsagProvider.PointRecord.TVALUE, AsagProvider.PointRecord.TVALUE);
        sPersonsProjectionMap.put(AsagProvider.PointRecord.WAYNUMBER, AsagProvider.PointRecord.WAYNUMBER);
        sPersonsProjectionMap.put(AsagProvider.PointRecord.OTWO, AsagProvider.PointRecord.OTWO);
        sPersonsProjectionMap.put(AsagProvider.PointRecord.PHVALUE, AsagProvider.PointRecord.PHVALUE);
    }  
}
