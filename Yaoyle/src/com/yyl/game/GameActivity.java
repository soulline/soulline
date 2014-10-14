package com.yyl.game;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.yyl.BaseActivity;
import com.yyl.R;
import com.yyl.application.YylApp;
import com.yyl.fragment.BaseFragmentListener;
import com.yyl.fragment.ChooseDiceFragment;
import com.yyl.fragment.GameResultFragment;
import com.yyl.media.YylPlayer;
import com.yyl.mode.AccountInfo;
import com.yyl.mode.GameResultInfo;
import com.yyl.mode.GamerGessInfo;
import com.yyl.mode.GamerInfo;
import com.yyl.mode.RoomItemEntry;
import com.yyl.net.RequestListener;
import com.yyl.operater.LoadAccountOperater;
import com.yyl.sensor.OnShakeListener;
import com.yyl.sensor.YylSensorManager;
import com.yyl.share.ShareManager;
import com.yyl.utils.BroadcastCode;
import com.yyl.utils.DataUtils;
import com.yyl.utils.GameAction;
import com.yyl.utils.ImageOperater;
import com.yyl.utils.RandomUtils;
import com.yyl.utils.YylRequestCode;

import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameActivity extends BaseActivity implements OnClickListener,
		IWXAPIEventHandler {

	private HashMap<String, GamerInfo> gamerMap = new HashMap<String, GamerInfo>();
	
	private HashMap<Integer, String> gamerIdMap = new HashMap<Integer, String>();
	
	private HashMap<Integer, Runnable> runMap = new HashMap<Integer, Runnable>();

	private YylSensorManager sensorManager;
	private YylPlayer player;

	private ImageView boxSelf;

	private ImageView boxGamer1;
	private ImageView boxGamer2;
	private ImageView boxGamer3;
	private ImageView boxGamer4;

	private ImageView selfDice1;
	private ImageView selfDice2;
	private ImageView selfDice3;
	private ImageView selfDice4;
	private ImageView selfDice5;
	private ImageView selfDice6;

	private TextView msgNumber1;
	private TextView msgNumber2;
	private TextView msgNumber3;
	private TextView msgNumber4;
	private TextView msgNumberSelf;

	private ImageView msgDice1;
	private ImageView msgDice2;
	private ImageView msgDice3;
	private ImageView msgDice4;
	private ImageView msgDiceSelf;

	private ImageView portraitGamer1;
	private ImageView portraitGamer2;
	private ImageView portraitGamer3;
	private ImageView portraitGamer4;
	private ImageView portraitGamerSelf;

	private ImageView portraitFrameSelf;
	private ImageView portraitFrame1;
	private ImageView portraitFrame2;
	private ImageView portraitFrame3;
	private ImageView portraitFrame4;

	private Button quickOpenBtn;

	private TextView gamer1name, gamer2name, gamer3name, gamer4name;

	private TextView selfName;

	private TextView roomNum, baseCoin;

	private Button soundOnOff;

	private HashMap<Integer, Integer> drawaleMap = new HashMap<Integer, Integer>();

	private ArrayList<GamerInfo> gamersInfo = new ArrayList<GamerInfo>();

	private GameState GAME_STATE = GameState.DEFAULT;

	private RoomItemEntry roomItem = new RoomItemEntry();

	private int CEN_CHOOSE = 0;

	private int DICE_CHOOSE = 0;

	private int TOKEN_USER = -1;

	private GameResultInfo resultInfo = new GameResultInfo();

	private TextView selfCoin;

	private boolean isShowDialog = false;

	private ShareManager shareManager = null;

	private GamerGessInfo gessInfo = new GamerGessInfo();

	private LocalBroadcastManager lbm = LocalBroadcastManager
			.getInstance(YylApp.getInstance());

	private boolean isAreadyExit = false;
	
	private int nowLoading = -1;

	public enum GameState {
		PREPARE, STATING, SHAKED, WAITING_GESS, SELF_GESS, FINISH, DEFAULT
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_activity);
		setFullScreen();
		registerGameReceiver();
		initView();
		initsmallDiceMap();
		initSensor();
		initContent();
		initShared();
		initRunMap();
		if (savedInstanceState != null) {
			shareManager.handlerWeibo(getIntent());
		}
	}

	private void initShared() {
		shareManager = new ShareManager(this);
		shareManager.initShare();
	}

	private void initRunMap(){
		runMap.put(0, selfRun);
		runMap.put(1, gamer1Run);
		runMap.put(2, gamer2Run);
		runMap.put(3, gamer3Run);
		runMap.put(4, gamer4Run);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		shareManager.onNewIntent(intent, this);
	}

	public void shareToSinaWb() {
		shareManager.sharedSinaWeibo();
	}

	public void shareToWX() {
		shareManager.sharedMsgToWX(1);
	}

	private void doPrepare() {
		if (roomItem == null)
			return;
		JSONObject prepareObj = new JSONObject();
		try {
			prepareObj.put("actionCode", GameAction.GAME_SELF_PREPARE);
			prepareObj.put("gamblingId", roomItem.id);
			prepareObj.put("uid", app.getUID());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Intent prepareD = new Intent(BroadcastCode.ACTION_SEND_CMD);
		prepareD.putExtra("request_str", prepareObj.toString());
		lbm.sendBroadcast(prepareD);
	}

	private void initContent() {
		roomItem = (RoomItemEntry) getIntent()
				.getSerializableExtra("room_item");
		if (roomItem != null) {
			roomNum.setText(roomItem.boardNo);
			baseCoin.setText(roomItem.score);
		} else {
			showToast(getString(R.string.room_init_failed));
			finish();
			return;
		}
		boolean isSound = DataUtils.getPreferences(DataUtils.KEY_SOUND_ONOFF,
				true);
		if (isSound) {
			soundOnOff.setBackgroundResource(R.drawable.sound_on);
		} else {
			soundOnOff.setBackgroundResource(R.drawable.sound_off);
		}
		getGamerMap();
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				loadAccountInfo();
			}
		}, 500);
	}

	private Runnable selfRun = new Runnable() {
		
		@Override
		public void run() {
			portraitFrameSelf.setImageResource(R.drawable.head_kong);
			checkOverGess();
		}
	};
	
	private Runnable gamer1Run = new Runnable() {
		
		@Override
		public void run() {
			portraitFrame1.setImageResource(R.drawable.head_kong);
		}
	};
	
	private Runnable gamer2Run = new Runnable() {
		
		@Override
		public void run() {
			portraitFrame3.setImageResource(R.drawable.head_kong);
		}
	};
	
	private Runnable gamer3Run = new Runnable() {
		
		@Override
		public void run() {
			portraitFrame2.setImageResource(R.drawable.head_kong);
		}
	};
	
	private Runnable gamer4Run = new Runnable() {
		
		@Override
		public void run() {
			portraitFrame4.setImageResource(R.drawable.head_kong);
		}
	};
	
	private void showGameLoading(boolean isLoading, final ImageView imageView, int tokenUser) {
		if (isLoading) {
			imageView.setImageResource(R.drawable.game_loading);
			final AnimationDrawable animDrawable = (AnimationDrawable) imageView
					.getDrawable();
			animDrawable.start();
			if (tokenUser != -1) {
				handler.postDelayed(runMap.get(tokenUser), 29000);
			}
		} else {
			if (tokenUser != -1) {
				handler.removeCallbacks(runMap.get(tokenUser));
			}
			imageView.setImageResource(R.drawable.head_kong);
		}
	}

	private void initView() {
		boxSelf = (ImageView) findViewById(R.id.game_box_self);
		boxGamer1 = (ImageView) findViewById(R.id.game_box_1);
		boxGamer2 = (ImageView) findViewById(R.id.game_box_2);
		boxGamer3 = (ImageView) findViewById(R.id.game_box_3);
		boxGamer4 = (ImageView) findViewById(R.id.game_box_4);
		selfDice1 = (ImageView) findViewById(R.id.self_dice_1);
		selfDice2 = (ImageView) findViewById(R.id.self_dice_2);
		selfDice3 = (ImageView) findViewById(R.id.self_dice_3);
		selfDice4 = (ImageView) findViewById(R.id.self_dice_4);
		selfDice5 = (ImageView) findViewById(R.id.self_dice_5);

		/*
		 * gessDice1 = (ImageView) findViewById(R.id.self_choose_1); gessDice2 =
		 * (ImageView) findViewById(R.id.self_choose_2); gessDice3 = (ImageView)
		 * findViewById(R.id.self_choose_3); gessDice4 = (ImageView)
		 * findViewById(R.id.self_choose_4); gessDice5 = (ImageView)
		 * findViewById(R.id.self_choose_5); gessDice6 = (ImageView)
		 * findViewById(R.id.self_choose_6); gessDice1.setOnClickListener(this);
		 * gessDice2.setOnClickListener(this);
		 * gessDice3.setOnClickListener(this);
		 * gessDice4.setOnClickListener(this);
		 * gessDice5.setOnClickListener(this);
		 * gessDice6.setOnClickListener(this);
		 */

		portraitFrameSelf = (ImageView) findViewById(R.id.portrait_frame_self);
		portraitFrame1 = (ImageView) findViewById(R.id.portrait_frame_1);
		portraitFrame2 = (ImageView) findViewById(R.id.portrait_frame_2);
		portraitFrame3 = (ImageView) findViewById(R.id.portrait_frame_3);
		portraitFrame4 = (ImageView) findViewById(R.id.portrait_frame_4);

		selfName = (TextView) findViewById(R.id.self_name);
		selfCoin = (TextView) findViewById(R.id.self_coin);
		quickOpenBtn = (Button) findViewById(R.id.quick_open_button);
		// findViewById(R.id.left_move).setOnClickListener(this);
		// findViewById(R.id.right_move).setOnClickListener(this);
		quickOpenBtn.setOnClickListener(this);

		soundOnOff = (Button) findViewById(R.id.sound_on_off);
		soundOnOff.setOnClickListener(this);
		msgNumber1 = (TextView) findViewById(R.id.msg_number_1);
		msgNumber2 = (TextView) findViewById(R.id.msg_number_2);
		msgNumber3 = (TextView) findViewById(R.id.msg_number_3);
		msgNumber4 = (TextView) findViewById(R.id.msg_number_4);
		msgNumberSelf = (TextView) findViewById(R.id.msg_number_self);
		msgDice1 = (ImageView) findViewById(R.id.msg_dice_1);
		msgDice2 = (ImageView) findViewById(R.id.msg_dice_2);
		msgDice3 = (ImageView) findViewById(R.id.msg_dice_3);
		msgDice4 = (ImageView) findViewById(R.id.msg_dice_4);
		msgDiceSelf = (ImageView) findViewById(R.id.msg_dice_self);

		portraitGamer1 = (ImageView) findViewById(R.id.portrait_gamer_1);
		portraitGamer2 = (ImageView) findViewById(R.id.portrait_gamer_2);
		portraitGamer3 = (ImageView) findViewById(R.id.portrait_gamer_3);
		portraitGamer4 = (ImageView) findViewById(R.id.portrait_gamer_4);
		portraitGamerSelf = (ImageView) findViewById(R.id.portrait_gamer_self);

		gamer1name = (TextView) findViewById(R.id.gamer_1_name);
		gamer2name = (TextView) findViewById(R.id.gamer_2_name);
		gamer3name = (TextView) findViewById(R.id.gamer_3_name);
		gamer4name = (TextView) findViewById(R.id.gamer_4_name);

		roomNum = (TextView) findViewById(R.id.room_num);
		baseCoin = (TextView) findViewById(R.id.base_coin);
	}

	private void showSelfDiceNote() {
		findViewById(R.id.note_self_tx).setVisibility(View.VISIBLE);
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				findViewById(R.id.note_self_tx).setVisibility(View.GONE);
			}
		}, 1500);
	}

	private void showMsgBoxNote(int gamer, int number, int dice) {
		if (gamer == -1)
			return;
		View viewL = null;
		switch (gamer) {
		case 0:
			viewL = findViewById(R.id.msg_box_self);
			msgNumberSelf.setText(number + getString(R.string.number_ge));
			msgDiceSelf.setBackgroundResource(drawaleMap.get(dice));
			break;
		case 1:
			viewL = findViewById(R.id.msg_box_1);
			msgNumber1.setText(number + getString(R.string.number_ge));
			msgDice1.setBackgroundResource(drawaleMap.get(dice));
			break;

		case 2:
			viewL = findViewById(R.id.msg_box_3);
			msgNumber3.setText(number + getString(R.string.number_ge));
			msgDice3.setBackgroundResource(drawaleMap.get(dice));
			break;

		case 3:
			viewL = findViewById(R.id.msg_box_2);
			msgNumber2.setText(number + getString(R.string.number_ge));
			msgDice2.setBackgroundResource(drawaleMap.get(dice));
			break;

		case 4:
			viewL = findViewById(R.id.msg_box_4);
			msgNumber4.setText(number + getString(R.string.number_ge));
			msgDice4.setBackgroundResource(drawaleMap.get(dice));
			break;

		default:
			break;
		}
		if (viewL != null) {
			viewL.setVisibility(View.VISIBLE);
			final View viewN = viewL;
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					viewN.setVisibility(View.GONE);
				}
			}, 2000);
		}
	}

	private void registerGameReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BroadcastCode.ACTION_SELF_PREPARE);
		filter.addAction(BroadcastCode.ACTION_GAME_START);
		filter.addAction(BroadcastCode.ACTION_OTHER_JOIN);
		filter.addAction(BroadcastCode.ACTION_OTHER_PREPARE);
		filter.addAction(BroadcastCode.ACTION_GAME_EXIT);
		filter.addAction(BroadcastCode.ACTION_SELF_SHAKED);
		filter.addAction(BroadcastCode.ACTION_SELF_GESS_DICE);
		filter.addAction(BroadcastCode.ACTION_OPEN_DICE);
		filter.addAction(BroadcastCode.ACTION_OTHER_SHAKE);
		filter.addAction(BroadcastCode.ACTION_START_GESS);
		filter.addAction(BroadcastCode.ACTION_OTHER_GESS);
		filter.addAction(BroadcastCode.ACTION_GAME_RESULT);
		filter.addAction(BroadcastCode.ACTION_REQUST_ERROR);
		filter.addAction(BroadcastCode.ACTION_QIANG_FINISH);
		lbm.registerReceiver(gameStatausReceiver, filter);
	}

	private void initSensor() {
		player = new YylPlayer();
		sensorManager = new YylSensorManager(this, new OnShakeListener() {

			@Override
			public void onShake() {
				Animation ta = AnimationUtils.loadAnimation(context,
						R.anim.shake);
				boxSelf.startAnimation(ta);
				player.playSound(1, 1);
				if (sensorManager != null) {
					sensorManager.unRegistSensor();
				}
				findViewById(R.id.start_game_note).setVisibility(View.GONE);
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						ArrayList<Integer> randoms = RandomUtils
								.getRollDices(5);
						doShakeSocket(randoms);
						showSelfDiceInfo(randoms);
						showSelfDiceNote();
					}
				}, 1500);
			}
		});
	}

	private void doShakeSocket(ArrayList<Integer> randoms) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < randoms.size(); i++) {
			sb.append(randoms.get(i));
			if (i != (randoms.size() - 1)) {
				sb.append("-");
			}
		}
		JSONObject shakeObj = new JSONObject();
		try {
			shakeObj.put("actionCode", GameAction.GAMER_SELF_SHAKE_DICE);
			shakeObj.put("dicePoint", sb.toString());
			shakeObj.put("gamblingId", roomItem.id);
			shakeObj.put("uid", app.getUID());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Intent shakeD = new Intent(BroadcastCode.ACTION_SEND_CMD);
		shakeD.putExtra("request_str", shakeObj.toString());
		lbm.sendBroadcast(shakeD);
	}

	private void initsmallDiceMap() {
		drawaleMap.put(1, R.drawable.super_ds_1);
		drawaleMap.put(2, R.drawable.super_ds_2);
		drawaleMap.put(3, R.drawable.super_ds_3);
		drawaleMap.put(4, R.drawable.super_ds_4);
		drawaleMap.put(5, R.drawable.super_ds_5);
		drawaleMap.put(6, R.drawable.super_ds_6);
	}

	private void doExitGame() {
		JSONObject exitObj = new JSONObject();
		try {
			exitObj.put("actionCode", GameAction.GAME_EXIT);
			exitObj.put("gamblingId", roomItem.id);
			exitObj.put("uid", app.getUID());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Intent exitD = new Intent(BroadcastCode.ACTION_SEND_CMD);
		exitD.putExtra("request_str", exitObj.toString());
		lbm.sendBroadcast(exitD);
		isAreadyExit = true;
	}

	private void doOpenDice() {
		JSONObject openObj = new JSONObject();
		try {
			openObj.put("actionCode", GameAction.GAMER_OPEN_DICE);
			openObj.put("gamblingId", roomItem.id);
			openObj.put("uid", app.getUID());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Intent openD = new Intent(BroadcastCode.ACTION_SEND_CMD);
		openD.putExtra("request_str", openObj.toString());
		lbm.sendBroadcast(openD);
	}

	private void doGessDice(String diceNum, String dicePoint) {
		JSONObject gessObj = new JSONObject();
		try {
			gessObj.put("actionCode", GameAction.GAMER_SELF_GESS_DICE);
			gessObj.put("diceNum", diceNum);
			gessObj.put("dicePoint", dicePoint);
			gessObj.put("gamblingId", roomItem.id);
			gessObj.put("uid", app.getUID());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Intent gessD = new Intent(BroadcastCode.ACTION_SEND_CMD);
		gessD.putExtra("request_str", gessObj.toString());
		lbm.sendBroadcast(gessD);
	}

	private void showSelfDiceInfo(ArrayList<Integer> randoms) {
		findViewById(R.id.self_dice_show_layout).setVisibility(View.VISIBLE);
		for (int i = 0; i < randoms.size(); i++) {
			switch (i) {
			case 1:
				selfDice1.setBackgroundResource(drawaleMap.get(randoms.get(i)));
				break;
			case 2:
				selfDice2.setBackgroundResource(drawaleMap.get(randoms.get(i)));
				break;
			case 3:
				selfDice3.setBackgroundResource(drawaleMap.get(randoms.get(i)));
				break;
			case 4:
				selfDice4.setBackgroundResource(drawaleMap.get(randoms.get(i)));
				break;
			case 5:
				selfDice5.setBackgroundResource(drawaleMap.get(randoms.get(i)));
				break;

			default:
				break;
			}
		}
	}

	private void onOtherShake(int gamerId) {
		Animation ta = AnimationUtils.loadAnimation(context, R.anim.shake);
		switch (gamerId) {
		case 1:
			boxGamer1.startAnimation(ta);
			break;
		case 2:
			boxGamer3.startAnimation(ta);
			break;
		case 3:
			boxGamer2.startAnimation(ta);
			break;
		case 4:
			boxGamer4.startAnimation(ta);
			break;
		default:
			break;
		}
	}

	private void showOhterPrepare(String gamerId) {
		int gamer = getGamer(gamerId);
		switch (gamer) {
		case 1:
			findViewById(R.id.gamer_1_prepare).setVisibility(View.VISIBLE);
			break;
		case 2:
			findViewById(R.id.gamer_3_prepare).setVisibility(View.VISIBLE);
			break;
		case 3:
			findViewById(R.id.gamer_2_prepare).setVisibility(View.VISIBLE);
			break;
		case 4:
			findViewById(R.id.gamer_4_prepare).setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}

	private void removeOhterPrepare(String gamerId) {
		int gamer = getGamer(gamerId);
		switch (gamer) {
		case 1:
			findViewById(R.id.gamer_1_prepare).setVisibility(View.GONE);
			break;
		case 2:
			findViewById(R.id.gamer_3_prepare).setVisibility(View.GONE);
			break;
		case 3:
			findViewById(R.id.gamer_2_prepare).setVisibility(View.GONE);
			break;
		case 4:
			findViewById(R.id.gamer_4_prepare).setVisibility(View.GONE);
			break;

		default:
			break;
		}
	}

	private void clearOtherPrepareView() {
		findViewById(R.id.gamer_1_prepare).setVisibility(View.GONE);
		findViewById(R.id.gamer_2_prepare).setVisibility(View.GONE);
		findViewById(R.id.gamer_3_prepare).setVisibility(View.GONE);
		findViewById(R.id.gamer_4_prepare).setVisibility(View.GONE);
	}

	private void playPai() {
		player.playSound(2, 0);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (!isAreadyExit) {
			doExitGame();
		}
		lbm.unregisterReceiver(gameStatausReceiver);
		if (sensorManager != null) {
			sensorManager.unRegistSensor();
		}
	}

	private void getGamerMap() {
		gamersInfo = (ArrayList<GamerInfo>) getIntent().getSerializableExtra(
				"gamers_info");
		if (gamersInfo != null && gamersInfo.size() > 0) {
			int count = 0;
			for (int i = 0; i < gamersInfo.size(); i++) {
				gamerMap.put(gamersInfo.get(i).id, gamersInfo.get(i));
				gamerIdMap.put(i + 1, gamersInfo.get(i).id);
				count++;
			}
			initGamerView(count);
			initGamerDetailView(gamersInfo);
		}
	}

	private void initGamerView(int count) {
		switch (count) {
		case 1:
			findViewById(R.id.gamer_1_layout).setVisibility(View.VISIBLE);
			findViewById(R.id.gamer_2_layout).setVisibility(View.GONE);
			findViewById(R.id.gamer_3_layout).setVisibility(View.GONE);
			findViewById(R.id.gamer_4_layout).setVisibility(View.GONE);
			break;
		case 2:
			findViewById(R.id.gamer_1_layout).setVisibility(View.VISIBLE);
			findViewById(R.id.gamer_2_layout).setVisibility(View.GONE);
			findViewById(R.id.gamer_3_layout).setVisibility(View.VISIBLE);
			findViewById(R.id.gamer_4_layout).setVisibility(View.GONE);
			break;
		case 3:
			findViewById(R.id.gamer_1_layout).setVisibility(View.VISIBLE);
			findViewById(R.id.gamer_2_layout).setVisibility(View.VISIBLE);
			findViewById(R.id.gamer_3_layout).setVisibility(View.VISIBLE);
			findViewById(R.id.gamer_4_layout).setVisibility(View.GONE);
			break;
		case 4:
			findViewById(R.id.gamer_1_layout).setVisibility(View.VISIBLE);
			findViewById(R.id.gamer_2_layout).setVisibility(View.VISIBLE);
			findViewById(R.id.gamer_3_layout).setVisibility(View.VISIBLE);
			findViewById(R.id.gamer_4_layout).setVisibility(View.VISIBLE);
			break;
		case 0:
			findViewById(R.id.gamer_1_layout).setVisibility(View.GONE);
			findViewById(R.id.gamer_2_layout).setVisibility(View.GONE);
			findViewById(R.id.gamer_3_layout).setVisibility(View.GONE);
			findViewById(R.id.gamer_4_layout).setVisibility(View.GONE);
			break;

		default:
			break;
		}
	}

	private void initGamerDetailView(ArrayList<GamerInfo> gamers) {
		for (int i = 0; i < gamers.size(); i++) {
			GamerInfo gamer = gamers.get(i);
			switch (i) {
			case 0:
				gamer1name.setText(gamer.name);
				if (gamer.sex.equals("1")) {
					portraitGamer1.setBackgroundResource(R.drawable.head_boy);
				} else if (gamer.sex.equals("2")) {
					portraitGamer1.setBackgroundResource(R.drawable.head_girl);
				}
				if (!TextUtils.isEmpty(gamer.photoUrl)) {
					String url = gamer.photoUrl + "&" + System.currentTimeMillis();
					ImageOperater.getInstance(context).onLoadImage(
							url, portraitGamer1);
				}
				if (gamer.status.equals("1")) {
					findViewById(R.id.gamer_1_prepare).setVisibility(View.VISIBLE);
				} else {
					findViewById(R.id.gamer_1_prepare).setVisibility(View.GONE);
				}
				break;
			case 1:
				gamer3name.setText(gamer.name);
				if (gamer.sex.equals("1")) {
					portraitGamer3.setBackgroundResource(R.drawable.head_boy);
				} else if (gamer.sex.equals("2")) {
					portraitGamer3.setBackgroundResource(R.drawable.head_girl);
				}
				if (!TextUtils.isEmpty(gamer.photoUrl)) {
					String url = gamer.photoUrl + "&" + System.currentTimeMillis();
					ImageOperater.getInstance(context).onLoadImage(
							url, portraitGamer3);
				}
				if (gamer.status.equals("1")) {
					findViewById(R.id.gamer_3_prepare).setVisibility(View.VISIBLE);
				} else {
					findViewById(R.id.gamer_3_prepare).setVisibility(View.GONE);
				}
				break;
			case 2:
				gamer2name.setText(gamer.name);
				if (gamer.sex.equals("1")) {
					portraitGamer2.setBackgroundResource(R.drawable.head_boy);
				} else if (gamer.sex.equals("2")) {
					portraitGamer2.setBackgroundResource(R.drawable.head_girl);
				}
				if (!TextUtils.isEmpty(gamer.photoUrl)) {
					String url = gamer.photoUrl + "&" + System.currentTimeMillis();
					ImageOperater.getInstance(context).onLoadImage(
							url, portraitGamer2);
				}
				if (gamer.status.equals("1")) {
					findViewById(R.id.gamer_2_prepare).setVisibility(View.VISIBLE);
				} else {
					findViewById(R.id.gamer_2_prepare).setVisibility(View.GONE);
				}
				break;
			case 3:
				gamer4name.setText(gamer.name);
				if (gamer.sex.equals("1")) {
					portraitGamer4.setBackgroundResource(R.drawable.head_boy);
				} else if (gamer.sex.equals("2")) {
					portraitGamer4.setBackgroundResource(R.drawable.head_girl);
				}
				if (!TextUtils.isEmpty(gamer.photoUrl)) {
					String url = gamer.photoUrl + "&" + System.currentTimeMillis();
					ImageOperater.getInstance(context).onLoadImage(
							url, portraitGamer4);
				}
				if (gamer.status.equals("1")) {
					findViewById(R.id.gamer_4_prepare).setVisibility(View.VISIBLE);
				} else {
					findViewById(R.id.gamer_4_prepare).setVisibility(View.GONE);
				}
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onBackPressed() {
		if (!isShowDialog) {
			if (GAME_STATE == GameState.STATING) {
				showTipNoteDialog(getString(R.string.exit_warning).replaceAll(
						"%", roomItem.score));
			} else {
				doExitGame();
				super.onBackPressed();
			}
		}
	}

	private void updateConin() {
		final LoadAccountOperater loadAccount = new LoadAccountOperater(context);
		loadAccount.setShowLoading(false);
		loadAccount.onRequest(new RequestListener() {
			
			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCallBack(Object data) {
				final AccountInfo account = (AccountInfo) loadAccount.getData();
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						selfName.setText(account.name);
						selfCoin.setText(account.scoreAmount);
					}
				});
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
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						displayAccount(account);
					}
				});
			}
		});
	}
	
	private void displayAccount(AccountInfo account) {
		if (account != null) {
			selfName.setText(account.name);
			selfCoin.setText(account.scoreAmount);
			if (account.sex.equals("1")) {
				portraitGamerSelf
						.setBackgroundResource(R.drawable.head_boy);
			} else if (account.sex.equals("2")) {
				portraitGamerSelf
						.setBackgroundResource(R.drawable.head_girl);
			}
			if (!TextUtils.isEmpty(account.photoUrl)) {
				String url = account.photoUrl + "&" + System.currentTimeMillis();
				ImageOperater.getInstance(context).onLoadImage(
						url, portraitGamerSelf);
			}
		}
	}

	private void showTipNoteDialog(final String msg) {
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
				b.setPositiveButton(R.string.action_sure,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int arg1) {
								doExitGame();
								dialog.dismiss();
							}
						});
				b.show();
			}
		});
	}

	private void removeExitGamer(String gamerId) {
		int n1 = -1;
		int n2 = -1;
		for (int i = 0; i < gamersInfo.size(); i++) {
			if (gamerId.equals(gamersInfo.get(i).id)) {
				n1 = i;
				break;
			}
		}
		for (int i = 1; i < (gamerIdMap.size() + 1); i++) {
			if (gamerId.equals(gamerIdMap.get(i))) {
				n2 = i;
				break;
			}
		}
		if (n1 != -1 && n2 != -1) {
			gamersInfo.remove(n1);
			gamerIdMap.remove(n2);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		shareManager.onActivityResult(requestCode, resultCode, data);
	}

	private void showSelfChoose(int goupNum) {
		Bundle b = new Bundle();
		b.putInt("group_num", goupNum);
		if (gessInfo != null) {
			b.putSerializable("last_gess", gessInfo);
		}
		displayFragment(true, "choose_dice", b, new BaseFragmentListener() {

			@Override
			public void onCallBack(Object object) {
				String diceInfo = (String) object;
				if (!TextUtils.isEmpty(diceInfo) && diceInfo.contains("-")) {
					String[] array = diceInfo.split("-");
					showGameLoading(false, portraitFrameSelf, 0);
					doGessDice(array[0], array[1]);
					CEN_CHOOSE = Integer.valueOf(array[0]);
					DICE_CHOOSE = Integer.valueOf(array[1]);
				}
			}
		});
	}
	
	private void clearGamerLoading() {
		showGameLoading(false, portraitFrameSelf, 0);
		showGameLoading(false, portraitFrame1, 1);
		showGameLoading(false, portraitFrame2, 2);
		showGameLoading(false, portraitFrame3, 3);
		showGameLoading(false, portraitFrame4, 4);
	}
	
	private ImageView getGamerImageView(String userId) {
		int gamer = getGamer(userId);
		ImageView imageView = null;
		nowLoading = gamer;
		switch (gamer) {
		case 0:
			imageView = portraitFrameSelf;
			break;
		case 1:
			imageView = portraitFrame1;
			break;
		case 2:
			imageView = portraitFrame3;
			break;
		case 3:
			imageView = portraitFrame2;
			break;
		case 4:
			imageView = portraitFrame4;
			break;

		default:
			break;
		}
		return imageView;
	}

	public BroadcastReceiver gameStatausReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(BroadcastCode.ACTION_SELF_PREPARE)) {
				showToast(getString(R.string.action_ready));
				quickOpenBtn.setVisibility(View.VISIBLE);
				quickOpenBtn.setText(getString(R.string.action_already_ready));
				GAME_STATE = GameState.PREPARE;
			} else if (intent.getAction().equals(
					BroadcastCode.ACTION_GAME_START)) {
				gessInfo = new GamerGessInfo();
				clearOtherPrepareView();
				GAME_STATE = GameState.STATING;
				showToast(getString(R.string.game_start_shake));
				findViewById(R.id.start_game_note).setVisibility(View.VISIBLE);
				if (sensorManager != null) {
					sensorManager.initSensor();
				}
			} else if (intent.getAction().equals(
					BroadcastCode.ACTION_OTHER_JOIN)) {
				GamerInfo gamer = (GamerInfo) intent
						.getSerializableExtra("gamer_info");
				showToast(gamer.name + getString(R.string.join_game));
				if (gamersInfo == null) {
					gamersInfo = new ArrayList<GamerInfo>();
				}
				gamersInfo.add(gamer);
				gamerIdMap.put(gamersInfo.size(), gamer.id);
				gamerMap.put(gamer.id, gamer);
				initGamerView(gamersInfo.size());
				removeOhterPrepare(gamer.id);
				initGamerDetailView(gamersInfo);
			} else if (intent.getAction().equals(
					BroadcastCode.ACTION_OTHER_PREPARE)) {
				String id = intent.getStringExtra("gamer_id");
				if (TextUtils.isEmpty(id))
					return;
				showToast(gamerMap.get(id).name
						+ getString(R.string.action_already_ready));
				showOhterPrepare(id);
			} else if (intent.getAction()
					.equals(BroadcastCode.ACTION_GAME_EXIT)) {
				if (!TextUtils.isEmpty(intent.getStringExtra("gamer_id"))) {
					String gamerId = intent.getStringExtra("gamer_id");
					showToast(gamerMap.get(gamerId).name
							+ getString(R.string.exit_game));
					removeExitGamer(gamerId);
					initGamerView(gamersInfo.size());
					if (GAME_STATE != GameState.PREPARE
							|| GAME_STATE == GameState.DEFAULT) {
						initGameState();
					}
					if (gamersInfo.size() == 0) {
						handler.removeCallbacks(runMap.get(0));
					}
				} else {
					showToast(getString(R.string.exit_game));
					finish();
				}
			} else if (intent.getAction().equals(
					BroadcastCode.ACTION_SELF_SHAKED)) {
				GAME_STATE = GameState.SHAKED;
			} else if (intent.getAction().equals(
					BroadcastCode.ACTION_SELF_GESS_DICE)) {
				showMsgBoxNote(0, CEN_CHOOSE, DICE_CHOOSE);
				CEN_CHOOSE = 0;
				DICE_CHOOSE = 0;
				GAME_STATE = GameState.WAITING_GESS;
				closeFragment("choose_dice");
			} else if (intent.getAction()
					.equals(BroadcastCode.ACTION_OPEN_DICE)) {

			} else if (intent.getAction().equals(
					BroadcastCode.ACTION_OTHER_SHAKE)) {
				String gamerId = intent.getStringExtra("id");
				shakeWithOther(gamerId);
			} else if (intent.getAction().equals(
					BroadcastCode.ACTION_START_GESS)) {
				String tokenUserId = intent.getStringExtra("tokenUserId");
				if (!TextUtils.isEmpty(tokenUserId)) {
					TOKEN_USER = getGamer(tokenUserId);
					ImageView view = getGamerImageView(tokenUserId);
					if (view != null) {
						clearGamerLoading();
						showGameLoading(true, view, TOKEN_USER);
					}
					if (TOKEN_USER == 0) {
						quickOpenBtn.setVisibility(View.VISIBLE);
						quickOpenBtn.setText(getString(R.string.action_open));
						showSelfChoose(0);
						GAME_STATE = GameState.SELF_GESS;
					} else if (TOKEN_USER != -1) {
						quickOpenBtn.setVisibility(View.GONE);
						GAME_STATE = GameState.WAITING_GESS;
					}
					String name = "";
					if (TOKEN_USER == 0) {
						name = app.getAccount().name;
					} else if (TOKEN_USER > 0) {
						name = gamerMap.get(tokenUserId).name;
					}
					if (!TextUtils.isEmpty(name)) {
						showToast(name + getString(R.string.start_gess));
					}
				}
			} else if (intent.getAction().equals(
					BroadcastCode.ACTION_OTHER_GESS)) {
				gessInfo = (GamerGessInfo) intent
						.getSerializableExtra("gess_info");
				if (gessInfo != null) {
					TOKEN_USER = getGamer(gessInfo.tokenUserId);
					showMsgBoxNote(getGamer(gessInfo.preUserId),
							Integer.valueOf(gessInfo.preDiceNum),
							Integer.valueOf(gessInfo.preDicePoint));
					ImageView view = getGamerImageView(gessInfo.tokenUserId);
					if (view != null) {
						clearGamerLoading();
						showGameLoading(true, view, TOKEN_USER);
					}
					if (TOKEN_USER == 0) {
						quickOpenBtn.setVisibility(View.VISIBLE);
						quickOpenBtn.setText(getString(R.string.action_open));
						int diceNum = Integer. valueOf(gessInfo.preDiceNum);
						int group = getGroupNumber(diceNum);
						showSelfChoose(group);
						GAME_STATE = GameState.SELF_GESS;
					} else if (TOKEN_USER != -1) {
						quickOpenBtn.setVisibility(View.GONE);
						GAME_STATE = GameState.WAITING_GESS;
					}
				}
			} else if (intent.getAction().equals(
					BroadcastCode.ACTION_GAME_RESULT)) {
				GAME_STATE = GameState.FINISH;
				clearGamerLoading();
				updateConin();
				resultInfo = null;
				resultInfo = (GameResultInfo) intent
						.getSerializableExtra("result_info");
				if (resultInfo != null) {
					closeFragment("choose_dice");
					findViewById(R.id.self_dice_show_layout).setVisibility(
							View.GONE);
					if (resultInfo.win.equals("1")) {
						player.playSound(3, 0);
					} else if (resultInfo.win.equals("2")) {
						player.playSound(4, 0);
					}
					GAME_STATE = GameState.DEFAULT;
					quickOpenBtn.setVisibility(View.VISIBLE);
					quickOpenBtn.setText(getString(R.string.action_ready));
					disPlayResult();
				}
			} else if (intent.getAction().equals(
					BroadcastCode.ACTION_REQUST_ERROR)) {
				showLoading(false);
				int status = intent.getIntExtra("status", -1);
				if (status == 206) {
					doExitGame();
				}
				if (status == GameAction.GAMER_SELF_GESS_DICE) {
					doOpenDice();
				}
			} else if (intent.getAction().equals(
					BroadcastCode.ACTION_QIANG_FINISH)) {
				initGameState();
				String gamerId = intent.getStringExtra("gamer_id");
				if (!TextUtils.isEmpty(gamerId)) {
					showToast(gamerMap.get(gamerId).name
							+ getString(R.string.exit_game));
					removeExitGamer(gamerId);
					initGamerView(gamersInfo.size());
				}
			}
		}
	};

	private void checkOverGess() {
		if (!TextUtils.isEmpty(gessInfo.preDiceNum) && 
				!TextUtils.isEmpty(gessInfo.preDicePoint)) {
			int diceNum = Integer.valueOf(gessInfo.preDiceNum);
			int dicePoint = Integer.valueOf(gessInfo.preDicePoint);
			if (dicePoint < 6) {
				dicePoint++;
			} else if (dicePoint == 6) {
				diceNum++;
				dicePoint = 1;
			}
			CEN_CHOOSE = diceNum;
			DICE_CHOOSE = dicePoint;
			doGessDice(diceNum + "", dicePoint + "");
		} else {
			int length = gamerMap.size();
			CEN_CHOOSE = length + 1;
			DICE_CHOOSE = 1;
			doGessDice(CEN_CHOOSE + "", DICE_CHOOSE + "");
		}
	}

	private void initGameState() {
		closeFragment("choose_dice");
		findViewById(R.id.start_game_note).setVisibility(View.GONE);
		findViewById(R.id.self_dice_show_layout).setVisibility(View.GONE);
		GAME_STATE = GameState.DEFAULT;
		quickOpenBtn.setVisibility(View.VISIBLE);
		quickOpenBtn.setText(getString(R.string.action_ready));
	}

	private int getGroupNumber(int diceNum) {
		if ((diceNum > 5 || diceNum == 5) && (diceNum < 10)) {
			return 1;
		} else if ((diceNum > 10 || diceNum == 10) && (diceNum < 15)) {
			return 2;
		} else if ((diceNum > 15 || diceNum == 15) && (diceNum < 20)) {
			return 3;
		} else if ((diceNum > 20 || diceNum == 20) && (diceNum < 25)) {
			return 4;
		}
		return 0;
	}

	private void disPlayResult() {
		Bundle b = new Bundle();
		b.putSerializable("result", resultInfo);
		displayFragment(true, "game_result", b, new BaseFragmentListener() {

			@Override
			public void onCallBack(Object object) {
				isShowDialog = false;
			}
		});
		isShowDialog = true;
		/*
		 * handler.postDelayed(new Runnable() {
		 * 
		 * @Override public void run() { closeFragment("game_result"); } },
		 * 3000);
		 */
	}

	private int getGamer(String gamerId) {
		int n = -1;
		if (gamerId.equals(app.getAccount().accountId)) {
			n = 0;
		} else {
			for (int i = 1; i < (gamerIdMap.size() + 1); i++) {
				if (!TextUtils.isEmpty(gamerIdMap.get(i))
						&& gamerIdMap.get(i).equals(gamerId)) {
					n = i;
					break;
				}
			}
		}
		return n;
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
		if (tag.equals("game_result")) {
			GameResultFragment gameResultF = new GameResultFragment(context, b);
			gameResultF.addFragmentListener(listener);
			return gameResultF;
		} else if (tag.equals("choose_dice")) {
			ChooseDiceFragment cdicef = new ChooseDiceFragment(context, b);
			cdicef.addFragmentListener(listener);
			cdicef.setCancelable(false);
			return cdicef;
		}
		return null;
	}

	private void shakeWithOther(String gamerId) {
		int n = getGamer(gamerId);
		if (n > 0) {
			onOtherShake(n);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.quick_open_button:
			if (GAME_STATE == GameState.STATING
					|| GAME_STATE == GameState.PREPARE) {
				return;
			}
			playPai();
			if (GAME_STATE == GameState.DEFAULT
					|| GAME_STATE == GameState.FINISH) {
				doPrepare();
			} else if (GAME_STATE == GameState.SELF_GESS
					|| GAME_STATE == GameState.WAITING_GESS) {
				clearGamerLoading();
				doOpenDice();
			}
			break;
		case R.id.sound_on_off:
			boolean isSound = DataUtils.getPreferences(
					DataUtils.KEY_SOUND_ONOFF, true);
			if (isSound) {
				DataUtils.putPreferences(DataUtils.KEY_SOUND_ONOFF, false);
				soundOnOff.setBackgroundResource(R.drawable.sound_off);
			} else {
				DataUtils.putPreferences(DataUtils.KEY_SOUND_ONOFF, true);
				soundOnOff.setBackgroundResource(R.drawable.sound_on);
			}
			lbm.sendBroadcast(new Intent(BroadcastCode.ACTION_ONOFF_SOUND));
			break;
		default:
			break;
		}

	}

	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResp(BaseResp arg0) {
		// TODO Auto-generated method stub

	}
}
