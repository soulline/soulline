package com.asag.serial;

import java.util.ArrayList;
import java.util.HashMap;

import com.asag.serial.mode.PointRecord;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class PointRecordAdapter extends ArrayAdapter<PointRecord> {

	private Context context;

	private HashMap<String, Integer> drawableMap = new HashMap<String, Integer>();

	public PointRecordAdapter(Context context) {
		super(context, 0);
		initMap();
	}

	public void addData(ArrayList<PointRecord> list) {
		synchronized (list) {
			for (PointRecord point : list) {
				add(point);
			}
		}
	}

	private void initMap() {
		drawableMap.put("1", R.drawable.red_state);
		drawableMap.put("2", R.drawable.orange_state);
		drawableMap.put("3", R.drawable.yellow_state);
		drawableMap.put("4", R.drawable.green_state);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.point_record_item,
					null);
			holder.item_check = (CheckBox) convertView
					.findViewById(R.id.item_check);
			holder.item_date = (TextView) convertView
					.findViewById(R.id.item_date);
			holder.state_0_icon = (ImageView) convertView
					.findViewById(R.id.state_0_icon);
			holder.state_1_icon = (ImageView) convertView
					.findViewById(R.id.state_1_icon);
			holder.state_2_icon = (ImageView) convertView
					.findViewById(R.id.state_2_icon);
			holder.state_3_icon = (ImageView) convertView
					.findViewById(R.id.state_3_icon);
			holder.state_4_icon = (ImageView) convertView
					.findViewById(R.id.state_4_icon);
			holder.state_5_icon = (ImageView) convertView
					.findViewById(R.id.state_5_icon);
			holder.state_6_icon = (ImageView) convertView
					.findViewById(R.id.state_6_icon);
			holder.state_7_icon = (ImageView) convertView
					.findViewById(R.id.state_7_icon);
			holder.state_8_icon = (ImageView) convertView
					.findViewById(R.id.state_8_icon);
			holder.state_9_icon = (ImageView) convertView
					.findViewById(R.id.state_9_icon);
			holder.state_10_icon = (ImageView) convertView
					.findViewById(R.id.state_10_icon);
			holder.state_11_icon = (ImageView) convertView
					.findViewById(R.id.state_11_icon);
			holder.state_12_icon = (ImageView) convertView
					.findViewById(R.id.state_12_icon);
			holder.state_13_icon = (ImageView) convertView
					.findViewById(R.id.state_13_icon);
			holder.state_14_icon = (ImageView) convertView
					.findViewById(R.id.state_14_icon);
			holder.state_15_icon = (ImageView) convertView
					.findViewById(R.id.state_15_icon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		PointRecord record = getItem(position);
		holder.item_check.setChecked(record.isCheck);
		holder.item_date.setText(record.date);
		holder.state_0_icon.setBackgroundResource(drawableMap.get(record.way0State));
		holder.state_1_icon.setBackgroundResource(drawableMap.get(record.way1State));
		holder.state_2_icon.setBackgroundResource(drawableMap.get(record.way2State));
		holder.state_3_icon.setBackgroundResource(drawableMap.get(record.way3State));
		holder.state_4_icon.setBackgroundResource(drawableMap.get(record.way4State));
		holder.state_5_icon.setBackgroundResource(drawableMap.get(record.way5State));
		holder.state_6_icon.setBackgroundResource(drawableMap.get(record.way6State));
		holder.state_7_icon.setBackgroundResource(drawableMap.get(record.way7State));
		holder.state_8_icon.setBackgroundResource(drawableMap.get(record.way8State));
		holder.state_9_icon.setBackgroundResource(drawableMap.get(record.way9State));
		holder.state_10_icon.setBackgroundResource(drawableMap.get(record.way10State));
		holder.state_11_icon.setBackgroundResource(drawableMap.get(record.way11State));
		holder.state_12_icon.setBackgroundResource(drawableMap.get(record.way12State));
		holder.state_13_icon.setBackgroundResource(drawableMap.get(record.way13State));
		holder.state_14_icon.setBackgroundResource(drawableMap.get(record.way14State));
		holder.state_15_icon.setBackgroundResource(drawableMap.get(record.way15State));
		return convertView;
	}

	public class ViewHolder {
		public CheckBox item_check;
		public TextView item_date;

		public ImageView state_0_icon, state_1_icon, state_2_icon,
				state_3_icon, state_4_icon, state_5_icon, state_6_icon,
				state_7_icon, state_8_icon, state_9_icon, state_10_icon,
				state_11_icon, state_12_icon, state_13_icon, state_14_icon,
				state_15_icon;
	}

}
