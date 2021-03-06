package com.asag.serial;

import java.util.ArrayList;
import java.util.HashMap;

import com.asag.serial.mode.PointItemRecord;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class CanganRecordAdapter extends ArrayAdapter<PointItemRecord> {

	private Context context;

	private HashMap<String, Integer> drawableMap = new HashMap<String, Integer>();

	private float size = 2.2f;
	
	private OnPointCheckListener listener;

	public interface OnPointCheckListener {
		public void onCheck(int position);
	}

	public void addOnPointCheckListener(OnPointCheckListener listener) {
		this.listener = listener;
	}

	public CanganRecordAdapter(Context context) {
		super(context, 0);
		this.context = context;
		initMap();
	}

	public void setItemTextSize(int bili) {
		switch (bili) {
		case 1:
			size = 1.4f;
			break;
		case 2:
			size = 1.6f;
			break;

		case 3:
			size = 1.8f;
			break;

		case 4:
			size = 2.0f;
			break;

		case 5:
			size = 2.2f;
			break;

		default:
			break;
		}
	}

	private void initMap() {
		drawableMap.put("1", R.drawable.green_state);
		drawableMap.put("2", R.drawable.yellow_state);
		drawableMap.put("3", R.drawable.orange_state);
		drawableMap.put("4", R.drawable.red_state);
	}

	public void addData(ArrayList<PointItemRecord> list) {
		synchronized (list) {
			for (PointItemRecord point : list) {
				add(point);
			}
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.cangan_record_item,
					null);
			holder.item_check = (ImageView) convertView
					.findViewById(R.id.item_check);
			holder.item_date = (TextView) convertView
					.findViewById(R.id.item_date);
			holder.ph3_value = (TextView) convertView
					.findViewById(R.id.ph3_value);
			holder.o2_value = (TextView) convertView
					.findViewById(R.id.o2_value);
			holder.co2_value = (TextView) convertView
					.findViewById(R.id.co2_value);
			holder.safe_state = (ImageView) convertView
					.findViewById(R.id.safe_state_icon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		PointItemRecord record = getItem(position);
		holder.item_date.setTextSize(14.0f * size);
		holder.ph3_value.setTextSize(14.0f * size);
		holder.o2_value.setTextSize(14.0f * size);
		holder.co2_value.setTextSize(14.0f * size);
		if (record.isCheck) {
			holder.item_check.setBackgroundResource(R.drawable.point_item_check);
		} else {
			holder.item_check.setBackgroundResource(R.drawable.point_item_normal);
		}
		holder.item_date.setText(record.checkDate);
		holder.ph3_value.setText(record.ph3Value);
		holder.o2_value.setText(record.o2Value);
		holder.co2_value.setText(record.co2);
		Log.d("zhao", "record status : " + record.status);
		holder.safe_state.setBackgroundResource(drawableMap.get(record.status + ""));
		if (record.isCheck) {
			convertView.findViewById(R.id.record_item_layout)
					.setBackgroundColor(
							context.getResources().getColor(R.color.gray));
		} else {
			convertView.findViewById(R.id.record_item_layout)
					.setBackgroundColor(
							context.getResources().getColor(R.color.white));
		}
		return convertView;
	}

	public class ViewHolder {
		public ImageView item_check;
		public TextView item_date, ph3_value, o2_value, co2_value;

		public ImageView safe_state;

	}

}
