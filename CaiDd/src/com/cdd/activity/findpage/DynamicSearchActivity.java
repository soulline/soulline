package com.cdd.activity.findpage;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
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
import com.cdd.operater.NewsSearchOp;
import com.cdd.util.CddRequestCode;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

public class DynamicSearchActivity extends BaseActivity implements OnClickListener{

	private PullToRefreshListView dynamicListView;

	private int pageNum = 0;

	private int requestPage = 1;

	private ArrayList<DynamicEntry> dynamicList = new ArrayList<DynamicEntry>();

	private View footMoreView;

	private DynamicAdapter adapter;
	
	private EditText searchContent;
	
	private String keywordStr = "";
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.dynamic_search_activity);
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
		if (!TextUtils.isEmpty(keywordStr)) {
			requestDynamicList(pageNumber, keywordStr, true);
		}
	}

	private void initView() {
		footMoreView = View.inflate(context, R.layout.load_more_view, null);
		footMoreView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadMore();
			}
		});
		searchContent = (EditText) findViewById(R.id.search_content);
		findViewById(R.id.empty_content_layout).setOnClickListener(this);
		findViewById(R.id.search_icon).setOnClickListener(this);
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
				if (refreshView.isHeaderShown() && !TextUtils.isEmpty(keywordStr)) {
					doRefreshListView();
				} else if (refreshView.isHeaderShown()) {
					dynamicListView.onRefreshComplete();
				}
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
		if (!TextUtils.isEmpty(keywordStr)) {
			requestDynamicList(pageNumber, keywordStr, false);
		}
	}

	private void initContent() {
		
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
				}

			}
		});
	}

	private void requestDynamicList(String pageNumber, String keyword, final boolean isShowNow) {
		final NewsSearchOp dingdangOp = new NewsSearchOp(
				context);
		dingdangOp.setParams(pageNumber, keyword);
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
						if (!TextUtils.isEmpty(keywordStr)) {
							requestDynamicList(pageNumber, keywordStr, false);
						}
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
		case R.id.empty_content_layout:
			if (!TextUtils.isEmpty(keywordStr)) {
				requestDynamicList("1", keywordStr, true);
			}
			break;

		case R.id.search_icon:
			keywordStr = searchContent.getText().toString().trim();
			if (!TextUtils.isEmpty(keywordStr)) {
				pageNum = 0;
				requestPage = 1;
				dynamicList.clear();
				requestDynamicList("1", keywordStr, true);
			} else {
				showToast("请输入关键字搜索");
			}
			break;
			
		default:
			break;
		}

	}





}
