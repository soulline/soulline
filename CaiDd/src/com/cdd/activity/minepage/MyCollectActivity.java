package com.cdd.activity.minepage;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.cdd.R;
import com.cdd.activity.sqpage.SqAskDetailActivity;
import com.cdd.activity.sqpage.SqForumAdapter;
import com.cdd.activity.sqpage.SqForumAdapter.onZanListener;
import com.cdd.base.BaseActivity;
import com.cdd.mode.SqAskItem;
import com.cdd.net.RequestListener;
import com.cdd.operater.AskZanOp;
import com.cdd.operater.MyQuestionListOp;
import com.cdd.operater.SqMyShoucangListOp;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

public class MyCollectActivity extends BaseActivity implements OnClickListener{

	private ImageView scAskImg, scDongtaiImg;
	
	private TextView scAskTx, scDongtaiTx;
	
	private int checkStatus = 0;
	
	private SqForumAdapter sqAdapter;

	private PullToRefreshListView sqListview, dongtaiListview;

	private View footMoreView;

	private int pageNum = 0;

	private int requestPage = 1;

	private ArrayList<SqAskItem> askList = new ArrayList<SqAskItem>();
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.my_collect_activity);
		initTitle("我的收藏");
		initView();
		initContent();
	}

	private void loadMore() {
		sqListview.getRefreshableView().removeFooterView(footMoreView);
		int page = pageNum + 1;
		String pageNn = page + "";
		if (checkStatus == 0) {
			requestAskList(pageNn, true);
		} else if (checkStatus == 1) {
			
		}
	}
	
	private void initAskRefreshListView() {
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
					if (checkStatus == 0) {
						requestAskList(pageNn, false);
					} else if (checkStatus == 1) {
						
					}
				}
			}
		});
	}
	
	private void onAskZanRequest(final int position) {
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
					onAskZanRequest(position);
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
		final SqMyShoucangListOp askOp = new SqMyShoucangListOp(context);
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
	
	private void initView() {
		findViewById(R.id.empty_content_layout).setOnClickListener(this);
		footMoreView = View.inflate(context, R.layout.load_more_view, null);
		footMoreView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadMore();
			}
		});
		findViewById(R.id.sc_ask_layout).setOnClickListener(this);
		findViewById(R.id.sc_dongtai_layout).setOnClickListener(this);
		scAskImg = (ImageView) findViewById(R.id.sc_ask_img);
		scDongtaiImg = (ImageView) findViewById(R.id.sc_dongtai_img);
		scAskTx = (TextView) findViewById(R.id.sc_ask_tx);
		scDongtaiTx = (TextView) findViewById(R.id.sc_dongtai_tx);
		sqListview = (PullToRefreshListView) findViewById(R.id.sq_listview);
		initAskRefreshListView();
		dongtaiListview = (PullToRefreshListView) findViewById(R.id.dongtai_listview);
	}
	
	private void initContent() {
		setCheck(checkStatus);
	}
	
	private void setCheck(int check) {
		switch (check) {
		case 0:
			scAskImg
					.setBackgroundResource(R.drawable.sc_askt_press);
			findViewById(R.id.sc_ask_layout).setBackgroundColor(
					getResources().getColor(R.color.sq_blue));
			scAskTx.setTextColor(getResources().getColor(R.color.white));
			scDongtaiImg.setBackgroundResource(R.drawable.sc_dongtai);
			findViewById(R.id.sc_dongtai_layout).setBackgroundColor(
					getResources().getColor(R.color.white));
			scDongtaiTx.setTextColor(getResources().getColor(R.color.sq_blue));
			sqListview.setVisibility(View.VISIBLE);
			dongtaiListview.setVisibility(View.GONE);
			requestAskList("1", true);
			break;
		case 1:
			scAskImg
					.setBackgroundResource(R.drawable.sc_ask);
			findViewById(R.id.sc_ask_layout).setBackgroundColor(
					getResources().getColor(R.color.white));
			scAskTx
					.setTextColor(getResources().getColor(R.color.sq_blue));
			scDongtaiImg.setBackgroundResource(R.drawable.sc_dongtai_press);
			findViewById(R.id.sc_dongtai_layout).setBackgroundColor(
					getResources().getColor(R.color.sq_blue));
			scDongtaiTx.setTextColor(getResources().getColor(R.color.white));
			askList.clear();
			sqListview.setVisibility(View.GONE);
			dongtaiListview.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sc_ask_layout:
			checkStatus = 0;
			setCheck(checkStatus);
			break;

		case R.id.sc_dongtai_layout:
			checkStatus = 1;
			setCheck(checkStatus);
			break;
			
		case R.id.empty_content_layout:
			if (checkStatus == 0) {
				requestAskList("1", true);
			} else if (checkStatus == 1) {
			}
			break;
		default:
			break;
		}
		
	}
}
