package com.cdd.activity.alarmpage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.cdd.R;
import com.cdd.mode.AlarmItemEntry;
import com.cdd.mode.RemindEntry;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AlarmAdapter extends ArrayAdapter<RemindEntry> {

	private Context context;
	
	private OnRemoveListener listener;
	
	public interface OnRemoveListener {
		public void onRemove(int position);
	}
	
	public AlarmAdapter(Context context) {
		super(context, 0);
		this.context = context;
	}

	public void addData(ArrayList<RemindEntry> list) {
		synchronized (list) {
			for (RemindEntry entry : list) {
				add(entry);
			}
		}
	}
	
	public void addOnRemoveListener(OnRemoveListener listener) {
		this.listener = listener;
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
		RemindEntry item = getItem(position);
		String typeStr = "";
		String sencondType = "";
		if (item.type.equals("1")) {
			typeStr = "考试倒计时";
			sencondType = "考试期";
		} else if (item.type.equals("2")) {
			typeStr = "报名倒计时";
			sencondType = "报名期";
		} else if (item.type.equals("3")) {
			typeStr = "学习计划";
		}
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy年MM月dd日");
		holder.alarmType.setText(typeStr);
		
		if (!TextUtils.isEmpty(item.des) && !item.des.equals("null")) {
			holder.alarmDetail.setText(item.des);
		}
		holder.alarmTitle.setText(item.title);
		
		int days = 0;
		try {
			Date date = format1.parse(item.remindTime);
			holder.alarmDate.setText(format2.format(date));
			long nowTime = System.currentTimeMillis();
			days = (int)((nowTime - date.getTime()) / (24L * 3600L * 1000L));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (days > 0) {
			holder.alarmDays.setVisibility(View.VISIBLE);
			convertView.findViewById(R.id.days_title).setVisibility(View.VISIBLE);
			holder.alarmBgLayout.setBackgroundResource(R.drawable.alarm_item_bg);
			holder.alarmDays.setText(days + "");
		} else if (days == 0) {
			holder.alarmBgLayout.setBackgroundResource(R.drawable.alarm_item_today_bg);
			holder.alarmDays.setVisibility(View.GONE);
			convertView.findViewById(R.id.days_title).setVisibility(View.GONE);
		}
		if (TextUtils.isEmpty(sencondType)) {
			holder.alarmNote.setVisibility(View.GONE);
		} else {
			holder.alarmNote.setVisibility(View.VISIBLE);
			holder.alarmNote.setText(sencondType);
		}
		if (position == (getCount() - 1)) {
			convertView.findViewById(R.id.bottom_line).setVisibility(View.GONE);
		} else {
			convertView.findViewById(R.id.bottom_line).setVisibility(View.VISIBLE);
		}
		final int removePostion = position;
		convertView.findViewById(R.id.alarm_rm_layout).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onRemove(removePostion);
				}
			}
		});
		return convertView;
	}
	
	
	public class ViewHolder {
		public TextView alarmType, alarmDays, alarmDate, alarmNote, alarmDetail, alarmTitle;
		public RelativeLayout alarmBgLayout;
	}
	

}
