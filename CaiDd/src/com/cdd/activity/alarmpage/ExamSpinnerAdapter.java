package com.cdd.activity.alarmpage;

import java.util.ArrayList;

import com.cdd.R;
import com.cdd.mode.ForumItem;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ExamSpinnerAdapter extends ArrayAdapter<ForumItem> {


	private Context context;
	
	public ExamSpinnerAdapter(Context context) {
		super(context, 0);
		this.context = context;
	}

	public void addData(ArrayList<ForumItem> list) {
		synchronized (list) {
			for (ForumItem item : list) {
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
