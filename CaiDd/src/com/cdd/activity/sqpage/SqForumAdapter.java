package com.cdd.activity.sqpage;

import java.util.ArrayList;

import com.cdd.R;
import com.cdd.base.BaseActivity;
import com.cdd.mode.ForumEntry;
import com.cdd.mode.SqAnswerItem;
import com.cdd.mode.SqAskItem;
import com.cdd.util.ImageOperater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SqForumAdapter extends ArrayAdapter<SqAskItem> {

	private Context context;

	public interface onZanListener {
		public void onZan(int position);
	}

	private onZanListener listener;

	public SqForumAdapter(Context context) {
		super(context, 0);
		this.context = context;
	}

	public void addOnZanListener(onZanListener listener) {
		this.listener = listener;
	}

	public void addData(ArrayList<SqAskItem> list) {
		synchronized (list) {
			for (SqAskItem entry : list) {
				add(entry);
			}
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.forum_list_item, null);
			holder.forumTitle = (TextView) convertView
					.findViewById(R.id.forum_title);
			holder.zanCount = (TextView) convertView
					.findViewById(R.id.zan_count);
			holder.answer1Content = (TextView) convertView
					.findViewById(R.id.answer1_content);
			holder.answer1Icon = (ImageView) convertView
					.findViewById(R.id.answer1_icon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		SqAskItem entry = getItem(position);
		holder.forumTitle.setText(entry.title);
		holder.zanCount.setText("（" + entry.likeCount + "）");
		if (entry.answerList.size() > 0) {
			convertView.findViewById(R.id.answer_layout).setVisibility(
					View.VISIBLE);
			SqAnswerItem answer = entry.answerList.get(0);
			holder.answer1Content.setText(answer.content);
			if (answer.anonymous.equals("0")) {
				if (answer.memberSex.equals("1")) {
					holder.answer1Icon
							.setImageResource(R.drawable.default_man_portrait);
				} else if (answer.memberSex.equals("2")) {
					holder.answer1Icon
							.setImageResource(R.drawable.default_woman_portrait);
				} else {
					holder.answer1Icon
							.setImageResource(R.drawable.default_woman_portrait);
				}
				if (!TextUtils.isEmpty(answer.memberPhoto)
						&& !answer.memberPhoto.equals("null")) {
					ImageOperater.getInstance(context).onLoadImage(
							answer.memberPhoto, holder.answer1Icon);
				}

			} else if (answer.anonymous.equals("1")) {
				holder.answer1Icon
						.setImageResource(R.drawable.default_woman_portrait);
			}
		} else {
			convertView.findViewById(R.id.answer_layout).setVisibility(
					View.GONE);
		}
		final int itemPosition = position;
		convertView.findViewById(R.id.zan_layout).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (listener != null) {
							listener.onZan(itemPosition);
						}
					}
				});
		return convertView;
	}

	public class ViewHolder {
		public TextView forumTitle, zanCount, answer1Content;

		public ImageView answer1Icon;
	}

}
