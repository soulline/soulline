package com.cdd.activity.findpage;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.cdd.R;
import com.cdd.activity.findpage.DynamicAdapter.OnAnswerMemberClickLister;
import com.cdd.activity.findpage.DynamicAdapter.OnImageClickListener;
import com.cdd.activity.findpage.DynamicAdapter.OnPackListener;
import com.cdd.activity.findpage.DynamicAdapter.OnWorkListener;
import com.cdd.activity.image.ImageNetPageActivity;
import com.cdd.base.BaseActivity;
import com.cdd.fragment.BaseFragmentListener;
import com.cdd.fragment.BottomReplyFragment;
import com.cdd.mode.DynamicEntry;
import com.cdd.mode.DynamicReplay;
import com.cdd.mode.PhotosEntry;
import com.cdd.net.RequestListener;
import com.cdd.operater.HotNewsListOp;
import com.cdd.operater.NewsShareOp;
import com.cdd.operater.NewsShoucangOp;
import com.cdd.operater.NewsZanOp;
import com.cdd.util.CddRequestCode;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

public class HotNewsListActivity extends BaseActivity implements OnClickListener{


	private PullToRefreshListView dynamicListView;

	private int pageNum = 0;

	private int requestPage = 1;

	private ArrayList<DynamicEntry> dynamicList = new ArrayList<DynamicEntry>();

	private View footMoreView;

