package com.asag.serial;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import com.asag.serial.base.BaseActivity;
import com.asag.serial.data.AsagProvider;
import com.asag.serial.mode.PointInfo;
import com.asag.serial.utils.DataUtils;

public class PointFangActivity extends BaseActivity {

	private GridView pointGrid;
	
	private ArrayList<PointInfo> pointList = new ArrayList<PointInfo>();
	
	private PointFangAdapter adapter;
	
	private ProgressDialog dialog;
	
	private TextView top_title_tx;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.point_fang_activity);
		initView();
		initTextSize();
		initContent();
	}

	private void initView() {
		pointGrid = (GridView) findViewById(R.id.point_grid);
		top_title_tx = (TextView) findViewById(R.id.top_title_tx);
	}
	
	private void initTextSize() {
		int size = DataUtils.getPreferences(DataUtils.KEY_TEXT_SIZE, 1);
		switch (size) {
		case 1:
			reloadTextSize(1.0f);
			break;
		case 2:
			reloadTextSize(1.2f);
			break;
		case 3:
			reloadTextSize(1.4f);
			break;
		case 4:
			reloadTextSize(1.6f);
			break;
		case 5:
			reloadTextSize(1.8f);
			break;

		default:
			break;
		}
	}
	
	private void reloadTextSize(float size) {
		top_title_tx.setTextSize(top_title_tx.getTextSize() * size);
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
				int size = DataUtils.getPreferences(DataUtils.KEY_TEXT_SIZE, 1);
				if (adapter == null) {
					adapter = new PointFangAdapter(context);
					adapter.addData(list);
					adapter.setItemTextSize(size);
					pointGrid.setAdapter(adapter);
				} else {
					adapter.clear();
					adapter.addData(list);
					adapter.setItemTextSize(size);
					adapter.notifyDataSetChanged();
				}
			}
		});
	}
	
	private void showLoading(final boolean isShow) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if (isShow) {
					dialog = new ProgressDialog(context);
					dialog.setTitle("提示");
					dialog.setMessage("正在加载中，请稍后...");
					dialog.show();
				} else if (dialog != null) {
					dialog.dismiss();
				}
			}
		});
	}
	
	private void sortList(ArrayList<PointInfo> list) {
		Collections.sort(list, new Comparator<PointInfo>() {
			@Override
			public int compare(PointInfo lhs, PointInfo rhs) {
				int lh = Integer.valueOf(lhs.way);
				int rh = Integer.valueOf(rhs.way);
				if (lh > rh) {
					return 1;
				} else if (lh < rh || lh == rh) {
					return -1;
				}
				return 0;
			}
		});
	}
	
	private void initContent() {
		showLoading(true);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				ArrayList<PointInfo> list = queryData();
				sortList(list);
				showList(list);
				showLoading(false);
			}
		}).start();
	}
	
}
