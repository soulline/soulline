package com.cdd.activity.minepage;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.cdd.R;
import com.cdd.activity.minepage.MessageAdapter.OnReplyListener;
import com.cdd.base.BaseActivity;
import com.cdd.mode.MessageEntry;
import com.cdd.net.RequestListener;
import com.cdd.operater.MessageListOp;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

public class MessageListActivity extends BaseActivity implements
		OnClickListener {

	private int pageNum = 0;

	private int requestPage = 1;

	private View footMoreView;

	private ArrayList<MessageEntry> messageList = new ArrayList<MessageEntry>();

	private MessageAdapter adapter;

	private PullToRefreshListView messageListview;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.message_list_activity);
		initView();
		initContent();
	}

	private void initView() {
		initTitle("我的私信");
		footMoreView = View.inflate(context, R.layout.load_more_view, null);
		footMoreView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadMore();
			}
		});
		findViewById(R.id.empty_content_layout).setOnClickListener(this);
		messageListview = (PullToRefreshListView) findViewById(R.id.message_list);
		initMessageListView();
	}

	private void initContent() {
		requestMessgeList("1", true);
	}

	private void loadMore() {
		messageListview.getRefreshableView().removeFooterView(footMoreView);
		int page = pageNum + 1;
		String pageNumber = "";
		if (pageNum == 0) {
			pageNumber = "1";
		} else {
			pageNumber = page + "";
		}
		requestMessgeList(pageNumber, true);
	}

	private void initMessageListView() {
		messageListview.getRefreshableView().setOnItemClickListener(
				new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

					}
				});
		messageListview.setMode(Mode.PULL_FROM_START);
		messageListview.getLoadingLayoutProxy(true, true).setPullLabel(
				"下拉刷新...");
		messageListview.getLoadingLayoutProxy(true, true).setRefreshingLabel(
				"正在刷新...");
		messageListview.getLoadingLayoutProxy(true, true).setReleaseLabel(
				"释放刷新...");
		messageListview.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshView.isHeaderShown()) {
					doRefreshListView();
				}
			}
		});
	}

	private void doRefreshListView() {
		messageListview.getRefreshableView().removeFooterView(footMoreView);
		requestPage = 1;
		String pageNumber = "";
		if (pageNum == 0) {
			pageNumber = 1 + "";
		} else {
			pageNumber = requestPage + "";
		}
		messageList.clear();
		requestMessgeList(pageNumber, false);
	}
	
	private void requestMessgeList(String page, final boolean isShowNow) {
		final MessageListOp msgListOp = new MessageListOp(context);
		msgListOp.setParams(page);
		msgListOp.onRequest(new RequestListener() {
			
			@Override
			public void onError(Object error) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						if (adapter == null
								|| (adapter != null && adapter.getCount() == 0)) {
							messageListview.setVisibility(View.GONE);
							findViewById(R.id.empty_content_layout)
									.setVisibility(View.VISIBLE);
						}
						messageListview.onRefreshComplete();
					}
				});
			}
			
			@Override
			public void onCallBack(Object data) {

				if (msgListOp.getMessageList().size() > 0) {
					messageList.addAll(msgListOp.getMessageList());
					if (isShowNow) {
						pageNum++;
						handler.post(new Runnable() {

							@Override
							public void run() {
								messageListview.onRefreshComplete();
								initMsgListContent(messageList);
							}
						});
					} else if (requestPage < pageNum) {
						requestPage++;
						String pageNumber = requestPage + "";
						requestMessgeList(pageNumber, false);
					} else if ((requestPage == pageNum) || (pageNum == 0)) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								messageListview.onRefreshComplete();
								initMsgListContent(messageList);
							}
						});
					}
				} else {
					if (adapter == null
							|| (adapter != null && adapter.getCount() == 0)) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								messageListview.setVisibility(View.GONE);
								findViewById(R.id.empty_content_layout)
										.setVisibility(View.VISIBLE);
							}
						});
					}
					pageNum = 1;
					handler.post(new Runnable() {

						@Override
						public void run() {
							messageListview.onRefreshComplete();
						}
					});
				}
			}
		});
	}
	
	private void initMsgListContent(ArrayList<MessageEntry> list) {
		messageListview.setVisibility(View.VISIBLE);
		findViewById(R.id.empty_content_layout).setVisibility(View.GONE);
		if (adapter == null) {
				adapter = new MessageAdapter(context);
			adapter.addData(list);
			messageListview.getRefreshableView().setAdapter(adapter);
			adapter.addOnReplyListener(new OnReplyListener() {
				
				@Override
				public void onReply(int position) {
					MessageEntry entry = adapter.getItem(position);
				}
			});
		} else {
			adapter.clear();
			adapter.addData(list);
			adapter.notifyDataSetChanged();
		}
		if ((adapter.getCount() % 20) == 0) {
			messageListview.getRefreshableView().addFooterView(footMoreView);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.empty_content_layout:
			requestMessgeList("1", true);
			break;

		default:
			break;
		}

	}

}
