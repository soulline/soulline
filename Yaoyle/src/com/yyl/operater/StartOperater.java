package com.yyl.operater;

import org.json.JSONArray;
import org.json.JSONObject;

import com.yyl.mode.FirstLoginInfo;
import com.yyl.mode.YylEntry;
import com.yyl.utils.DataUtils;

import android.content.Context;

public class StartOperater extends BaseOperater {

	private FirstLoginInfo firstLogin = new FirstLoginInfo();

	public StartOperater(Context context) {
		super(context);
	}

	@Override
	public void initAction() {
		action = "start.do";

	}
	
	@Override
	public void initRequest() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onParser(JSONObject response) {
		String uid = response.optString("uid");
		firstLogin.isFirstLogin = response.optString("isFirstLogin");
		firstLogin.firstLoginScore = response.optString("firstLoginScore");
		DataUtils.putPreferences(DataUtils.KEY_UUID, uid);
	}

	@Override
	public void onParser(JSONArray response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initEntity() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public YylEntry getData() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public FirstLoginInfo isFirstLogin() {
		return firstLogin;
	}

}
