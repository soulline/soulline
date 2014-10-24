package com.cdd.login;

import com.cdd.R;
import com.cdd.base.BaseActivity;
import com.cdd.mode.LoginEntry;
import com.cdd.net.RequestListener;
import com.cdd.operater.LoginOperater;

import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends BaseActivity {

	private TextView txtv1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		txtv1.setText(android.os.Build.MODEL);
	}

	private void doLogin() {
		LoginEntry login = new LoginEntry();
		login.longinName = "";
		login.password = "";
		LoginOperater loginOp = new LoginOperater(context);
		loginOp.setParams(login);
		loginOp.onRequest(new RequestListener() {
			
			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCallBack(Object data) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

}
