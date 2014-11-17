package com.cdd.activity.findpage;

import java.util.ArrayList;

import com.cdd.R;
import com.cdd.activity.minepage.FansAdapter.OnListenChange;
import com.cdd.activity.minepage.FansAdapter.ViewHolder;
import com.cdd.mode.FansEntry;
import com.cdd.util.ImageOperater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UserFansAdapter extends ArrayAdapter<FansEntry> {

	private Context context;

	private int type = 1;
	
	public interface OnListenChange {
		public void onChange(int position, String relation);
	}
	
	private OnListenChange listener;

	public UserFansAdapter(Context context, int type) {
		super(context, 0);
		this.context = context;
		this.type = type;
	}
	
	public void addOnListenChange(OnListenChange listener) {
		this.listener = listener;
	}

	public void addData(ArrayList<FansEntry> list) {
		synchronized (list) {
			for (FansEntry entry : list) {
				add(entry);
			}
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.my_fans_item, null);
			holder.fansPortrait = (ImageView) convertView
					.findViewById(R.id.fans_portrait);
			holder.listenState = (ImageView) convertView
					.findViewById(R.id.listen_state);
			holder.nickName = (TextView) convertView
					.findViewById(R.id.nick_name);
			holder.levelTx = (TextView) convertView.findViewById(R.id.level_tx);
			holder.simpleText = (TextView) convertView
					.findViewById(R.id.simple_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		FansEntry entry = getItem(position);
		if (entry.sex.equals("1")) {
			holder.fansPortrait
					.setImageResource(R.drawable.default_man_portrait);
		} else if (entry.sex.equals("2")) {
			holder.fansPortrait
					.setImageResource(R.drawable.default_woman_portrait);
		} else {
			holder.fansPortrait
					.setImageResource(R.drawable.default_woman_portrait);
		}
		if (!TextUtils.isEmpty(entry.photo)) {
			ImageOperater.getInstance(context).onLoadImage(entry.photo,
					holder.fansPortrait);
		}
		if (!TextUtils.isEmpty(entry.name) && !entry.name.equals("null")) {
			holder.nickName.setText(entry.name);
		}
		holder.levelTx.setText(entry.levelName);
		if (!TextUtils.isEmpty(entry.description) && !entry.description.equals("null")) {
			holder.simpleText.setText(entry.description);
		}
		if (type == 1 && entry.relation.equals("0")) {
			holder.listenState.setBackgroundResource(R.drawable.add_listen_selector);
		} else if (type == 1 && entry.relation.equals("1")) {
			holder.listenState.setBackgroundResource(R.drawable.cancel_listen);
		} else if (type == 2 && entry.relation.equals("0")) {
			holder.listenState.setBackgroundResource(R.drawable.cancel_listen_selector);
		} else if (type == 2 && entry.relation.equals("1")) {
			holder.listenState.setBackgroundResource(R.drawable.add_listen_selector);
		}
		final int clickPosition = position;
		holder.listenState.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onChange(clickPosition, getItem(clickPosition).relation);
				}
			}
		});
		if (position == (getCount() - 1)) {
			convertView.findViewById(R.id.bottom_line).setVisibility(View.GONE);
		} else {
			convertView.findViewById(R.id.bottom_line).setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	public class ViewHolder {
		public ImageView fansPortrait, listenState;

		public TextView nickName, levelTx, simpleText;
	}


}
