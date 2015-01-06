package com.asag.serial;

import java.util.ArrayList;

import com.asag.serial.mode.PointInfo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PointFangAdapter extends ArrayAdapter<PointInfo> {

	private Context context;
	
	private float size = 1.0f;
	
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
	
	public PointFangAdapter(Context context) {
		super(context, 0);
		this.context = context;
	}

	public void addData(ArrayList<PointInfo> list) {
		synchronized (list) {
			for (PointInfo info : list) {
				add(info);
			}
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.point_quadrate_item, null);
			holder.way = (TextView) convertView.findViewById(R.id.way_title);
			holder.x_point = (TextView) convertView.findViewById(R.id.x_point);
			holder.y_point = (TextView) convertView.findViewById(R.id.y_point);
			holder.z_point = (TextView) convertView.findViewById(R.id.z_point);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		PointInfo info = getItem(position);
		holder.way.setTextSize(20.0f* size);
		holder.x_point.setTextSize(14.0f * size);
		holder.y_point.setTextSize(14.0f* size);
		holder.z_point.setTextSize(14.0f * size);
		holder.way.setText(info.way);
		holder.x_point.setText(info.xpoint);
		holder.y_point.setText(info.ypoint);
		holder.z_point.setText(info.zpoint);
		return convertView;
	}
	
	public class ViewHolder {
		public TextView way, x_point, y_point, z_point;
	}
	
}
