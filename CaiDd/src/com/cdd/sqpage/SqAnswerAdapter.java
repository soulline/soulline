package com.cdd.sqpage;

import java.util.ArrayList;

import com.cdd.R;
import com.cdd.mode.PhotosEntry;
import com.cdd.mode.SqAnswerDetailEntry;
import com.cdd.util.ImageOperater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SqAnswerAdapter extends ArrayAdapter<SqAnswerDetailEntry> {

	private Context context;

	public interface OnZanClickListener {
		public void onZan(int position);
	}

	public interface OnImageClickListener {
		public void onImageClick(int position, int index);
	}

	private OnZanClickListener zanListener;

	private OnImageClickListener imgListener;

	public void addOnZanClickListener(OnZanClickListener zanListener) {
		this.zanListener = zanListener;
	}

	public void addOnImageClickListener(OnImageClickListener imgListener) {
		this.imgListener = imgListener;
	}

	public SqAnswerAdapter(Context context) {
		super(context, 0);
		this.context = context;
	}

	public void addData(ArrayList<SqAnswerDetailEntry> list) {
		synchronized (list) {
			for (SqAnswerDetailEntry entry : list) {
				add(entry);
			}
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.sq_answer_item, null);
			holder.answerIcon = (ImageView) convertView
					.findViewById(R.id.answer_icon);
			holder.photo1 = (ImageView) convertView.findViewById(R.id.photo1);
			holder.photo2 = (ImageView) convertView.findViewById(R.id.photo2);
			holder.photo3 = (ImageView) convertView.findViewById(R.id.photo3);
			holder.answerName = (TextView) convertView
					.findViewById(R.id.answer_name);
			holder.answerLevel = (TextView) convertView
					.findViewById(R.id.answer_level);
			holder.zanCount = (TextView) convertView
					.findViewById(R.id.zan_count);
			holder.answerContent = (TextView) convertView
					.findViewById(R.id.answer_content);
			holder.answerDate = (TextView) convertView
					.findViewById(R.id.answer_date);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		SqAnswerDetailEntry answer = getItem(position);
		if (answer.anonymous.equals("0")) {
			holder.answerName.setText(answer.memberName);
			if (answer.sex.equals("1")) {
				holder.answerIcon
						.setBackgroundResource(R.drawable.default_man_portrait);
			} else if (answer.sex.equals("2")) {
				holder.answerIcon
						.setBackgroundResource(R.drawable.default_woman_portrait);
			}
			if (!TextUtils.isEmpty(answer.memberPhoto)) {
				ImageOperater.getInstance(context).onLoadImage(
						answer.memberPhoto, holder.answerIcon);
			}
			holder.answerLevel.setText(answer.level);
		} else if (answer.anonymous.equals("1")) {
			holder.answerName.setText("匿名");
			holder.answerIcon
					.setBackgroundResource(R.drawable.default_woman_portrait);
			holder.answerLevel.setText("");
		}
		holder.zanCount.setText("（" + answer.likeCount + "）");
		holder.answerContent.setText(answer.content);
		holder.answerDate.setText(answer.createTime);
		final int zanPosition = position;
		convertView.findViewById(R.id.zan_layout).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (zanListener != null) {
							zanListener.onZan(zanPosition);
						}
					}
				});
		if (answer.photos.size() > 0) {
			convertView.findViewById(R.id.answer_photo_layout).setVisibility(
					View.VISIBLE);
			holder.photo1.setVisibility(View.GONE);
			holder.photo2.setVisibility(View.GONE);
			holder.photo3.setVisibility(View.GONE);
			for (int i = 0; i < answer.photos.size(); i++) {
				PhotosEntry photo = answer.photos.get(i);
				if (i == 0 && !TextUtils.isEmpty(photo.url)) {
					holder.photo1.setVisibility(View.VISIBLE);
					ImageOperater.getInstance(context).onLoadImage(photo.url,
							holder.photo1);
					holder.photo1.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (imgListener != null) {
								imgListener.onImageClick(zanPosition, 0);
							}
						}
					});
				} else if (i == 1 && !TextUtils.isEmpty(photo.url)) {
					holder.photo2.setVisibility(View.VISIBLE);
					ImageOperater.getInstance(context).onLoadImage(photo.url,
							holder.photo2);
					holder.photo2.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (imgListener != null) {
								imgListener.onImageClick(zanPosition, 1);
							}
						}
					});
				} else if (i == 2 && !TextUtils.isEmpty(photo.url)) {
					holder.photo3.setVisibility(View.VISIBLE);
					ImageOperater.getInstance(context).onLoadImage(photo.url,
							holder.photo3);
					holder.photo3.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (imgListener != null) {
								imgListener.onImageClick(zanPosition, 2);
							}
						}
					});
					break;
				}
			}
		} else {
			convertView.findViewById(R.id.answer_photo_layout).setVisibility(
					View.GONE);
		}
		if (position == (getCount() - 1)) {
			convertView.findViewById(R.id.bottom_dash_line).setVisibility(
					View.GONE);
		} else {
			convertView.findViewById(R.id.bottom_dash_line).setVisibility(
					View.VISIBLE);
		}
		return convertView;
	}

	public class ViewHolder {
		public ImageView answerIcon, photo1, photo2, photo3;
		public TextView answerName, answerLevel, zanCount, answerContent,
				answerDate;
	}
}
