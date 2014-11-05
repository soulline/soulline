package com.asag.serial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class Welcome extends Activity {
	private ProgressBar progressBar;
	private Message msg;
	private Handler handler =new Handler(){
		public void handleMessage(Message msg) {
			progressBar.setProgress(msg.arg1);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.start);
		progressBar = (ProgressBar) findViewById(R.id.bar_welcome);
		progressBar.setVisibility(View.VISIBLE);
		
		//启动子线程去配合handler更新主线程的UI
		new Thread(new Runnable() {
			@Override
			public void run() {
				int i=0;
				while (i<404) {
					msg=handler.obtainMessage();
					msg.arg1=i;
					i+=1;
					handler.sendMessage(msg);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();

		// 延迟40秒后执行run方法中的页面跳转
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(Welcome.this, MainActivity.class);
				startActivity(intent);
				Welcome.this.finish();
			}
		}, 40000);
	}

}
