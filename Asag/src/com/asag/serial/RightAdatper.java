package com.asag.serial;

import java.util.ArrayList;

import com.asag.serial.mode.RightDataEntry;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RightAdatper extends ArrayAdapter<RightDataEntry> {

	private Context context;
	
	private float size;
	
	public RightAdatper(Context context) {
		super(context, 0);
		this.context = context;
	}

	public void addData(ArrayList<RightDataEntry> list) {
		synchronized (list) {
			for (RightDataEntry data : list) {
				add(data);
			}
		}
	}
	
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.right_data_item, null);
			holder = new ViewHolder();
			holder.co2Tv = (TextView) convertView.findViewById(R.id.co2_value);
			holder.wenduTv = (TextView) convertView.findViewById(R.id.wendu_value);
			holder.shiduTv = (TextView) convertView.findViewById(R.id.shidu_value);
			holder.dataNo = (TextView) convertView.findViewById(R.id.data_no);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();
		RightDataEntry data = getItem(position);
		holder.co2Tv.setTextSize(holder.co2Tv.getTextSize() * size);
		holder.wenduTv.setTextSize(holder.wenduTv.getTextSize() * size);
		holder.shiduTv.setTextSize(holder.shiduTv.getTextSize() * size);
		holder.dataNo.setTextSize(holder.dataNo.getTextSize() * size);
		holder.co2Tv.setText(data.co2);
		holder.wenduTv.setText(data.wendu);
		holder.shiduTv.setText(data.shidu);
		holder.dataNo.setText(data.number);
		return convertView;
	}
	
	public class ViewHolder {
		public TextView co2Tv, wenduTv, shiduTv, dataNo;
	}
}
