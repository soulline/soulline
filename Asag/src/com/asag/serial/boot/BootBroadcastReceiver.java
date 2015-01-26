package com.asag.serial.boot;

import com.asag.serial.MainPageActivity;
import com.asag.serial.alarm.JcAlarm;
import com.asag.serial.app.SerialApp;
import com.asag.serial.mode.AlarmInfo;
import com.asag.serial.utils.DataUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class BootBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            Intent ootStartIntent=new Intent(context, MainPageActivity.class);
            ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(ootStartIntent);
            AlarmInfo alarmInfo = new AlarmInfo();
            alarmInfo.firstTimeN = DataUtils.getPreferences("first_alarm_time", 0L);
			alarmInfo.minuteN = DataUtils.getPreferences("interval_time", 0);
			SerialApp.getInstance().alarmInfo = alarmInfo;
			Log.d("zhao", "boot firstTime : " + alarmInfo.firstTimeN + "   -- minuteN : " + alarmInfo.minuteN);
			if (alarmInfo.firstTimeN > 0 && alarmInfo.minuteN > 0) {
				Log.d("zhao", "boot setAlarm...");
				setAlarmCheck(alarmInfo);
			}
        }
	}
	
	private void setAlarmCheck(final AlarmInfo alarm) {
		if (alarm.firstTimeN < System.currentTimeMillis()) {
			alarm.firstTimeN = System.currentTimeMillis() + alarm.minuteN * 60
					* 1000L;
		}
		if (SerialApp.getInstance().isAreadyAlarm) {
			JcAlarm.cancelSendAlarm();
			SerialApp.getInstance().isAreadyAlarm = false;
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					JcAlarm.setSendAlarm(alarm); 
					SerialApp.getInstance().isAreadyAlarm = true;
				}
			}, 500);
		} else {
			JcAlarm.setSendAlarm(alarm);
			SerialApp.getInstance().isAreadyAlarm = true;
		}
	}

}
