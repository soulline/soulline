package com.asag.serial.data;
import android.net.Uri;
import android.provider.BaseColumns;

public class AsagProvider {

	    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.asag.serial";

	    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.asag.serial";

		public static final class PointColumns implements BaseColumns {
			public static final String AUTHORITY = "com.asag.serial.point";
			// CONTENT_URI跟数据库的表关联，最后根据CONTENT_URI来查询对应的表
			public static final Uri CONTENT_URI = Uri.parse("content://"+ AUTHORITY +"/point_info");
			public static final String TABLE_NAME = "point_info";
			public static final String DEFAULT_SORT_ORDER = "number";
			
			public static final String NUMBER = "number";
			public static final String XPOINT = "xpoint";
			public static final String YPOINT = "ypoint";
			public static final String ZPOINT = "zpoint";
			
		}
		
		public static final class PointRecord implements BaseColumns {
			public static final String AUTHORITY = "com.asag.serial.record";
			// CONTENT_URI跟数据库的表关联，最后根据CONTENT_URI来查询对应的表
			public static final Uri CONTENT_URI = Uri.parse("content://"+ AUTHORITY +"/point_record");
			public static final String TABLE_NAME = "point_record";
			public static final String DEFAULT_SORT_ORDER = "wayNum";
			
			public static final String WAYNUMBER = "waynum";
			public static final String COTWO = "cotwo";
			public static final String RHVALUE = "rhvalue";
			public static final String TVALUE = "tvalue";
			public static final String SSI = "ssi";
			
			public static final String OTWO = "otwo";
			public static final String PHVALUE = "phvalue";
			
			public static final String MMI = "mmi";
			public static final String CHECKDATE = "checkdate";
			public static final String CHECKTYPE = "checktype";
			public static final String STATUS = "status";
			
			public static final String SAVE_TIME = "save_time";
			
		}
		
		public static final class CheckDetail implements BaseColumns {
			public static final String AUTHORITY = "com.asag.serial.check";
			// CONTENT_URI跟数据库的表关联，最后根据CONTENT_URI来查询对应的表
			public static final Uri CONTENT_URI = Uri.parse("content://"+ AUTHORITY +"/check_detail");
			public static final String TABLE_NAME = "check_detail";
			public static final String DEFAULT_SORT_ORDER = "canghao";
			
			public static final String CANGHAO = "canghao";
			public static final String LIANGZHONG = "liangzhong";
			public static final String SHULIANG = "shuliang";
			public static final String SHUIFEN = "shuifen";
			public static final String CHANDI = "chandi";
			
			public static final String RUKUDATE = "rukudate";
			public static final String CHECKDATE = "checkdate";
			public static final String CHECKTYPE = "checktype";
			
			public static final String SAVE_TIME = "save_time";
			
		}
		

}
