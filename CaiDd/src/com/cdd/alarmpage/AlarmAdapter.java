package com.cdd.alarmpage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.cdd.R;
import com.cdd.mode.AlarmItemEntry;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AlarmAdapter extends ArrayAdapter<AlarmItemEntry> {

	private Context context;
	
	public AlarmAdapter(Context context) {
		super(context, 0);
		this.context = context;
	}

	public void addData(ArrayList<AlarmItemEntry> list) {
		synchronized (list) {
			for (AlarmItemEntry entry : list) {
				add(entry);
			}
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.alarm_item, null);
			holder.alarmType = (TextView) convertView.findViewById(R.id.alarm_type);
			holder.alarmDays = (TextView) convertView.findViewById(R.id.alarm_days);
			holder.alarmDate = (TextView) convertView.findViewById(R.id.alarm_date);
			holder.alarmNote = (TextView) convertView.findViewById(R.id.alarm_note);
			holder.alarmDetail = (TextView) convertView.findViewById(R.id.alarm_detail);
			holder.alarmTitle = (TextView) convertView.findViewById(R.id.alarm_title);
			holder.alarmBgLayout = (RelativeLayout) convertView.findViewById(R.id.alarm_bg_layout);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		AlarmItemEntry item = getItem(position);
		holder.alarmType.setText(item.alarmtype);
		holder.alarmDate.setText(item.alarmDate);
		holder.alarmDetail.setText(item.alarmDetail);
		holder.alarmTitle.setText(item.alarmTitle);
		holder.alarmDays.setText(item.aliveDays);
		if (TextUtils.isEmpty(item.alarmSecondType)) {
			holder.alarmNote.setVisibility(View.GONE);
		} else {
			holder.alarmNote.setVisibility(View.VISIBLE);
			holder.alarmNote.setText(item.alarmSecondType);
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		String today = format.format(new Date());
		if (today.equals(item.alarmDate)) {
			holder.alarmBgLayout.setBackgroundResource(R.drawable.alarm_item_today_bg);
			holder.alarmDays.setVisibility(View.GONE);
			convertView.findViewById(R.id.days_title).setVisibility(View.GONE);
		} else {
			holder.alarmDays.setVisibility(View.VISIBLE);
			convertView.findViewById(R.id.days_title).setVisibility(View.VISIBLE);
			holder.alarmBgLayout.setBackgroundResource(R.drawable.alarm_item_bg);
		}
		if (position == (getCount() - 1)) {
			convertView.findViewById(R.id.bottom_line).setVisibility(View.GONE);
		} else {
			convertView.findViewById(R.id.bottom_line).setVisibility(View.VISIBLE);
		}
		convertView.findViewById(R.id.alarm_rm_layout).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		return convertView;
	}
	
	
	public class ViewHolder {
		public TextView alarmType, alarmDays, alarmDate, alarmNote, alarmDetail, alarmTitle;
		public RelativeLayout alarmBgLayout;
	}
	

}
