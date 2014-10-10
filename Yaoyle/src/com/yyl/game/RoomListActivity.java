package com.yyl.game;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yyl.BaseActivity;
import com.yyl.R;
import com.yyl.account.AccountActivity;
import com.yyl.application.YylApp;
import com.yyl.download.DownLoadFragment;
import com.yyl.fragment.BaseFragmentListener;
import com.yyl.fragment.ChooseDiceFragment;
import com.yyl.fragment.GameResultFragment;
import com.yyl.mode.AccountInfo;
import com.yyl.mode.FirstLoginInfo;
import com.yyl.mode.GamerInfo;
import com.yyl.mode.RoomItemEntry;
import com.yyl.mode.RoomListInfo;
import com.yyl.mode.VersionEntry;
import com.yyl.net.RequestListener;
import com.yyl.operater.CheckVersionOperater;
import com.yyl.operater.LoadAccountOperater;
import com.yyl.operater.RoomListOperater;
import com.yyl.operater.SearchRoomOperater;
import com.yyl.raffle.RaffleActivity;
import com.yyl.service.YylService;
import com.yyl.utils.BaseActivityCloseListener;
import com.yyl.utils.BroadcastCode;
import com.yyl.utils.GameAction;
import com.yyl.utils.ImageOperater;
import com.yyl.utils.ServiceUtil;
import com.yyl.utils.YylConfig;
import com.yyl.utils.YylRequestCode;

public class RoomListActivity extends BaseActivity implements OnClickListener {

	private RoomListAdapter adapter;

	private ListView gameListView;

	private RoomListInfo roomListInfo = new RoomListInfo();

	private RoomItemEntry roomItem = new RoomItemEntry();

	private int PAGE_NUM = 1;

	private TextView userName;
	private ImageView portraitSelf;

	private EditText searchContent;

