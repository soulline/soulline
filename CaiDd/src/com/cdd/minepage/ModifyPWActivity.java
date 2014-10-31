package com.cdd.minepage;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.cdd.R;
import com.cdd.base.BaseActivity;
import com.cdd.mode.ModifyPWEntry;
import com.cdd.net.RequestListener;
import com.cdd.operater.ModifyPWOp;

public class ModifyPWActivity extends BaseActivity implements OnClickListener{

	private EditText oldPwInput, pwInput, ensurepwInput;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.modify_pw_activity);
		initView();
	}
	
	private void initView() {
		oldPwInput = (EditText) findViewById(R.id.old_pw_input);
		pwInput = (EditText) findViewById(R.id.pw_input);
		ensurepwInput = (EditText) findViewById(R.id.ensurepw_input);
		findViewById(R.id.modify_ok).setOnClickListener(this);
		findViewById(R.id.modify_cancel).setOnClickListener(this);
	}
	
	private boolean checkInput() {
		if (TextUtils.isEmpty(oldPwInput.getText().toString().trim())) {
			showToast("请输入旧密码");
			return false;
		}
		if (TextUtils.isEmpty(pwInput.getText().toString().trim())) {
			showToast("请输入新密码");
			return false;
		}
		if (TextUtils.isEmpty(ensurepwInput.getText().toString().trim())) {
			showToast("请再次输入新密码");
			return false;
		}
		if (!pwInput.getText().toString().trim().equals(ensurepwInput.getText().toString().trim())) {
			showToast("两次输入密码不一致，请重新输入");
			return false;
		}
		return true;
	}

	private void doModifyPw(ModifyPWEntry modify) {
		ModifyPWOp modifyOp = new ModifyPWOp(context);
		modifyOp.setParams(modify);
		modifyOp.onRequest(new RequestListener() {
			
			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCallBack(Object data) {
				showToast("修改密码成功");
				finish();
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.modify_ok:
			if (checkInput()) {
				ModifyPWEntry entry = new ModifyPWEntry();
				entry.oldPassword = oldPwInput.getText().toString().trim();
				entry.password = pwInput.getText().toString().trim();
				entry.confirmPassword = ensurepwInput.getText().toString().trim();
				doModifyPw(entry);
			}
			break;
			
		case R.id.modify_cancel:
			finish();
			break;

		default:
			break;
		}
		
	}

}
