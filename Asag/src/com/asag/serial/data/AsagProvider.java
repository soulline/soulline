package com.asag.serial.data;
import android.net.Uri;
import android.provider.BaseColumns;

public class AsagProvider {

		// 这个是每个Provider的标识，在Manifest中使用
		public static final String AUTHORITY = "com.serial.provider.point";
		
	    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.asag.serial";

	    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.asag.serial";

	    /**
	     * 跟Person表相关的常量
	     * @author jacp
	     *
	     */
		public static final class PointColumns implements BaseColumns {
			// CONTENT_URI跟数据库的表关联，最后根据CONTENT_URI来查询对应的表
			public static final Uri CONTENT_URI = Uri.parse("content://"+ AUTHORITY +"/point");
			public static final String TABLE_NAME = "point_info";
			public static final String DEFAULT_SORT_ORDER = "point desc";
			
			public static final String NUMBER = "number";
			public static final String XPOINT = "xpoint";
			public static final String YPOINT = "ypoint";
			public static final String ZPOINT = "zpoint";
			
		}
		

}
