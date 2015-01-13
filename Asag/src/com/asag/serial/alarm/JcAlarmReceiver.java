package com.asag.serial.alarm;

import com.asag.serial.app.SerialApp;
import com.asag.serial.mode.AlarmInfo;
import com.asag.serial.utils.SerialBroadCode;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

public class JcAlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(SerialBroadCode.ACTION_ALARM_CHECK)) {
			Log.d("SERIAL", "received ----- ");
			/*
			 * if (SerialApp.getInstance().isCheckIng) { Toast.makeText(context,
			 * "检测正在进行中，定时器此次触发无效", Toast.LENGTH_SHORT) .show(); } else {
			 */
			AlarmInfo alarm = (AlarmInfo) intent
					.getSerializableExtra("alarm_info");
			LocalBroadcastManager lbm = LocalBroadcastManager
					.getInstance(context);
			Intent data = new Intent(
					SerialBroadCode.ACTION_ALARM_CHECK_STARTING);
			if (alarm != null) {
				data.putExtra("alarm_info", alarm);
			}
			unlock(context);
			lbm.sendBroadcast(data);
			Toast.makeText(context, "定时器开始检测", Toast.LENGTH_SHORT).show();
		}
		// }
	}

	private void unlock(Context context) {
		// 声明键盘管理器
		KeyguardManager mKeyguardManager = null;
		// 声明键盘锁
		KeyguardLock mKeyguardLock = null;
		// 声明电源管理器
		PowerManager pm;
		PowerManager.WakeLock wakeLock;
		// 获取电源的服务
		pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		// 获取系统服务
		mKeyguardManager = (KeyguardManager) context
				.getSystemService(Context.KEYGUARD_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
				| PowerManager.SCREEN_DIM_WAKE_LOCK, "serial wake");
		wakeLock.acquire();
		Log.i("Log : ", "------>mKeyguardLock");
		// 初始化键盘锁，可以锁定或解开键盘锁
		mKeyguardLock = mKeyguardManager.newKeyguardLock("");
		// 禁用显示键盘锁定
		mKeyguardLock.disableKeyguard();
		wakeLock.release();
	}

}
