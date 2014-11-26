package com.cdd.activity.alarmpage;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cdd.R;
import com.cdd.base.BaseActivity;
import com.cdd.third.spinnerwheel.AbstractWheel;
import com.cdd.third.spinnerwheel.AbstractWheelTextAdapter;
import com.cdd.third.spinnerwheel.OnWheelChangedListener;
import com.cdd.third.spinnerwheel.WheelVerticalView;

public class AAATestActivity extends BaseActivity {

	private WheelVerticalView date;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.aaa_test_layout);
		date = (WheelVerticalView) findViewById(R.id.date);
		DateAdapter adapter = new DateAdapter(context);
		ArrayList<String> list = new ArrayList<String>();
		list.add("2014");
		list.add("2012");
		list.add("2013");
		list.add("2011");
		list.add("2010");
		adapter.setDateList(list);
		date.setViewAdapter(adapter);
		date.setCyclic(true);
		date.setVisibleItems(5);
		date.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
				
			}
		});
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
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			// TODO Auto-generated method stub
			return "";
		}
		
	}

}