	private LocalBroadcastManager lbm = LocalBroadcastManager
			.getInstance(YylApp.getInstance());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_list_activity);
		setFullScreen();
		initView();
		initService();
		registerGameReceiver();
		app.putClosePath(YylConfig.PATH_KEY, new BaseActivityCloseListener() {

			@Override
			public void onFinish() {
				finish();
			}
		});
		FirstLoginInfo firstL = (FirstLoginInfo) getIntent()
				.getSerializableExtra("first_login");
		if (firstL != null && firstL.isFirstLogin.equals("1")) {
			showTipDialog(getString(R.string.first_login_note).replaceAll("%", firstL.firstLoginScore));
		}
		checkUpdate();
	}

	private void checkUpdate() {
		final CheckVersionOperater checkOp = new CheckVersionOperater(context);
		checkOp.setParams("100" + getVerCode());
		checkOp.onRequest(new RequestListener() {

			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCallBack(Object data) {
				VersionEntry version = (VersionEntry) checkOp.getData();
				if (version != null && version.upgrade.equals("1")) {
					showTipNoteDialog(getString(R.string.new_version_note).replaceAll("%", version.ver),
							version.address);
				}

			}
		});

	}

	private void showTipNoteDialog(final String msg, final String address) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				Builder b = new Builder(context);
				b.setMessage(msg);
				b.setNegativeButton(getString(R.string.action_cancel),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				b.setPositiveButton(getString(R.string.action_update),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int arg1) {
								Bundle b = new Bundle();
								if (!TextUtils.isEmpty(address)) {
									b.putString("download_url", address);
								}
								displayFragment(true, "download_frag", b,
										new BaseFragmentListener() {

											@Override
											public void onCallBack(Object object) {

											}
										});
								dialog.dismiss();
							}
						});
				b.show();
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
		if (tag.equals("download_frag")) {
			DownLoadFragment downloadF = new DownLoadFragment(context, b);
			return downloadF;
		}
		return null;
	}

	private int getVerCode() {
		try {
			PackageInfo pi = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			return pi.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}

	private void initService() {
		if (!ServiceUtil.isServiceRunning("com.yyl.service.YylService")) {
			Intent service = new Intent(context, YylService.class);
			startService(service);
		}
	}

	private void createRoom() {
		JSONObject roomObj = new JSONObject();
		try {
			roomObj.put("actionCode", GameAction.GAME_SELF_CREATE);
			roomObj.put("gamerNum", 5);
			roomObj.put("uid", app.getUID());
			roomItem.score = "50";
			roomItem.maxGamerNum = 5;
		} catch (JSONException e) {
			e.printStackTrace();
			showLoading(false);
		}
		if (!JSONObject.NULL.equals(roomObj)) {
			Intent cmdI = new Intent(BroadcastCode.ACTION_SEND_CMD);
			cmdI.putExtra("request_str", roomObj.toString());
			lbm.sendBroadcast(cmdI);
		}
	}

	private JSONObject getJoinGameJSON(RoomItemEntry item) {
		JSONObject joinObj = new JSONObject();
		try {
			joinObj.put("actionCode", GameAction.GAME_SELF_JOIN);
			joinObj.put("gamblingId", item.id);
			joinObj.put("uid", app.getUID());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return joinObj;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		lbm.unregisterReceiver(gameStatusReceiver);
		Intent service = new Intent(context, YylService.class);
		stopService(service);
		app.popClosePath(false, YylConfig.PATH_KEY);
	}

	private void initView() {
		gameListView = (ListView) findViewById(R.id.game_list);
		userName = (TextView) findViewById(R.id.user_name);
		portraitSelf = (ImageView) findViewById(R.id.portrait_self);
		searchContent = (EditText) findViewById(R.id.search_content);
		findViewById(R.id.refresh_btn).setOnClickListener(this);
		findViewById(R.id.user_info_btn).setOnClickListener(this);
		findViewById(R.id.enter_room).setOnClickListener(this);
		findViewById(R.id.quick_start).setOnClickListener(this);
		findViewById(R.id.search_btn).setOnClickListener(this);
		findViewById(R.id.create_room).setOnClickListener(this);
		findViewById(R.id.raffle_go).setOnClickListener(this);
		gameListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				roomItem = adapter.getItem(position);
				if (roomItem.gamerNum < roomItem.maxGamerNum) {
					JSONObject request = getJoinGameJSON(roomItem);
					if (!JSONObject.NULL.equals(request)) {
						Intent cmdI = new Intent(BroadcastCode.ACTION_SEND_CMD);
						cmdI.putExtra("request_str", request.toString());
						lbm.sendBroadcast(cmdI);
					}
				}
			}
		});
	}

	private void loadAccountInfo() {
		final LoadAccountOperater loadAccount = new LoadAccountOperater(context);
		loadAccount.onRequest(new RequestListener() {

			@Override
			public void onError(Object error) {

			}

			@Override
			public void onCallBack(Object data) {
				final AccountInfo account = (AccountInfo) loadAccount.getData();
				handler.post(new Runnable() {

					@Override
					public void run() {
						if (account != null) {
							userName.setText(account.name);
							if (account.sex.equals("1")) {
								portraitSelf
										.setBackgroundResource(R.drawable.head_boy);
							} else if (account.sex.equals("2")) {
								portraitSelf
										.setBackgroundResource(R.drawable.head_girl);
							}
							if (!TextUtils.isEmpty(account.photoUrl)) {
								String url = account.photoUrl + "&" + System.currentTimeMillis();
								ImageOperater.getInstance(context).onLoadImage(
										url, portraitSelf);
							}
						}
					}
				});

			}
		});
	}

	private void requestRoomList() {
		final RoomListOperater roomOperater = new RoomListOperater(context);
		if (PAGE_NUM < roomListInfo.totalPage) {
			PAGE_NUM++;
			roomOperater.setParmas(PAGE_NUM + "");
		} else {
			PAGE_NUM = 0;
			roomOperater.setParmas("1");
		}
		roomOperater.onRequest(new RequestListener() {

			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCallBack(Object data) {
				roomListInfo = (RoomListInfo) roomOperater.getData();
				handler.post(new Runnable() {

					@Override
					public void run() {
						initAdapter(roomListInfo.roomList);
					}
				});
			}
		});
	}

	private void initContent() {
		requestRoomList();
		loadAccountInfo();
	}

	private void initAdapter(ArrayList<RoomItemEntry> list) {
		if (adapter == null) {
			adapter = new RoomListAdapter(context);
			adapter.addAll(list);
			gameListView.setAdapter(adapter);
		} else {
			adapter.clear();
			adapter.addAll(list);
			adapter.notifyDataSetChanged();
		}
	}

	private void registerGameReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BroadcastCode.ACTION_GAME_CREATE);
		filter.addAction(BroadcastCode.ACTION_SELF_JOIN);
		filter.addAction(BroadcastCode.ACTION_REQUST_ERROR);
		lbm.registerReceiver(gameStatusReceiver, filter);
	}

	private BroadcastReceiver gameStatusReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(BroadcastCode.ACTION_GAME_CREATE)) {
				showLoading(false);
				roomItem.boardNo = intent.getStringExtra("boardNo");
				roomItem.id = intent.getStringExtra("gamblingId");
				Intent gameC = new Intent(context, GameActivity.class);
				gameC.putExtra("room_item", roomItem);
				startActivity(gameC);
			} else if (intent.getAction()
					.equals(BroadcastCode.ACTION_SELF_JOIN)) {
				showLoading(false);
				ArrayList<GamerInfo> gamers = (ArrayList<GamerInfo>) intent
						.getSerializableExtra("gamers_info");
				Intent gameJ = new Intent(context, GameActivity.class);
				gameJ.putExtra("room_item", roomItem);
				if (gamers != null && gamers.size() > 0) {
					gameJ.putExtra("gamers_info", gamers);
				}
				startActivity(gameJ);
			} else if (intent.getAction().equals(
					BroadcastCode.ACTION_REQUST_ERROR)) {
				showLoading(false);
				int status = intent.getIntExtra("status", -1);
				if (status == GameAction.ROOM_OUT_USE) {
					requestRoomList();
				}
			}
		}
	};

	private void quickStart() {
		if (roomListInfo.roomList.size() > 0) {
			RoomItemEntry item = null;
			boolean hasRoom = false;
			for (int i = 0; i < roomListInfo.roomList.size(); i++) {
				item = roomListInfo.roomList.get(i);
				if (item.gamerNum < item.maxGamerNum) {
					hasRoom = true;
					break;
				}
			}
			if (hasRoom) {
				roomItem = item;
				JSONObject request = getJoinGameJSON(item);
				if (!JSONObject.NULL.equals(request)) {
					Intent cmdI = new Intent(BroadcastCode.ACTION_SEND_CMD);
					cmdI.putExtra("request_str", request.toString());
					lbm.sendBroadcast(cmdI);
				}
			} else {
				createRoom();
			}
		} else {
			createRoom();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		initContent();
	}

	private void searchRoomByNo(String roomNo) {
		if (TextUtils.isEmpty(roomNo)) {
			showToast(getString(R.string.please_input_search));
			return;
		}
		final SearchRoomOperater searchRoom = new SearchRoomOperater(context);
		searchRoom.setParams(roomNo);
		searchRoom.onRequest(new RequestListener() {

			@Override
			public void onError(Object error) {

			}

			@Override
			public void onCallBack(Object data) {
				roomItem = (RoomItemEntry) searchRoom.getData();
				if (roomItem != null && !roomItem.isStarting) {
					JSONObject request = getJoinGameJSON(roomItem);
					if (!JSONObject.NULL.equals(request)) {
						Intent cmdI = new Intent(BroadcastCode.ACTION_SEND_CMD);
						cmdI.putExtra("request_str", request.toString());
						lbm.sendBroadcast(cmdI);
					}
				} else if (roomItem != null && roomItem.isStarting) {
					showToast(getString(R.string.room_gaming_not_join));
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.refresh_btn:
			requestRoomList();
			break;
		case R.id.user_info_btn:
			Intent userInfo = new Intent(context, AccountActivity.class);
			startActivity(userInfo);
			break;
		case R.id.enter_room:
			searchRoomByNo(searchContent.getText().toString().trim());
			break;
		case R.id.quick_start:
			showLoading(true);
			quickStart();
			break;
		case R.id.search_btn:
			searchRoomByNo(searchContent.getText().toString().trim());
			break;
		case R.id.create_room:
			showLoading(true);
			createRoom();
			break;
		case R.id.raffle_go:
			Intent intent = new Intent(context, RaffleActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}

	}
}
