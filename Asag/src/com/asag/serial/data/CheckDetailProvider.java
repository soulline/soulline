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

public class CheckDetailProvider extends ContentProvider {

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
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor c;
        switch (sUriMatcher.match(uri)) {  
        case POINT:  
        	c = db.query(AsagProvider.CheckDetail.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
            break;  
  
        case POINT_ID:
        	String id = uri.getPathSegments().get(1);
            c = db.query(AsagProvider.CheckDetail.TABLE_NAME, projection, AsagProvider.CheckDetail._ID + "="+id+(!TextUtils.isEmpty(selection)?"AND("+selection+')':""),selectionArgs, null, null, sortOrder);
            break;  
  
        default:  
            throw new IllegalArgumentException("Unknown URI " + uri);  
        }  
  
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
        if (values.containsKey(AsagProvider.CheckDetail.CHECKDATE) == false) {  
            values.put(AsagProvider.CheckDetail.CHECKDATE, "");  
        }  
  
        if (values.containsKey(AsagProvider.CheckDetail.CHANDI) == false) {  
            values.put(AsagProvider.CheckDetail.CHANDI, "");  
        } 
  
        if (values.containsKey(AsagProvider.CheckDetail.SHUIFEN) == false) {  
            values.put(AsagProvider.CheckDetail.SHUIFEN, "");  
        }
        
        if (values.containsKey(AsagProvider.CheckDetail.SHULIANG) == false) {  
            values.put(AsagProvider.CheckDetail.SHULIANG, "");  
        } 
        
        if (values.containsKey(AsagProvider.CheckDetail.LIANGZHONG) == false) {  
            values.put(AsagProvider.CheckDetail.LIANGZHONG, "");  
        } 
        
        if (values.containsKey(AsagProvider.CheckDetail.CANGHAO) == false) {  
            values.put(AsagProvider.CheckDetail.CANGHAO, "");  
        } 
        
        if (values.containsKey(AsagProvider.CheckDetail.CHECKTYPE) == false) {  
            values.put(AsagProvider.CheckDetail.CHECKTYPE, "");  
        } 
        if (values.containsKey(AsagProvider.CheckDetail.CHECKDATE) == false) {  
            values.put(AsagProvider.CheckDetail.CHECKDATE, "");  
        }
        if (values.containsKey(AsagProvider.CheckDetail.SAVE_TIME) == false) {
        	values.put(AsagProvider.CheckDetail.SAVE_TIME, "");
        }
        if (values.containsKey(AsagProvider.CheckDetail.CHULIANGSTATE) == false) {
        	values.put(AsagProvider.CheckDetail.CHULIANGSTATE, "");
        }
        if (values.containsKey(AsagProvider.CheckDetail.SHUIFENSTATE) == false) {
        	values.put(AsagProvider.CheckDetail.SHUIFENSTATE, "");
        }
        
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();  
        long rowId = db.insert(AsagProvider.CheckDetail.TABLE_NAME, AsagProvider.CheckDetail.CHECKDATE, values);  
        if (rowId > 0) {  
            Uri noteUri = ContentUris.withAppendedId(AsagProvider.CheckDetail.CONTENT_URI, rowId);  
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
            count = db.delete(AsagProvider.CheckDetail.TABLE_NAME, selection, selectionArgs);  
            break;  
  
        case POINT_ID:  
            String noteId = uri.getPathSegments().get(1);  
            count = db.delete(AsagProvider.CheckDetail.TABLE_NAME, AsagProvider.CheckDetail._ID + "=" + noteId  
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
            count = db.update(AsagProvider.CheckDetail.TABLE_NAME, values, selection, selectionArgs);
            break;  
  
        case POINT_ID:  
            String noteId = uri.getPathSegments().get(1);  
            count = db.update(AsagProvider.CheckDetail.TABLE_NAME, values, AsagProvider.CheckDetail._ID + "=" + noteId  
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
        sUriMatcher.addURI(AsagProvider.CheckDetail.AUTHORITY, "check_detail", POINT);  
        sUriMatcher.addURI(AsagProvider.CheckDetail.AUTHORITY, "check_detail/#", POINT_ID);  
  
        sPersonsProjectionMap = new HashMap<String, String>();  
        sPersonsProjectionMap.put(AsagProvider.CheckDetail._ID, AsagProvider.CheckDetail._ID);  
        sPersonsProjectionMap.put(AsagProvider.CheckDetail.CHECKDATE, AsagProvider.CheckDetail.CHECKDATE);  
        sPersonsProjectionMap.put(AsagProvider.CheckDetail.CHECKTYPE, AsagProvider.CheckDetail.CHECKTYPE);
        sPersonsProjectionMap.put(AsagProvider.CheckDetail.CANGHAO, AsagProvider.CheckDetail.CANGHAO);
        sPersonsProjectionMap.put(AsagProvider.CheckDetail.LIANGZHONG, AsagProvider.CheckDetail.LIANGZHONG);
        sPersonsProjectionMap.put(AsagProvider.CheckDetail.SHULIANG, AsagProvider.CheckDetail.SHULIANG);
        sPersonsProjectionMap.put(AsagProvider.CheckDetail.SHUIFEN, AsagProvider.CheckDetail.SHUIFEN);
        sPersonsProjectionMap.put(AsagProvider.CheckDetail.CHANDI, AsagProvider.CheckDetail.CHANDI);
        sPersonsProjectionMap.put(AsagProvider.CheckDetail.RUKUDATE, AsagProvider.CheckDetail.RUKUDATE);
        sPersonsProjectionMap.put(AsagProvider.CheckDetail.SAVE_TIME, AsagProvider.CheckDetail.SAVE_TIME);
        sPersonsProjectionMap.put(AsagProvider.CheckDetail.CHULIANGSTATE, AsagProvider.CheckDetail.CHULIANGSTATE);
        sPersonsProjectionMap.put(AsagProvider.CheckDetail.SHUIFENSTATE, AsagProvider.CheckDetail.SHUIFENSTATE);
    }  
}
