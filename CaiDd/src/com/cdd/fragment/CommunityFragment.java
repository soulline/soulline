package com.cdd.fragment;

import java.util.ArrayList;

import com.cdd.R;
import com.cdd.activity.login.LoginActivity;
import com.cdd.activity.sqpage.AccountingHotAdapter;
import com.cdd.activity.sqpage.ExamListAdapter;
import com.cdd.activity.sqpage.SqAskDetailActivity;
import com.cdd.activity.sqpage.SqForumAdapter;
import com.cdd.activity.sqpage.SqListActivity;
import com.cdd.activity.sqpage.SqForumAdapter.onZanListener;
import com.cdd.app.CddApp;
import com.cdd.base.BaseActivity;
import com.cdd.base.MainActivity;
import com.cdd.mode.ForumEntry;
import com.cdd.mode.ForumItem;
import com.cdd.mode.MemberInfoEntry;
import com.cdd.mode.SqAskItem;
import com.cdd.mode.SqAskListRequest;
import com.cdd.mode.SubForumItem;
import com.cdd.net.RequestListener;
import com.cdd.operater.AskZanOp;
import com.cdd.operater.ExamItemOperater;
import com.cdd.operater.ForumItemOperater;
import com.cdd.operater.GetMemberInfoOp;
import com.cdd.operater.SignTodayOp;
import com.cdd.operater.SqHotAskOp;
import com.cdd.util.CddRequestCode;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CommunityFragment extends Fragment implements OnClickListener {

	private Context context;

	private View view;

	private ImageView sqHotThreadImg, dingdangSqImg;
	private TextView sqHotThreadTx, dingdangSqTx;

	private ListView accountingListview, examListview;

	private PullToRefreshListView sqListview;

	private TextView nicknameTx;

	private int checkCode = 0;

	private AccountingHotAdapter accountingAdapter;

	private ExamListAdapter examAdapter;

	private SqForumAdapter sqAdapter;

	private View footMoreView;

	private int pageNum = 0;

	private int requestPage = 1;

	private ArrayList<SqAskItem> askList = new ArrayList<SqAskItem>();

	public CommunityFragment() {
		super();
	}

	public CommunityFragment(Context context) {
		this.context = context;
	}

	private void loadMemberInfo() {
		final GetMemberInfoOp memberOp = new GetMemberInfoOp(getActivity());
		memberOp.onRequest(new RequestListener() {

			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCallBack(Object data) {
				final MemberInfoEntry memberInfo = memberOp.getMemberInfo();
				if (getActivity() instanceof BaseActivity) {
					((BaseActivity) getActivity()).handler.post(new Runnable() {

						@Override
						public void run() {
							nicknameTx.setText(memberInfo.name);
						}
					});
				}

			}
		});
	}

	private void loadMore() {
		sqListview.getRefreshableView().removeFooterView(footMoreView);
		String pageN = "";
		int page = pageNum + 1;
		if (pageNum == 0) {
			pageN = "1";
		} else {
			pageN = page + "";
		}
		requestHotAskList(pageN, true);
	}

	private void initHotSqListView(ArrayList<SqAskItem> list) {
		sqListview.setVisibility(View.VISIBLE);
		view.findViewById(R.id.empty_content_layout).setVisibility(View.GONE);
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
				((BaseActivity) getActivity()).handler.post(new Runnable() {

					@Override
					public void run() {
						int oldzanCount = Integer.valueOf(sqAdapter
								.getItem(position).likeCount);
						oldzanCount++;
						sqAdapter.getItem(position).likeCount = oldzanCount
								+ "";
						sqAdapter.notifyDataSetChanged();
						((BaseActivity) getActivity()).showToast("点赞成功");
					}
				});
			}
		});
	}

	private void requestHotAskList(String pageNumber, final boolean isShowNow) {
		final SqHotAskOp hotAskOp = new SqHotAskOp(getActivity());
		hotAskOp.setParmas(pageNumber);
		hotAskOp.onRequest(new RequestListener() {

			@Override
			public void onError(Object error) {
				((BaseActivity) getActivity()).handler.post(new Runnable() {

					@Override
					public void run() {
						if (sqAdapter == null
								|| (sqAdapter != null && sqAdapter.getCount() == 0)) {
							sqListview.setVisibility(View.GONE);
							view.findViewById(R.id.empty_content_layout)
									.setVisibility(View.VISIBLE);
						}
						sqListview.onRefreshComplete();
					}
				});
			}

			@Override
			public void onCallBack(Object data) {
				if (hotAskOp.getHotAskList().size() > 0) {
					askList.addAll(hotAskOp.getHotAskList());
					if (isShowNow) {
						pageNum++;
						((BaseActivity) getActivity()).handler
								.post(new Runnable() {

									@Override
									public void run() {
										sqListview.onRefreshComplete();
										initHotSqListView(askList);
									}
								});
					} else if (requestPage < pageNum) {
						requestPage++;
						String pageN = requestPage + "";
						requestHotAskList(pageN, false);
					} else if ((requestPage == pageNum) || (pageNum == 0)) {
						((BaseActivity) getActivity()).handler
								.post(new Runnable() {

									@Override
									public void run() {
										sqListview.onRefreshComplete();
										initHotSqListView(askList);
									}
								});
					}
				} else {
					if (sqAdapter == null
							|| (sqAdapter != null && sqAdapter.getCount() == 0)) {
						((BaseActivity) getActivity()).handler
								.post(new Runnable() {

									@Override
									public void run() {
										sqListview.setVisibility(View.GONE);
										view.findViewById(
												R.id.empty_content_layout)
												.setVisibility(View.VISIBLE);
									}
								});
					}
					pageNum = 1;
					((BaseActivity) getActivity()).handler.post(new Runnable() {

						@Override
						public void run() {
							sqListview.onRefreshComplete();
						}
					});
				}
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.community_fragment, null);
		initView();
		initContent();
		return view;
	}

	public void initContent() {
		setCheck(checkCode);
		loadMemberInfo();
	}

	private void doSignToday() {
		SignTodayOp signOp = new SignTodayOp(getActivity());
		signOp.onRequest(new RequestListener() {

			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCallBack(Object data) {
				((MainActivity) getActivity()).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						((MainActivity) getActivity())
								.showSignSuccessFragment();
					}
				});
			}
		});
	}

	private void gotoDetail(ForumItem item) {
		Intent detail = new Intent(getActivity(), SqListActivity.class);
		detail.putExtra("forum_item", item);
		startActivity(detail);
	}

	private void initView() {
		footMoreView = View.inflate(context, R.layout.load_more_view, null);
		footMoreView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadMore();
			}
		});
		view.findViewById(R.id.empty_content_layout).setOnClickListener(this);
		view.findViewById(R.id.accounting_hot_thread_layout)
				.setOnClickListener(this);
		view.findViewById(R.id.dingdang_sq_layout).setOnClickListener(this);
		view.findViewById(R.id.checkout_login_tx).setOnClickListener(this);
		view.findViewById(R.id.unlogin_tx).setOnClickListener(this);
		view.findViewById(R.id.sign_today_layout).setOnClickListener(this);
		nicknameTx = (TextView) view.findViewById(R.id.nickname_tx);
		accountingListview = (ListView) view
				.findViewById(R.id.accounting_listview);
		sqListview = (PullToRefreshListView) view
				.findViewById(R.id.sq_listview);
		examListview = (ListView) view.findViewById(R.id.exam_listview);
		sqHotThreadImg = (ImageView) view.findViewById(R.id.sq_hot_thread_img);
		dingdangSqImg = (ImageView) view.findViewById(R.id.dingdang_sq_img);
		sqHotThreadTx = (TextView) view.findViewById(R.id.sq_hot_thread_tx);
		dingdangSqTx = (TextView) view.findViewById(R.id.dingdang_sq_tx);
		accountingListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (accountingAdapter != null
						&& accountingAdapter.getItem(position).subItems.size() > 0) {
					initSubList(accountingAdapter.getItem(position).subItems);
				} else {
					ForumItem forum = accountingAdapter.getItem(position);
					gotoDetail(forum);
				}
			}
		});
		examListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SubForumItem forum = examAdapter.getItem(position);
				ForumItem item = new ForumItem();
				item.fatherId = forum.fatherId;
				item.id = forum.id;
				item.name = forum.name;
				gotoDetail(item);
			}
		});
		sqListview.setOnItemClickListener(new OnItemClickListener() {

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
		sqListview.getLoadingLayoutProxy(true, true).setReleaseLabel("释放刷新...");
		sqListview.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshView.isHeaderShown()) {
					sqListview.getRefreshableView().removeFooterView(
							footMoreView);
					requestPage = 1;
					String pangeN = "";
					if (pageNum == 0) {
						pangeN = 1 + "";
					} else {
						pangeN = requestPage + "";
					}
					askList.clear();
					requestHotAskList(pangeN, false);
				}
			}
		});
		showTitleLayout();
	}

	public void showTitleLayout() {
		if (((BaseActivity) getActivity()).app.isLogin()) {
			view.findViewById(R.id.already_login_layout).setVisibility(
					View.VISIBLE);
			view.findViewById(R.id.unlogin_tx).setVisibility(View.GONE);
			view.findViewById(R.id.checkout_login_tx).setVisibility(
					View.VISIBLE);
		} else {
			view.findViewById(R.id.already_login_layout).setVisibility(
					View.GONE);
			view.findViewById(R.id.unlogin_tx).setVisibility(View.VISIBLE);
			view.findViewById(R.id.checkout_login_tx).setVisibility(View.GONE);
		}
	}

	private void getForumList() {
		final ForumItemOperater forumOp = new ForumItemOperater(getActivity());
		forumOp.onRequest(new RequestListener() {

			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCallBack(Object data) {
				final ArrayList<ForumItem> itemList = forumOp.getForumList();
				((BaseActivity) getActivity()).handler.post(new Runnable() {

					@Override
					public void run() {
						initAccountingList(itemList);
					}
				});
			}
		});
	}

	private void setCheck(int check) {
		switch (check) {
		case 0:
			sqHotThreadImg
					.setBackgroundResource(R.drawable.accounting_hot_thread_check);
			view.findViewById(R.id.accounting_hot_thread_layout)
					.setBackgroundColor(
							getActivity().getResources().getColor(
									R.color.sq_blue));
			sqHotThreadTx.setTextColor(getActivity().getResources().getColor(
					R.color.white));
			dingdangSqImg.setBackgroundResource(R.drawable.dingdang_sq);
			view.findViewById(R.id.dingdang_sq_layout).setBackgroundColor(
					getActivity().getResources().getColor(R.color.white));
			dingdangSqTx.setTextColor(getActivity().getResources().getColor(
					R.color.sq_blue));
			getForumList();
			break;
		case 1:
			sqHotThreadImg
					.setBackgroundResource(R.drawable.accounting_hot_thread);
			view.findViewById(R.id.accounting_hot_thread_layout)
					.setBackgroundColor(
							getActivity().getResources()
									.getColor(R.color.white));
			sqHotThreadTx.setTextColor(getActivity().getResources().getColor(
					R.color.sq_blue));
			dingdangSqImg.setBackgroundResource(R.drawable.dingdang_sq_check);
			view.findViewById(R.id.dingdang_sq_layout).setBackgroundColor(
					getActivity().getResources().getColor(R.color.sq_blue));
			dingdangSqTx.setTextColor(getActivity().getResources().getColor(
					R.color.white));
			initSqList();
			break;

		default:
			break;
		}
	}

	private void initAccountingList(ArrayList<ForumItem> list) {
		accountingListview.setVisibility(View.VISIBLE);
		sqListview.setVisibility(View.GONE);
		examListview.setVisibility(View.GONE);
		if (list.size() == 0 && accountingAdapter != null) {
			accountingAdapter.clear();
			accountingAdapter.notifyDataSetChanged();
			return;
		}
		if (accountingAdapter == null) {
			accountingAdapter = new AccountingHotAdapter(CddApp.getInstance());
			accountingAdapter.addData(list);
			accountingListview.setAdapter(accountingAdapter);
		} else {
			accountingAdapter.clear();
			accountingAdapter.addData(list);
			accountingAdapter.notifyDataSetChanged();
		}
	}

	private void initSubList(ArrayList<SubForumItem> list) {
		accountingListview.setVisibility(View.GONE);
		sqListview.setVisibility(View.GONE);
		examListview.setVisibility(View.VISIBLE);
		if (list.size() == 0 && examAdapter != null) {
			examAdapter.clear();
			examAdapter.notifyDataSetChanged();
			return;
		}
		if (examAdapter == null) {
			examAdapter = new ExamListAdapter(CddApp.getInstance());
			examAdapter.addData(list);
			examListview.setAdapter(examAdapter);
		} else {
			examAdapter.clear();
			examAdapter.addData(list);
			examAdapter.notifyDataSetChanged();
		}
	}

	private void initSqList() {
		accountingListview.setVisibility(View.GONE);
		sqListview.setVisibility(View.VISIBLE);
		examListview.setVisibility(View.GONE);
		requestHotAskList("1", true);
	}

	private void showTipNoteDialog(final String msg) {
		((BaseActivity) getActivity()).handler.post(new Runnable() {

			@Override
			public void run() {
				Builder b = new Builder(getActivity());
				b.setMessage(msg);
				b.setNegativeButton(getString(R.string.action_cancel),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				b.setPositiveButton(R.string.action_sure,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int arg1) {
								CddApp.getInstance().clearLogin();
								Intent login = new Intent(getActivity(),
										LoginActivity.class);
								((MainActivity) getActivity())
										.startActivityForResult(login,
												CddRequestCode.LOGIN_REQUEST);
								dialog.dismiss();
							}
						});
				b.show();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.accounting_hot_thread_layout:
			checkCode = 0;
			setCheck(0);
			break;
		case R.id.dingdang_sq_layout:
			askList.clear();
			pageNum = 0;
			checkCode = 1;
			setCheck(1);
			break;

		case R.id.checkout_login_tx:
			showTipNoteDialog("确定要退出当前用户切换吗？");
			break;

		case R.id.unlogin_tx:
			Intent login = new Intent(getActivity(), LoginActivity.class);
			((MainActivity) getActivity()).startActivityForResult(login,
					CddRequestCode.LOGIN_REQUEST);
			break;

		case R.id.sign_today_layout:
			doSignToday();
			break;

		case R.id.empty_content_layout:
			requestHotAskList("1", true);
			break;

		default:
			break;
		}

	}

}
