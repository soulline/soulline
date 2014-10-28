package com.cdd.fragment;

import java.util.ArrayList;

import com.cdd.R;
import com.cdd.alarmpage.AlarmAdapter;
import com.cdd.mode.AlarmItemEntry;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class AlarmFragment extends Fragment {
	private Context context;
	
	private View view;
	
	private ListView alarmList;
	
	private AlarmAdapter adapter;
	
	public AlarmFragment(Context context) {
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.alarm_fragment, null);
		initView();
		return view;
	}
	
	private void initView() {
		alarmList = (ListView) view.findViewById(R.id.alarm_list);
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
		item2.alarmDate = "2014年10月27日";
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
}