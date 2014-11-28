package com.cdd.share;

import java.util.HashMap;

import com.cdd.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CddShareAdapter extends ArrayAdapter<String> {

	private Context context;
	
	private HashMap<Integer, Integer> drawableMap = new HashMap<Integer, Integer>();
	
	public CddShareAdapter(Context context) {
		super(context, 0);
		this.context = context;
		initDrawableMap();
	}
	
	public void addData(String[] array) {
		synchronized (array) {
			for (String title : array) {
				add(title);
			}
		}
	}

	private void initDrawableMap() {
		drawableMap.put(0, R.drawable.sina_wb_share_selector);
		drawableMap.put(1, R.drawable.wx_share_selector);
		drawableMap.put(2, R.drawable.wx_friends_share_selector);
		drawableMap.put(3, R.drawable.qzone_share_selector);
		drawableMap.put(4, R.drawable.qq_share_selector);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.share_item, null);
			holder.share_title = (TextView) convertView.findViewById(R.id.share_tx);
			holder.share_icon = (ImageView) convertView.findViewById(R.id.share_icon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String share = getItem(position);
		holder.share_title.setText(share);
		holder.share_icon.setBackgroundResource(drawableMap.get(position));
		return convertView;
	}
	
	public class ViewHolder {
		public TextView share_title;
		
		public ImageView share_icon;
	}

}
