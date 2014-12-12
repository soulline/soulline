package com.asag.serial.alarm;

import com.asag.serial.app.SerialApp;
import com.asag.serial.mode.AlarmInfo;
import com.asag.serial.utils.SerialBroadCode;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class JcAlarm {

	private static AlarmInfo alarmInfo = new AlarmInfo();
	
	public static void setSendAlarm(AlarmInfo alarm) {
		AlarmManager manager = (AlarmManager) SerialApp.getInstance().getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(SerialApp.getInstance(), JcAlarmReceiver.class);
		intent.setAction(SerialBroadCode.ACTION_ALARM_CHECK);
		intent.putExtra("alarm_info", alarm);
		PendingIntent sender = PendingIntent.getBroadcast(SerialApp.getInstance(), 0, intent, 0);
		long interval = alarm.minuteN * 60 * 1000;
		manager.set(AlarmManager.RTC, alarm.firstTimeN, sender);
//		manager.setRepeating(AlarmManager.RTC_WAKEUP, alarm.firstTimeN, interval, sender);
	}
	
	public static void cancelSendAlarm() {
		AlarmManager manager = (AlarmManager) SerialApp.getInstance().getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(SerialApp.getInstance(), JcAlarmReceiver.class);
		intent.setAction(SerialBroadCode.ACTION_ALARM_CHECK);
		intent.putExtra("alarm_info", alarmInfo);
		PendingIntent sender = PendingIntent.getBroadcast(SerialApp.getInstance(), 0, intent, 0);
		manager.cancel(sender);
	}
}
