package com.yyl.account;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.yyl.BaseActivity;
import com.yyl.R;
import com.yyl.net.RequestListener;
import com.yyl.operater.ModifyPWOperater;

public class ModifyPWActivity extends BaseActivity implements OnClickListener{

	private EditText oldPwInput, newPwInput, newPwInputSure;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_password_activity);
		setFullScreen();
		initView();
	}
	
	private void initView() {
		oldPwInput = (EditText) findViewById(R.id.old_pw_input);
		newPwInput = (EditText) findViewById(R.id.new_pw_input);
		newPwInputSure = (EditText) findViewById(R.id.new_pw_input_sure);
		findViewById(R.id.modify_btn).setOnClickListener(this);
	}

	private void modifyPW() {
		if (!checkInput()) return;
		if (checkSamePassword()) {
			ModifyPWOperater mdpwo = new ModifyPWOperater(context);
			mdpwo.setParams(oldPwInput.getText().toString().trim(), newPwInput.getText().toString().trim());
			mdpwo.onRequest(new RequestListener() {
				
				@Override
				public void onError(Object error) {
					
				}
				
				@Override
				public void onCallBack(Object data) {
					showToast(getString(R.string.modify_pw_success));
					finish();
				}
			});
		} else {
			showToast(getString(R.string.pw_not_same));
		}
	}

	private boolean checkInput() {
		if (TextUtils.isEmpty(oldPwInput.getText().toString().trim())) {
			showToast(getString(R.string.please_input_old_pw));
			return false;
		}
		if (TextUtils.isEmpty(newPwInput.getText().toString().trim())) {
			showToast(getString(R.string.please_input_new_pw));
			return false;
		}
		if (TextUtils.isEmpty(newPwInputSure.getText().toString().trim())) {
			showToast(getString(R.string.please_input_new_pw_agin));
			return false;
		}
		return true;
	}
	
	private boolean checkSamePassword() {
		if (newPwInput.getText().toString().trim().equals
				(newPwInputSure.getText().toString().trim())) {
			return true;
		}
		return false;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.modify_btn:
			modifyPW();
			break;

		default:
			break;
		}
		
	}

}
