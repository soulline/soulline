package com.asag.serial;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import com.asag.serial.mode.RightDataEntry;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;



@SuppressLint("NewApi")
public class PadFragment extends Fragment{

	private ListView listview;
	
	private View view;
	
	private ImageView co2status, o2status, ph3status, rhstatus, tstatus;
	
	private TextView co2Tx, o2Tx, ph3tx, rhtx, ttx;
	
	private RightAdatper adapter;
	
	private TextView paikongAleaveMinute, checkAleaveMinute;
	
	private TextView paikongTitle, checkTitle;
	
	private TextView dateNow;
	
	ArrayList<RightDataEntry> rightList = new ArrayList<RightDataEntry>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.pad_pager, null); //先解析file.xml布局，得到一个view
		initView();
		initContent();
		return view;
	}
	
	private void initView() {
		listview = (ListView) view.findViewById(R.id.listid);
		co2status = (ImageView) view.findViewById(R.id.co2_status_img);
		o2status = (ImageView) view.findViewById(R.id.o2_status_img);
		ph3status = (ImageView) view.findViewById(R.id.ph3_status_img);
		rhstatus = (ImageView) view.findViewById(R.id.rh_status_img);
		tstatus = (ImageView) view.findViewById(R.id.t_status_img);
		co2Tx = (TextView) view.findViewById(R.id.co2id);
		o2Tx = (TextView) view.findViewById(R.id.o2id);
		ph3tx = (TextView) view.findViewById(R.id.ph3id);
		rhtx = (TextView) view.findViewById(R.id.rhid);
		ttx = (TextView) view.findViewById(R.id.tid);
		paikongAleaveMinute = (TextView) view.findViewById(R.id.paikong_aleave_minute);
		checkAleaveMinute = (TextView) view.findViewById(R.id.check_aleave_minute);
		paikongTitle = (TextView) view.findViewById(R.id.paikong_title);
		checkTitle = (TextView) view.findViewById(R.id.check_title);
	}
	
	public void updatePaikongMinute(String value) {
		paikongAleaveMinute.setText(value);
		if (value.equals("0.0")) {
			showPointView(paikongTitle, true);
		} else {
			showPointView(paikongTitle, false);
		}
	}
	
	public void updateCheckMinute(String value) {
		checkAleaveMinute.setText(value);
		if (value.equals("0.0")) {
			showPointView(checkTitle, true);
		} else {
			showPointView(checkTitle, false);
		}
	}
	
	private void showPointView(TextView title, boolean isRed) {
		Drawable drawable = null;
		if (isRed) {
			drawable = getResources().getDrawable(
					R.drawable.redpoint);
		} else {
			drawable = getResources().getDrawable(
					R.drawable.big_greenpoint);
		}
		if (drawable != null) {
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			title.setCompoundDrawables(drawable, null, null, null);
		}
	}
	
	public void setCo2Value(long value) {
		if ((value < 50000 && value > 0) || value == 50000) {
			co2status.setBackgroundResource(R.drawable.greenpoint);
		} else if (value > 50000) {
			co2status.setBackgroundResource(R.drawable.redpoint);
		}
		co2Tx.setText(value + "");
	}
	
	public void setO2Value(float value) {
		if (value > 0) {
			o2status.setBackgroundResource(R.drawable.greenpoint);
		} else {
			o2status.setBackgroundResource(R.drawable.redpoint);
		}
		o2Tx.setText(value + "");
	}
	
	public void setPH3Value(long value) {
		if ((value < 50000 && value > 0) || value == 50000) {
			ph3status.setBackgroundResource(R.drawable.greenpoint);
		} else if (value > 50000) {
			ph3status.setBackgroundResource(R.drawable.redpoint);
		}
		ph3tx.setText(value + "");
	}
	
	public void setRHValue(float value) {
		if (value > 30.0f) {
			rhstatus.setBackgroundResource(R.drawable.redpoint);
		} else if ((value < 30.0f && value > 0) || value == 30.0f) {
			rhstatus.setBackgroundResource(R.drawable.greenpoint);
		}
		rhtx.setText(value + "");
	}
	
	public void setTValue(float value) {
		if ((value < 80.0f && value > 0.0f) || value == 80.0f) {
			tstatus.setBackgroundResource(R.drawable.greenpoint);
		} else if (value > 80.0f) {
			tstatus.setBackgroundResource(R.drawable.redpoint);
		}
		ttx.setText(value + "");
	}
	
	private void initContent() {
	}

	public void initAdapter(ArrayList<RightDataEntry> list) {
		if (adapter == null) {
			adapter = new RightAdatper(getActivity());
			rightList = list;
			Collections.sort(rightList, comparatorRight);
			adapter.addData(rightList);
			listview.setAdapter(adapter);
		} else {
			adapter.clear();
			rightList.clear();
			rightList = list;
			Collections.sort(rightList, comparatorRight);
			adapter.addData(rightList);
			adapter.notifyDataSetChanged();
		}
	}
	
	public void clearRightData() {
		if (adapter != null) {
			adapter.clear();
			rightList.clear();
			adapter.notifyDataSetChanged();
		}
	}
	
	public void addData(RightDataEntry data) {
		int index = -1;
		for (int i=0; i< rightList.size(); i++) {
			RightDataEntry entry = rightList.get(i);
			if (entry.number.equals(data.number)) {
				index = i;
				break;
			}
		}
		if (index != -1 && rightList != null) {
			rightList.remove(index);
		}
		if (adapter == null) {
			adapter = new RightAdatper(getActivity());
			rightList.add(data);
			adapter.add(data);
			listview.setAdapter(adapter);
		} else {
			rightList.add(data);
			adapter.clear();
			Collections.sort(rightList, comparatorRight);
			adapter.addData(rightList);
			adapter.notifyDataSetChanged();
		}
	}
	
	public Comparator<RightDataEntry> comparatorRight = new Comparator<RightDataEntry>() {

		@Override
		public int compare(RightDataEntry rd1, RightDataEntry rd2) {
			int number1 = Integer.valueOf(rd1.number);
			int number2 = Integer.valueOf(rd2.number);
			if (number1 < number2) {
				return -1;
			} else if (number1 == number2) {
				return 0;
			}
			return 1;
		}
	};
	
	@Override
    public void onResume()
    {
        super.onResume();
        System.out.println("PadFragment onResume");
    }
    
    @Override
    public void onPause()
    {
        super.onPause();
        System.out.println("PadtFragment onPause");
    }
    
    @Override
    public void onStop()
    {
        super.onStop();
        System.out.println("RightFragment onStop");
    }
	
}