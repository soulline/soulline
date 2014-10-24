package com.cdd.operater;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.cdd.mode.BaseEntry;
import com.cdd.mode.LoginEntry;

public class LoginOperater extends BaseOperater {

	public LoginOperater(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initAction() {
		action = "";
	}
	
	public void setParams(LoginEntry login) {
		params.put("name", login.longinName);
		params.put("password", login.password);
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
		// TODO Auto-generated method stub

	}

	@Override
	public void onParser(JSONArray response) {
		// TODO Auto-generated method stub

	}

	@Override
	public BaseEntry getData() {
		// TODO Auto-generated method stub
		return null;
	}

}
