package com.cdd.activity.minepage;

import java.util.ArrayList;

import com.cdd.R;
import com.cdd.mode.MessageEntry;
import com.cdd.util.ImageOperater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageAdapter extends ArrayAdapter<MessageEntry> {

	private Context context;
	
	public interface OnReplyListener {
		public void onReply(int position);
	}
	
	private OnReplyListener listener;
	
	public MessageAdapter(Context context) {
		super(context, 0);
		this.context = context;
	}
	
	public void addOnReplyListener(OnReplyListener listener) {
		this.listener = listener;
	}
	
	public void addData(ArrayList<MessageEntry> list) {
		synchronized (list) {
			for (MessageEntry entry : list) {
				add(entry);
			}
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.message_item, null);
			holder.portaitIv = (ImageView) convertView.findViewById(R.id.portait_iv);
			holder.messageNick = (TextView) convertView.findViewById(R.id.message_nick);
			holder.messageDate = (TextView) convertView.findViewById(R.id.message_date);
			holder.messageContent = (TextView) convertView.findViewById(R.id.message_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MessageEntry entry = getItem(position);
		if (entry.fromMemberSex.equals("1")) {
			holder.portaitIv
					.setImageResource(R.drawable.default_man_portrait);
		} else if (entry.fromMemberSex.equals("2")) {
			holder.portaitIv
					.setImageResource(R.drawable.default_woman_portrait);
		} else {
			holder.portaitIv
					.setImageResource(R.drawable.default_woman_portrait);
		}
		if (!TextUtils.isEmpty(entry.fromMemberPhoto)) {
			ImageOperater.getInstance(context).onLoadImage(entry.fromMemberPhoto,
					holder.portaitIv);
		}
		if (!TextUtils.isEmpty(entry.fromMemberName) && !entry.fromMemberName.equals("null")) {
			holder.messageNick.setText(entry.fromMemberName);
		}
		if (!TextUtils.isEmpty(entry.createTime) && !entry.createTime.equals("null")) {
			holder.messageDate.setText(entry.createTime);
		}
		if (!TextUtils.isEmpty(entry.msg) && !entry.msg.equals("null")) {
			holder.messageContent.setText(entry.msg);
		}
		final int replyPosition = position;
		convertView.findViewById(R.id.reply_layout).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onReply(replyPosition);
				}
			}
		});
		return convertView;
	}

	public class ViewHolder {
		public ImageView portaitIv;
		
		public TextView messageNick, messageDate, messageContent;
	}
	
}
