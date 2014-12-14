package com.asag.serial;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.GridView;

import com.asag.serial.base.BaseActivity;
import com.asag.serial.data.AsagProvider;
import com.asag.serial.mode.PointInfo;

public class PointFangActivity extends BaseActivity {

	private GridView pointGrid;
	
	private ArrayList<PointInfo> pointList = new ArrayList<PointInfo>();
	
	private PointFangAdapter adapter;
	
	private ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.point_fang_activity);
		initView();
		initContent();
	}

	private void initView() {
		pointGrid = (GridView) findViewById(R.id.point_grid);
	}
	
	private ArrayList<PointInfo> queryData() {
		ArrayList<PointInfo> list = new ArrayList<PointInfo>();
		Cursor cursor = getContentResolver().query(AsagProvider.PointColumns.CONTENT_URI, new String[] { AsagProvider.PointColumns.NUMBER, AsagProvider.PointColumns.XPOINT, AsagProvider.PointColumns.YPOINT
				,AsagProvider.PointColumns.ZPOINT}, null, null, null);
		if (cursor != null) {
			while(cursor.moveToNext()) {
				PointInfo point = new PointInfo();
				point.way = cursor.getString(cursor.getColumnIndexOrThrow(AsagProvider.PointColumns.NUMBER));
				point.xpoint = cursor.getString(cursor.getColumnIndexOrThrow(AsagProvider.PointColumns.XPOINT));
				point.ypoint = cursor.getString(cursor.getColumnIndexOrThrow(AsagProvider.PointColumns.YPOINT));
				point.zpoint = cursor.getString(cursor.getColumnIndexOrThrow(AsagProvider.PointColumns.ZPOINT));
				list.add(point);
			}
			cursor.close();
		}
		return list;
	}
	
	private void showList(final ArrayList<PointInfo> list) {
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				if (adapter == null) {
					adapter = new PointFangAdapter(context);
					adapter.addData(list);
					pointGrid.setAdapter(adapter);
				} else {
					adapter.clear();
					adapter.addData(list);
					adapter.notifyDataSetChanged();
				}
			}
		});
	}
	
	private void showLoading(final boolean isShow) {
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				if (isShow) {
					dialog = new ProgressDialog(context);
					dialog.setTitle("正在加载中，请稍后...");
					dialog.show();
				} else if (dialog != null) {
					dialog.dismiss();
				}
			}
		});
	}
	
	private void initContent() {
		showLoading(true);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				ArrayList<PointInfo> list = queryData();
				showList(list);
				showLoading(false);
			}
		}).start();
	}
	
}
