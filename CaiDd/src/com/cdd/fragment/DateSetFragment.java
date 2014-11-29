package com.cdd.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.cdd.R;
import com.cdd.third.spinnerwheel.AbstractWheel;
import com.cdd.third.spinnerwheel.AbstractWheelTextAdapter;
import com.cdd.third.spinnerwheel.OnWheelChangedListener;
import com.cdd.third.spinnerwheel.WheelVerticalView;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class DateSetFragment extends DialogFragment implements OnClickListener {

	private View view;

	private TextView dateTitle;

	private WheelVerticalView yearSpinner, monthSpinner, daySpinner;

	private long currentTime = 0L;

	private int startYear = 1970;

	private int endYear = 2050;

	private Calendar calendar = Calendar.getInstance();

	private DateAdapter yearAdapter, monthAdapter, dayAdapter;

	private BaseFragmentListener listener;

	public DateSetFragment() {

	}

	public DateSetFragment(Context context, Bundle b) {
		currentTime = b.getLong("current_time", 0L);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.date_set_fragment, null);
		initView();
		initContent();
		return view;
	}

	public void addBaseFragmentListener(BaseFragmentListener listener) {
		this.listener = listener;
	}

	private void initContent() {
		ArrayList<String> yearList = getYearList();
		ArrayList<String> monthList = getMonthList();
		yearAdapter = new DateAdapter(getActivity());
		yearAdapter.setDateList(yearList);
		yearSpinner.setViewAdapter(yearAdapter);
		monthAdapter = new DateAdapter(getActivity());
		monthAdapter.setDateList(monthList);
		monthSpinner.setViewAdapter(monthAdapter);
		if (currentTime == 0L) {
			currentTime = System.currentTimeMillis();
		}
		calendar.setTimeInMillis(currentTime);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		month++;
		ArrayList<String> dayList = getDays(year, month);
		dayAdapter = new DateAdapter(getActivity());
		dayAdapter.setDateList(dayList);
		daySpinner.setViewAdapter(dayAdapter);
		yearSpinner.setCurrentItem(year - startYear);
		monthSpinner.setCurrentItem(month - 1);
		daySpinner.setCurrentItem(day - 1);
	}

	private ArrayList<String> getYearList() {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = startYear; i < (endYear + 1); i++) {
			String year = i + "";
			list.add(year);
		}
		return list;
	}

	private ArrayList<String> getMonthList() {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 1; i < 13; i++) {
			String month = "";
			if (i < 10) {
				month = "0" + i;
			} else {
				month = i + "";
			}
			list.add(month);
		}
		return list;
	}

	private ArrayList<String> getDays(int year, int month) {
		ArrayList<String> list = new ArrayList<String>();
		int total = 32;
		if ((month == 1) || (month == 3) || (month == 5) || (month == 7)
				|| (month == 8) || (month == 10) || (month == 12)) {
			total = 32;
		} else if ((month == 4) || (month == 6) || (month == 9)
				|| (month == 11)) {
			total = 31;
		} else if (((year % 4) == 0) && month == 2) {
			total = 30;
		} else if (((year % 4) > 0) && month == 2) {
			total = 29;
		}
		for (int i = 1; i < total; i++) {
			String day = "";
			if (i < 10) {
				day = "0" + i;
			} else {
				day = i + "";
			}
			list.add(day);
		}
		return list;
	}

	private void initView() {
		dateTitle = (TextView) view.findViewById(R.id.date_title);
		yearSpinner = (WheelVerticalView) view.findViewById(R.id.year_spinner);
		monthSpinner = (WheelVerticalView) view
				.findViewById(R.id.month_spinner);
		daySpinner = (WheelVerticalView) view.findViewById(R.id.day_spinner);
		view.findViewById(R.id.date_cancel).setOnClickListener(this);
		view.findViewById(R.id.date_ok).setOnClickListener(this);
		view.findViewById(R.id.date_bg_click).setOnClickListener(this);
		initWheelView();
	}

	private void initWheelView() {
		yearSpinner.setCyclic(true);
		yearSpinner.setVisibleItems(5);
		yearSpinner.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(AbstractWheel wheel, int oldValue,
					int newValue) {
				if (yearAdapter != null) {
					String yearStr = yearAdapter.getItemText(newValue)
							.toString();
					int year = Integer.valueOf(yearStr);
					calendar.set(Calendar.YEAR, year);
					SimpleDateFormat format = new SimpleDateFormat(
							"yyyy-MM-dd E");
					dateTitle.setText(format.format(calendar.getTime()));
					int month = monthSpinner.getCurrentItem();
					if (month == 1) {
						reloadDaySpinner(year, month + 1);
					}
				}
			}
		});
		monthSpinner.setCyclic(true);
		monthSpinner.setVisibleItems(5);
		monthSpinner.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(AbstractWheel wheel, int oldValue,
					int newValue) {
				String monthStr = monthAdapter.getItemText(newValue).toString();
				int month = Integer.valueOf(monthStr);
				calendar.set(Calendar.MONTH, month - 1);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd E");
				dateTitle.setText(format.format(calendar.getTime()));
				int yearIndex = yearSpinner.getCurrentItem();
				int year = yearIndex + startYear;
				reloadDaySpinner(year, month);
			}
		});
		daySpinner.setCyclic(true);
		daySpinner.setVisibleItems(5);
		daySpinner.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(AbstractWheel wheel, int oldValue,
					int newValue) {
				String dayStr = dayAdapter.getItemText(newValue).toString();
				int day = Integer.valueOf(dayStr);
				calendar.set(Calendar.DAY_OF_MONTH, day);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd E");
				dateTitle.setText(format.format(calendar.getTime()));
			}
		});
	}

	private void reloadDaySpinner(int year, int month) {
		ArrayList<String> dayList = getDays(year, month);
		int oldDayIndex = daySpinner.getCurrentItem();
		dayAdapter = null;
		dayAdapter = new DateAdapter(getActivity());
		dayAdapter.setDateList(dayList);
		daySpinner.setViewAdapter(dayAdapter);
		if (oldDayIndex > 27) {
			daySpinner.setCurrentItem(27);
		} else {
			daySpinner.setCurrentItem(oldDayIndex);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.date_cancel:
			dismissAllowingStateLoss();
			break;

		case R.id.date_ok:
			if (listener != null) {
				listener.onCallBack(calendar.getTimeInMillis());
			}
			dismissAllowingStateLoss();
			break;

		case R.id.date_bg_click:
			dismissAllowingStateLoss();
			break;

		default:
			break;
		}

	}

	public class DateAdapter extends AbstractWheelTextAdapter {

		private ArrayList<String> list = new ArrayList<String>();

		protected DateAdapter(Context context) {
			super(context, R.layout.time_picker_custom_time, R.id.time_value);
		}

		public void setDateList(ArrayList<String> list) {
			this.list = list;
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			TextView date = (TextView) view.findViewById(R.id.time_value);
			date.setText(list.get(index));
			return view;
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return list.get(index);
		}

	}
}
