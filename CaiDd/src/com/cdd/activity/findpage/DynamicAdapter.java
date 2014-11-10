package com.cdd.activity.findpage;

import java.util.ArrayList;

import com.cdd.R;
import com.cdd.mode.DynamicEntry;
import com.cdd.mode.PhotosEntry;
import com.cdd.util.ImageOperater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DynamicAdapter extends ArrayAdapter<DynamicEntry> {

	private Context context;

	public interface OnImageClickListener {
		public void onImageClick(int position, int index);
	}

	public interface OnWorkListener {
		public void onWork(int position, int type);
	}
	
	public interface OnPackListener {
		public void onPack(boolean pack);
	}

	private OnImageClickListener imgListener;

	private OnWorkListener workListener;
	
	private OnPackListener packListener;

	public DynamicAdapter(Context context) {
		super(context, 0);
		this.context = context;
	}

	public void addOnImageClickListener(OnImageClickListener imgListener) {
		this.imgListener = imgListener;
	}

	public void addOnWorkListener(OnWorkListener workListener) {
		this.workListener = workListener;
	}
	
	public void addOnPackListener(OnPackListener packListener) {
		this.packListener = packListener;
	}

	public void addData(ArrayList<DynamicEntry> list) {
		synchronized (list) {
			for (DynamicEntry entry : list) {
				add(entry);
			}
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.dynamic_main_item,
					null);
			holder.askIcon = (ImageView) convertView
					.findViewById(R.id.ask_icon);
			holder.photo1 = (ImageView) convertView.findViewById(R.id.photo1);
			holder.photo2 = (ImageView) convertView.findViewById(R.id.photo2);
			holder.photo3 = (ImageView) convertView.findViewById(R.id.photo3);
			holder.askName = (TextView) convertView.findViewById(R.id.ask_name);
			holder.askLevel = (TextView) convertView
					.findViewById(R.id.ask_level);
			holder.zanCount = (TextView) convertView
					.findViewById(R.id.zan_count);
			holder.shoucangCount = (TextView) convertView
					.findViewById(R.id.shoucang_count);
			holder.shareCount = (TextView) convertView
					.findViewById(R.id.zhuanfa_count);
			holder.askContent = (TextView) convertView
					.findViewById(R.id.ask_content);
			holder.askDate = (TextView) convertView.findViewById(R.id.ask_date);
			holder.packIcon = (ImageView) convertView
					.findViewById(R.id.pack_icon);
			holder.packTv = (TextView) convertView.findViewById(R.id.pack_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		doWithItem(position, convertView, holder);
		return convertView;
	}

	private void doWithItem(int position, View convertView, ViewHolder holder) {
		DynamicEntry dynamic = getItem(position);
		holder.askName.setText(dynamic.memberName);
		if (dynamic.memberSex.equals("1")) {
			holder.askIcon.setImageResource(R.drawable.default_man_portrait);
		} else if (dynamic.memberSex.equals("2")) {
			holder.askIcon.setImageResource(R.drawable.default_woman_portrait);
		} else {
			holder.askIcon.setImageResource(R.drawable.default_woman_portrait);
		}
		if (!TextUtils.isEmpty(dynamic.memberPhoto)) {
			ImageOperater.getInstance(context).onLoadImage(dynamic.memberPhoto,
					holder.askIcon);
		}
		if (!TextUtils.isEmpty(dynamic.memberLevelName)
				&& !dynamic.memberLevelName.equals("null")) {
			holder.askLevel.setText(dynamic.memberLevelName);
		}
		holder.askDate.setText(dynamic.createTime);
		doWithWorkfor(position, convertView, holder, dynamic);
		final int itemPosition = position;
		final boolean isPack = dynamic.isPack;
		convertView.findViewById(R.id.pack_up_layout).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (packListener != null) {
					if (isPack) {
						packListener.onPack(false);
					} else {
						packListener.onPack(true);
					}
				}
			}
		});
		if (!TextUtils.isEmpty(dynamic.content)
				&& dynamic.content.length() > 40) {
			convertView.findViewById(R.id.pack_up_layout).setVisibility(
					View.VISIBLE);
			if (dynamic.isPack) {
				holder.packTv.setText("全文");
				holder.packIcon.setImageResource(R.drawable.show_all);
				String contentN = dynamic.content.substring(0, 40);
				holder.askContent.setText(contentN);
			} else {
				holder.packTv.setText("收起");
				holder.packIcon.setImageResource(R.drawable.pack_up);
				holder.askContent.setText(dynamic.content);
			}
		} else {
			convertView.findViewById(R.id.pack_up_layout).setVisibility(
					View.GONE);
		}
		if (dynamic.photos.size() > 0) {
			convertView.findViewById(R.id.answer_photo_layout).setVisibility(
					View.VISIBLE);
			holder.photo1.setVisibility(View.GONE);
			holder.photo2.setVisibility(View.GONE);
			holder.photo3.setVisibility(View.GONE);
			for (int i = 0; i < dynamic.photos.size(); i++) {
				PhotosEntry photo = dynamic.photos.get(i);
				if (i == 0 && !TextUtils.isEmpty(photo.url)) {
					holder.photo1.setVisibility(View.VISIBLE);
					ImageOperater.getInstance(context).onLoadImage(photo.url,
							holder.photo1);
					holder.photo1.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (imgListener != null) {
								imgListener.onImageClick(itemPosition, 0);
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
								imgListener.onImageClick(itemPosition, 1);
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
								imgListener.onImageClick(itemPosition, 2);
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
	}

	private void doWithWorkfor(int position, View convertView,
			ViewHolder holder, DynamicEntry dynamic) {
		holder.zanCount.setText("（" + dynamic.likeCount + "）");
		holder.shoucangCount.setText("（" + dynamic.favoriteCount + "）");
		holder.shareCount.setText("（" + dynamic.shareCount + "）");
		final int itemPosition = position;
		convertView.findViewById(R.id.zan_layout).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (workListener != null) {
							workListener.onWork(itemPosition, 0);
						}
					}
				});
		convertView.findViewById(R.id.shoucang_layout).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (workListener != null) {
							workListener.onWork(itemPosition, 1);
						}
					}
				});
		convertView.findViewById(R.id.zhuanfa_layout).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (workListener != null) {
							workListener.onWork(itemPosition, 2);
						}
					}
				});
	}

	public class ViewHolder {
		public ImageView askIcon, photo1, photo2, photo3, packIcon;

		public TextView askName, askLevel, zanCount, shoucangCount, shareCount,
				askContent, askDate, packTv;
	}
}
