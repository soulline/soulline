package com.asag.serial;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.asag.serial.PointRecordAdapter.OnPointCheckListener;
import com.asag.serial.mode.PointItemRecord;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RecordItemAdapter extends ArrayAdapter<PointItemRecord> {

	private Context context;

	private float size = 2.2f;

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
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		PointItemRecord record = getItem(position);
		holder.bianhao_value.setTextSize(14.0f * size);
		holder.co2_value.setTextSize(14.0f * size);
		holder.rh_value.setTextSize(14.0f * size);
		holder.t_value.setTextSize(14.0f * size);
		holder.ssi_value.setTextSize(14.0f * size);
		holder.mmi_value.setTextSize(14.0f * size);
		holder.bianhao_value.setText(record.wayNum);
		holder.co2_value.setText(record.co2);
		holder.rh_value.setText(record.rhValue);
		float thv = Float.valueOf(record.tValue);
		BigDecimal b = new BigDecimal(thv);
		float fwendu = b.setScale(1, BigDecimal.ROUND_HALF_UP)
				.floatValue();
		holder.t_value.setText(fwendu + "");
		holder.ssi_value.setText(record.ssi);
		float mmiv = Float.valueOf(record.mmi);
		BigDecimal b1 = new BigDecimal(mmiv);
		float fmmi = b1.setScale(1, BigDecimal.ROUND_HALF_UP)
				.floatValue();
		holder.mmi_value.setText(fmmi + "");
		Log.d("zhao", "show item mmi : " + fmmi + "ssi : " + record.ssi + "  tvalue : " + record.tValue + " after tvalue : " + fwendu);
		return convertView;
	}
	

	public class ViewHolder {
		public TextView bianhao_value, co2_value, rh_value, t_value, ssi_value,
				mmi_value;
	}
}
