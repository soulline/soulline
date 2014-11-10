package com.cdd.activity.sqpage;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cdd.R;
import com.cdd.activity.image.ImageNetPageActivity;
import com.cdd.activity.sqpage.SqAnswerAdapter.OnImageClickListener;
import com.cdd.activity.sqpage.SqAnswerAdapter.OnZanClickListener;
import com.cdd.base.BaseActivity;
import com.cdd.mode.PhotosEntry;
import com.cdd.mode.SqAnswerDetailEntry;
import com.cdd.mode.SqAskDetailEntry;
import com.cdd.mode.SqAskListRequest;
import com.cdd.net.RequestListener;
import com.cdd.operater.AskZanOp;
import com.cdd.operater.SqAnswerDetailOp;
import com.cdd.operater.SqAnswerZanOp;
import com.cdd.operater.SqAskDetailOp;
import com.cdd.operater.SqAskShareOp;
import com.cdd.operater.SqAskShoucangOp;
import com.cdd.util.CddRequestCode;
import com.cdd.util.ImageOperater;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

public class SqAskDetailActivity extends BaseActivity implements
		OnClickListener {

	private PullToRefreshListView sqAnswerList;

	private SqAnswerAdapter adapter;

	private TextView askTitle, askDate, zanCount, shoucangCount, zhuanfaCount;

	private ImageView photo1, photo2, photo3;

	private String askId = "";

	private SqAskDetailEntry askDetail = new SqAskDetailEntry();

	private ArrayList<SqAnswerDetailEntry> answerList = new ArrayList<SqAnswerDetailEntry>();

	private int pageNum = 0;

	private int requestPage = 1;

	private View footMoreView;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.ask_detail_activity);
		initView();
		initContent();
	}

	private void initContent() {
		askId = getIntent().getStringExtra("ask_id");
		requestAskDetail(askId);
		requestAnswerList(askId, "1", true);
	}

	private void initAskDetail(SqAskDetailEntry askDetail) {
		askTitle.setText(askDetail.title);
		askDate.setText(askDetail.createTime);
		zanCount.setText("（" + askDetail.likeCount + "）");
		shoucangCount.setText("（" + askDetail.favoriteCount + "）");
		zhuanfaCount.setText("（" + askDetail.shareCount + "）");
		if (askDetail.photos.size() > 0) {
			findViewById(R.id.ask_photo_layout).setVisibility(View.VISIBLE);
			initAskPhotos(askDetail.photos);
		} else {
			findViewById(R.id.ask_photo_layout).setVisibility(View.GONE);
		}
	}

	private void initAskPhotos(final ArrayList<PhotosEntry> photoList) {
		photo1.setVisibility(View.GONE);
		photo2.setVisibility(View.GONE);
		photo3.setVisibility(View.GONE);
		for (int i = 0; i < photoList.size(); i++) {
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

	private void requestAnswerList(String subjectId, String pageNumber,
			final boolean isShowNow) {
		final SqAnswerDetailOp answerDetailOp = new SqAnswerDetailOp(context);
		answerDetailOp.setParmas(subjectId, pageNumber);
		answerDetailOp.onRequest(new RequestListener() {

			@Override
			public void onError(Object error) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						if (adapter == null
								|| (adapter != null && adapter.getCount() == 0)) {
							sqAnswerList.setVisibility(View.GONE);
							findViewById(R.id.empty_content_layout)
									.setVisibility(View.VISIBLE);
						}
						sqAnswerList.onRefreshComplete();
					}
				});
			}

			@Override
			public void onCallBack(Object data) {
				ArrayList<SqAnswerDetailEntry> list = answerDetailOp
						.getAnswerList();

				if (list.size() > 0) {
					answerList.addAll(list);
					if (isShowNow) {
						pageNum++;
						handler.post(new Runnable() {

							@Override
							public void run() {
								sqAnswerList.onRefreshComplete();
								initSqAnswerList(answerList);
							}
						});
					} else if (requestPage < pageNum) {
						requestPage++;
						String pageNn = requestPage + "";
						requestAnswerList(askId, pageNn, false);
					} else if ((requestPage == pageNum) || (pageNum == 0)) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								sqAnswerList.onRefreshComplete();
								initSqAnswerList(answerList);
							}
						});
					}
				} else {
					if (adapter == null
							|| (adapter != null && adapter.getCount() == 0)) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								sqAnswerList.setVisibility(View.GONE);
								findViewById(R.id.empty_content_layout)
										.setVisibility(View.VISIBLE);
							}
						});
					}
					pageNum = 1;
					handler.post(new Runnable() {

						@Override
						public void run() {
							sqAnswerList.onRefreshComplete();
						}
					});
				}

			}
		});
	}

	private void requestAskDetail(String id) {
		if (!TextUtils.isEmpty(id)) {
			final SqAskDetailOp askDetailOp = new SqAskDetailOp(context);
			askDetailOp.setParams(id);
			askDetailOp.onRequest(new RequestListener() {

				@Override
				public void onError(Object error) {

				}

				@Override
				public void onCallBack(Object data) {
					askDetail = askDetailOp.getAskDetail();
					if (askDetail != null) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								initAskDetail(askDetail);
							}
						});
					}
				}
			});
		}
	}

	private void loadMore() {
		sqAnswerList.getRefreshableView().removeFooterView(footMoreView);
		int page = pageNum + 1;
		String pageNnn = page + "";
		requestAnswerList(askId, pageNnn, true);
	}

	private void initPullToRefresh() {
		sqAnswerList.setMode(Mode.PULL_FROM_START);
		sqAnswerList.getLoadingLayoutProxy(true, true).setPullLabel("下拉刷新...");
		sqAnswerList.getLoadingLayoutProxy(true, true).setRefreshingLabel(
				"正在刷新...");
		sqAnswerList.getLoadingLayoutProxy(true, true).setReleaseLabel(
				"释放刷新...");
		sqAnswerList.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshView.isHeaderShown()) {
					sqAnswerList.getRefreshableView().removeFooterView(
							footMoreView);
					requestPage = 1;
					String pageNn = "";
					if (pageNum == 0) {
						pageNn = 1 + "";
					} else {
						pageNn = requestPage + "";
					}
					answerList.clear();
					requestAnswerList(askId, pageNn, false);
				}
			}
		});
	}

	private void initView() {
		footMoreView = View.inflate(context, R.layout.load_more_view, null);
		footMoreView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadMore();
			}
		});
		findViewById(R.id.empty_content_layout).setOnClickListener(this);
		sqAnswerList = (PullToRefreshListView) findViewById(R.id.sq_answer_list);
		initPullToRefresh();
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
	
	private void onAskZanRequest(final String id) {
		AskZanOp zanOp = new AskZanOp(context);
		zanOp.setParmas(id);
		zanOp.onRequest(new RequestListener() {
			
			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCallBack(Object data) {
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						int oldzanCount = Integer.valueOf(askDetail.likeCount);
						oldzanCount++;
						askDetail.likeCount = oldzanCount + "";
						zanCount.setText("（" + askDetail.likeCount + "）");
						showToast("点赞成功");
					}
				});
			}
		});
	}
	
	private void onAskSCRequest(final String id) {
		SqAskShoucangOp shoucangOp = new SqAskShoucangOp(context);
		shoucangOp.setParams(id);
		shoucangOp.onRequest(new RequestListener() {
			
			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCallBack(Object data) {
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						int oldscCount = Integer.valueOf(askDetail.favoriteCount);
						oldscCount++;
						askDetail.favoriteCount = oldscCount + "";
						shoucangCount.setText("（" + askDetail.favoriteCount + "）");
						showToast("收藏成功");
					}
				});
			}
		});
	}
	
	private void onAskShareRequest(final String id) {
		SqAskShareOp shareOp = new SqAskShareOp(context);
		shareOp.setParams(id);
		shareOp.onRequest(new RequestListener() {
			
			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCallBack(Object data) {
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						int oldshareCount = Integer.valueOf(askDetail.shareCount);
						oldshareCount++;
						askDetail.shareCount = oldshareCount + "";
						zhuanfaCount.setText("（" + askDetail.shareCount + "）");
						showToast("分享成功");
					}
				});
			}
		});
	}
	
	private void onAnswerZanRequest(final int position) {
		SqAnswerZanOp zanOp = new SqAnswerZanOp(context);
		zanOp.setParmas(adapter.getItem(position).id);
		zanOp.onRequest(new RequestListener() {
			
			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCallBack(Object data) {
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						int oldzanCount = Integer.valueOf(adapter.getItem(position).likeCount);
						oldzanCount++;
						adapter.getItem(position).likeCount = oldzanCount + "";
						adapter.notifyDataSetChanged();
						showToast("点赞成功");
					}
				});
			}
		});
	}

	private void initSqAnswerList(ArrayList<SqAnswerDetailEntry> list) {
		sqAnswerList.setVisibility(View.VISIBLE);
		findViewById(R.id.empty_content_layout).setVisibility(View.GONE);
		if (adapter == null) {
			adapter = new SqAnswerAdapter(context);
			adapter.addData(list);
			sqAnswerList.setAdapter(adapter);
			adapter.addOnZanClickListener(new OnZanClickListener() {

				@Override
				public void onZan(int position) {
					onAnswerZanRequest(position);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == CddRequestCode.ANSWER_PULISH_REQUEST) {
			sqAnswerList.getRefreshableView().removeFooterView(
					footMoreView);
			requestPage = 1;
			String pageNn = "";
			if (pageNum == 0) {
				pageNn = 1 + "";
			} else {
				pageNn = requestPage + "";
			}
			answerList.clear();
			requestAnswerList(askId, pageNn, false);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.photo1:
			gotoPhotoDetail(askDetail.photos, 0);
			break;
		case R.id.photo2:
			gotoPhotoDetail(askDetail.photos, 1);
			break;
		case R.id.photo3:
			gotoPhotoDetail(askDetail.photos, 2);
			break;
		case R.id.detail_answer_layout:
			Intent answerIntent = new Intent(context, PulishActivity.class);
			answerIntent.putExtra("ask_id", askId);
			answerIntent.putExtra("pulish_type", 2);
			startActivityForResult(answerIntent, CddRequestCode.ANSWER_PULISH_REQUEST);
			break;
		case R.id.zan_layout:
			onAskZanRequest(askId);
			break;
		case R.id.shoucang_layout:
			onAskSCRequest(askId);
			break;
		case R.id.zhuanfa_layout:
			onAskShareRequest(askId);
			break;

		case R.id.empty_content_layout:
			if (!TextUtils.isEmpty(askId)) {
				requestAnswerList(askId, "1", true);
			}
			break;
		default:
			break;
		}

	}

}
