package com.cdd.fragment;

import java.util.ArrayList;

import com.cdd.R;
import com.cdd.activity.alarmpage.AlarmAdapter;
import com.cdd.base.BaseActivity;
import com.cdd.mode.RemindEntry;
import com.cdd.mode.RemindInfo;
import com.cdd.net.RequestListener;
import com.cdd.operater.RemindListOp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class AlarmFragment extends Fragment implements OnClickListener{
	
	private View view;
	
	private ListView alarmList;
	
	private AlarmAdapter adapter;
	
	private TextView announcementContent;
	
	private RemindInfo remindInfo = new RemindInfo();
	
	public AlarmFragment() {
		super();
	}
	
	public AlarmFragment(Context context) {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.alarm_fragment, null);
		initView();
		initContent();
		return view;
	}
	
	private void initView() {
		view.findViewById(R.id.add_alarm_layout).setOnClickListener(this);
		announcementContent = (TextView) view.findViewById(R.id.announcement_content);
		alarmList = (ListView) view.findViewById(R.id.alarm_list);
		alarmList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
			}
		});
	}
	
	private void requestRemindInfo() {
		final RemindListOp remindListOp = new RemindListOp(getActivity());
		remindListOp.onRequest(new RequestListener() {
			
			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCallBack(Object data) {
				remindInfo = remindListOp.getRemind();
				if (getActivity() instanceof BaseActivity) {
					((BaseActivity) getActivity()).handler.post(new Runnable() {
						
						@Override
						public void run() {
							if (remindInfo.noticeList.size() > 0) {
								announcementContent.setText(remindInfo.noticeList.get(0).message);
							}
							initAdapater(remindInfo.remindList);
						}
					});
				}
			}
		});
	}
	
	public void initContent() {
		requestRemindInfo();
	}
	
	public void initAdapater(ArrayList<RemindEntry> list) {
		if (adapter == null) {
			adapter = new AlarmAdapter(getActivity());
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
