package com.cdd.activity.sqpage;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.cdd.R;
import com.cdd.activity.sqpage.SqForumAdapter.onZanListener;
import com.cdd.base.BaseActivity;
import com.cdd.mode.ForumEntry;
import com.cdd.mode.ForumItem;
import com.cdd.mode.SqAskItem;
import com.cdd.mode.SqAskListRequest;
import com.cdd.mode.SqSearchRequestEntry;
import com.cdd.net.RequestListener;
import com.cdd.operater.AskZanOp;
import com.cdd.operater.SqAskListOp;
import com.cdd.operater.SqSearchOp;
import com.cdd.util.CddRequestCode;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class SqListActivity extends BaseActivity implements OnClickListener {

	private PullToRefreshListView sqContentList;

	private SqForumAdapter sqAdapter;

	private ForumItem forumItem = new ForumItem();

	private int pageNum = 0;

	private int requestPage = 1;

	private ArrayList<SqAskItem> askList = new ArrayList<SqAskItem>();
	
	private EditText searchContent;
	
	private View footMoreView;
	
	private int loadMode = 1; // 1为普通模式   2为搜索模式

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.sq_list_activity);
		initView();
		initContent();
	}

	private void loadMore() {
		sqContentList.getRefreshableView().removeFooterView(footMoreView);
		int page = pageNum + 1;
		if (!TextUtils.isEmpty(searchContent.getText().toString()) && loadMode == 2) {
			SqSearchRequestEntry requestS = new SqSearchRequestEntry();
			requestS.keyword = searchContent.getText().toString();
			if (!TextUtils.isEmpty(forumItem.fatherId) && !TextUtils.isEmpty(forumItem.id)) {
				requestS.itemId = forumItem.fatherId;
				requestS.subItemId = forumItem.id;
			} else if (TextUtils.isEmpty(forumItem.fatherId) && !TextUtils.isEmpty(forumItem.id)) {
				requestS.itemId = forumItem.id;
			}
			if (pageNum == 0) {
				requestS.pageNum = "1";
			} else {
				requestS.pageNum = page + "";
			}
			requestSearchList(requestS, true);
		} else if ((!TextUtils.isEmpty(searchContent.getText().toString()) && loadMode == 1)
				|| (TextUtils.isEmpty(searchContent.getText().toString()))) {
			searchContent.setText("");
			loadMode = 1;
			SqAskListRequest request = new SqAskListRequest();
			if (!TextUtils.isEmpty(forumItem.fatherId)
					&& !TextUtils.isEmpty(forumItem.id)) {
				request.itemId = forumItem.fatherId;
				request.subItemId = forumItem.id;
			} else if (TextUtils.isEmpty(forumItem.fatherId)
					&& !TextUtils.isEmpty(forumItem.id)) {
				request.itemId = forumItem.id;
			}
			if (pageNum == 0) {
				request.pageNum = "1";
			} else {
				request.pageNum = page + "";
			}
			requestAskList(request, true);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == CddRequestCode.ASK_PULISH_REQUEST) {
			searchContent.setText("");
			loadMode = 1;
			sqContentList.getRefreshableView().removeFooterView(footMoreView);
			requestPage = 1;
			SqAskListRequest request = new SqAskListRequest();
			if (!TextUtils.isEmpty(forumItem.fatherId)
					&& !TextUtils.isEmpty(forumItem.id)) {
				request.itemId = forumItem.fatherId;
				request.subItemId = forumItem.id;
			} else if (TextUtils.isEmpty(forumItem.fatherId)
					&& !TextUtils.isEmpty(forumItem.id)) {
				request.itemId = forumItem.id;
			}
			if (pageNum == 0) {
				request.pageNum = 1 + "";
			} else {
				request.pageNum = requestPage + "";
			}
			askList.clear();
			requestAskList(request, false);
		}
	}

	private void initView() {
		searchContent = (EditText) findViewById(R.id.search_content);
		footMoreView = View.inflate(context, R.layout.load_more_view, null);
		footMoreView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadMore();
			}
		});
		findViewById(R.id.empty_content_layout).setOnClickListener(this);
		findViewById(R.id.search_icon).setOnClickListener(this);
		findViewById(R.id.sq_ask_layout).setOnClickListener(this);
		sqContentList = (PullToRefreshListView) findViewById(R.id.sq_content_list);
		sqContentList.getRefreshableView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(context, SqAskDetailActivity.class);
				intent.putExtra("ask_id", sqAdapter.getItem(position - 1).id);
				startActivity(intent);
			}
		});
		sqContentList.setMode(Mode.PULL_FROM_START);
		sqContentList.getLoadingLayoutProxy(true, true).setPullLabel("下拉刷新...");
		sqContentList.getLoadingLayoutProxy(true, true).setRefreshingLabel(
				"正在刷新...");
		sqContentList.getLoadingLayoutProxy(true, true).setReleaseLabel(
				"释放刷新...");
		sqContentList.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshView.isHeaderShown()) {
					sqContentList.getRefreshableView().removeFooterView(footMoreView);
					requestPage = 1;
					
					
					if (!TextUtils.isEmpty(searchContent.getText().toString()) && loadMode == 2) {
						SqSearchRequestEntry request = new SqSearchRequestEntry();
						request.keyword = searchContent.getText().toString();
						if (!TextUtils.isEmpty(forumItem.fatherId)
								&& !TextUtils.isEmpty(forumItem.id)) {
							request.itemId = forumItem.fatherId;
							request.subItemId = forumItem.id;
						} else if (TextUtils.isEmpty(forumItem.fatherId)
								&& !TextUtils.isEmpty(forumItem.id)) {
							request.itemId = forumItem.id;
						}
						if (pageNum == 0) {
							request.pageNum = 1 + "";
						} else {
							request.pageNum = requestPage + "";
						}
						askList.clear();
						requestSearchList(request, false);
					} else if ((!TextUtils.isEmpty(searchContent.getText().toString()) && loadMode == 1)
							|| (TextUtils.isEmpty(searchContent.getText().toString()))) {
						loadMode = 1;
						searchContent.setText("");
						SqAskListRequest request = new SqAskListRequest();
						if (!TextUtils.isEmpty(forumItem.fatherId)
								&& !TextUtils.isEmpty(forumItem.id)) {
							request.itemId = forumItem.fatherId;
							request.subItemId = forumItem.id;
						} else if (TextUtils.isEmpty(forumItem.fatherId)
								&& !TextUtils.isEmpty(forumItem.id)) {
							request.itemId = forumItem.id;
						}
						if (pageNum == 0) {
							request.pageNum = 1 + "";
						} else {
							request.pageNum = requestPage + "";
						}
						askList.clear();
						requestAskList(request, false);
					}
				}
			}
		});
		searchContent.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId ==EditorInfo.IME_ACTION_SEARCH) {
					((InputMethodManager) searchContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
					if (TextUtils.isEmpty(searchContent.getText().toString())) {
						showToast("请输入搜索内容后再搜索");
						return false;
					}
					askList.clear();
					SqSearchRequestEntry requestS = new SqSearchRequestEntry();
					requestS.keyword = searchContent.getText().toString();
					if (!TextUtils.isEmpty(forumItem.fatherId) && !TextUtils.isEmpty(forumItem.id)) {
						requestS.itemId = forumItem.fatherId;
						requestS.subItemId = forumItem.id;
					} else if (TextUtils.isEmpty(forumItem.fatherId) && !TextUtils.isEmpty(forumItem.id)) {
						requestS.itemId = forumItem.id;
					}
					requestPage = 1;
					pageNum = 0;
					requestS.pageNum = "1";
					requestSearchList(requestS, true);
					return true;
				}
				return false;
			}
		});
	}

	private void initContent() {
		if (getIntent().getSerializableExtra("forum_item") != null
				&& getIntent().getSerializableExtra("forum_item") instanceof ForumItem) {
			forumItem = (ForumItem) getIntent().getSerializableExtra(
					"forum_item");
		}
		// showSqListView();
		if (forumItem != null) {
			SqAskListRequest request = new SqAskListRequest();
			if (forumItem != null && !TextUtils.isEmpty(forumItem.fatherId)
					&& !TextUtils.isEmpty(forumItem.id)) {
				request.itemId = forumItem.fatherId;
				request.subItemId = forumItem.id;
			} else if (forumItem != null && TextUtils.isEmpty(forumItem.fatherId)
					&& !TextUtils.isEmpty(forumItem.id)) {
				request.itemId = forumItem.id;
			}
			request.pageNum = "1";
			requestAskList(request, true);
		}
	}
	
	private void onZanRequest(final int position) {
		AskZanOp zanOp = new AskZanOp(context);
		zanOp.setParmas(sqAdapter.getItem(position).id);
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
						int oldzanCount = Integer.valueOf(sqAdapter.getItem(position).likeCount);
						oldzanCount++;
						sqAdapter.getItem(position).likeCount = oldzanCount + "";
						sqAdapter.notifyDataSetChanged();
						showToast("点赞成功");
					}
				});
			}
		});
	}

	private void initSqList(ArrayList<SqAskItem> list) {
		sqContentList.setVisibility(View.VISIBLE);
		findViewById(R.id.empty_content_layout).setVisibility(View.GONE);
		if (sqAdapter == null) {
			sqAdapter = new SqForumAdapter(context);
			sqAdapter.addData(list);
			sqContentList.getRefreshableView().setAdapter(sqAdapter);
			sqAdapter.addOnZanListener(new onZanListener() {
				
				@Override
				public void onZan(int position) {
					onZanRequest(position);
				}
			});
		} else {
			sqAdapter.clear();
			sqAdapter.addData(list);
			sqAdapter.notifyDataSetChanged();
		}
		if ((sqAdapter.getCount() % 20) == 0) {
			sqContentList.getRefreshableView().addFooterView(footMoreView);
		}
	}
	
	private void requestSearchList(SqSearchRequestEntry request,
			final boolean isShowNow) {
		final SqSearchOp searchOp = new SqSearchOp(context);
		searchOp.setParams(request);
		searchOp.onRequest(new RequestListener() {

			@Override
			public void onError(Object error) {
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						loadMode = 1;
						if (sqAdapter == null || (sqAdapter != null)) {
							pageNum = 1;
							sqContentList.setVisibility(View.GONE);
							findViewById(R.id.empty_content_layout).setVisibility(View.VISIBLE);
						}
						sqContentList.onRefreshComplete();
					}
				});
			}

			@Override
			public void onCallBack(Object data) {
				if (searchOp.getAskList().size() > 0) {
					loadMode = 2;
					askList.addAll(searchOp.getAskList());
					if (isShowNow) {
						pageNum++;
						handler.post(new Runnable() {

							@Override
							public void run() {
								sqContentList.onRefreshComplete();
								initSqList(askList);
							}
						});
					} else if (requestPage < pageNum) {
						requestPage++;
						SqSearchRequestEntry request = new SqSearchRequestEntry();
						request.keyword = searchContent.getText().toString();
						if (!TextUtils.isEmpty(forumItem.fatherId) && !TextUtils.isEmpty(forumItem.id)) {
							request.itemId = forumItem.fatherId;
							request.subItemId = forumItem.id;
						} else if (TextUtils.isEmpty(forumItem.fatherId) && !TextUtils.isEmpty(forumItem.id)) {
							request.itemId = forumItem.id;
						}
						request.pageNum = requestPage + "";
						requestSearchList(request, false);
					} else if ((requestPage == pageNum) || (pageNum == 0)) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								sqContentList.onRefreshComplete();
								initSqList(askList);
							}
						});
					}
				} else {
					if (sqAdapter == null || (sqAdapter != null && sqAdapter.getCount() == 0)) {
						loadMode = 1;
						sqAdapter.clear();
						askList.clear();
						handler.post(new Runnable() {
							
							@Override
							public void run() {
								sqContentList.setVisibility(View.GONE);
								findViewById(R.id.empty_content_layout).setVisibility(View.VISIBLE);
							}
						});
					}
					pageNum = 1;
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							sqContentList.onRefreshComplete();
						}
					});
				}
			}
		});
	}

	private void requestAskList(SqAskListRequest request,
			final boolean isShowNow) {
		final SqAskListOp askOp = new SqAskListOp(context);
		askOp.setParams(request);
		askOp.onRequest(new RequestListener() {

			@Override
			public void onError(Object error) {
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						if (sqAdapter == null || (sqAdapter != null)) {
							pageNum = 1;
							sqContentList.setVisibility(View.GONE);
							findViewById(R.id.empty_content_layout).setVisibility(View.VISIBLE);
						}
						sqContentList.onRefreshComplete();
					}
				});
			}

			@Override
			public void onCallBack(Object data) {
				if (askOp.getAskList().size() > 0) {
					askList.addAll(askOp.getAskList());
					if (isShowNow) {
						pageNum++;
						handler.post(new Runnable() {

							@Override
							public void run() {
								sqContentList.onRefreshComplete();
								initSqList(askList);
							}
						});
					} else if (requestPage < pageNum) {
						requestPage++;
						SqAskListRequest request = new SqAskListRequest();
						if (forumItem != null && !TextUtils.isEmpty(forumItem.fatherId)
								&& !TextUtils.isEmpty(forumItem.id)) {
							request.itemId = forumItem.fatherId;
							request.subItemId = forumItem.id;
						} else if (forumItem != null && TextUtils.isEmpty(forumItem.fatherId)
								&& !TextUtils.isEmpty(forumItem.id)) {
							request.itemId = forumItem.id;
						}
						request.pageNum = requestPage + "";
						requestAskList(request, false);
					} else if ((requestPage == pageNum) || (pageNum == 0)) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								sqContentList.onRefreshComplete();
								initSqList(askList);
							}
						});
					}
				} else {
					if (sqAdapter == null || (sqAdapter != null && sqAdapter.getCount() == 0)) {
						sqAdapter.clear();
						askList.clear();
						handler.post(new Runnable() {
							
							@Override
							public void run() {
								sqContentList.setVisibility(View.GONE);
								findViewById(R.id.empty_content_layout).setVisibility(View.VISIBLE);
							}
						});
					}
					pageNum = 1;
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							sqContentList.onRefreshComplete();
						}
					});
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_icon:
			if (TextUtils.isEmpty(searchContent.getText().toString())) {
				showToast("请输入搜索内容后再搜索");
				return;
			}
			askList.clear();
			SqSearchRequestEntry requestS = new SqSearchRequestEntry();
			requestS.keyword = searchContent.getText().toString();
			if (!TextUtils.isEmpty(forumItem.fatherId) && !TextUtils.isEmpty(forumItem.id)) {
				requestS.itemId = forumItem.fatherId;
				requestS.subItemId = forumItem.id;
			} else if (TextUtils.isEmpty(forumItem.fatherId) && !TextUtils.isEmpty(forumItem.id)) {
				requestS.itemId = forumItem.id;
			}
			requestPage = 1;
			pageNum = 0;
			requestS.pageNum = "1";
			requestSearchList(requestS, true);
			break;

		case R.id.sq_ask_layout:
			Intent askIntent = new Intent(context, PulishActivity.class);
			askIntent.putExtra("forum_item", forumItem);
			askIntent.putExtra("pulish_type", 1);
			startActivityForResult(askIntent, CddRequestCode.ASK_PULISH_REQUEST);
			break;
			
		case R.id.empty_content_layout:
			if (forumItem != null) {
				sqContentList.getRefreshableView().removeFooterView(footMoreView);
				searchContent.setText("");
				askList.clear();
				SqAskListRequest request = new SqAskListRequest();
				if (forumItem != null && !TextUtils.isEmpty(forumItem.fatherId)
						&& !TextUtils.isEmpty(forumItem.id)) {
					request.itemId = forumItem.fatherId;
					request.subItemId = forumItem.id;
				} else if (forumItem != null && TextUtils.isEmpty(forumItem.fatherId)
						&& !TextUtils.isEmpty(forumItem.id)) {
					request.itemId = forumItem.id;
				}
				requestPage = 1;
				pageNum = 0;
				request.pageNum = "1";
				requestAskList(request, true);
			}
			break;
		default:
			break;
		}

	}

}
