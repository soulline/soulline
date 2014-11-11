package com.asag.serial;

import java.util.ArrayList;

import com.asag.serial.mode.SpinnerItem;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SerialSpinnerAdapter extends ArrayAdapter<SpinnerItem> {

	private Context context;
	
	public SerialSpinnerAdapter(Context context) {
		super(context, 0);
		this.context = context;
	}

	public void addData(ArrayList<SpinnerItem> list) {
		synchronized (list) {
			for (SpinnerItem item : list) {
				add(item);
			}
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.spinner_item, null);
			holder.name = (TextView) convertView.findViewById(R.id.spinner_item_tx);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(getItem(position).name);
		return convertView;
	}

	public class ViewHolder {
		public TextView name;
	}
}
