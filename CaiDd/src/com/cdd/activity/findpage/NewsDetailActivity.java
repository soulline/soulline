package com.cdd.activity.findpage;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdd.R;
import com.cdd.activity.image.ImageNetPageActivity;
import com.cdd.base.BaseActivity;
import com.cdd.fragment.BaseFragmentListener;
import com.cdd.fragment.BottomReplyFragment;
import com.cdd.mode.DynamicEntry;
import com.cdd.mode.PhotosEntry;
import com.cdd.net.RequestListener;
import com.cdd.operater.NewsDetailOp;
import com.cdd.operater.NewsShareOp;
import com.cdd.operater.NewsShoucangOp;
import com.cdd.operater.NewsZanOp;
import com.cdd.util.ImageOperater;

public class NewsDetailActivity extends BaseActivity implements OnClickListener{

	private ImageView askIcon, photo1, photo2, photo3, forwardPhoto;

	private TextView askName, askLevel, zanCount, shoucangCount, shareCount,
			askContent, askDate, forwardTx;

	private String cofId = "";

	private DynamicEntry dynamicEntry = new DynamicEntry();

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.news_detail_activity);
		initView();
		initContent();
	}

	private void initContent() {
		cofId = getIntent().getStringExtra("cofId");
		requestDynamicInfo();
	}

	private void initView() {
		askIcon = (ImageView) findViewById(R.id.ask_icon);
		photo1 = (ImageView) findViewById(R.id.photo1);
		photo2 = (ImageView) findViewById(R.id.photo2);
		photo3 = (ImageView) findViewById(R.id.photo3);
		askName = (TextView) findViewById(R.id.ask_name);
		askLevel = (TextView) findViewById(R.id.ask_level);
		zanCount = (TextView) findViewById(R.id.zan_count);
		shoucangCount = (TextView) findViewById(R.id.shoucang_count);
		shareCount = (TextView) findViewById(R.id.zhuanfa_count);
		askContent = (TextView) findViewById(R.id.ask_content);
		askDate = (TextView) findViewById(R.id.ask_date);
		forwardPhoto = (ImageView) findViewById(R.id.forward_photo);
		forwardTx = (TextView) findViewById(R.id.forward_tx);
		findViewById(R.id.forward_layout).setOnClickListener(this);
		findViewById(R.id.zan_layout).setOnClickListener(this);
		findViewById(R.id.shoucang_layout).setOnClickListener(this);
		findViewById(R.id.zhuanfa_layout).setOnClickListener(this);
		findViewById(R.id.reply_layout).setOnClickListener(this);
	}

	private void loadDynamicInfo(DynamicEntry dynamic) {
		if (dynamic.memberSex.equals("1")) {
			askIcon.setImageResource(R.drawable.default_man_portrait);
		} else if (dynamic.memberSex.equals("2")) {
			askIcon.setImageResource(R.drawable.default_woman_portrait);
		} else {
			askIcon.setImageResource(R.drawable.default_woman_portrait);
		}
		if (!TextUtils.isEmpty(dynamic.memberPhoto)) {
			ImageOperater.getInstance(context).onLoadImage(dynamic.memberPhoto,
					askIcon);
		}
		if (!TextUtils.isEmpty(dynamic.memberLevelName)
				&& !dynamic.memberLevelName.equals("null")) {
			askLevel.setText(dynamic.memberLevelName);
		}
		askDate.setText(dynamic.createTime);
		askName.setText(dynamic.memberName);
		zanCount.setText("（" + dynamic.likeCount + "）");
		shoucangCount.setText("（" + dynamic.favoriteCount + "）");
		shareCount.setText("（" + dynamic.shareCount + "）");
		
		if (dynamic.isForward.equals("1")) {
			findViewById(R.id.share_note).setVisibility(View.VISIBLE);
			findViewById(R.id.forward_layout).setVisibility(View.VISIBLE);
			findViewById(R.id.ask_content_layout).setVisibility(View.GONE);
			if (!TextUtils.isEmpty(dynamic.forward.photo)) {
				forwardPhoto.setVisibility(View.VISIBLE);
				ImageOperater.getInstance(context).onLoadImage(dynamic.forward.photo, forwardPhoto);
			} else {
				forwardPhoto.setVisibility(View.GONE);
			}
			forwardTx.setText(dynamic.forward.content);
		} else {
			findViewById(R.id.share_note).setVisibility(View.GONE);
			findViewById(R.id.forward_layout).setVisibility(View.GONE);
			findViewById(R.id.ask_content_layout).setVisibility(View.VISIBLE);
			if (!TextUtils.isEmpty(dynamic.content)) {
				askContent.setText(dynamic.content);
			}
			if (dynamic.photos.size() > 0) {
				findViewById(R.id.answer_photo_layout).setVisibility(
						View.VISIBLE);
				photo1.setVisibility(View.GONE);
				photo2.setVisibility(View.GONE);
				photo3.setVisibility(View.GONE);
				for (int i = 0; i < dynamic.photos.size(); i++) {
					PhotosEntry photo = dynamic.photos.get(i);
					if (i == 0 && !TextUtils.isEmpty(photo.url)) {
						photo1.setVisibility(View.VISIBLE);
						ImageOperater.getInstance(context).onLoadImage(photo.url,
								photo1);
						photo1.setOnClickListener(this);
					} else if (i == 1 && !TextUtils.isEmpty(photo.url)) {
						photo2.setVisibility(View.VISIBLE);
						ImageOperater.getInstance(context).onLoadImage(photo.url,
								photo2);
						photo2.setOnClickListener(this);
					} else if (i == 2 && !TextUtils.isEmpty(photo.url)) {
						photo3.setVisibility(View.VISIBLE);
						ImageOperater.getInstance(context).onLoadImage(photo.url,
								photo3);
						photo3.setOnClickListener(this);
						break;
					}
				}
			} else {
				findViewById(R.id.answer_photo_layout).setVisibility(
						View.GONE);
			}
		}
	}

	private void requestDynamicInfo() {
		final NewsDetailOp newsOp = new NewsDetailOp(context);
		newsOp.setParams(cofId);
		newsOp.onRequest(new RequestListener() {

			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCallBack(Object data) {
				dynamicEntry = newsOp.getDynamic();
				handler.post(new Runnable() {

					@Override
					public void run() {
						loadDynamicInfo(dynamicEntry);
					}
				});
			}
		});
	}
	
	private void gotoPhotoDetail(ArrayList<PhotosEntry> list, int index) {
		Intent intent = new Intent(context, ImageNetPageActivity.class);
		intent.putExtra("image_urls", list);
		intent.putExtra("image_index", index);
		startActivity(intent);
	}

	public void displayFragment(boolean isOpen, String tag, Bundle bundle,
			BaseFragmentListener listener) {
		if (isOpen) {
			showFragment(tag, -1, createFragment(tag, bundle, listener));
		} else {
			closeFragment(tag);
		}
	}

	public DialogFragment createFragment(final String tag, Bundle b,
			BaseFragmentListener listener) {
		if (tag.equals("bottom_reply")) {
			BottomReplyFragment bottomReplay = new BottomReplyFragment(context,
					b);
			bottomReplay.addBaseFragmentListener(listener);
			return bottomReplay;
		}
		return null;
	}
	
	private void onNewsZanRequest(String cofId) {
		NewsZanOp zanOp = new NewsZanOp(context);
		zanOp.setParams(cofId);
		zanOp.onRequest(new RequestListener() {
			
			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCallBack(Object data) {
				showToast("点赞成功");
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						String count = dynamicEntry.likeCount;
						int countN = Integer.valueOf(count);
						countN++;
						dynamicEntry.likeCount = countN + "";
						zanCount.setText("（" + dynamicEntry.likeCount + "）");
					}
				});
			}
		});
	}
	
	private void onNewsShoucangRequest(String cofId) {
		NewsShoucangOp shoucangOp = new NewsShoucangOp(context);
		shoucangOp.setParams(cofId);
		shoucangOp.onRequest(new RequestListener() {
			
			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCallBack(Object data) {
				showToast("收藏成功");
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						String count = dynamicEntry.favoriteCount;
						int countN = Integer.valueOf(count);
						countN++;
						dynamicEntry.favoriteCount = countN + "";
						shoucangCount.setText("（" + dynamicEntry.favoriteCount + "）");
					}
				});
			}
		});
	}
	
	private void onNewsShareRequest(String cofId) {
		NewsShareOp shareOp = new NewsShareOp(context);
		shareOp.setParams(cofId);
		shareOp.onRequest(new RequestListener() {
			
			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCallBack(Object data) {
				showToast("分享成功");
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						String count = dynamicEntry.shareCount;
						int countN = Integer.valueOf(count);
						countN++;
						dynamicEntry.shareCount = countN + "";
						shareCount.setText("（" + dynamicEntry.shareCount + "）");
					}
				});
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.photo1:
			gotoPhotoDetail(dynamicEntry.photos, 0);
			break;
			
		case R.id.photo2:
			gotoPhotoDetail(dynamicEntry.photos, 1);
			break;
			
		case R.id.photo3:
			gotoPhotoDetail(dynamicEntry.photos, 2);
			break;
			
		case R.id.forward_layout:
			
			break;
			
		case R.id.zan_layout:
			onNewsZanRequest(cofId);
			break;
			
		case R.id.shoucang_layout:
			onNewsShoucangRequest(cofId);
			break;
			
		case R.id.zhuanfa_layout:
			onNewsShareRequest(cofId);
			break;
			
		case R.id.reply_layout:
			Bundle b = new Bundle();
			b.putString("cofId", dynamicEntry.id);
			b.putInt("reply_type", 1);
			displayFragment(true, "bottom_reply", b,
					new BaseFragmentListener() {

						@Override
						public void onCallBack(Object object) {
							if (object instanceof String) {
								String result = (String) object;
								if (result.equals("success")) {
									requestDynamicInfo();
								}
							}
						}
					});
			break;

		default:
			break;
		}
		
	}

}
