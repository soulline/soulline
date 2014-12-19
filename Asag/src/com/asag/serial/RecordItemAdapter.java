package com.asag.serial;

import java.util.ArrayList;

import com.asag.serial.mode.PointItemRecord;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RecordItemAdapter extends ArrayAdapter<PointItemRecord> {

	private Context context;

	private float size = 1.0f;

	public void setItemTextSize(int bili) {
		switch (bili) {
		case 1:
			size = 1.0f;
			break;
		case 2:
			size = 1.2f;
			break;

		case 3:
			size = 1.4f;
			break;

		case 4:
			size = 1.6f;
			break;

		case 5:
			size = 1.8f;
			break;

		default:
			break;
		}
	}

	public RecordItemAdapter(Context context) {
		super(context, 0);
		this.context = context;
	}

	public void addData(ArrayList<PointItemRecord> list) {
		synchronized (list) {
			for (PointItemRecord record : list) {
				add(record);
			}
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.point_detail_item,
					null);
			holder.bianhao_value = (TextView) convertView
					.findViewById(R.id.bianhao_value);
			holder.co2_value = (TextView) convertView
					.findViewById(R.id.co2_value);
			holder.rh_value = (TextView) convertView
					.findViewById(R.id.rh_value);
			holder.t_value = (TextView) convertView.findViewById(R.id.t_value);
			holder.ssi_value = (TextView) convertView
					.findViewById(R.id.ssi_value);
			holder.mmi_value = (TextView) convertView
					.findViewById(R.id.mmi_value);
			convertView.setTag(convertView);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		PointItemRecord record = getItem(position);
		holder.bianhao_value.setTextSize(14.0f* size);
		holder.co2_value.setTextSize(14.0f* size);
		holder.rh_value.setTextSize(14.0f* size);
		holder.t_value.setTextSize(14.0f* size);
		holder.ssi_value.setTextSize(14.0f* size);
		holder.mmi_value.setTextSize(14.0f* size);
		holder.bianhao_value.setText(record.wayNum);
		holder.co2_value.setText(record.co2);
		holder.rh_value.setText(record.rhValue);
		holder.t_value.setText(record.tValue);
		holder.ssi_value.setText(record.ssi);
		holder.mmi_value.setText(record.mmi);
		return convertView;
	}

	public class ViewHolder {
		public TextView bianhao_value, co2_value, rh_value, t_value, ssi_value,
				mmi_value;
	}
}
