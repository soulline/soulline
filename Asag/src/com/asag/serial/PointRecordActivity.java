package com.asag.serial;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.asag.serial.PointRecordAdapter.OnPointCheckListener;
import com.asag.serial.base.BaseActivity;
import com.asag.serial.data.AsagProvider;
import com.asag.serial.mode.CheckDetailItem;
import com.asag.serial.mode.PointInfo;
import com.asag.serial.mode.PointItemRecord;
import com.asag.serial.mode.PointRecord;
import com.asag.serial.utils.ButtonUtils;
import com.asag.serial.utils.DataUtils;
import com.asag.serial.utils.ExcellUtils;

public class PointRecordActivity extends BaseActivity implements
		OnClickListener {

	private TextView top_title_tx;

	private TextView date_title, way_0_title, way_1_title, way_2_title,
			way_3_title, way_4_title, way_5_title, way_6_title, way_7_title,
			way_8_title, way_9_title, way_10_title, way_11_title, way_12_title,
			way_13_title, way_14_title, way_15_title; 

	private TextView all_select, detail_btn, delete_item, save_as;

	private ProgressDialog dialog;

	private ArrayList<CheckDetailItem> checkList = new ArrayList<CheckDetailItem>();

	private ArrayList<PointRecord> recordList = new ArrayList<PointRecord>();

	private PointRecordAdapter adapter;

	private ListView record_list;

	private int checkState = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.point_record_activity);
		initView();
		initTextSize();
		initContent();
	}

	private void initTextSize() {
		int size = DataUtils.getPreferences(DataUtils.KEY_TEXT_SIZE, 5);
		switch (size) {
		case 1:
			reloadTextSize(1.4f);
			break;
		case 2:
			reloadTextSize(1.6f);
			break;
		case 3:
			reloadTextSize(1.8f);
			break;
		case 4:
			reloadTextSize(2.0f);
			break;
		case 5:
			reloadTextSize(2.2f);
			break;

		default:
			break;
		}
	}

	private ArrayList<CheckDetailItem> queryData() {
		ArrayList<CheckDetailItem> list = new ArrayList<CheckDetailItem>();
		Cursor cursor = getContentResolver().query(
				AsagProvider.CheckDetail.CONTENT_URI,
				new String[] { AsagProvider.CheckDetail._ID, AsagProvider.CheckDetail.CANGHAO,
						AsagProvider.CheckDetail.CHANDI,
						AsagProvider.CheckDetail.CHECKDATE,
						AsagProvider.CheckDetail.CHECKTYPE,
						AsagProvider.CheckDetail.LIANGZHONG,
						AsagProvider.CheckDetail.RUKUDATE,
						AsagProvider.CheckDetail.SHUIFEN,
						AsagProvider.CheckDetail.SHULIANG,
						AsagProvider.CheckDetail.CHULIANGSTATE,
						AsagProvider.CheckDetail.SHUIFENSTATE,
						AsagProvider.CheckDetail.SAVE_TIME},
				AsagProvider.CheckDetail.CHECKTYPE + "='" + checkState + "'", null,
				null);
		Log.d("zhao", "cursor count : " + cursor.getCount());
		if (cursor != null) {
			while (cursor.moveToNext()) {
				CheckDetailItem point = new CheckDetailItem();
				point.id = cursor.getInt(cursor
						.getColumnIndexOrThrow(AsagProvider.CheckDetail._ID));
				point.canghao = cursor
						.getString(cursor
								.getColumnIndexOrThrow(AsagProvider.CheckDetail.CANGHAO));
				point.liangzhong = cursor
						.getString(cursor
								.getColumnIndexOrThrow(AsagProvider.CheckDetail.LIANGZHONG));
				point.shuliang = cursor
						.getString(cursor
								.getColumnIndexOrThrow(AsagProvider.CheckDetail.SHULIANG));
				point.shuifen = cursor
						.getString(cursor
								.getColumnIndexOrThrow(AsagProvider.CheckDetail.SHUIFEN));
				point.chandi = cursor
						.getString(cursor
								.getColumnIndexOrThrow(AsagProvider.CheckDetail.CHANDI));
				point.rukuDate = cursor
						.getString(cursor
								.getColumnIndexOrThrow(AsagProvider.CheckDetail.RUKUDATE));
				point.checkDate = cursor
						.getString(cursor
								.getColumnIndexOrThrow(AsagProvider.CheckDetail.CHECKDATE));
				point.shuifenState = cursor
						.getString(cursor
								.getColumnIndexOrThrow(AsagProvider.CheckDetail.SHUIFENSTATE));
				point.chuliangState = cursor
						.getString(cursor
								.getColumnIndexOrThrow(AsagProvider.CheckDetail.CHULIANGSTATE));
				point.checkType = cursor
						.getString(cursor
								.getColumnIndexOrThrow(AsagProvider.CheckDetail.CHECKTYPE));
				point.saveTime = cursor.getString(cursor.getColumnIndexOrThrow(AsagProvider.CheckDetail.SAVE_TIME));
				list.add(point);
				Log.d("zhao", "query PointRecord cursor id : " + point.id + " checkDate : " + point.checkDate + "" +
						"  -- checkType : " + point.checkType);
				
			}
			cursor.close();
		}
		return list;
	}
	
	private synchronized void fillPointRecord(CheckDetailItem point) {
		Cursor cursor1 = getContentResolver().query(
				AsagProvider.PointRecord.CONTENT_URI,
				new String[] { AsagProvider.PointRecord._ID, AsagProvider.PointRecord.WAYNUMBER,
						AsagProvider.PointRecord.COTWO,
						AsagProvider.PointRecord.CHECKDATE,
						AsagProvider.PointRecord.CHECKTYPE,
						AsagProvider.PointRecord.MMI,
						AsagProvider.PointRecord.RHVALUE,
						AsagProvider.PointRecord.SSI,
						AsagProvider.PointRecord.TVALUE,
						AsagProvider.PointRecord.OTWO,
						AsagProvider.PointRecord.PHVALUE,
						AsagProvider.PointRecord.STATUS,
						AsagProvider.PointRecord.SAVE_TIME },
				AsagProvider.PointRecord.SAVE_TIME + "='"
					    + point.saveTime.trim() + "' AND "
						+ AsagProvider.PointRecord.CHECKTYPE + "="
						+ point.checkType.trim(), null, null);
		Log.d("zhao", " fillPointRecord  checkDate : " + point + " checkTYPE : " + point.checkType);
		if (cursor1 != null) {
			Log.d("zhao", "query fillPointRecord cursor1 count : " + cursor1.getCount());
		}
		if (cursor1 != null) {
			while (cursor1.moveToNext()) {
				PointItemRecord record = new PointItemRecord();
				record.id = cursor1
						.getInt(cursor1
								.getColumnIndexOrThrow(AsagProvider.PointRecord._ID));
				record.wayNum = cursor1
						.getString(cursor1
								.getColumnIndexOrThrow(AsagProvider.PointRecord.WAYNUMBER));
				record.co2 = cursor1
						.getString(cursor1
								.getColumnIndexOrThrow(AsagProvider.PointRecord.COTWO));
				record.mmi = cursor1
						.getString(cursor1
								.getColumnIndexOrThrow(AsagProvider.PointRecord.MMI));
				record.rhValue = cursor1
						.getString(cursor1
								.getColumnIndexOrThrow(AsagProvider.PointRecord.RHVALUE));
				record.ssi = cursor1
						.getString(cursor1
								.getColumnIndexOrThrow(AsagProvider.PointRecord.SSI));
				record.status = cursor1
						.getInt(cursor1
								.getColumnIndexOrThrow(AsagProvider.PointRecord.STATUS));
				record.checkDate = cursor1
						.getString(cursor1
								.getColumnIndexOrThrow(AsagProvider.PointRecord.CHECKDATE));
				record.checkType = cursor1
						.getString(cursor1
								.getColumnIndexOrThrow(AsagProvider.PointRecord.CHECKTYPE));
				record.tValue = cursor1
						.getString(cursor1
								.getColumnIndexOrThrow(AsagProvider.PointRecord.TVALUE));
				record.o2Value = cursor1
						.getString(cursor1
								.getColumnIndexOrThrow(AsagProvider.PointRecord.OTWO));
				record.ph3Value = cursor1
						.getString(cursor1
								.getColumnIndexOrThrow(AsagProvider.PointRecord.PHVALUE));
				record.saveTime = cursor1.getString(cursor1.getColumnIndexOrThrow(AsagProvider.PointRecord.SAVE_TIME));
				point.pointList.add(record);
				Log.d("zhao", "query PointRecord cursor1 id : " + record.id);
			}
			cursor1.close();
		}
	}

	private PointRecord getWayState(ArrayList<PointItemRecord> recordList) {
		PointRecord state = new PointRecord();
		for (PointItemRecord record : recordList) {
			state.date = record.checkDate;
			if (record.wayNum.equals("0")) {
				state.way0State = record.status + "";
			} else if (record.wayNum.equals("1")) {
				state.way1State = record.status + "";
			} else if (record.wayNum.equals("2")) {
				state.way2State = record.status + "";
			} else if (record.wayNum.equals("3")) {
				state.way3State = record.status + "";
			} else if (record.wayNum.equals("4")) {
				state.way4State = record.status + "";
			} else if (record.wayNum.equals("5")) {
				state.way5State = record.status + "";
			} else if (record.wayNum.equals("6")) {
				state.way6State = record.status + "";
			} else if (record.wayNum.equals("7")) {
				state.way7State = record.status + "";
			} else if (record.wayNum.equals("8")) {
				state.way8State = record.status + "";
			} else if (record.wayNum.equals("9")) {
				state.way9State = record.status + "";
			} else if (record.wayNum.equals("10")) {
				state.way10State = record.status + "";
			} else if (record.wayNum.equals("11")) {
				state.way11State = record.status + "";
			} else if (record.wayNum.equals("12")) {
				state.way12State = record.status + "";
			} else if (record.wayNum.equals("13")) {
				state.way13State = record.status + "";
			} else if (record.wayNum.equals("14")) {
				state.way14State = record.status + "";
			} else if (record.wayNum.equals("15")) {
				state.way15State = record.status + "";
			}
		}
		return state;
	}

	private void reloadTextSize(float size) {
		top_title_tx.setTextSize(top_title_tx.getTextSize() * size);
		date_title.setTextSize(date_title.getTextSize() * size);
		way_0_title.setTextSize(way_0_title.getTextSize() * size);
		way_1_title.setTextSize(way_1_title.getTextSize() * size);
		way_2_title.setTextSize(way_2_title.getTextSize() * size);
		way_3_title.setTextSize(way_3_title.getTextSize() * size);
		way_4_title.setTextSize(way_4_title.getTextSize() * size);
		way_5_title.setTextSize(way_5_title.getTextSize() * size);
		way_6_title.setTextSize(way_6_title.getTextSize() * size);
		way_7_title.setTextSize(way_7_title.getTextSize() * size);
		way_8_title.setTextSize(way_8_title.getTextSize() * size);
		way_9_title.setTextSize(way_9_title.getTextSize() * size);
		way_10_title.setTextSize(way_10_title.getTextSize() * size);
		way_11_title.setTextSize(way_11_title.getTextSize() * size);
		way_12_title.setTextSize(way_12_title.getTextSize() * size);
		way_13_title.setTextSize(way_13_title.getTextSize() * size);
		way_14_title.setTextSize(way_14_title.getTextSize() * size);
		way_15_title.setTextSize(way_15_title.getTextSize() * size);
		all_select.setTextSize(all_select.getTextSize() * size);
		detail_btn.setTextSize(detail_btn.getTextSize() * size);
		delete_item.setTextSize(delete_item.getTextSize() * size);
		save_as.setTextSize(save_as.getTextSize() * size);
	}

	private void initView() {
		top_title_tx = (TextView) findViewById(R.id.top_title_tx);
		record_list = (ListView) findViewById(R.id.record_list);

		date_title = (TextView) findViewById(R.id.date_title);
		way_0_title = (TextView) findViewById(R.id.way_0_title);
		way_1_title = (TextView) findViewById(R.id.way_1_title);
		way_2_title = (TextView) findViewById(R.id.way_2_title);
		way_3_title = (TextView) findViewById(R.id.way_3_title);
		way_4_title = (TextView) findViewById(R.id.way_4_title);
		way_5_title = (TextView) findViewById(R.id.way_5_title);
		way_6_title = (TextView) findViewById(R.id.way_6_title);
		way_7_title = (TextView) findViewById(R.id.way_7_title);
		way_8_title = (TextView) findViewById(R.id.way_8_title);
		way_9_title = (TextView) findViewById(R.id.way_9_title);
		way_10_title = (TextView) findViewById(R.id.way_10_title);
		way_11_title = (TextView) findViewById(R.id.way_11_title);
		way_12_title = (TextView) findViewById(R.id.way_12_title);
		way_13_title = (TextView) findViewById(R.id.way_13_title);
		way_14_title = (TextView) findViewById(R.id.way_14_title);
		way_15_title = (TextView) findViewById(R.id.way_15_title);

		all_select = (TextView) findViewById(R.id.all_select);
		detail_btn = (TextView) findViewById(R.id.detail_btn);
		delete_item = (TextView) findViewById(R.id.delete_item);
		save_as = (TextView) findViewById(R.id.save_as);

		all_select.setOnClickListener(this);
		detail_btn.setOnClickListener(this);
		delete_item.setOnClickListener(this);
		save_as.setOnClickListener(this);
		record_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("zhao", "pointRecord onItemClick position : " + position + "   check : " + adapter.getItem(position).isCheck);
				if (adapter != null && adapter.getItem(position).isCheck) {
					adapter.getItem(position).isCheck = false;
					recordList.get(position).isCheck = false;
					adapter.notifyDataSetChanged();
				} else if (adapter != null
						&& !adapter.getItem(position).isCheck) {
					recordList.get(position).isCheck = true;
					adapter.getItem(position).isCheck = true;
					adapter.notifyDataSetChanged();
				}

			}
		});
	}

	private void showLoading(final boolean isShow, final int type) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if (dialog == null) {
					dialog = new ProgressDialog(context);
					dialog.setTitle("提示");
				}
				if (type == 1) {
					dialog.setMessage("正在加载中，请稍候...");
				} else if (type == 2) {
					dialog.setMessage("正在删除中，请稍候...");
				} else if (type == 3) {
					dialog.setMessage("正在导出中，请稍候...");
				}
				if (isShow) {
					dialog.show();
				} else if (dialog != null) {
					dialog.dismiss();
				}
			}
		});
	}

	private void initContent() {
		String title = getIntent().getStringExtra("record_title");
		checkState = getIntent().getIntExtra("record_type", 1);
		if (!TextUtils.isEmpty(title)) {
			top_title_tx.setText(title);
		}
		showLoading(true, 1);
		new Thread(new Runnable() {

			@Override
			public void run() {
				Log.d("zhao", "checkState : " + checkState);
				checkList = queryData();
				for (CheckDetailItem detailItem : checkList) {
					fillPointRecord(detailItem);
				}
				sortList(checkList);
				for (CheckDetailItem detail : checkList) {
					sortPoint(detail.pointList);
				}
				recordList = getRecordStateList(checkList);
				Log.d("zhao", "PointRecordActivity recordList size : " + recordList.size());
				showLoading(false, 1);
				handler.post(new Runnable() {

					@Override
					public void run() {
						initRecordAdapter(recordList);
					}
				});
			}
		}).start();
	}

	private ArrayList<PointRecord> getRecordStateList(
			ArrayList<CheckDetailItem> checkList) {
		ArrayList<PointRecord> recordList = new ArrayList<PointRecord>();
		for (CheckDetailItem detailItem : checkList) {
			recordList.add(getWayState(detailItem.pointList));
		}
		return recordList;
	}

	private void initRecordAdapter(ArrayList<PointRecord> list) {
		if (adapter == null) {
			adapter = new PointRecordAdapter(context);
			adapter.addData(list);
			/*adapter.addOnPointCheckListener(new OnPointCheckListener() {

				@Override
				public void onCheck(int position) {
					PointRecord record = adapter.getItem(position);
					if (record.isCheck) {
						adapter.getItem(position).isCheck = false;
						recordList.get(position).isCheck = false;
					} else {
						adapter.getItem(position).isCheck = true;
						recordList.get(position).isCheck = true;
					}
					adapter.notifyDataSetChanged();
				}
			});*/
			record_list.setAdapter(adapter);
		} else {
			adapter.clear();
			adapter.addData(list);
			adapter.notifyDataSetChanged();
		}
	}

	private void selectAll() {
		for (PointRecord record : recordList) {
			record.isCheck = true;
		}
		initRecordAdapter(recordList);
	}

	private ArrayList<Integer> getSelectList() {
		ArrayList<Integer> selectList = new ArrayList<Integer>();
		for (int i = 0; i < recordList.size(); i++) {
			PointRecord record = recordList.get(i);
			if (record.isCheck) {
				selectList.add(i);
			}
		}
		return selectList;
	}

	private void sortList(ArrayList<CheckDetailItem> list) {
		Collections.sort(list, new Comparator<CheckDetailItem>() {
			@Override
			public int compare(CheckDetailItem lhs, CheckDetailItem rhs) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date lhD = format.parse(lhs.checkDate);
					Date rhD = format.parse(rhs.checkDate);
					long lhL = lhD.getTime();
					long rhL = rhD.getTime();
					if (lhL > rhL) {
						return 1;
					} else if (lhL < rhL || lhL == rhL) {
						return -1;
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return 0;
			}
		});
	}

	private void sortPoint(ArrayList<PointItemRecord> list) {
		Collections.sort(list, new Comparator<PointItemRecord>() {
			@Override
			public int compare(PointItemRecord lhs, PointItemRecord rhs) {
				int lhI = Integer.valueOf(lhs.wayNum);
				long rhI = Integer.valueOf(rhs.wayNum);
				if (lhI > rhI) {
					return 1;
				} else if (lhI < rhI || lhI == rhI) {
					return -1;
				}
				return 0;
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (ButtonUtils.isFastClick()) {
			return;
		}
		switch (v.getId()) {
		case R.id.all_select:
			selectAll();
			break;

		case R.id.detail_btn:
			ArrayList<Integer> selectList = getSelectList();
			if (selectList.size() == 1 && adapter != null) {
				CheckDetailItem checkDetail = checkList.get(selectList.get(0));
				Intent intent = new Intent(context,
						CheckDetailRecordActivity.class);
				Log.d("zhao", "goto detail checkDetail size : " + checkDetail.pointList.size() + " " + checkDetail.checkDate);
				intent.putExtra("check_detail", checkDetail);
				startActivity(intent);
			} else if (selectList.size() > 1) {
				showToast("请选择单项后查询详细");
			} else if (selectList.size() == 0) {
				showToast("请选择一项后查询详细");
			}
			break;

		case R.id.delete_item:
			final ArrayList<Integer> selectListD = getSelectList();
			showLoading(true, 2);
			if (selectListD.size() > 0) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						for (int i : selectListD) {
							CheckDetailItem checkDetail = checkList.get(i);
							getContentResolver().delete(
									AsagProvider.CheckDetail.CONTENT_URI,
									AsagProvider.CheckDetail._ID + "="
											+ checkDetail.id, null);
							getContentResolver()
									.delete(AsagProvider.PointRecord.CONTENT_URI,
											AsagProvider.PointRecord.SAVE_TIME
													+ "='"
													+ checkDetail.saveTime
													+ "' AND "
													+ AsagProvider.PointRecord.CHECKTYPE
													+ "='"
													+ checkDetail.checkType + "'",
											null);
						}
						showToast("删除成功");
						for (int i : selectListD) {
							checkList.remove(i);
						}
						sortList(checkList);
						for (CheckDetailItem detail : checkList) {
							sortPoint(detail.pointList);
						}
						recordList = getRecordStateList(checkList);
						showLoading(false, 2);
						handler.post(new Runnable() {

							@Override
							public void run() {
								initRecordAdapter(recordList);
							}
						});
					}
				}).start();
			}
			break;

		case R.id.save_as:
			final ArrayList<Integer> selectListS = getSelectList();
			if (selectListS.size() == 0) {
				showToast("请选择指定导出的检测记录");
				return;
			}
			showLoading(true, 3);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					
					Log.d("zhao", "save list size : " + selectListS.size());
					for (int check : selectListS) {
						CheckDetailItem checkDetail = checkList.get(check);
						String dirName = "";
						if (checkDetail.checkType.equals("1")) {
							dirName = "lianganalarm";
						} else if (checkDetail.checkType.equals("2")) {
							dirName = "lianganpoint";
						} else if (checkDetail.checkType.equals("3")) {
							dirName = "cangancheck";
						}
						try {
							ExcellUtils.writeExcell(ExcellUtils.getPointCheckList(checkDetail), dirName, checkDetail.checkDate + "type" + checkDetail.checkType, "checkwith" + checkDetail.checkType);
							File fileSd = new File(ExcellUtils.sdcardfilePath);
							int saveType = 1;
							if (fileSd.isDirectory()) {
								saveType = 1;
							} else {
								File upanFile = new File(ExcellUtils.upanfilePath);
								if (upanFile.isDirectory()) {
									saveType = 2;
								}
							}
							if (saveType == 1) {
								showToast("已成功导出到sd卡");
							} else if (saveType == 2) {
								showToast("已成功导出到U盘");
							}
							
							showLoading(false, 3);
						} catch (Exception e) {
							e.printStackTrace();
							Log.d("zhao", "导出失败==== " + e.getMessage());
							showToast("导出失败");
							showLoading(false, 3);
						}
					}
				}
			}).start();
			break;

		default:
			break;
		}

	}

}
