package com.cdd.alarmpage;

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

public class AlarmActivity extends BaseActivity implements OnClickListener{

	
	private ListView alarmList;
	
	private AlarmAdapter adapter;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.alarm_activity);
		initView();
	}

	private void initView() {
		findViewById(R.id.add_alarm_layout).setOnClickListener(this);
		alarmList = (ListView) findViewById(R.id.alarm_list);
		ArrayList<AlarmItemEntry> list = new ArrayList<AlarmItemEntry>();
		AlarmItemEntry item1 = new AlarmItemEntry();
		item1.alarmDate = "2014年12月30日";
		item1.alarmDetail = "今天应该复习经济法了";
		item1.alarmSecondType = "报名期";
		item1.alarmTitle = "初级会计证报名";
		item1.alarmtype = "报名倒计时";
		item1.aliveDays = "30";
		list.add(item1);
		AlarmItemEntry item2 = new AlarmItemEntry();
		item2.alarmDate = "2014年10月28日";
		item2.alarmDetail = "今天应该复习经济法了";
		item2.alarmSecondType = "";
		item2.alarmTitle = "今日学习计划";
		item2.alarmtype = "学习计划";
		item2.aliveDays = "";
		list.add(item2);
		AlarmItemEntry item3 = new AlarmItemEntry();
		item3.alarmDate = "2014年12月30日";
		item3.alarmDetail = "今天应该复习经济法了";
		item3.alarmSecondType = "考试期";
		item3.alarmTitle = "初级会计证考试";
		item3.alarmtype = "考试倒计时";
		item3.aliveDays = "30";
		list.add(item3);
		initAdapater(list);
		alarmList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
			}
		});
	}
	
	public void initAdapater(ArrayList<AlarmItemEntry> list) {
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
