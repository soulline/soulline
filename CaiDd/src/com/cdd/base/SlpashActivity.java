package com.cdd.base;

import android.content.Intent;
import android.os.Bundle;

import com.cdd.R;
import com.cdd.base.BaseActivity;
import com.cdd.net.RequestListener;
import com.cdd.operater.StartOp;

public class SlpashActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setFullScreen();
		setContentView(R.layout.slpash_activity);
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				doStart();
			}
		}, 1500);
	}
	
	private void doStart() {
		StartOp startOp = new StartOp(context);
		startOp.onRequest(new RequestListener() {
			
			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCallBack(Object data) {
				Intent intent = new Intent(context, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	

}
