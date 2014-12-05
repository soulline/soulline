package com.cdd.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.cdd.R;
import com.cdd.activity.login.QQLoginManger.OnQQLoginListener;
import com.cdd.app.BaseActivityCloseListener;
import com.cdd.base.BaseActivity;
import com.cdd.mode.AccountInfo;
import com.cdd.mode.LoginEntry;
import com.cdd.mode.QQLoginEntry;
import com.cdd.net.RequestListener;
import com.cdd.operater.LoginOperater;
import com.cdd.operater.QQLoginOp;
import com.cdd.util.CddConfig;

public class LoginActivity extends BaseActivity implements OnClickListener {

	private EditText accountInput, pwInput;
	
	private boolean isRebuild = false;
	
	private QQLoginManger qqLoginManager = null;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.login_activity);
		initView();
		isRebuild = getIntent().getBooleanExtra("rebuild", false);
		app.putClosePath(CddConfig.LOGIN_PATH_KEY, new BaseActivityCloseListener() {
			
			@Override
			public void onFinish() {
				finish();
			}
		});
		qqLoginManager = new QQLoginManger(this);
		qqLoginManager.initQQ();
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
	
	private void doLogin(LoginEntry login) {
		app.isRebuild = false;
		final LoginOperater loginOp = new LoginOperater(context);
		loginOp.setParams(login);
		loginOp.onRequest(new RequestListener() {
			
			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCallBack(Object data) {
				AccountInfo account = loginOp.getAccount();
				app.setAccount(account);
				showToast("登录成功");
				setResult(RESULT_OK);
				app.popClosePath(true, CddConfig.LOGIN_PATH_KEY);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (qqLoginManager != null) {
			qqLoginManager.loginOutQQ();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (qqLoginManager != null) {
			qqLoginManager.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	private void doQQLogin(QQLoginEntry entry) {
		app.isRebuild = false;
		final QQLoginOp loginOp = new QQLoginOp(context);
		loginOp.setParams(entry);
		loginOp.onRequest(new RequestListener() {
			
			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCallBack(Object data) {
				qqLoginManager.loginOutQQ();
				AccountInfo account = loginOp.getAccount();
				app.setAccount(account);
				showToast("登录成功");
				setResult(RESULT_OK);
				app.popClosePath(true, CddConfig.LOGIN_PATH_KEY);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_qq:
			showLoading(true);
			qqLoginManager.initTencentShare();
			qqLoginManager.addOnQQLoginListener(new OnQQLoginListener() {
				
				@Override
				public void onSuccess() {
					showLoading(false);
					QQLoginEntry entry = qqLoginManager.getQQEntry();
					doQQLogin(entry);
				}
				
				@Override
				public void onError() {
					showLoading(false);
					showToast("登录失败");
				}
			});
			if (qqLoginManager.readyQQ()) {
				qqLoginManager.getUserInfo();
			} else {
				qqLoginManager.doLoginQQ();
			}
			break;
		case R.id.login_btn:
			if (checkInput()) {
				LoginEntry login = new LoginEntry();
				login.loginId = accountInput.getText().toString().trim();
				login.password = pwInput.getText().toString().trim();
				login.deviceFlag = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE))
						.getDeviceId();
				doLogin(login);
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
