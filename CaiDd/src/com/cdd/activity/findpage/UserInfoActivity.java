package com.cdd.activity.findpage;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.cdd.R;
import com.cdd.activity.findpage.DynamicAdapter.OnAnswerMemberClickLister;
import com.cdd.activity.findpage.DynamicAdapter.OnImageClickListener;
import com.cdd.activity.findpage.DynamicAdapter.OnPackListener;
import com.cdd.activity.findpage.DynamicAdapter.OnWorkListener;
import com.cdd.activity.image.ImageNetPageActivity;
import com.cdd.activity.minepage.FansOrListenActivity;
import com.cdd.activity.sqpage.SqAskDetailActivity;
import com.cdd.activity.sqpage.SqForumAdapter;
import com.cdd.activity.sqpage.SqForumAdapter.onZanListener;
import com.cdd.base.BaseActivity;
import com.cdd.fragment.BaseFragmentListener;
import com.cdd.fragment.BottomReplyFragment;
import com.cdd.mode.DynamicEntry;
import com.cdd.mode.DynamicReplay;
import com.cdd.mode.MemberInfoEntry;
import com.cdd.mode.PhotosEntry;
import com.cdd.mode.SqAskItem;
import com.cdd.net.RequestListener;
import com.cdd.operater.AskZanOp;
import com.cdd.operater.AttentionOp;
import com.cdd.operater.DingdangDynamicListOp;
import com.cdd.operater.MemberNewsListOp;
import com.cdd.operater.MemberSubjectListOp;
import com.cdd.operater.NewsShareOp;
import com.cdd.operater.NewsShoucangOp;
import com.cdd.operater.NewsZanOp;
import com.cdd.operater.OtherMemberInfoOp;
import com.cdd.operater.SqMyShoucangListOp;
import com.cdd.operater.UnAttentionOp;
import com.cdd.util.ImageOperater;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

public class UserInfoActivity extends BaseActivity implements OnClickListener {

	private String memberId = "";

	private MemberInfoEntry memberInfo = new MemberInfoEntry();

	private ImageView userPortrait;

	private TextView userName, userLevel, userCoin, listenCount, fansCount;

	private TextView nickName, sexType, localCity, simpleTx, levelValue,
			dingdangbiValue;

	private Button listenTo;

