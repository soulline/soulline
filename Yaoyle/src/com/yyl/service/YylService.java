package com.yyl.service;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yyl.MainActivity;
import com.yyl.R;
import com.yyl.application.YylApp;
import com.yyl.mode.GameResultInfo;
import com.yyl.mode.GamerDiceInfo;
import com.yyl.mode.GamerGessInfo;
import com.yyl.mode.GamerInfo;
import com.yyl.socket.SocketManager;
import com.yyl.socket.SocketRequestListener;
import com.yyl.utils.BroadcastCode;
import com.yyl.utils.DataUtils;
import com.yyl.utils.GameAction;
import com.yyl.utils.YylConfig;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class YylService extends Service {

	private SocketManager socketManager = null;

	private final int NOTIFICATION_ID = 10347;
	
	private MediaPlayer player;
	
	private LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(YylApp.getInstance());

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		player = MediaPlayer.create(this, R.raw.game_bg);
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		player.setLooping(true);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		registerServiceReceiver();
		setForeground();
		socketThread.start();
		
		if(!player.isPlaying() && DataUtils.getPreferences(DataUtils.KEY_SOUND_ONOFF, true)){  
            player.start();  
        } 
		return START_STICKY;
	}

	public Thread socketThread = new Thread(new Runnable() {

		@Override
		public void run() {
			initSocket();
		}
	});

	private void initSocket() {
		socketManager = new SocketManager();
		socketManager.onRequestListener(new SocketRequestListener() {

			@Override
			public void onError(String errorStr) {
				showToast(errorStr);
			}

			@Override
			public void onSuccess(JSONObject result) {
				checkGameInfo(result);
			}
		});
	}

	private void checkGameInfo(JSONObject result) {
		int code = result.optInt("code");
		switch (code) {
		case GameAction.GAME_JOIN_CODE:
			parseOtherJoin(result);
			break;
		case GameAction.GAME_PREPARE_CODE:
			parseOtherPrepare(result);
			break;
		case GameAction.GAME_SELF_JOIN:
			parseSelfJoin(result);
			break;
		case GameAction.GAME_SELF_PREPARE:
			parseSelfPrepare(result);
			break;
		case GameAction.GAME_START_CODE:
			parseGameStart(result);
			break;
		case GameAction.GAME_SELF_CREATE:
			parseSelfCreate(result);
			break;
		case GameAction.GAME_EXIT:
			parseGameExit(result);
			break;
		case GameAction.GAMER_SELF_SHAKE_DICE:
			parseSelfShake(result);
			break;
		case GameAction.GAMER_SELF_GESS_DICE:
			parseSelfGess(result);
			break;
		case GameAction.GAMER_OPEN_DICE:
			parseOpenDice(result);
			break;
		case GameAction.OTHER_PLAYER_SHAKED:
			parseOtherShake(result);
			break;
		case GameAction.START_GESS_DICE:
			parseStartGess(result);
			break;
		case GameAction.OTHER_GESS_FINISHED:
			parseOtherGess(result);
			break;
		case GameAction.GAME_RESULT:
			parseGameResult(result);
			break;
		case GameAction.OTHER_EXIT_GAME:
			parseGameExit(result);
			break;
		case GameAction.GAMER_ONLINE_DOWN:
			parseGameExit(result);
			break;
		case GameAction.GAME_QIANG_FINISH:
			parseGameQiangfinish(result);
			break;
		default:
			break;
		}
	}
	
	private void parseGameQiangfinish(JSONObject result) {
		JSONObject response = result.optJSONObject("content");
		String gamerId = response.optString("id");
		if (!TextUtils.isEmpty(gamerId)) {
			Intent qiang = new Intent(BroadcastCode.ACTION_QIANG_FINISH);
			qiang.putExtra("gamer_id", gamerId);
			lbm.sendBroadcast(qiang);
		}
	}

	private void parseGameResult(JSONObject result) {
		JSONObject response = result.optJSONObject("content");
		GameResultInfo resultInfo = new GameResultInfo();
		resultInfo.score = response.optString("score");
		resultInfo.win = response.optString("win");
		resultInfo.diceNum = response.optString("diceNum");
		resultInfo.dicePoint = response.optString("dicePoint");
		JSONArray array = response.optJSONArray("diceList");
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = array.optJSONObject(i);
			GamerDiceInfo diceInfo = new GamerDiceInfo();
			diceInfo.dice = obj.optString("dice");
			diceInfo.userId = obj.optString("userId");
			diceInfo.userName = obj.optString("userName");
			resultInfo.diceList.add(diceInfo);
		}
		Intent exit = new Intent(BroadcastCode.ACTION_GAME_RESULT);
		exit.putExtra("result_info", resultInfo);
		lbm.sendBroadcast(exit);
	}

	private void parseOtherGess(JSONObject result) {
		JSONObject response = result.optJSONObject("content");
		GamerGessInfo gessInfo = new GamerGessInfo();
		gessInfo.preDiceNum = response.optString("preDiceNum");
		gessInfo.preDicePoint = response.optString("preDicePoint");
		gessInfo.preUserId = response.optString("preUserId");
		gessInfo.tokenUserId = response.optString("tokenUserId");
		Intent exit = new Intent(BroadcastCode.ACTION_OTHER_GESS);
		exit.putExtra("gess_info", gessInfo);
		lbm.sendBroadcast(exit);
	}

	private void parseStartGess(JSONObject result) {
		JSONObject response = result.optJSONObject("content");
		Intent exit = new Intent(BroadcastCode.ACTION_START_GESS);
		exit.putExtra("tokenUserId", response.optString("tokenUserId"));
		lbm.sendBroadcast(exit);
	}

	private void parseOtherShake(JSONObject result) {
		JSONObject response = result.optJSONObject("content");
		Intent exit = new Intent(BroadcastCode.ACTION_OTHER_SHAKE);
		exit.putExtra("id", response.optString("id"));
		lbm.sendBroadcast(exit);
	}

	private void parseOpenDice(JSONObject result) {
		if (result.has("status")) {
			int status = result.optInt("status");
			if (status == 200) {
				Intent exit = new Intent(BroadcastCode.ACTION_OPEN_DICE);
				lbm.sendBroadcast(exit);
			} else {
				showToast(result.optString("msg"));
				Intent error = new Intent(BroadcastCode.ACTION_REQUST_ERROR);
				lbm.sendBroadcast(error);
			}
		}
	}

	private void parseSelfGess(JSONObject result) {
		if (result.has("status")) {
			int status = result.optInt("status");
			if (status == 200) {
				Intent exit = new Intent(BroadcastCode.ACTION_SELF_GESS_DICE);
				lbm.sendBroadcast(exit);
			} else {
				showToast(result.optString("msg"));
				Intent error = new Intent(BroadcastCode.ACTION_REQUST_ERROR);
				error.putExtra("status", GameAction.GAMER_SELF_GESS_DICE);
				lbm.sendBroadcast(error);
			}
		}
	}

	private void parseSelfShake(JSONObject result) {
		if (result.has("status")) {
			int status = result.optInt("status");
			if (status == 200) {
				Intent exit = new Intent(BroadcastCode.ACTION_SELF_SHAKED);
				lbm.sendBroadcast(exit);
			} else {
				showToast(result.optString("msg"));
				Intent error = new Intent(BroadcastCode.ACTION_REQUST_ERROR);
				lbm.sendBroadcast(error);
			}
		}
	}

	private void parseGameExit(JSONObject result) {
		if (result.has("status")) {
			int status = result.optInt("status");
			if (status == 200) {
				Intent exit = new Intent(BroadcastCode.ACTION_GAME_EXIT);
				lbm.sendBroadcast(exit);
			} else {
				showToast(result.optString("msg"));
				Intent error = new Intent(BroadcastCode.ACTION_REQUST_ERROR);
				lbm.sendBroadcast(error);
			}
		} else {
			JSONObject response = result.optJSONObject("content");
			String gamerId = response.optString("id");
			Intent exitO = new Intent(BroadcastCode.ACTION_GAME_EXIT);
			exitO.putExtra("gamer_id", gamerId);
			lbm.sendBroadcast(exitO);
		}
	}

	private void parseGameStart(JSONObject result) {
		Intent join = new Intent(BroadcastCode.ACTION_GAME_START);
		lbm.sendBroadcast(join);
	}

	private void setForeground() {
		Intent notificationIntent = new Intent(this, MainActivity.class); // 点击该通知后要跳转的Activity
		PendingIntent contentItent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		Notification notf = new Notification(R.drawable.ic_launcher, getString(R.string.app_name),
				System.currentTimeMillis());
		notf.setLatestEventInfo(this, getString(R.string.app_name), getString(R.string.welcome_play), contentItent);
		startForeground(NOTIFICATION_ID, notf);
	}

	private void parseOtherPrepare(JSONObject result) {
		JSONObject obj = result.optJSONObject("content");
		String id = obj.optString("id");
		Intent join = new Intent(BroadcastCode.ACTION_OTHER_PREPARE);
		join.putExtra("gamer_id", id);
		lbm.sendBroadcast(join);  
	}

	private void parseOtherJoin(JSONObject result) {
		JSONObject obj = result.optJSONObject("content");
		GamerInfo gamer = new GamerInfo();
		gamer.id = obj.optString("id");
		gamer.name = obj.optString("newGamerName");
		gamer.photoUrl = obj.optString("photoUrl");
		gamer.sex = obj.optString("sex");
		Intent join = new Intent(BroadcastCode.ACTION_OTHER_JOIN);
		join.putExtra("gamer_info", gamer);
		lbm.sendBroadcast(join);
	}

	private void parseSelfPrepare(JSONObject result) {
		int status = result.optInt("status");
		if (status == 200) {
			lbm.sendBroadcast(new Intent(BroadcastCode.ACTION_SELF_PREPARE));
		} else {
			showToast(result.optString("msg"));
			Intent error = new Intent(BroadcastCode.ACTION_REQUST_ERROR);
			error.putExtra("status", 206);
			lbm.sendBroadcast(error);
		}
	}

	private void parseSelfJoin(JSONObject result) {
		int status = result.optInt("status");
		if (status == 200) {
			if (result.has("re")) {
				JSONObject response = result.optJSONObject("re");
				JSONArray array = response.optJSONArray("gamers");
				ArrayList<GamerInfo> gamers = new ArrayList<GamerInfo>();
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.optJSONObject(i);
					GamerInfo gamer = new GamerInfo();
					gamer.id = obj.optString("id");
					gamer.name = obj.optString("name");
					gamer.sex = obj.optString("sex");
					gamer.photoUrl = obj.optString("photoUrl");
					gamer.status = obj.optString("status");
					gamers.add(gamer);
				}
				if (gamers.size() > 0) {
					Intent join = new Intent(BroadcastCode.ACTION_SELF_JOIN);
					join.putExtra("gamers_info", gamers);
					lbm.sendBroadcast(join);
				}
			}
		} else {
			showToast(result.optString("msg"));
			Intent error = new Intent(BroadcastCode.ACTION_REQUST_ERROR);
			error.putExtra("status", GameAction.ROOM_OUT_USE);
			lbm.sendBroadcast(error);
		}
	}

	private void parseSelfCreate(JSONObject result) {
		int status = result.optInt("status");
		if (status == 200) {
			if (result.has("re")) {
				JSONObject response = result.optJSONObject("re");
				String gamblingId = response.optString("gamblingId");
				String boardNo = response.optString("boardNo");
				Intent create = new Intent(BroadcastCode.ACTION_GAME_CREATE);
				create.putExtra("gamblingId", gamblingId);
				create.putExtra("boardNo", boardNo);
				lbm.sendBroadcast(create);
			}
		} else {
			showToast(result.optString("msg"));
			Intent error = new Intent(BroadcastCode.ACTION_REQUST_ERROR);
			lbm.sendBroadcast(error);
		}
	}

	public void showToast(final String message) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(YylService.this, message, Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}

	};

	private void registerServiceReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BroadcastCode.ACTION_SEND_CMD);
		filter.addAction(BroadcastCode.ACTION_ONOFF_SOUND);
		lbm.registerReceiver(serviceReceiver, filter);
	}

	private void sendCMD(final JSONObject request) {
		if (socketManager == null || !socketManager.isSocketOpen()) {
			if (YylConfig.IS_DEBUG) {
				Log.i("YYL", getString(R.string.socket_not_connect));
			}
			showToast(getString(R.string.diss_connect));
			Intent error = new Intent(BroadcastCode.ACTION_REQUST_ERROR);
			lbm.sendBroadcast(error);
			return;
		}
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				socketManager.sendCMD(request);
			}
		}).start();
	}

	private BroadcastReceiver serviceReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String request = intent.getStringExtra("request_str");
			if (intent.getAction().equals(BroadcastCode.ACTION_SEND_CMD)) {
				JSONObject obj = null;
				try {
					obj = new JSONObject(request);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (!JSONObject.NULL.equals(obj)) {
					sendCMD(obj);
				}
			} else if (intent.getAction().equals(BroadcastCode.ACTION_ONOFF_SOUND)) {
				boolean isSound = DataUtils.getPreferences(DataUtils.KEY_SOUND_ONOFF, true);
				if (isSound) {
					if (!player.isPlaying()) {
						player.start();
					}
				} else {
					if (player.isPlaying()) {
						player.pause();
					}
				}
			}

		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		lbm.unregisterReceiver(serviceReceiver);
		if (socketThread.isAlive()) {
			socketThread.interrupt();
		}
		if (socketManager != null) {
			socketManager.onDestory();
		}
		if(player.isPlaying()){  
            player.stop();  
        } 
		stopForeground(true);
	}

}
