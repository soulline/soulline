package com.example.alarm;

import com.example.testbutton.app.SerialApp;
import com.example.testbutton.mode.AlarmInfo;
import com.example.testbutton.utils.SerialBroadCode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

public class JcAlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(SerialBroadCode.ACTION_ALARM_CHECK)) {
			Log.d("SERIAL", "received ----- ");
			if (SerialApp.getInstance().isCheckIng) {
				Toast.makeText(context, "检测正在进行中，定时器此次触发无效", Toast.LENGTH_SHORT).show();
			} else {
				AlarmInfo alarm = (AlarmInfo) intent.getSerializableExtra("alarm_info");
				LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(context);
				Intent data = new Intent(SerialBroadCode.ACTION_ALARM_CHECK_STARTING);
				if (alarm != null) {
					data.putExtra("alarm_info", alarm);
				}
				lbm.sendBroadcast(data);
				Toast.makeText(context, "定时器开始检测", Toast.LENGTH_SHORT).show();
			}
		}
	}

}
