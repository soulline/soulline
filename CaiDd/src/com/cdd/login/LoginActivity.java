package com.cdd.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.cdd.R;
import com.cdd.app.BaseActivityCloseListener;
import com.cdd.base.BaseActivity;
import com.cdd.util.CddConfig;

public class LoginActivity extends BaseActivity implements OnClickListener {

	private EditText accountInput, pwInput;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.login_activity);
		initView();
		app.putClosePath(CddConfig.LOGIN_PATH_KEY, new BaseActivityCloseListener() {
			
			@Override
			public void onFinish() {
				finish();
			}
		});
	}

	private void initView() {
		accountInput = (EditText) findViewById(R.id.account_input);
		pwInput = (EditText) findViewById(R.id.pw_input);
		findViewById(R.id.login_qq).setOnClickListener(this);
		findViewById(R.id.login_btn).setOnClickListener(this);
		findViewById(R.id.regist_btn).setOnClickListener(this);
	}

	private boolean checkInput() {
		if (TextUtils.isEmpty(accountInput.getText().toString().trim())) {
			showToast("请输入账号");
			return false;
		}
		if (TextUtils.isEmpty(pwInput.getText().toString().trim())) {
			showToast("请输入密码");
			return false;
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_qq:

			break;
		case R.id.login_btn:
			if (checkInput()) {

			}
			break;
		case R.id.regist_btn:
			Intent regist = new Intent(context, RegisterActivity.class);
			startActivity(regist);
			break;

		default:
			break;
		}

	}

}
