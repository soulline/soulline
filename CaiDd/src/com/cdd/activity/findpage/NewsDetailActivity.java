package com.cdd.activity.findpage;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.cdd.R;
import com.cdd.activity.findpage.ReplyDetaiAdapter.OnAnswerMemberClickLister;
import com.cdd.activity.image.ImageNetPageActivity;
import com.cdd.base.BaseActivity;
import com.cdd.fragment.BaseFragmentListener;
import com.cdd.fragment.BottomReplyFragment;
import com.cdd.mode.DynamicEntry;
import com.cdd.mode.DynamicReplay;
import com.cdd.mode.PhotosEntry;
import com.cdd.net.RequestListener;
import com.cdd.operater.NewsDetailOp;
import com.cdd.operater.NewsReplyListOp;
import com.cdd.operater.NewsShareOp;
import com.cdd.operater.NewsShoucangOp;
import com.cdd.operater.NewsZanOp;
import com.cdd.util.ImageOperater;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

public class NewsDetailActivity extends BaseActivity implements OnClickListener{

	private ImageView askIcon, photo1, photo2, photo3, forwardPhoto;

	private TextView askName, askLevel, zanCount, shoucangCount, shareCount,
			askContent, askDate, forwardTx;

	private String cofId = "";

	private DynamicEntry dynamicEntry = new DynamicEntry();
	
	private int pageNum = 0;

	private int requestPage = 1;
	
	private View footMoreView;
	
	private ArrayList<DynamicReplay> replyList = new ArrayList<DynamicReplay>();
	
	private PullToRefreshListView replyListView;
	
	private ReplyDetaiAdapter adapter;

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
		requestDynamicList("1", true);
	}
	
	private void loadMore() {
		replyListView.getRefreshableView().removeFooterView(footMoreView);
		int page = pageNum + 1;
		String pageNumber = "";
		if (pageNum == 0) {
			pageNumber = "1";
		} else {
			pageNumber = page + "";
		}
		requestDynamicList(pageNumber, true);
	}
	
	private void initDynamicList(ArrayList<DynamicReplay> list) {
		replyListView.setVisibility(View.VISIBLE);
		findViewById(R.id.empty_content_layout).setVisibility(View.GONE);
		if (adapter == null) {
			adapter = new ReplyDetaiAdapter(context);
			adapter.addData(list);
			adapter.addOnAnswerMemberClickLister(new OnAnswerMemberClickLister() {
				
				@Override
				public void onAnswerClick(DynamicReplay replay, int position) {
					Intent userInfo = new Intent(context, UserInfoActivity.class);
					userInfo.putExtra("memberId", replay.memberId);
					startActivity(userInfo);
				}
			});
			replyListView.getRefreshableView().setAdapter(adapter);
		} else {
			adapter.clear();
			adapter.addData(list);
			adapter.notifyDataSetChanged();
		}
		if ((adapter.getCount() % 20) == 0) {
			replyListView.getRefreshableView().addFooterView(footMoreView);
		}
	}
	
	private void doRefreshListView() {
		replyListView.getRefreshableView().removeFooterView(
				footMoreView);
		requestPage = 1;
		String pageNumber = "";
		if (pageNum == 0) {
			pageNumber = 1 + "";
		} else {
			pageNumber = requestPage + "";
		}
		replyList.clear();
		requestDynamicList(pageNumber, false);
	}
	
	private void initDynamicListView() {
		replyListView.getRefreshableView().setOnItemClickListener(
				new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent detail = new Intent(context, NewsDetailActivity.class);
						detail.putExtra("cofId", adapter.getItem(position).id);
						startActivity(detail);
					}
				});
		replyListView.setMode(Mode.PULL_FROM_START);
		replyListView.getLoadingLayoutProxy(true, true).setPullLabel(
				"下拉刷新...");
		replyListView.getLoadingLayoutProxy(true, true).setRefreshingLabel(
				"正在刷新...");
		replyListView.getLoadingLayoutProxy(true, true).setReleaseLabel(
				"释放刷新...");
		replyListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshView.isHeaderShown()) {
					doRefreshListView();
				}
			}
		});
	}
	
	private void requestDynamicList(String pageNumber, final boolean isShowNow) {
		final NewsReplyListOp dingdangOp = new NewsReplyListOp(
				context);
		dingdangOp.setParams(cofId, "1");
		dingdangOp.onRequest(new RequestListener() {

			@Override
			public void onError(Object error) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						if (adapter == null
								|| (adapter != null && adapter.getCount() == 0)) {
							replyListView.setVisibility(View.GONE);
							findViewById(R.id.empty_content_layout)
									.setVisibility(View.VISIBLE);
						}
						replyListView.onRefreshComplete();
					}
				});

			}

			@Override
			public void onCallBack(Object data) {
				if (dingdangOp.getReplyList().size() > 0) {
					replyList.addAll(dingdangOp.getReplyList());
					if (isShowNow) {
						pageNum++;
						handler.post(new Runnable() {

							@Override
							public void run() {
								replyListView.onRefreshComplete();
								initDynamicList(replyList);
							}
						});
					} else if (requestPage < pageNum) {
						requestPage++;
						String pageNumber = requestPage + "";
						requestDynamicList(pageNumber, false);
					} else if ((requestPage == pageNum) || (pageNum == 0)) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								replyListView.onRefreshComplete();
								initDynamicList(replyList);
							}
						});
					}
				} else {
					if (adapter == null
							|| (adapter != null && adapter.getCount() == 0)) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								replyListView.setVisibility(View.GONE);
								findViewById(R.id.empty_content_layout)
										.setVisibility(View.VISIBLE);
							}
						});
					}
					pageNum = 1;
					handler.post(new Runnable() {

						@Override
						public void run() {
							replyListView.onRefreshComplete();
						}
					});
				}

			}
		});
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
		footMoreView = View.inflate(context, R.layout.load_more_view, null);
		footMoreView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadMore();
			}
		});
		findViewById(R.id.empty_content_layout).setOnClickListener(this);
		replyListView = (PullToRefreshListView) findViewById(R.id.dynamic_list);
		initDynamicListView();
		askIcon.setOnClickListener(this);
		askName.setOnClickListener(this);
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
			Intent forward = new Intent(context, ForwardDetailActivity.class);
			forward.putExtra("cofId", dynamicEntry.forward.id);
			startActivity(forward);
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
									doRefreshListView();
								}
							}
						}
					});
			break;
			
		case R.id.empty_content_layout:
			requestDynamicList("1", true);
			break;
			
		case R.id.ask_icon:
			gotoMemberDetail();
			break;
			
		case R.id.ask_name:
			gotoMemberDetail();
			break;

		default:
			break;
		}
		
	}
	
	private void gotoMemberDetail() {
		Intent userInfo = new Intent(context, UserInfoActivity.class);
		userInfo.putExtra("memberId", dynamicEntry.memberId);
		startActivity(userInfo);
	}

}
