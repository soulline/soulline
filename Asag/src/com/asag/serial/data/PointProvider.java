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

public class PointProvider extends ContentProvider {

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
        	c = db.query(AsagProvider.PointColumns.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
            break;  
  
        case POINT_ID:  
        	String id = uri.getPathSegments().get(1);
            c = db.query(AsagProvider.PointColumns.TABLE_NAME, projection, AsagProvider.PointColumns._ID + "="+id+(!TextUtils.isEmpty(selection)?"AND("+selection+')':""),selectionArgs, null, null, sortOrder);
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
        if (values.containsKey(AsagProvider.PointColumns.NUMBER) == false) {  
            values.put(AsagProvider.PointColumns.NUMBER, "");  
        }  
  
        if (values.containsKey(AsagProvider.PointColumns.XPOINT) == false) {  
            values.put(AsagProvider.PointColumns.XPOINT, "");  
        }
        
        if (values.containsKey(AsagProvider.PointColumns.YPOINT) == false) {  
            values.put(AsagProvider.PointColumns.YPOINT, "");  
        } 
  
        if (values.containsKey(AsagProvider.PointColumns.ZPOINT) == false) {  
            values.put(AsagProvider.PointColumns.ZPOINT, "");  
        } 
        
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();  
        long rowId = db.insert(AsagProvider.PointColumns.TABLE_NAME, AsagProvider.PointColumns.NUMBER, values);  
        if (rowId > 0) {  
            Uri noteUri = ContentUris.withAppendedId(AsagProvider.PointColumns.CONTENT_URI, rowId);  
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
            count = db.delete(AsagProvider.PointColumns.TABLE_NAME, selection, selectionArgs);  
            break;  
  
        case POINT_ID:  
            String noteId = uri.getPathSegments().get(1);  
            count = db.delete(AsagProvider.PointColumns.TABLE_NAME, AsagProvider.PointColumns._ID + "=" + noteId  
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
            count = db.update(AsagProvider.PointColumns.TABLE_NAME, values, selection, selectionArgs);
            break;  
  
        case POINT_ID:  
            String noteId = uri.getPathSegments().get(1);  
            count = db.update(AsagProvider.PointColumns.TABLE_NAME, values, AsagProvider.PointColumns._ID + "=" + noteId  
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
        sUriMatcher.addURI(AsagProvider.PointColumns.AUTHORITY, "point_info", POINT);  
        sUriMatcher.addURI(AsagProvider.PointColumns.AUTHORITY, "point_info/#", POINT_ID);  
  
        sPersonsProjectionMap = new HashMap<String, String>();  
        sPersonsProjectionMap.put(AsagProvider.PointColumns._ID, AsagProvider.PointColumns._ID);  
        sPersonsProjectionMap.put(AsagProvider.PointColumns.NUMBER, AsagProvider.PointColumns.NUMBER);  
        sPersonsProjectionMap.put(AsagProvider.PointColumns.XPOINT, AsagProvider.PointColumns.XPOINT);
        sPersonsProjectionMap.put(AsagProvider.PointColumns.YPOINT, AsagProvider.PointColumns.YPOINT);
        sPersonsProjectionMap.put(AsagProvider.PointColumns.ZPOINT, AsagProvider.PointColumns.ZPOINT);
    }  
}
