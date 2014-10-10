package com.yyl.account;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.yyl.BaseActivity;
import com.yyl.R;
import com.yyl.game.RoomListActivity;
import com.yyl.mode.FirstLoginInfo;
import com.yyl.mode.LoginEntry;
import com.yyl.net.RequestListener;
import com.yyl.operater.LoginOperater;
import com.yyl.operater.StartOperater;
import com.yyl.utils.DataUtils;

public class LoginActivity extends BaseActivity implements OnClickListener {

	private TextView remenberPassword;

	private EditText loginPhoneInput;
	private EditText loginPasswordInput;

	private boolean isRemenberPw = true;

	private static final String LOGIN_ACCOUNT = "login_account";
	private static final String LOGIN_PASSWORD = "login_password";

	private int LENGTH_INPUT_BEFORE = 0;
	private int LENGTH_INPUT_AFTER = 0;

	private static final int REGISTER_REQUEST = 10323;

	private boolean isRebuild = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		setFullScreen();
		initView();
		initAccountText();
		isRebuild = getIntent().getBooleanExtra("rebuild", false);
		if (isRebuild) {
			checkFirstLogin();
		}
	}
	
	private void checkFirstLogin() {
		final StartOperater startOperater = new StartOperater(context);
		startOperater.onRequest(new RequestListener() {
			
			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCallBack(Object data) {
				FirstLoginInfo firstLogin = startOperater.isFirstLogin();
				if (!isRebuild) {
					if (firstLogin != null && firstLogin.isFirstLogin.equals("1")) {
						showNTipDialog(getString(R.string.first_login_note).replaceAll("%", firstLogin.firstLoginScore));
					} else {
						turnLoginDo();
					}
				} else {
					finish();
				}
			}
		});
	}

	public void showNTipDialog(final String msg) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				if (isFinishing()) {
					return;
				}
				Builder b = new Builder(context);
				b.setMessage(msg);
				b.setPositiveButton(R.string.action_sure,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int arg1) {
								dialog.dismiss();
								if(isRebuild) {
									finish();
								} else {
									turnLoginDo();
								}
							} 
						});
				b.show();
			}
		});
	}
	
	private void initAccountText() {
		String account = DataUtils.getPreferences(LOGIN_ACCOUNT, "");
		String password = DataUtils.getPreferences(LOGIN_PASSWORD, "");
		if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password)) {
			loginPhoneInput.setText(account);
			loginPasswordInput.setText(password);
		}
	}

	private TextWatcher accountWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			LENGTH_INPUT_BEFORE = loginPhoneInput.getText().toString().trim()
					.length();
		}

		@Override
		public void afterTextChanged(Editable s) {
			LENGTH_INPUT_AFTER = loginPhoneInput.getText().toString().trim()
					.length();
			if (LENGTH_INPUT_BEFORE == 11 && LENGTH_INPUT_AFTER < 11) {
				loginPasswordInput.setText("");
			}
			if (LENGTH_INPUT_AFTER == 11) {
				String password = DataUtils.getPreferences(loginPhoneInput
						.getText().toString().trim(), "");
				if (!TextUtils.isEmpty(password)) {
					loginPasswordInput.setText(password);
				}
			}
		}
	};

	private boolean checkInput() {
		if (TextUtils.isEmpty(loginPhoneInput.getText().toString().trim())) {
			showToast(getString(R.string.please_input_phone));
			return false;
		}
		if (TextUtils.isEmpty(loginPasswordInput.getText().toString().trim())) {
			showToast(getString(R.string.please_input_pw));
			return false;
		}
		return true;
	}

	private void initView() {
		findViewById(R.id.register_account).setOnClickListener(this);
		loginPhoneInput = (EditText) findViewById(R.id.login_phone_input);
		loginPhoneInput.addTextChangedListener(accountWatcher);
		loginPasswordInput = (EditText) findViewById(R.id.login_password_input);
		remenberPassword = (TextView) findViewById(R.id.remenber_password);
		remenberPassword.setOnClickListener(this);
		findViewById(R.id.login_enter_btn).setOnClickListener(this);
	}

	private void showRemenberView() {
		Drawable drawable = null;
		if (isRemenberPw) {
			isRemenberPw = false;
			drawable = getResources().getDrawable(
					R.drawable.password_remenber_no);
		} else {
			isRemenberPw = true;
			drawable = getResources().getDrawable(
					R.drawable.password_remenber_yes);
		}
		if (drawable != null) {
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			remenberPassword.setCompoundDrawables(drawable, null, null, null);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REGISTER_REQUEST && resultCode == RESULT_OK) {
			String phone = data.getStringExtra(LOGIN_ACCOUNT);
			String password = data.getStringExtra(LOGIN_PASSWORD);
			if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password)) {
				loginPhoneInput.setText(phone);
				loginPasswordInput.setText(password);
			}
		}
	}

	private void loginIn() {
		if (!checkInput())
			return;
		app.isRebuild = false;
		LoginOperater login = new LoginOperater(context);
		LoginEntry entry = new LoginEntry();
		entry.mobile = loginPhoneInput.getText().toString().trim();
		entry.password = loginPasswordInput.getText().toString().trim();
		login.setParams(entry);
		login.onRequest(new RequestListener() {

			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCallBack(Object data) {
				showToast(getString(R.string.login_success));
				if (isRemenberPw) {
					DataUtils.putPreferences(LOGIN_ACCOUNT, loginPhoneInput
							.getText().toString().trim());
					DataUtils.putPreferences(LOGIN_PASSWORD, loginPasswordInput
							.getText().toString().trim());
					DataUtils.putPreferences(loginPhoneInput.getText()
							.toString().trim(), loginPasswordInput.getText()
							.toString().trim());
				} else {
					DataUtils.putPreferences(LOGIN_ACCOUNT, "");
					DataUtils.putPreferences(LOGIN_PASSWORD, "");
					DataUtils.putPreferences(loginPhoneInput.getText()
							.toString().trim(), "");
				}
				checkFirstLogin();
			}
		});
	}

	private void turnLoginDo() {
		if (!isRebuild) {
			if (TextUtils.isEmpty(app.getAccount().name)
					|| app.getAccount().name.equals("null")) {
				Intent createP = new Intent(context, CreatePeopleActivity.class);
				startActivity(createP);
			} else {
				Intent intent = new Intent(context, RoomListActivity.class);
				startActivity(intent);
			}
			finish();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_account:
			Intent register = new Intent(context, RegisterActivity.class);
			startActivityForResult(register, REGISTER_REQUEST);
			break;
		case R.id.remenber_password:
			showRemenberView();
			break;
		case R.id.login_enter_btn:
			loginIn();
			break;

		default:
			break;
		}

	}

}
