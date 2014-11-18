package com.cdd.activity.alarmpage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.cdd.R;
import com.cdd.base.BaseActivity;
import com.cdd.widget.KCalendar;
import com.cdd.widget.KCalendar.OnCalendarClickListener;
import com.cdd.widget.KCalendar.OnCalendarDateChangedListener;

public class RemindCalendarActivity extends BaseActivity implements OnClickListener{

	private String date = "";
	
	private KCalendar remindCalendar;
	
	private TextView monthOfYear, weekValue, yearValue;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.calendar_detail_activity);
		initTitle("初级会计资格证");
		initView();
	}
	
	private void initView() {
		monthOfYear = (TextView) findViewById(R.id.month_of_year);
		weekValue = (TextView) findViewById(R.id.week_value);
		yearValue = (TextView) findViewById(R.id.year_value);
		remindCalendar = (KCalendar) findViewById(R.id.remind_calendar);
		initCalendar();
		findViewById(R.id.last_month_btn).setOnClickListener(this);
		findViewById(R.id.next_month_btn).setOnClickListener(this);
	}

	private void initCalendar() {
		if (!TextUtils.isEmpty(date)) {

			int year = Integer.parseInt(date.substring(0,
					date.indexOf("-")));
			int month = Integer.parseInt(date.substring(
					date.indexOf("-") + 1, date.lastIndexOf("-")));
			setDateText(year, month);
			remindCalendar.showCalendar(year, month);
			remindCalendar.setCalendarDayBgColor(date,
					R.drawable.calendar_date_focused);				
		}
		
		List<String> list = new ArrayList<String>(); //设置标记列表
		list.add("2014-11-18");
		list.add("2014-11-19");
		list.add("2014-11-20");
		list.add("2014-11-21");
		remindCalendar.setCalendarDaysBgColor(list, R.drawable.calendar_date_focused);

		//监听所选中的日期
		remindCalendar.setOnCalendarClickListener(new OnCalendarClickListener() {

			public void onCalendarClick(int row, int col, String dateFormat) {
				date = dateFormat;
				int month = Integer.parseInt(dateFormat.substring(
						dateFormat.indexOf("-") + 1,
						dateFormat.lastIndexOf("-")));
				int year = Integer.parseInt(dateFormat.substring(0,
						dateFormat.indexOf("-")));
				setDateText(year, month);
				if (remindCalendar.getCalendarMonth() - month == 1//跨年跳转
						|| remindCalendar.getCalendarMonth() - month == -11) {
					remindCalendar.lastMonth();
					
				} else if (month - remindCalendar.getCalendarMonth() == 1 //跨年跳转
						|| month - remindCalendar.getCalendarMonth() == -11) {
					remindCalendar.nextMonth();
					
				} else {
					remindCalendar.removeAllBgColor(); 
					remindCalendar.setCalendarDayBgColor(dateFormat,
							R.drawable.calendar_date_focused);
				}
			}
		});

		//监听当前月份
		remindCalendar.setOnCalendarDateChangedListener(new OnCalendarDateChangedListener() {
			public void onCalendarDateChanged(int year, int month) {
				setDateText(year, month);
			}
		});
	}
	
	private void setDateText(int year, int month) {
		monthOfYear.setText(month + "月");
		yearValue.setText(year + "年");
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format2 = new SimpleDateFormat("E");
		try {
			Date dateN = format1.parse(date);
			weekValue.setText(format2.format(dateN));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.last_month_btn:
			remindCalendar.lastMonth();
			break;
			
		case R.id.next_month_btn:
			remindCalendar.nextMonth();
			break;

		default:
			break;
		}
		
	}
}
