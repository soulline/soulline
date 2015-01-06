package com.asag.serial.base;


import com.asag.serial.R;
import com.asag.serial.app.SerialApp;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BaseActivity extends FragmentActivity {

	public Context context;
	
	public SerialApp app = SerialApp.getInstance();
	
	
	public FragmentManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		manager = getSupportFragmentManager();
	}

	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
		
	};
	
	public void setFullScreen() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	public void showToast(final String text) {
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
			}
		});
	}
	

	public void showTipDialog(final String msg) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				if (isFinishing()) {
					return;
				}
				Builder b = new Builder(context);
				b.setMessage(msg);
				b.setPositiveButton("确定",
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int arg1) {
								dialog.dismiss();
							}
						});
				b.show();
			}
		});
	}
	
	/*public void showLoading(final boolean show) {

		final FrameLayout fl = (FrameLayout) findViewById(R.id.base_loading);
		if (fl != null) {
			handler.post(new Runnable() {

				@Override
				public void run() {
					if (isFinishing()) {
						return;
					}
					fl.removeAllViews();
					if (show) {
						View load = LayoutInflater.from(context).inflate(
								R.layout.base_loading_layout, null);
						fl.addView(load);
					}
				}
			});

		}

	}*/
	
	public void closeFragment(final String tag) {
		if (this.isFinishing()) {
			return;
		}
		FragmentTransaction ft = manager.beginTransaction();
		ft.setCustomAnimations(R.anim.bottom_in, R.anim.bottom_out);
		Fragment prev = manager.findFragmentByTag(tag);
		if (prev != null) {
			ft.remove(prev).commitAllowingStateLoss();
		}
		if (manager.getBackStackEntryCount() > 0) {
			manager.popBackStackImmediate();
		}
	}

	public void showFragment(String tag, int layoutId, Fragment fragment) {
		if (this.isFinishing()) {
			return;
		}
		if (fragment == null || TextUtils.isEmpty(tag)) {
			return;
		}
		FragmentTransaction ft = manager.beginTransaction();
		ft.setCustomAnimations(R.anim.bottom_in, R.anim.bottom_out);
		Fragment prev = manager.findFragmentByTag(tag);
		if (prev != null) {
			ft.remove(prev);
		}
//		ft.addToBackStack(null);
		ft.replace(layoutId <= 0 ? R.id.base_extra_layout : layoutId, fragment,
				tag).commitAllowingStateLoss();
	}

	public void showNetworkError() {
		showToast("网络连接失败");
	}
}
