package com.asag.serial.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "asag_data.db";  
    private static final int DATABASE_VERSION = 2;

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
		
		db.execSQL("CREATE TABLE " + AsagProvider.PointRecord.TABLE_NAME + " ("  
                + AsagProvider.PointRecord._ID + " INTEGER PRIMARY KEY,"  
                + AsagProvider.PointRecord.WAYNUMBER + " TEXT,"  
                + AsagProvider.PointRecord.COTWO + " TEXT,"
                + AsagProvider.PointRecord.RHVALUE + " TEXT,"
                + AsagProvider.PointRecord.TVALUE + " TEXT,"
                + AsagProvider.PointRecord.SSI + " TEXT,"
                + AsagProvider.PointRecord.MMI + " TEXT,"
                + AsagProvider.PointRecord.CHECKDATE + " TEXT,"
                + AsagProvider.PointRecord.CHECKTYPE + " TEXT,"
                + AsagProvider.PointRecord.OTWO + " TEXT,"
                + AsagProvider.PointRecord.PHVALUE + " TEXT,"
                + AsagProvider.CheckDetail.SAVE_TIME + " TEXT,"
                + AsagProvider.PointRecord.STATUS + " INTEGER"
                + ");");
		db.execSQL("CREATE TABLE " + AsagProvider.CheckDetail.TABLE_NAME + " ("  
                + AsagProvider.CheckDetail._ID + " INTEGER PRIMARY KEY,"  
                + AsagProvider.CheckDetail.CANGHAO + " TEXT,"  
                + AsagProvider.CheckDetail.LIANGZHONG + " TEXT,"
                + AsagProvider.CheckDetail.SHULIANG + " TEXT,"
                + AsagProvider.CheckDetail.SHUIFEN + " TEXT,"
                + AsagProvider.CheckDetail.CHANDI + " TEXT,"
                + AsagProvider.CheckDetail.RUKUDATE + " TEXT,"
                + AsagProvider.CheckDetail.CHECKDATE + " TEXT,"
                + AsagProvider.CheckDetail.CHECKTYPE + " TEXT,"
                + AsagProvider.CheckDetail.CHULIANGSTATE + " TEXT,"
                + AsagProvider.CheckDetail.SHUIFENSTATE + " TEXT,"
                + AsagProvider.CheckDetail.SAVE_TIME + " TEXT"
                + ");");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + AsagProvider.PointColumns.TABLE_NAME);  
        onCreate(db);  
	}

}
