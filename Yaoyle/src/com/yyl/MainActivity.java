package com.yyl;

import com.yyl.account.CreatePeopleActivity;
import com.yyl.account.LoginActivity;
import com.yyl.game.RoomListActivity;
import com.yyl.mode.FirstLoginInfo;
import com.yyl.net.RequestListener;
import com.yyl.operater.StartOperater;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setFullScreen();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				startOperaterDo();
			}
		}, 1000);
	}

	private void startOperaterDo() {
		final StartOperater start = new StartOperater(context);
		start.setShowLoading(false);
		start.onRequest(new RequestListener() {

			@Override
			public void onError(Object error) {
				showToast(getString(R.string.init_failed));
				Intent login = new Intent(context, LoginActivity.class);
				startActivity(login);
				finish();
			}

			@Override
			public void onCallBack(Object data) {
				FirstLoginInfo firstLogin = start.isFirstLogin();
				if (!app.getLoginState()) {
					Intent login = new Intent(context, LoginActivity.class);
					login.putExtra("first_login", firstLogin);
					startActivity(login);
				} else if (TextUtils.isEmpty(app.getAccount().name)
						|| app.getAccount().name.equals("null")) {
					Intent createP = new Intent(context,
							CreatePeopleActivity.class);
					createP.putExtra("first_login", firstLogin);
					startActivity(createP);
				} else {
					Intent intent = new Intent(context, RoomListActivity.class);
					intent.putExtra("first_login", firstLogin);
					startActivity(intent);
				}
				finish();
			}
		});
	}

}
