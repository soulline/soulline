package com.cdd.activity.findpage;

import java.util.ArrayList;

import com.cdd.R;
import com.cdd.mode.DynamicEntry;
import com.cdd.mode.DynamicReplay;
import com.cdd.mode.PhotosEntry;
import com.cdd.util.ImageOperater;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
		public void onPack(int position, boolean pack);
	}
	
	public interface OnAnswerMemberClickLister {
		public void onAnswerClick(DynamicReplay replay, int position);
	}

	private OnImageClickListener imgListener;

	private OnWorkListener workListener;

	private OnPackListener packListener;
	
	private OnAnswerMemberClickLister answerClickListener;

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
	
	public void addOnAnswerMemberClickLister(OnAnswerMemberClickLister answerClickListener) {
		this.answerClickListener = answerClickListener;
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
			holder.forwardPhoto = (ImageView) convertView.findViewById(R.id.forward_photo);
			holder.forwardTx = (TextView) convertView.findViewById(R.id.forward_tx);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		doWithItem(position, convertView, holder);
		doWithAnswerList(position, convertView, holder, getItem(position));
		return convertView;
	}

	private void doWithAnswerList(int position, View convertView,
			ViewHolder holder, DynamicEntry dynamic) {
		LinearLayout answerList = (LinearLayout) convertView
				.findViewById(R.id.answer_list_layout);
		answerList.removeAllViews();
		if (dynamic.replyList.size() > 0) {
			answerList.setVisibility(View.VISIBLE);
			for (int i = 0; i < dynamic.replyList.size(); i++) {
				DynamicReplay replay = dynamic.replyList.get(i);
				SpannableString spanStr = getSpannableString(replay, i);
				View childView = View.inflate(context,
						R.layout.dynamic_answer_item, null);
				TextView txV = (TextView) childView.findViewById(R.id.answer_tx);
				txV.setHighlightColor(Color.TRANSPARENT);
				txV.setText(spanStr);
				txV.setMovementMethod(LinkMovementMethod.getInstance());
				answerList.addView(childView);
			}
		} else {
			answerList.setVisibility(View.GONE);
		}
	}

	private SpannableString getSpannableString(final DynamicReplay replay,
			final int position) {
		String span = replay.memberName + "：" + replay.message;
		SpannableString spanStr = new SpannableString(span);
		spanStr.setSpan(new ClickableSpan() {

			@Override
			public void updateDrawState(TextPaint ds) {
				super.updateDrawState(ds);
				ds.setColor(Color.YELLOW);
				ds.setUnderlineText(false);
			}

			@Override
			public void onClick(View widget) {
				if (answerClickListener != null) {
					answerClickListener.onAnswerClick(replay, position);
				}
			}
		}, 0, (replay.memberName.length() + 1),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spanStr.setSpan(new ForegroundColorSpan(context.getResources()
				.getColor(R.color.self_info_blue)), 0, (replay.memberName
				.length() + 1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spanStr;
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
		if (dynamic.isForward.equals("1")) {
			convertView.findViewById(R.id.share_note).setVisibility(View.VISIBLE);
			convertView.findViewById(R.id.forward_layout).setVisibility(View.VISIBLE);
			convertView.findViewById(R.id.ask_content_layout).setVisibility(View.GONE);
			if (!TextUtils.isEmpty(dynamic.forward.photo)) {
				holder.forwardPhoto.setVisibility(View.VISIBLE);
				ImageOperater.getInstance(context).onLoadImage(dynamic.forward.photo, holder.forwardPhoto);
			} else {
				holder.forwardPhoto.setVisibility(View.GONE);
			}
			holder.forwardTx.setText(dynamic.forward.content);
		} else {
			convertView.findViewById(R.id.share_note).setVisibility(View.GONE);
			convertView.findViewById(R.id.forward_layout).setVisibility(View.GONE);
			convertView.findViewById(R.id.ask_content_layout).setVisibility(View.VISIBLE);
			final boolean isPack = dynamic.isPack;
			convertView.findViewById(R.id.pack_up_layout).setOnClickListener(
					new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							if (packListener != null) {
								if (isPack) {
									packListener.onPack(itemPosition, false);
								} else {
									packListener.onPack(itemPosition, true);
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
					holder.packIcon.setBackgroundResource(R.drawable.show_all);
					String contentN = dynamic.content.substring(0, 40);
					holder.askContent.setText(contentN);
				} else {
					holder.packTv.setText("收起");
					holder.packIcon.setBackgroundResource(R.drawable.pack_up);
					holder.askContent.setText(dynamic.content);
				}
			} else {
				holder.askContent.setText(dynamic.content);
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
		convertView.findViewById(R.id.reply_layout).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (workListener != null) {
					workListener.onWork(itemPosition, 3);
				}
			}
		});
	}

	public class ViewHolder {
		public ImageView askIcon, photo1, photo2, photo3, packIcon, forwardPhoto;

		public TextView askName, askLevel, zanCount, shoucangCount, shareCount,
				askContent, askDate, packTv, forwardTx;
	}
}
