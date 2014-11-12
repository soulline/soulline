package com.cdd.activity.minepage;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.cdd.R;
import com.cdd.activity.minepage.FansAdapter.OnListenChange;
import com.cdd.base.BaseActivity;
import com.cdd.mode.FansEntry;
import com.cdd.net.RequestListener;
import com.cdd.operater.AttentionOp;
import com.cdd.operater.FansListOp;
import com.cdd.operater.ListenListOp;
import com.cdd.operater.UnAttentionOp;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

public class FansOrListenActivity extends BaseActivity implements OnClickListener{

	private PullToRefreshListView fansListview;
	
	private ArrayList<FansEntry> fansList = new ArrayList<FansEntry>();
	
	private int pageNum = 0;

	private int requestPage = 1;
	
	private View footMoreView;
	
	private FansAdapter adapter;
	
	private String showType = "";
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.fans_listen_activity);
		initView();
		initContent();
	}

	private void initContent() {
		showType = getIntent().getStringExtra("fans_type");
		if (showType.equals("1")) {
			initTitle("粉丝");
			requestFansList("1", true);
		} else if (showType.equals("2")) {
			initTitle("关注");
			requestListenList("1", true);
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
		findViewById(R.id.empty_content_layout).setOnClickListener(this);
		fansListview = (PullToRefreshListView) findViewById(R.id.fans_list);
		initfansListView();
	}
	
	private void loadMore() {
		fansListview.getRefreshableView().removeFooterView(footMoreView);
		int page = pageNum + 1;
		String pageNumber = "";
		if (pageNum == 0) {
			pageNumber = "1";
		} else {
			pageNumber = page + "";
		}
		if (showType.equals("1")) {
			requestFansList(pageNumber, true);
		} else if (showType.equals("2")) {
			requestListenList(pageNumber, true);
		}
	}
	
	private void doRefreshListView() {
		fansListview.getRefreshableView().removeFooterView(
				footMoreView);
		requestPage = 1;
		String pageNumber = "";
		if (pageNum == 0) {
			pageNumber = 1 + "";
		} else {
			pageNumber = requestPage + "";
		}
		fansList.clear();
		if (showType.equals("1")) {
			requestFansList(pageNumber, false);
		} else if (showType.equals("2")) {
			requestListenList(pageNumber, false);
		}
	}
	
	private void attentionRequest(final int position, String idolId) {
		AttentionOp atOp = new AttentionOp(context);
		atOp.setParams(idolId);
		atOp.onRequest(new RequestListener() {
			
			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCallBack(Object data) {
				showToast("关注成功");
				if (showType.equals("1")) {
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							adapter.getItem(position).relation = "1";
							fansList.get(position).relation = "1";
							adapter.notifyDataSetChanged();
						}
					});
				}
			}
		});
	}
	
	private void unAttentionRequest(final int position, String idolId) {
		UnAttentionOp uatOp = new UnAttentionOp(context);
		uatOp.setParams(idolId);
		uatOp.onRequest(new RequestListener() {
			
			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCallBack(Object data) {
				showToast("已取消关注");
				if (showType.equals("1")) {
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							adapter.getItem(position).relation = "0";
							fansList.get(position).relation = "0";
							adapter.notifyDataSetChanged();
						}
					});
				} else if (showType.equals("2")) {
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							adapter.remove(adapter.getItem(position));
							fansList.remove(position);
							adapter.notifyDataSetChanged();
						}
					});
				}
			}
		});
	}
	
	private void initFansListContent(ArrayList<FansEntry> list) {
		fansListview.setVisibility(View.VISIBLE);
		findViewById(R.id.empty_content_layout).setVisibility(View.GONE);
		if (adapter == null) {
			if (showType.equals("1")) {
				adapter = new FansAdapter(context, 1);
			} else if (showType.equals("2")) {
				adapter = new FansAdapter(context, 2);
			}
			adapter.addData(list);
			fansListview.getRefreshableView().setAdapter(adapter);
			adapter.addOnListenChange(new OnListenChange() {
				
				@Override
				public void onChange(int position, String relation) {
					if (showType.equals("1") && relation.equals("1")) {
						unAttentionRequest(position, adapter.getItem(position).id);
					} else if (showType.equals("1") && relation.equals("0")) {
						attentionRequest(position, adapter.getItem(position).id);
					} else if (showType.equals("2")) {
						unAttentionRequest(position, adapter.getItem(position).id);
					}
				}
			});
		} else {
			adapter.clear();
			adapter.addData(list);
			adapter.notifyDataSetChanged();
		}
		if ((adapter.getCount() % 20) == 0) {
			fansListview.getRefreshableView().addFooterView(footMoreView);
		}
	}
	
	private void requestListenList(String page, final boolean isShowNow) {
		final ListenListOp listenOp = new ListenListOp(context);
		listenOp.setParams(page);
		listenOp.onRequest(new RequestListener() {
			
			@Override
			public void onError(Object error) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						if (adapter == null
								|| (adapter != null && adapter.getCount() == 0)) {
							fansListview.setVisibility(View.GONE);
							findViewById(R.id.empty_content_layout)
									.setVisibility(View.VISIBLE);
						}
						fansListview.onRefreshComplete();
					}
				});
			}
			
			@Override
			public void onCallBack(Object data) {

				if (listenOp.getFansList().size() > 0) {
					fansList.addAll(listenOp.getFansList());
					if (isShowNow) {
						pageNum++;
						handler.post(new Runnable() {

							@Override
							public void run() {
								fansListview.onRefreshComplete();
								initFansListContent(fansList);
							}
						});
					} else if (requestPage < pageNum) {
						requestPage++;
						String pageNumber = requestPage + "";
						requestListenList(pageNumber, false);
					} else if ((requestPage == pageNum) || (pageNum == 0)) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								fansListview.onRefreshComplete();
								initFansListContent(fansList);
							}
						});
					}
				} else {
					if (adapter == null
							|| (adapter != null && adapter.getCount() == 0)) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								fansListview.setVisibility(View.GONE);
								findViewById(R.id.empty_content_layout)
										.setVisibility(View.VISIBLE);
							}
						});
					}
					pageNum = 1;
					handler.post(new Runnable() {

						@Override
						public void run() {
							fansListview.onRefreshComplete();
						}
					});
				}
			}
		});
	}
	
	private void requestFansList(String page, final boolean isShowNow) {
		final FansListOp fansOp = new FansListOp(context);
		fansOp.setParams(page);
		fansOp.onRequest(new RequestListener() {
			
			@Override
			public void onError(Object error) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						if (adapter == null
								|| (adapter != null && adapter.getCount() == 0)) {
							fansListview.setVisibility(View.GONE);
							findViewById(R.id.empty_content_layout)
									.setVisibility(View.VISIBLE);
						}
						fansListview.onRefreshComplete();
					}
				});
			}
			
			@Override
			public void onCallBack(Object data) {

				if (fansOp.getFansList().size() > 0) {
					fansList.addAll(fansOp.getFansList());
					if (isShowNow) {
						pageNum++;
						handler.post(new Runnable() {

							@Override
							public void run() {
								fansListview.onRefreshComplete();
								initFansListContent(fansList);
							}
						});
					} else if (requestPage < pageNum) {
						requestPage++;
						String pageNumber = requestPage + "";
						requestFansList(pageNumber, false);
					} else if ((requestPage == pageNum) || (pageNum == 0)) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								fansListview.onRefreshComplete();
								initFansListContent(fansList);
							}
						});
					}
				} else {
					if (adapter == null
							|| (adapter != null && adapter.getCount() == 0)) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								fansListview.setVisibility(View.GONE);
								findViewById(R.id.empty_content_layout)
										.setVisibility(View.VISIBLE);
							}
						});
					}
					pageNum = 1;
					handler.post(new Runnable() {

						@Override
						public void run() {
							fansListview.onRefreshComplete();
						}
					});
				}
			}
		});
	}
	
	private void initfansListView() {
		fansListview.getRefreshableView().setOnItemClickListener(
				new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						
					}
				});
		fansListview.setMode(Mode.PULL_FROM_START);
		fansListview.getLoadingLayoutProxy(true, true).setPullLabel(
				"下拉刷新...");
		fansListview.getLoadingLayoutProxy(true, true).setRefreshingLabel(
				"正在刷新...");
		fansListview.getLoadingLayoutProxy(true, true).setReleaseLabel(
				"释放刷新...");
		fansListview.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshView.isHeaderShown()) {
					doRefreshListView();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.empty_content_layout:
			doRefreshListView();
			break;

		default:
			break;
		}
		
	}
}
