package com.asag.serial;

import com.asag.serial.base.BaseActivity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

public class Welcome extends BaseActivity {

	private ImageView welcomeLoading;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setFullScreen();
		setContentView(R.layout.start);
		initView();
	}
	
	private void initView() {
		welcomeLoading = (ImageView) findViewById(R.id.welcome_loading);
		welcomeLoading.setImageResource(R.drawable.start_loading);
		final AnimationDrawable animDrawable = (AnimationDrawable) welcomeLoading
				.getDrawable();
		animDrawable.start();
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Intent intent = new Intent(context, MainPageActivity.class);
				startActivity(intent);
				finish();
			}
		}, 29000);
	}

}