	private DynamicAdapter adapter;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.dynamic_list_activity);
		initTitle("热门新鲜事");
		initView();
		initContent();
	}

	private void loadMore() {
		dynamicListView.getRefreshableView().removeFooterView(footMoreView);
		int page = pageNum + 1;
		String pageNumber = "";
		if (pageNum == 0) {
			pageNumber = "1";
		} else {
			pageNumber = page + "";
		}
		requestDynamicList(pageNumber, true);
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
		findViewById(R.id.add_news_layout).setOnClickListener(this);
		dynamicListView = (PullToRefreshListView) findViewById(R.id.dynamic_list);
		initDynamicListView();
	}

	private void initDynamicListView() {
		dynamicListView.getRefreshableView().setOnItemClickListener(
				new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent detail = new Intent(context, NewsDetailActivity.class);
						detail.putExtra("cofId", adapter.getItem(position - 1).id);
						startActivity(detail);
					}
				});
		dynamicListView.setMode(Mode.PULL_FROM_START);
		dynamicListView.getLoadingLayoutProxy(true, true).setPullLabel(
				"下拉刷新...");
		dynamicListView.getLoadingLayoutProxy(true, true).setRefreshingLabel(
				"正在刷新...");
		dynamicListView.getLoadingLayoutProxy(true, true).setReleaseLabel(
				"释放刷新...");
		dynamicListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshView.isHeaderShown()) {
					doRefreshListView();
				}
			}
		});
	}
	
	private void onNewsZanRequest(String cofId, final int position) {
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
						String count = adapter.getItem(position).likeCount;
						int countN = Integer.valueOf(count);
						countN++;
						adapter.getItem(position).likeCount = countN + "";
						adapter.notifyDataSetChanged();
					}
				});
			}
		});
	}
	
	private void onNewsShoucangRequest(String cofId, final int position) {
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
						String count = adapter.getItem(position).favoriteCount;
						int countN = Integer.valueOf(count);
						countN++;
						adapter.getItem(position).favoriteCount = countN + "";
						adapter.notifyDataSetChanged();
					}
				});
			}
		});
	}
	
	private void onNewsShareRequest(String cofId, final int position) {
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
						String count = adapter.getItem(position).shareCount;
						int countN = Integer.valueOf(count);
						countN++;
						adapter.getItem(position).shareCount = countN + "";
						adapter.notifyDataSetChanged();
					}
				});
			}
		});
	}
	
	private void doRefreshListView() {
		dynamicListView.getRefreshableView().removeFooterView(
				footMoreView);
		requestPage = 1;
		String pageNumber = "";
		if (pageNum == 0) {
			pageNumber = 1 + "";
		} else {
			pageNumber = requestPage + "";
		}
		dynamicList.clear();
		requestDynamicList(pageNumber, false);
	}

	private void initContent() {
		requestDynamicList("1", true);
	}

	private void initDynamicList(ArrayList<DynamicEntry> list) {
		dynamicListView.setVisibility(View.VISIBLE);
		findViewById(R.id.empty_content_layout).setVisibility(View.GONE);
		if (adapter == null) {
			adapter = new DynamicAdapter(context);
			adapter.addData(list);
			dynamicListView.getRefreshableView().setAdapter(adapter);
			addAdapterListener(adapter);
		} else {
			adapter.clear();
			adapter.addData(list);
			adapter.notifyDataSetChanged();
		}
		if ((adapter.getCount() % 20) == 0) {
			dynamicListView.getRefreshableView().addFooterView(footMoreView);
		}
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

	private void addAdapterListener(DynamicAdapter adapterN) {
		adapterN.addOnAnswerMemberClickLister(new OnAnswerMemberClickLister() {

			@Override
			public void onAnswerClick(DynamicReplay replay, int position) {
				Intent userInfo = new Intent(context, UserInfoActivity.class);
				userInfo.putExtra("memberId", replay.memberId);
				startActivity(userInfo);
			}
		});

		adapterN.addOnImageClickListener(new OnImageClickListener() {

			@Override
			public void onImageClick(int position, int index) {
				if (adapter != null) {
					gotoPhotoDetail(adapter.getItem(position).photos, index);
				}
			}
		});

		adapterN.addOnPackListener(new OnPackListener() {

			@Override
			public void onPack(int position, boolean pack) {
				if (adapter != null && !pack) {
					adapter.getItem(position).isPack = false;
					adapter.notifyDataSetChanged();
				} else if (adapter != null && pack) {
					adapter.getItem(position).isPack = true;
					adapter.notifyDataSetChanged();
				}
			}
		});

		adapterN.addOnWorkListener(new OnWorkListener() {

			@Override
			public void onWork(int position, int type) {
				closeFragment("bottom_reply");
				if (type == 3) {
					Bundle b = new Bundle();
					b.putString("cofId", adapter.getItem(position).id);
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
				} else if (type == 0) {
					onNewsZanRequest(adapter.getItem(position).id, position);
				} else if (type == 1) {
					onNewsShoucangRequest(adapter.getItem(position).id, position);
				} else if (type == 2) {
					onNewsShareRequest(adapter.getItem(position).id, position);
				} else if (type == 4) {
					Intent forward = new Intent(context, ForwardDetailActivity.class);
					forward.putExtra("cofId", adapter.getItem(position).forward.id);
					startActivity(forward);
				} else if (type == 5) {
					Intent userInfo = new Intent(context, UserInfoActivity.class);
					userInfo.putExtra("memberId", adapter.getItem(position).memberId);
					startActivity(userInfo);
				}
			}
		});
	}

	private void requestDynamicList(String pageNumber, final boolean isShowNow) {
		final HotNewsListOp dingdangOp = new HotNewsListOp(
				context);
		dingdangOp.setParams("1");
		dingdangOp.onRequest(new RequestListener() {

			@Override
			public void onError(Object error) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						if (adapter == null
								|| (adapter != null && adapter.getCount() == 0)) {
							dynamicListView.setVisibility(View.GONE);
							findViewById(R.id.empty_content_layout)
									.setVisibility(View.VISIBLE);
						}
						dynamicListView.onRefreshComplete();
					}
				});

			}

			@Override
			public void onCallBack(Object data) {
				if (dingdangOp.getDynamicList().size() > 0) {
					dynamicList.addAll(dingdangOp.getDynamicList());
					if (isShowNow) {
						pageNum++;
						handler.post(new Runnable() {

							@Override
							public void run() {
								dynamicListView.onRefreshComplete();
								initDynamicList(dynamicList);
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
								dynamicListView.onRefreshComplete();
								initDynamicList(dynamicList);
							}
						});
					}
				} else {
					if (adapter == null
							|| (adapter != null && adapter.getCount() == 0)) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								dynamicListView.setVisibility(View.GONE);
								findViewById(R.id.empty_content_layout)
										.setVisibility(View.VISIBLE);
							}
						});
					}
					pageNum = 1;
					handler.post(new Runnable() {

						@Override
						public void run() {
							dynamicListView.onRefreshComplete();
						}
					});
				}

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == CddRequestCode.DYNAMIC_PULISH_REQUEST) {
			doRefreshListView();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_news_layout:
			Intent addNews = new Intent(context, PulishDynamicActivity.class);
			startActivityForResult(addNews, CddRequestCode.DYNAMIC_PULISH_REQUEST);
			break;

		case R.id.empty_content_layout:
			requestDynamicList("1", true);
			break;

		default:
			break;
		}

	}


}
