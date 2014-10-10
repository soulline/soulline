package com.yyl.operater;

import org.json.JSONArray;
import org.json.JSONObject;

import com.yyl.mode.AccountInfo;
import com.yyl.mode.YylEntry;

import android.content.Context;

public class RegisterOperater extends BaseOperater {

	public RegisterOperater(Context context) {
		super(context);
	}

	@Override
	public void initAction() {
		action = "reg.do";
	}
	
	public void setParams(AccountInfo info) {
		params.put("mobile", info.mobile);
		params.put("password", info.password);
	}

	@Override
	public void initRequest() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onParser(JSONObject response) {
		// TODO Auto-generated method stub

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

}
