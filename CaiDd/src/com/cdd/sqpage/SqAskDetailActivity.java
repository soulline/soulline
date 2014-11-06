package com.cdd.sqpage;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdd.R;
import com.cdd.activity.image.ImageNetPageActivity;
import com.cdd.base.BaseActivity;
import com.cdd.mode.PhotosEntry;
import com.cdd.mode.SqAnswerDetailEntry;
import com.cdd.sqpage.SqAnswerAdapter.OnImageClickListener;
import com.cdd.sqpage.SqAnswerAdapter.OnZanClickListener;
import com.cdd.util.ImageOperater;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class SqAskDetailActivity extends BaseActivity implements OnClickListener{

	private PullToRefreshListView sqAnswerList;
	
	private SqAnswerAdapter adapter;
	
	private TextView askTitle, askDate, zanCount, shoucangCount, zhuanfaCount;
	
	private ImageView photo1, photo2, photo3;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.ask_detail_activity);
		initView();
		initContent();
	}
	
	private void initContent() {
		ArrayList<SqAnswerDetailEntry> list = new ArrayList<SqAnswerDetailEntry>();
		PhotosEntry photo1 = new PhotosEntry();
		photo1.id = "1";
		photo1.url = "http://h.hiphotos.baidu.com/image/pic/item/f31fbe096b63f62494304a8b8544ebf81b4ca3cd.jpg";
		PhotosEntry photo2 = new PhotosEntry();
		photo2.id = "2";
		photo2.url = "http://a.hiphotos.baidu.com/image/pic/item/54fbb2fb43166d2267ae2d99452309f79152d2e5.jpg";
		PhotosEntry photo3 = new PhotosEntry();
		photo3.id = "3";
		photo3.url = "http://h.hiphotos.baidu.com/image/pic/item/09fa513d269759ee22f4472fb0fb43166c22dfa9.jpg";
		PhotosEntry photo4 = new PhotosEntry();
		photo4.id = "4";
		photo4.url = "http://b.hiphotos.baidu.com/image/pic/item/caef76094b36acafff0500fb7ed98d1000e99cd4.jpg";
		ArrayList<PhotosEntry> photoList = new ArrayList<PhotosEntry>();
		photoList.add(photo1);
		photoList.add(photo2);
		photoList.add(photo3);
		photoList.add(photo4);
		SqAnswerDetailEntry entry1 = new SqAnswerDetailEntry();
		entry1.anonymous = "0";
		entry1.content = "洗吧洗吧下吧思密达思密达思密达思密达上面的上面的思密达思密达思密达思密达十多名";
		entry1.sex = "1";
		entry1.createTime = "2014-11-6 13:50";
		entry1.likeCount = "30";
		entry1.memberName = "周永康";
		entry1.level = "初级会员";
		entry1.photos = photoList;
		list.add(entry1);
		
		SqAnswerDetailEntry entry2 = new SqAnswerDetailEntry();
		entry2.anonymous = "0";
		entry2.content = "洗吧洗吧下吧思密达思密达思密达思密达上面的上面的思密达思密达思密达思密达十多名";
		entry2.sex = "1";
		entry2.createTime = "2014-11-6 13:50";
		entry2.likeCount = "30";
		entry2.memberName = "周永康";
		entry2.level = "初级会员";
		entry2.photos = photoList;
		list.add(entry2);
		
		SqAnswerDetailEntry entry3 = new SqAnswerDetailEntry();
		entry3.anonymous = "0";
		entry3.content = "洗吧洗吧下吧思密达思密达思密达思密达上面的上面的思密达思密达思密达思密达十多名";
		entry3.sex = "1";
		entry3.createTime = "2014-11-6 13:50";
		entry3.likeCount = "30";
		entry3.memberName = "周永康";
		entry3.level = "初级会员";
		entry3.photos = photoList;
		list.add(entry3);
		initSqAnswerList(list);
		if (photoList.size() > 0) {
			findViewById(R.id.ask_photo_layout).setVisibility(View.VISIBLE);
			initAskPhotos(photoList);
		} else {
			findViewById(R.id.ask_photo_layout).setVisibility(View.GONE);
		}
	}
	
	private void initAskPhotos(final ArrayList<PhotosEntry> photoList) {
		for (int i=0; i < photoList.size(); i++) {
			PhotosEntry photo = photoList.get(i);
			if (i == 0 && !TextUtils.isEmpty(photo.url)) {
				photo1.setVisibility(View.VISIBLE);
				ImageOperater.getInstance(context).onLoadImage(photo.url,
						photo1);
				photo1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						gotoPhotoDetail(photoList, 0);
					}
				});
			} else if (i == 1 && !TextUtils.isEmpty(photo.url)) {
				photo2.setVisibility(View.VISIBLE);
				ImageOperater.getInstance(context).onLoadImage(photo.url,
						photo2);
				photo2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						gotoPhotoDetail(photoList, 1);
					}
				});
			} else if (i == 2 && !TextUtils.isEmpty(photo.url)) {
				photo3.setVisibility(View.VISIBLE);
				ImageOperater.getInstance(context).onLoadImage(photo.url,
						photo3);
				photo3.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						gotoPhotoDetail(photoList, 2);
					}
				});
				break;
			}
		}
	}
	
	private void initView() {
		sqAnswerList = (PullToRefreshListView) findViewById(R.id.sq_answer_list);
		askTitle = (TextView) findViewById(R.id.ask_title);
		photo1 = (ImageView) findViewById(R.id.photo1);
		photo2 = (ImageView) findViewById(R.id.photo2);
		photo3 = (ImageView) findViewById(R.id.photo3);
		photo1.setOnClickListener(this);
		photo2.setOnClickListener(this);
		photo3.setOnClickListener(this);
		findViewById(R.id.detail_answer_layout).setOnClickListener(this);
		findViewById(R.id.zan_layout).setOnClickListener(this);
		findViewById(R.id.shoucang_layout).setOnClickListener(this);
		findViewById(R.id.zhuanfa_layout).setOnClickListener(this);
		askDate = (TextView) findViewById(R.id.ask_date);
		zanCount = (TextView) findViewById(R.id.zan_count);
		shoucangCount = (TextView) findViewById(R.id.shoucang_count);
		zhuanfaCount = (TextView) findViewById(R.id.zhuanfa_count);
	}
	
	private void gotoPhotoDetail(ArrayList<PhotosEntry> list, int index) {
		Intent intent = new Intent(context, ImageNetPageActivity.class);
		intent.putExtra("image_urls", list);
		intent.putExtra("image_index", index);
		startActivity(intent);
	}
	
	private void initSqAnswerList(ArrayList<SqAnswerDetailEntry> list) {
		if (adapter == null) {
			adapter = new SqAnswerAdapter(context);
			adapter.addData(list);
			sqAnswerList.setAdapter(adapter);
			adapter.addOnZanClickListener(new OnZanClickListener() {
				
				@Override
				public void onZan(int position) {
					// TODO Auto-generated method stub
					
				}
			});
			adapter.addOnImageClickListener(new OnImageClickListener() {
				
				@Override
				public void onImageClick(int position, int index) {
					gotoPhotoDetail(adapter.getItem(position).photos, index);
				}
			});
		} else {
			adapter.clear();
			adapter.addData(list);
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.photo1:
			
			break;
		case R.id.photo2:
			
			break;
		case R.id.photo3:
			
			break;
		case R.id.detail_answer_layout:
			
			break;
		case R.id.zan_layout:
			
			break;
		case R.id.shoucang_layout:
			
			break;
		case R.id.zhuanfa_layout:
			
			break;
		default:
			break;
		}
		
	}

}
