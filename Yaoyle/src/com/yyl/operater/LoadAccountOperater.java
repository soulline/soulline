package com.yyl.operater;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.yyl.mode.AccountInfo;
import com.yyl.mode.YylEntry;

public class LoadAccountOperater extends BaseOperater {

	private AccountInfo accountInfo = new AccountInfo();

	public LoadAccountOperater(Context context) {
		super(context);
	}

	@Override
	public void initAction() {
		action = "getMemberInfo.do";
	}

	@Override
	public void initRequest() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initEntity() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onParser(JSONObject response) {
		accountInfo.mobile = response.optString("mobile");
		accountInfo.name = response.optString("name");
		accountInfo.photoUrl = response.optString("photoUrl");
		accountInfo.scoreAmount = response.optString("scoreAmount");
		accountInfo.sex = response.optString("sex");
		accountInfo.accountId = response.optString("id");
		if (!TextUtils.isEmpty(accountInfo.mobile)) {
			app.setAccount(accountInfo);
		}
	}

	@Override
	public void onParser(JSONArray response) {
		// TODO Auto-generated method stub

	}

	@Override
	public YylEntry getData() {
		return accountInfo;
	}

}
