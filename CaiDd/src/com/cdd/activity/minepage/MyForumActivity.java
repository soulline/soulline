package com.cdd.activity.minepage;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.cdd.R;
import com.cdd.activity.sqpage.SqAskDetailActivity;
import com.cdd.activity.sqpage.SqForumAdapter;
import com.cdd.activity.sqpage.SqForumAdapter.onZanListener;
import com.cdd.app.CddApp;
import com.cdd.base.BaseActivity;
import com.cdd.mode.ForumEntry;
import com.cdd.mode.SqAskItem;
import com.cdd.mode.SqAskListRequest;
import com.cdd.net.RequestListener;
import com.cdd.operater.AskZanOp;
import com.cdd.operater.MyAnswersListOp;
import com.cdd.operater.MyQuestionListOp;
import com.cdd.operater.SqAskListOp;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

public class MyForumActivity extends BaseActivity implements OnClickListener{

	private int contentType = -1;

	private SqForumAdapter sqAdapter;

	private PullToRefreshListView sqListview;

	private View footMoreView;

	private int pageNum = 0;

	private int requestPage = 1;

	private ArrayList<SqAskItem> askList = new ArrayList<SqAskItem>();

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.my_forum_activity);
		contentType = getIntent().getIntExtra("content_type", -1);
		initContentTitle(contentType);
		initView();
		initContent();
	}

	private void initContent() {
		if (contentType == 1) {
			requestAskList("1", true);
		} else if (contentType == 2) {
			requestAnswerList("1", true);
		}
	}

	private void initView() {
		findViewById(R.id.empty_content_layout).setOnClickListener(this);
		footMoreView = View.inflate(context, R.layout.load_more_view, null);
		footMoreView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadMore();
			}
		});
		sqListview = (PullToRefreshListView) findViewById(R.id.sq_listview);
		initRefreshListView();
	}
	
	private void initRefreshListView() {
		sqListview.getRefreshableView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(context, SqAskDetailActivity.class);
				intent.putExtra("ask_id", sqAdapter.getItem(position - 1).id);
				startActivity(intent);
			}
		});
		sqListview.setMode(Mode.PULL_FROM_START);
		sqListview.getLoadingLayoutProxy(true, true).setPullLabel("下拉刷新...");
		sqListview.getLoadingLayoutProxy(true, true).setRefreshingLabel(
				"正在刷新...");
		sqListview.getLoadingLayoutProxy(true, true).setReleaseLabel(
				"释放刷新...");
		sqListview.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshView.isHeaderShown()) {
					sqListview.getRefreshableView().removeFooterView(footMoreView);
					requestPage = 1;
					String pageNn = "";
					if (pageNum == 0) {
						pageNn = 1 + "";
					} else {
						pageNn = requestPage + "";
					}
					askList.clear();
					if (contentType == 1) {
						requestAskList(pageNn, false);
					} else if (contentType == 2) {
						requestAnswerList(pageNn, false);
					}
				}
			}
		});
	}

	private void loadMore() {
		sqListview.getRefreshableView().removeFooterView(footMoreView);
		int page = pageNum + 1;
		String pageNn = page + "";
		if (contentType == 1) {
			requestAskList(pageNn, true);
		} else if (contentType == 2) {
			requestAnswerList(pageNn, true);
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
		sqListview.setVisibility(View.VISIBLE);
		findViewById(R.id.empty_content_layout).setVisibility(View.GONE);
		if (sqAdapter == null) {
			sqAdapter = new SqForumAdapter(context);
			sqAdapter.addData(list);
			sqListview.getRefreshableView().setAdapter(sqAdapter);
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
			sqListview.getRefreshableView().addFooterView(footMoreView);
		}
	}
	
	private void requestAskList(String pageNumber,
			final boolean isShowNow) {
		final MyQuestionListOp askOp = new MyQuestionListOp(context);
		askOp.setParams(pageNumber);
		askOp.onRequest(new RequestListener() {

			@Override
			public void onError(Object error) {
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						if (sqAdapter == null || (sqAdapter != null && sqAdapter.getCount() == 0)) {
							sqListview.setVisibility(View.GONE);
							findViewById(R.id.empty_content_layout).setVisibility(View.VISIBLE);
						}
						sqListview.onRefreshComplete();
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
								sqListview.onRefreshComplete();
								initSqList(askList);
							}
						});
					} else if (requestPage < pageNum) {
						requestPage++;
						String pageNn = requestPage + "";
						requestAskList(pageNn, false);
					} else if ((requestPage == pageNum) || (pageNum == 0)) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								sqListview.onRefreshComplete();
								initSqList(askList);
							}
						});
					}
				} else {
					if (sqAdapter == null || (sqAdapter != null && sqAdapter.getCount() == 0)) {
						handler.post(new Runnable() {
							
							@Override
							public void run() {
								sqListview.setVisibility(View.GONE);
								findViewById(R.id.empty_content_layout).setVisibility(View.VISIBLE);
							}
						});
					}
					pageNum = 1;
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							sqListview.onRefreshComplete();
						}
					});
				}
			}
		});
	}
	
	private void requestAnswerList(String pageNumber,
			final boolean isShowNow) {
		final MyAnswersListOp answerOp = new MyAnswersListOp(context);
		answerOp.setParams(pageNumber);
		answerOp.onRequest(new RequestListener() {

			@Override
			public void onError(Object error) {
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						if (sqAdapter == null || (sqAdapter != null && sqAdapter.getCount() == 0)) {
							sqListview.setVisibility(View.GONE);
							findViewById(R.id.empty_content_layout).setVisibility(View.VISIBLE);
						}
						sqListview.onRefreshComplete();
					}
				});
			}

			@Override
			public void onCallBack(Object data) {
				if (answerOp.getAskList().size() > 0) {
					askList.addAll(answerOp.getAskList());
					if (isShowNow) {
						pageNum++;
						handler.post(new Runnable() {

							@Override
							public void run() {
								sqListview.onRefreshComplete();
								initSqList(askList);
							}
						});
					} else if (requestPage < pageNum) {
						requestPage++;
						String pageNn = requestPage + "";
						requestAnswerList(pageNn, false);
					} else if ((requestPage == pageNum) || (pageNum == 0)) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								sqListview.onRefreshComplete();
								initSqList(askList);
							}
						});
					}
				} else {
					if (sqAdapter == null || (sqAdapter != null && sqAdapter.getCount() == 0)) {
						handler.post(new Runnable() {
							
							@Override
							public void run() {
								sqListview.setVisibility(View.GONE);
								findViewById(R.id.empty_content_layout).setVisibility(View.VISIBLE);
							}
						});
					}
					pageNum = 1;
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							sqListview.onRefreshComplete();
						}
					});
				}
			}
		});
	}

	private void initContentTitle(int type) {
		switch (type) {
		case 1:
			initTitle("我的提问");
			break;

		case 2:
			initTitle("我的回答");
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.empty_content_layout:
			if (contentType == 1) {
				requestAskList("1", true);
			} else if (contentType == 2) {
				requestAnswerList("1", true);
			}
			break;

		default:
			break;
		}
		
	}

}
