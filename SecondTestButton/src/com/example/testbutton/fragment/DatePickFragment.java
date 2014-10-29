package com.example.testbutton.fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.example.testbutton.R;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class DatePickFragment extends DialogFragment implements OnClickListener {

	private View view;

	private DatePicker picker;
	
	private TimePicker timePicker;

	private Context context;

	private BaseFragmentListener listener;

	private long time = 0L;
	
	private boolean isTime = false;

	public DatePickFragment(Context context, Bundle b) {
		this.context = context;
		if (b != null) {
			time = b.getLong("choose_time");
		}
	}
	
	public void setIsTime(boolean isTime) {
		this.isTime = isTime;
	}
	
	public void initIsTime() {
		if (isTime) {
			timePicker.setVisibility(View.VISIBLE);
		} else {
			timePicker.setVisibility(View.GONE);
		}
	}

	public void initTime() {
		if (time != 0L) {
			Calendar calendar = Calendar.getInstance(Locale.CHINA);
			Date date = new Date(time);
			calendar.setTime(date);
			picker.init(calendar.get(Calendar.YEAR), 
					calendar.get(Calendar.MONTH), 
					calendar.get(Calendar.DAY_OF_MONTH), null);
			if (isTime) {
				timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
				timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
			}
		} else {
			Calendar calendar = Calendar.getInstance(Locale.CHINA);
			Date date = new Date();
			calendar.setTime(date);
			picker.init(calendar.get(Calendar.YEAR), 
					calendar.get(Calendar.MONTH), 
					calendar.get(Calendar.DAY_OF_MONTH), null);
			if (isTime) {
				timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
				timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
			}
		}
	}

	public void addFragmentListener(BaseFragmentListener listener) {
		this.listener = listener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.date_pick_fragment, null);
		initView();
		initIsTime();
		initTime();
		return view;
	}

	private void initView() {
		picker = (DatePicker) view.findViewById(R.id.date_pick);
		timePicker = (TimePicker) view.findViewById(R.id.time_pick);
		view.findViewById(R.id.content_ok).setOnClickListener(this);
		view.findViewById(R.id.content_cancel).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.content_ok:
			Calendar calendar = Calendar.getInstance(Locale.CHINA);
			int year = picker.getYear();
			int month = picker.getMonth();
			int dayOfMonth = picker.getDayOfMonth();
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, month);
			calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			if (isTime) {
				int hourofDay = timePicker.getCurrentHour();
				int minute = timePicker.getCurrentMinute();
				calendar.set(Calendar.HOUR_OF_DAY, hourofDay);
				calendar.set(Calendar.MINUTE, minute);
				calendar.set(Calendar.SECOND, 0);
			}
			if (listener != null) {
				listener.onCallBack(calendar.getTime());
			}
			dismissAllowingStateLoss();
			break;

		case R.id.content_cancel:
			dismissAllowingStateLoss();
			break;
		default:
			break;
		}

	}

}
