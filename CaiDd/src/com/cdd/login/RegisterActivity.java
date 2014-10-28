package com.cdd.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.cdd.R;
import com.cdd.base.BaseActivity;
import com.cdd.util.CddConfig;

public class RegisterActivity extends BaseActivity implements OnClickListener{

	private EditText accountInput, nickNameInput, pwInput, ensurepwInput;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.register_activity);
		initView();
	}

	private void initView() {
		accountInput = (EditText) findViewById(R.id.account_input);
		pwInput = (EditText) findViewById(R.id.pw_input);
		nickNameInput = (EditText) findViewById(R.id.nickname_input);
		ensurepwInput = (EditText) findViewById(R.id.ensurepw_input);
		findViewById(R.id.regist_btn).setOnClickListener(this);
		findViewById(R.id.turn_login_layout).setOnClickListener(this);
	}

	private boolean checkInput() {
		if (TextUtils.isEmpty(accountInput.getText().toString().trim())) {
			showToast("请输入账号");
			return false;
		}
		if (TextUtils.isEmpty(nickNameInput.getText().toString().trim())) {
			showToast("请输入昵称");
			return false;
		}
		if (TextUtils.isEmpty(pwInput.getText().toString().trim())) {
			showToast("请输入密码");
			return false;
		}
		if (TextUtils.isEmpty(ensurepwInput.getText().toString().trim())) {
			showToast("请再次输入密码");
			return false;
		}
		return true;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.regist_btn:
			if (checkInput()) {
				
			}
			break;
		case R.id.turn_login_layout:
			app.popClosePath(true, CddConfig.LOGIN_PATH_KEY);
			Intent login = new Intent(context, LoginActivity.class);
			startActivity(login);
			finish();
			break;

		default:
			break;
		}
		
	}
}
