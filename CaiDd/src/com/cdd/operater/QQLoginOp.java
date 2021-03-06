package com.cdd.operater;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.cdd.mode.AccountInfo;
import com.cdd.mode.BaseEntry;
import com.cdd.mode.QQLoginEntry;

public class QQLoginOp extends BaseOperater {

	private AccountInfo account = new AccountInfo();
	
	public QQLoginOp(Context context) {
		super(context);
	}

	@Override
	public void initAction() {
		action = "member/loginByQQ.do";
	}
	
	public void setParams(QQLoginEntry entry) {
		params.put("deviceFlag", entry.deviceFlag);
		params.put("gender", entry.gender);
		params.put("nickname", entry.nickname);
		params.put("openId", entry.openId);
		params.put("photo", entry.photo);
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
		account.availableScore = response.optString("availableScore");
		account.description = response.optString("description");
		account.deviceFlag = response.optString("deviceFlag");
		account.isAdmin = response.optString("isAdmin");
		account.levelId = response.optString("levelId");
		account.loginId = response.optString("loginId");
		account.name = response.optString("name");
		account.scoreCeiling = response.optString("scoreCeiling");
		account.sex = response.optString("sex");
		account.id = response.optString("id");
		account.signTime = response.optString("signTime");
		account.status = response.optString("status");
	}

	@Override
	public void onParser(JSONArray response) {
		// TODO Auto-generated method stub

	}
	
	public AccountInfo getAccount() {
		return account;
	}

	@Override
	public BaseEntry getData() {
		// TODO Auto-generated method stub
		return null;
	}

}
