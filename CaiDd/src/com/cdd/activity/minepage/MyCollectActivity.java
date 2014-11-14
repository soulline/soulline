package com.cdd.activity.minepage;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.cdd.R;
import com.cdd.activity.findpage.DynamicAdapter;
import com.cdd.activity.findpage.ForwardDetailActivity;
import com.cdd.activity.findpage.NewsDetailActivity;
import com.cdd.activity.findpage.DynamicAdapter.OnAnswerMemberClickLister;
import com.cdd.activity.findpage.DynamicAdapter.OnImageClickListener;
import com.cdd.activity.findpage.DynamicAdapter.OnPackListener;
import com.cdd.activity.findpage.DynamicAdapter.OnWorkListener;
import com.cdd.activity.image.ImageNetPageActivity;
import com.cdd.activity.sqpage.SqAskDetailActivity;
import com.cdd.activity.sqpage.SqForumAdapter;
import com.cdd.activity.sqpage.SqForumAdapter.onZanListener;
import com.cdd.base.BaseActivity;
import com.cdd.fragment.BaseFragmentListener;
import com.cdd.fragment.BottomReplyFragment;
import com.cdd.mode.DynamicEntry;
import com.cdd.mode.DynamicReplay;
import com.cdd.mode.PhotosEntry;
import com.cdd.mode.SqAskItem;
import com.cdd.net.RequestListener;
import com.cdd.operater.AskZanOp;
import com.cdd.operater.DingdangDynamicListOp;
import com.cdd.operater.MyQuestionListOp;
import com.cdd.operater.NewsShareOp;
import com.cdd.operater.NewsShoucangOp;
import com.cdd.operater.NewsZanOp;
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
	
	private DynamicAdapter dynamicAdapter;

	private ArrayList<SqAskItem> askList = new ArrayList<SqAskItem>();
	
	private ArrayList<DynamicEntry> dynamicList = new ArrayList<DynamicEntry>();
	
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
			requestDynamicList("1", true);
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
		initDynamicListView();
	}
	
	private void initContent() {
		setCheck(checkStatus);
	}
	
	private void setCheck(int check) {
		pageNum = 0;
		requestPage = 1;
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
			requestDynamicList("1", true);
			break;

		default:
			break;
		}
	}
	
	private void doRefreshListView() {
		dongtaiListview.getRefreshableView().removeFooterView(
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
	
	private void initDynamicList(ArrayList<DynamicEntry> list) {
		dongtaiListview.setVisibility(View.VISIBLE);
		findViewById(R.id.empty_content_layout).setVisibility(View.GONE);
		if (dynamicAdapter == null) {
			dynamicAdapter = new DynamicAdapter(context);
			dynamicAdapter.addData(list);
			dongtaiListview.getRefreshableView().setAdapter(dynamicAdapter);
			addAdapterListener(dynamicAdapter);
		} else {
			dynamicAdapter.clear();
			dynamicAdapter.addData(list);
			dynamicAdapter.notifyDataSetChanged();
		}
		if ((dynamicAdapter.getCount() % 20) == 0) {
			dongtaiListview.getRefreshableView().addFooterView(footMoreView);
		}
	}
	
	private void gotoPhotoDetail(ArrayList<PhotosEntry> list, int index) {
		Intent intent = new Intent(context, ImageNetPageActivity.class);
		intent.putExtra("image_urls", list);
		intent.putExtra("image_index", index);
		startActivity(intent);
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
						String count = dynamicAdapter.getItem(position).likeCount;
						int countN = Integer.valueOf(count);
						countN++;
						dynamicAdapter.getItem(position).likeCount = countN + "";
						dynamicAdapter.notifyDataSetChanged();
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
						String count = dynamicAdapter.getItem(position).favoriteCount;
						int countN = Integer.valueOf(count);
						countN++;
						dynamicAdapter.getItem(position).favoriteCount = countN + "";
						dynamicAdapter.notifyDataSetChanged();
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
						String count = dynamicAdapter.getItem(position).shareCount;
						int countN = Integer.valueOf(count);
						countN++;
						dynamicAdapter.getItem(position).shareCount = countN + "";
						dynamicAdapter.notifyDataSetChanged();
					}
				});
			}
		});
	}
	
	private void addAdapterListener(DynamicAdapter adapterN) {
		adapterN.addOnAnswerMemberClickLister(new OnAnswerMemberClickLister() {

			@Override
			public void onAnswerClick(DynamicReplay replay, int position) {
				// TODO Auto-generated method stub
			}
		});

		adapterN.addOnImageClickListener(new OnImageClickListener() {

			@Override
			public void onImageClick(int position, int index) {
				if (dynamicAdapter != null) {
					gotoPhotoDetail(dynamicAdapter.getItem(position).photos, index);
				}
			}
		});

		adapterN.addOnPackListener(new OnPackListener() {

			@Override
			public void onPack(int position, boolean pack) {
				if (dynamicAdapter != null && !pack) {
					dynamicAdapter.getItem(position).isPack = false;
					dynamicAdapter.notifyDataSetChanged();
				} else if (dynamicAdapter != null && pack) {
					dynamicAdapter.getItem(position).isPack = true;
					dynamicAdapter.notifyDataSetChanged();
				}
			}
		});

		adapterN.addOnWorkListener(new OnWorkListener() {

			@Override
			public void onWork(int position, int type) {
				closeFragment("bottom_reply");
				if (type == 3) {
					Bundle b = new Bundle();
					b.putString("cofId", dynamicAdapter.getItem(position).id);
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
					onNewsZanRequest(dynamicAdapter.getItem(position).id, position);
				} else if (type == 1) {
					onNewsShoucangRequest(dynamicAdapter.getItem(position).id, position);
				} else if (type == 2) {
					onNewsShareRequest(dynamicAdapter.getItem(position).id, position);
				} else if (type == 4) {
					Intent forward = new Intent(context, ForwardDetailActivity.class);
					forward.putExtra("cofId", dynamicAdapter.getItem(position).forward.id);
					startActivity(forward);
				}
			}
		});
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
	
	private void requestDynamicList(String pageNumber, final boolean isShowNow) {
		final DingdangDynamicListOp dingdangOp = new DingdangDynamicListOp(
				context);
		dingdangOp.setParams("1");
		dingdangOp.onRequest(new RequestListener() {

			@Override
			public void onError(Object error) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						if (dynamicAdapter == null
								|| (dynamicAdapter != null && dynamicAdapter.getCount() == 0)) {
							dongtaiListview.setVisibility(View.GONE);
							findViewById(R.id.empty_content_layout)
									.setVisibility(View.VISIBLE);
						}
						dongtaiListview.onRefreshComplete();
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
								dongtaiListview.onRefreshComplete();
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
								dongtaiListview.onRefreshComplete();
								initDynamicList(dynamicList);
							}
						});
					}
				} else {
					if (dynamicAdapter == null
							|| (dynamicAdapter != null && dynamicAdapter.getCount() == 0)) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								dongtaiListview.setVisibility(View.GONE);
								findViewById(R.id.empty_content_layout)
										.setVisibility(View.VISIBLE);
							}
						});
					}
					pageNum = 1;
					handler.post(new Runnable() {

						@Override
						public void run() {
							dongtaiListview.onRefreshComplete();
						}
					});
				}

			}
		});
	}
	
	private void initDynamicListView() {
		dongtaiListview.getRefreshableView().setOnItemClickListener(
				new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent detail = new Intent(context, NewsDetailActivity.class);
						detail.putExtra("cofId", dynamicAdapter.getItem(position - 1).id);
						startActivity(detail);
					}
				});
		dongtaiListview.setMode(Mode.PULL_FROM_START);
		dongtaiListview.getLoadingLayoutProxy(true, true).setPullLabel(
				"下拉刷新...");
		dongtaiListview.getLoadingLayoutProxy(true, true).setRefreshingLabel(
				"正在刷新...");
		dongtaiListview.getLoadingLayoutProxy(true, true).setReleaseLabel(
				"释放刷新...");
		dongtaiListview.setOnRefreshListener(new OnRefreshListener<ListView>() {

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
				requestDynamicList("1", true);
			}
			break;
		default:
			break;
		}
		
	}
}
