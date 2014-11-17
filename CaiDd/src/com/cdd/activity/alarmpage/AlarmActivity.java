package com.cdd.activity.alarmpage;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.cdd.R;
import com.cdd.base.BaseActivity;
import com.cdd.mode.AlarmItemEntry;
import com.cdd.mode.RemindEntry;
import com.cdd.mode.RemindInfo;
import com.cdd.net.RequestListener;
import com.cdd.operater.RemindListOp;

public class AlarmActivity extends BaseActivity implements OnClickListener {

	private ListView alarmList;

	private AlarmAdapter adapter;

	private RemindInfo remindInfo = new RemindInfo();

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.alarm_activity);
		initView();
	}

	private void requestRemindInfo() {
		final RemindListOp remindListOp = new RemindListOp(context);
		remindListOp.onRequest(new RequestListener() {

			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCallBack(Object data) {
				remindInfo = remindListOp.getRemind();
				handler.post(new Runnable() {

					@Override
					public void run() {
						initAdapater(remindInfo.remindList);
					}
				});
			}
		});
	}

	private void initView() {
		findViewById(R.id.add_alarm_layout).setOnClickListener(this);
		alarmList = (ListView) findViewById(R.id.alarm_list);
		requestRemindInfo();
		alarmList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
		});
	}

	public void initAdapater(ArrayList<RemindEntry> list) {
		if (adapter == null) {
			adapter = new AlarmAdapter(context);
			adapter.addData(list);
			alarmList.setAdapter(adapter);
		} else {
			adapter.clear();
			adapter.addData(list);
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_alarm_layout:

			break;

		default:
			break;
		}

	}

}
