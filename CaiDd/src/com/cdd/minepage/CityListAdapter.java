package com.cdd.minepage;

import java.util.ArrayList;

import com.cdd.R;
import com.cdd.mode.CityItemEntry;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CityListAdapter extends ArrayAdapter<CityItemEntry> {

	private Context context;
	
	public CityListAdapter(Context context) {
		super(context, 0);
		this.context = context;
	}

	public void addData(ArrayList<CityItemEntry> list) {
		synchronized (list) {
			for (CityItemEntry entry : list) {
				add(entry);
			}
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.city_list_item, null);
			holder.cityName = (TextView) convertView.findViewById(R.id.city_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.cityName.setText(getItem(position).name);
		return convertView;
	}

	public class ViewHolder {
		public TextView cityName;
	}
}
