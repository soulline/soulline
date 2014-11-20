package com.cdd.activity.alarmpage;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.cdd.R;
import com.cdd.base.BaseActivity;
import com.cdd.mode.RemindEntry;
import com.cdd.mode.RemindInfo;
import com.cdd.net.RequestListener;
import com.cdd.operater.RemindListOp;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

public class AlarmActivity extends BaseActivity implements OnClickListener {

	private PullToRefreshListView alarmList;

	private AlarmAdapter adapter;

	private RemindInfo remindInfo = new RemindInfo();
	
	private TextView announcementContent;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.alarm_activity);
		initView();
		initContent();
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
							if (remindInfo.noticeList.size() > 0) {
								announcementContent.setVisibility(View.VISIBLE);
								announcementContent.setText(remindInfo.noticeList.get(0).message);
							} else {
								announcementContent.setVisibility(View.GONE);
							}
							if (remindInfo.remindList.size() > 0) {
								alarmList.setVisibility(View.VISIBLE);
								findViewById(R.id.empty_content_layout).setVisibility(View.GONE);
								initAdapater(remindInfo.remindList);
							} else {
								alarmList.setVisibility(View.GONE);
								findViewById(R.id.empty_content_layout).setVisibility(View.VISIBLE);
							}
						}
					});
			
			}
		});
	}
	
	private void initContent() {
		requestRemindInfo();
	}

	private void initView() {
		findViewById(R.id.add_alarm_layout).setOnClickListener(this);
		announcementContent = (TextView) findViewById(R.id.announcement_content);
		findViewById(R.id.empty_content_layout).setOnClickListener(this);
		alarmList = (PullToRefreshListView) findViewById(R.id.alarm_list);
		initAlarmListView();
	}

	private void initAlarmListView() {
		alarmList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
			}
		});
		alarmList.setMode(Mode.PULL_FROM_START);
		alarmList.getLoadingLayoutProxy(true, true).setPullLabel(
				"下拉刷新...");
		alarmList.getLoadingLayoutProxy(true, true).setRefreshingLabel(
				"正在刷新...");
		alarmList.getLoadingLayoutProxy(true, true).setReleaseLabel(
				"释放刷新...");
		alarmList.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshView.isHeaderShown()) {
					requestRemindInfo();
				}
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
			
		case R.id.empty_content_layout:
			requestRemindInfo();
			break;
			
		default:
			break;
		}

	}

}
