package com.cdd.fragment;

import java.util.ArrayList;

import com.cdd.R;
import com.cdd.base.BaseActivity;
import com.cdd.base.MainActivity;
import com.cdd.login.LoginActivity;
import com.cdd.mode.ForumEntry;
import com.cdd.mode.ForumItem;
import com.cdd.net.RequestListener;
import com.cdd.operater.ExamItemOperater;
import com.cdd.operater.ForumItemOperater;
import com.cdd.operater.SignTodayOp;
import com.cdd.sqpage.AccountingHotAdapter;
import com.cdd.sqpage.ExamListAdapter;
import com.cdd.sqpage.SqForumAdapter;
import com.cdd.sqpage.SqListActivity;
import com.cdd.util.CddRequestCode;

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

	private ListView accountingListview, sqListview, examListview;

	private TextView nicknameTx;

	private int checkCode = 0;

	private AccountingHotAdapter accountingAdapter;

	private ExamListAdapter examAdapter;

	private SqForumAdapter sqAdapter;
	
	public CommunityFragment() {
		super();
	}
	
	public CommunityFragment(Context context) {
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.community_fragment, null);
		initView();
		initContent();
		return view;
	}

	private void initContent() {
		setCheck(0);
		if (!TextUtils.isEmpty(((BaseActivity) getActivity()).app.getAccount().name)) {
			nicknameTx.setText(((BaseActivity) getActivity()).app.getAccount().name);
		}
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
						((MainActivity) getActivity()).showSignSuccessFragment();
					}
				});
			}
		});
	}
	
	private void gotoDetail() {
		Intent detail = new Intent(getActivity(), SqListActivity.class);
		startActivity(detail);
	}

	private void initView() {
		view.findViewById(R.id.accounting_hot_thread_layout)
				.setOnClickListener(this);
		view.findViewById(R.id.dingdang_sq_layout).setOnClickListener(this);
		view.findViewById(R.id.checkout_login_tx).setOnClickListener(this);
		view.findViewById(R.id.unlogin_tx).setOnClickListener(this);
		view.findViewById(R.id.sign_today_layout).setOnClickListener(this);
		nicknameTx = (TextView) view.findViewById(R.id.nickname_tx);
		accountingListview = (ListView) view
				.findViewById(R.id.accounting_listview);
		sqListview = (ListView) view.findViewById(R.id.sq_listview);
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
						&& accountingAdapter.getItem(position).id.equals("9")) {
					getExamList();
				} else {
					gotoDetail();
				}
			}
		});
		examListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				gotoDetail();
			}
		});
		sqListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				

			}
		});
		showTitleLayout();
	}

	private void showTitleLayout() {
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

	private void getExamList() {
		final ExamItemOperater examOp = new ExamItemOperater(getActivity());
		examOp.onRequest(new RequestListener() {

			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCallBack(Object data) {
				final ArrayList<ForumItem> itemList = examOp.getForumList();
				((BaseActivity) getActivity()).handler.post(new Runnable() {

					@Override
					public void run() {
						initExamList(itemList);
					}
				});
			}
		});
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
							getActivity().getResources().getColor(R.color.sq_blue));
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
							getActivity().getResources().getColor(R.color.white));
			sqHotThreadTx.setTextColor(getActivity().getResources().getColor(
					R.color.sq_blue));
			dingdangSqImg.setBackgroundResource(R.drawable.dingdang_sq_check);
			view.findViewById(R.id.dingdang_sq_layout).setBackgroundColor(
					getActivity().getResources().getColor(R.color.sq_blue));
			dingdangSqTx.setTextColor(getActivity().getResources().getColor(
					R.color.white));
			showSqListView();
			break;

		default:
			break;
		}
	}

	private void showSqListView() {
		ArrayList<ForumEntry> list = new ArrayList<ForumEntry>();
		ForumEntry entry1 = new ForumEntry();
		entry1.forumTitle = "财会考试是几号啊？";
		entry1.zanCount = "126";
		entry1.answerContent = "纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁";
		list.add(entry1);
		ForumEntry entry2 = new ForumEntry();
		entry2.forumTitle = "财会考试是几号啊？";
		entry2.zanCount = "126";
		entry2.answerContent = "纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁";
		list.add(entry2);
		ForumEntry entry3 = new ForumEntry();
		entry3.forumTitle = "财会考试是几号啊？";
		entry3.zanCount = "126";
		entry3.answerContent = "纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁";
		list.add(entry3);
		ForumEntry entry4 = new ForumEntry();
		entry4.forumTitle = "财会考试是几号啊？";
		entry4.zanCount = "126";
		entry4.answerContent = "纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁";
		list.add(entry4);
		initSqList(list);
	}

	private void initAccountingList(ArrayList<ForumItem> list) {
		accountingListview.setVisibility(View.VISIBLE);
		sqListview.setVisibility(View.GONE);
		examListview.setVisibility(View.GONE);
		if (list.size() == 0) {
			accountingAdapter.clear();
			accountingAdapter.notifyDataSetChanged();
			return;
		}
		if (accountingAdapter == null) {
			accountingAdapter = new AccountingHotAdapter(getActivity());
			accountingAdapter.addData(list);
			accountingListview.setAdapter(accountingAdapter);
		} else {
			accountingAdapter.clear();
			accountingAdapter.addData(list);
			accountingAdapter.notifyDataSetChanged();
		}
	}

	private void initExamList(ArrayList<ForumItem> list) {
		accountingListview.setVisibility(View.GONE);
		sqListview.setVisibility(View.GONE);
		examListview.setVisibility(View.VISIBLE);
		if (list.size() == 0) {
			examAdapter.clear();
			examAdapter.notifyDataSetChanged();
			return;
		}
		if (examAdapter == null) {
			examAdapter = new ExamListAdapter(getActivity());
			examAdapter.addData(list);
			examListview.setAdapter(examAdapter);
		} else {
			examAdapter.clear();
			examAdapter.addData(list);
			examAdapter.notifyDataSetChanged();
		}
	}

	private void initSqList(ArrayList<ForumEntry> list) {
		accountingListview.setVisibility(View.GONE);
		sqListview.setVisibility(View.VISIBLE);
		examListview.setVisibility(View.GONE);
		if (sqAdapter == null) {
			sqAdapter = new SqForumAdapter(getActivity());
			sqAdapter.addData(list);
			sqListview.setAdapter(sqAdapter);
		} else {
			sqAdapter.clear();
			sqAdapter.addData(list);
			sqAdapter.notifyDataSetChanged();
		}
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
								((BaseActivity) getActivity()).app.clearLogin();
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

		default:
			break;
		}

	}

}
