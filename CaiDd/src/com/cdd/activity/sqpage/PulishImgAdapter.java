package com.cdd.activity.sqpage;

import java.util.ArrayList;

import com.cdd.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class PulishImgAdapter extends ArrayAdapter<Drawable> {

	private Context context;
	
	public PulishImgAdapter(Context context) {
		super(context, 0);
		this.context = context;
	}
	
	public void addData(ArrayList<Drawable> list) {
		synchronized (list) {
			for (Drawable d : list) {
				add(d);
			}
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.pulish_img_item, null);
			holder.pulishImg = (ImageView) convertView.findViewById(R.id.img_pulish);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.pulishImg.setBackgroundDrawable(getItem(position));
		return convertView;
	}
	
	
	public class ViewHolder{
		public ImageView pulishImg;
	}

}