	private boolean isListen = false;

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
		setContentView(R.layout.user_info_activity);
		initTitle("个人详情");
		initView();
		initContent();
	}

	private void initContent() {
		memberId = getIntent().getStringExtra("memberId");
		setChecked(0);
		if (memberId.equals(app.getAccount().id)) {
			listenTo.setVisibility(View.GONE);
			findViewById(R.id.message_to_tv).setVisibility(View.GONE);
		} else {
			listenTo.setVisibility(View.VISIBLE);
			findViewById(R.id.message_to_tv).setVisibility(View.VISIBLE);
		}
	}

	private void requestMemberInfo() {
		final OtherMemberInfoOp otherOp = new OtherMemberInfoOp(context);
		otherOp.setParams(memberId);
		otherOp.onRequest(new RequestListener() {

			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCallBack(Object data) {
				memberInfo = otherOp.getMemberInfo();
				handler.post(new Runnable() {

					@Override
					public void run() {
						loadMemberInfo(memberInfo);
					}
				});
			}
		});
	}

	private void initSex(String sex) {
		if (sex.equals("1")) {
			sexType.setText("男");
		} else if (sex.equals("2")) {
			sexType.setText("女");
		}
	}

	private void attentionRequest(String idolId) {
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
				isListen = true;
				handler.post(new Runnable() {

					@Override
					public void run() {
						listenTo.setText("取消关注");
					}
				});
			}
		});
	}

	private void unAttentionRequest(String idolId) {
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
				isListen = false;
				handler.post(new Runnable() {

					@Override
					public void run() {
						listenTo.setText("关注");
					}
				});
			}
		});
	}

	private void loadMemberInfo(MemberInfoEntry member) {
		if (member.sex.equals("1")) {
			userPortrait.setImageResource(R.drawable.default_man_portrait);
		} else if (member.sex.equals("2")) {
			userPortrait.setImageResource(R.drawable.default_woman_portrait);
		} else {
			userPortrait.setImageResource(R.drawable.default_woman_portrait);
		}
		if (!TextUtils.isEmpty(member.photo) && !member.photo.equals("null")) {
			String ulr = member.photo;
			ImageOperater.getInstance(context).onLoadImage(ulr, userPortrait);
		}
		if (!TextUtils.isEmpty(member.name) && !member.name.equals("null")) {
			nickName.setText(member.name);
			userName.setText(member.name);
		}
		if (member.relation.equals("1") || member.relation.equals("2")) {
			listenTo.setText("取消关注");
			isListen = true;
		} else if (member.relation.equals("0") || member.relation.equals("3")) {
			listenTo.setText("关注");
			isListen = false;
		}
		sexType.setText("女");
		initSex(member.sex);
		if (!TextUtils.isEmpty(member.cityName)
				&& !member.cityName.equals("null")) {
			localCity.setText(member.cityName);
		}
		if (!TextUtils.isEmpty(member.description)
				&& !member.description.equals("null")) {
			simpleTx.setText(member.description);
		}
		if (!TextUtils.isEmpty(member.levelName)
				&& !member.levelName.equals("null")) {
			levelValue.setText(member.levelName);
			userLevel.setText(member.levelName);
		}
		if (!TextUtils.isEmpty(member.availableScore)
				&& !member.availableScore.equals("null")) {
			dingdangbiValue.setText(member.availableScore);
			userCoin.setText(member.availableScore);
		} else {
			dingdangbiValue.setText("0");
			userCoin.setText("0");
		}
		listenCount.setText(member.idolCount);
		fansCount.setText(member.fansCount);
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
		findViewById(R.id.listen_content_layout).setOnClickListener(this);
		findViewById(R.id.fans_content_layout).setOnClickListener(this);
		findViewById(R.id.ask_tv).setOnClickListener(this);
		findViewById(R.id.dynamic_tv).setOnClickListener(this);
		findViewById(R.id.detail_info_tv).setOnClickListener(this);
		findViewById(R.id.listen_to).setOnClickListener(this);
		findViewById(R.id.message_to_tv).setOnClickListener(this);
		listenTo = (Button) findViewById(R.id.listen_to);
		userPortrait = (ImageView) findViewById(R.id.user_portrait);
		userName = (TextView) findViewById(R.id.user_name);
		userLevel = (TextView) findViewById(R.id.user_level);
		userCoin = (TextView) findViewById(R.id.user_coin);
		listenCount = (TextView) findViewById(R.id.listen_count);
		fansCount = (TextView) findViewById(R.id.fans_count);
		nickName = (TextView) findViewById(R.id.nick_name);
		sexType = (TextView) findViewById(R.id.sex_type);
		localCity = (TextView) findViewById(R.id.local_city);
		simpleTx = (TextView) findViewById(R.id.simple_tx);
		levelValue = (TextView) findViewById(R.id.level_value);
		dingdangbiValue = (TextView) findViewById(R.id.dingdangbi_value);
		sqListview = (PullToRefreshListView) findViewById(R.id.sq_listview);
		initAskRefreshListView();
		dongtaiListview = (PullToRefreshListView) findViewById(R.id.dongtai_listview);
		initDynamicListView();
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
	
	private void requestDynamicList(String pageNumber, final boolean isShowNow) {
		final MemberNewsListOp dingdangOp = new MemberNewsListOp(
				context);
		dingdangOp.setParams(memberId, pageNumber);
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
	
	private void requestAskList(String pageNumber,
			final boolean isShowNow) {
		final MemberSubjectListOp askOp = new MemberSubjectListOp(context);
		askOp.setParams(memberId, pageNumber);
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

	private void setChecked(int type) {
		checkStatus = type;
		pageNum = 0;
		requestPage = 1;
		switch (type) {
		case 0:
			findViewById(R.id.ask_bottom).setVisibility(View.VISIBLE);
			findViewById(R.id.dynamic_bottom).setVisibility(View.GONE);
			findViewById(R.id.detail_info_bottom).setVisibility(View.GONE);
			findViewById(R.id.detail_info_layout).setVisibility(View.GONE);
			findViewById(R.id.listview_layout).setVisibility(View.VISIBLE);
			requestAskList("1", true);
			dynamicList.clear();
			sqListview.setVisibility(View.VISIBLE);
			dongtaiListview.setVisibility(View.GONE);
			break;

		case 1:
			findViewById(R.id.ask_bottom).setVisibility(View.GONE);
			findViewById(R.id.dynamic_bottom).setVisibility(View.VISIBLE);
			findViewById(R.id.detail_info_bottom).setVisibility(View.GONE);
			findViewById(R.id.detail_info_layout).setVisibility(View.GONE);
			findViewById(R.id.listview_layout).setVisibility(View.VISIBLE);
			requestDynamicList("1", true);
			askList.clear();
			sqListview.setVisibility(View.GONE);
			dongtaiListview.setVisibility(View.VISIBLE);
			break;

		case 2:
			findViewById(R.id.ask_bottom).setVisibility(View.GONE);
			findViewById(R.id.dynamic_bottom).setVisibility(View.GONE);
			findViewById(R.id.detail_info_bottom).setVisibility(View.VISIBLE);
			findViewById(R.id.detail_info_layout).setVisibility(View.VISIBLE);
			findViewById(R.id.listview_layout).setVisibility(View.GONE);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		requestMemberInfo();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ask_tv:
			setChecked(0);
			break;

		case R.id.dynamic_tv:
			setChecked(1);
			break;

		case R.id.detail_info_tv:
			setChecked(2);
			break;

		case R.id.listen_to:
			if (isListen) {
				unAttentionRequest(memberId);
			} else {
				attentionRequest(memberId);
			}
			break;
			
		case R.id.listen_content_layout:
			Intent guanzhu = new Intent(context, UserListenFansListActivity.class);
			guanzhu.putExtra("fans_type", "2");
			guanzhu.putExtra("memberId", memberId);
			startActivity(guanzhu);
			break;
		case R.id.fans_content_layout:
			Intent fans = new Intent(context, UserListenFansListActivity.class);
			fans.putExtra("fans_type", "1");
			fans.putExtra("memberId", memberId);
			startActivity(fans);
			break;

		case R.id.message_to_tv:
			Bundle b = new Bundle();
			b.putString("cofId", memberId);
			b.putInt("reply_type", 3);
			displayFragment(true, "bottom_reply", b,
					new BaseFragmentListener() {

						@Override
						public void onCallBack(Object object) {
							
						}
					});
			break;
		default:
			break;
		}

	}

}
