package com.yyl.account;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.yyl.BaseActivity;
import com.yyl.R;
import com.yyl.mode.AccountInfo;
import com.yyl.net.RequestListener;
import com.yyl.operater.RegisterOperater;
import com.yyl.utils.PhoneNumUtil;
import com.yyl.web.WebViewActivity;

public class RegisterActivity extends BaseActivity implements OnClickListener{

	private EditText loginPhoneInput;
	private EditText loginPasswordInput;
	private EditText loginPasswordInputSure;
	
	private TextView readCheck;
	
	private boolean isCheck = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setFullScreen();
		setContentView(R.layout.register_activity);
		initView();
	}

	private void initView() {
		loginPhoneInput = (EditText) findViewById(R.id.login_phone_input);
		loginPasswordInput = (EditText) findViewById(R.id.login_password_input);
		loginPasswordInputSure = (EditText) findViewById(R.id.login_password_input_sure);
		readCheck = (TextView) findViewById(R.id.read_check);
		readCheck.setOnClickListener(this);
		findViewById(R.id.register_enter_btn).setOnClickListener(this);
		findViewById(R.id.read_protocol).setOnClickListener(this);
	}

	private void showCheck() {
		Drawable drawable = null;
		if (isCheck) {
			isCheck = false;
			drawable = getResources().getDrawable(
					R.drawable.password_remenber_no);
		} else {
			isCheck = true;
			drawable = getResources().getDrawable(
					R.drawable.password_remenber_yes);
		}
		if (drawable != null) {
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			readCheck.setCompoundDrawables(drawable, null, null, null);
		}
	
	}

	private void registerDo() {
		if (!checkInput()) return;
		if (checkSamePassword()) {
			RegisterOperater register = new RegisterOperater(context);
			final AccountInfo account = new AccountInfo();
			account.mobile = loginPhoneInput.getText().toString().trim();
			account.password = loginPasswordInput.getText().toString().trim();
			register.setParams(account);
			register.onRequest(new RequestListener() {
				
				@Override
				public void onError(Object error) {
					
				}
				
				@Override
				public void onCallBack(Object data) {
					showToast(getString(R.string.register_success));
					Intent intent = new Intent();
					intent.putExtra("login_account", account.mobile);
					intent.putExtra("login_password", account.password);
					setResult(RESULT_OK, intent);
					finish();
				}
			});
		} else {
			showToast(getString(R.string.twice_not_same));
		}
	}

	private boolean checkInput() {
		if (TextUtils.isEmpty(loginPhoneInput.getText().toString().trim())) {
			showToast(getString(R.string.please_input_phone));
			return false;
		}
		if (!PhoneNumUtil.isPhoneNum(loginPhoneInput.getText().toString().trim())) {
			showToast(getString(R.string.please_input_correct_phone));
			return false;
		}
		if (TextUtils.isEmpty(loginPasswordInput.getText().toString().trim())) {
			showToast(getString(R.string.please_input_pw));
			return false;
		}
		if (TextUtils.isEmpty(loginPasswordInputSure.getText().toString().trim())) {
			showToast(getString(R.string.please_input_pw_agin));
			return false;
		}
		if (!isCheck) {
			showToast(getString(R.string.protocol_check_note));
			return false;
		}
		return true;
	}

	private boolean checkSamePassword() {
		if (loginPasswordInput.getText().toString().trim().equals
				(loginPasswordInputSure.getText().toString().trim())) {
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_enter_btn:
			registerDo();
			break;

		case R.id.read_protocol:
			Intent intent = new Intent(context, WebViewActivity.class);
			intent.putExtra("web_url", "http://www.yedianshaiwang.com/html5/memberRule.html");
			startActivity(intent);
			break;
			
		case R.id.read_check:
			showCheck();
			break;
		default:
			break;
		}
		
	}
}
