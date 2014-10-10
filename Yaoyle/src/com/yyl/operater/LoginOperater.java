package com.yyl.operater;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.yyl.mode.AccountInfo;
import com.yyl.mode.LoginEntry;
import com.yyl.mode.YylEntry;
import com.yyl.utils.DataUtils;

public class LoginOperater extends BaseOperater {

	public LoginOperater(Context context) {
		super(context);
	}

	@Override
	public void initAction() {
		action = "login.do";
	}

	public void setParams(LoginEntry login) {
		params.put("mobile", login.mobile);
		params.put("password", login.password);
	}
	
	@Override
	public void initRequest() {

	}

	@Override
	public void initEntity() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onParser(JSONObject response) {
		app.clearAccountInfo();
		AccountInfo account = new AccountInfo();
		account.mobile = response.optString("mobile");
		account.name = response.optString("name");
		account.sex = response.optString("sex");
		account.scoreAmount = response.optString("scoreAmount");
		account.accountId = response.optString("id");
		account.photoUrl = response.optString("photoUrl");
		app.setAccount(account);
	}

	@Override
	public void onParser(JSONArray response) {
		// TODO Auto-generated method stub

	}

	@Override
	public YylEntry getData() {
		// TODO Auto-generated method stub
		return null;
	}

}
