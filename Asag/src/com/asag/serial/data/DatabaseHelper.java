package com.asag.serial.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "asag_data.db";  
    private static final int DATABASE_VERSION = 1;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + AsagProvider.PointColumns.TABLE_NAME + " ("  
                + AsagProvider.PointColumns._ID + " INTEGER PRIMARY KEY,"  
                + AsagProvider.PointColumns.NUMBER + " TEXT,"  
                + AsagProvider.PointColumns.XPOINT + " TEXT,"
                + AsagProvider.PointColumns.YPOINT + " TEXT,"
                + AsagProvider.PointColumns.ZPOINT + " TEXT"
                + ");");  

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + AsagProvider.PointColumns.TABLE_NAME);  
        onCreate(db);  
	}

}
