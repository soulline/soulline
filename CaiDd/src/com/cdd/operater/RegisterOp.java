package com.cdd.operater;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.cdd.mode.BaseEntry;
import com.cdd.mode.RegisterEntry;

public class RegisterOp extends BaseOperater {

	public RegisterOp(Context context) {
		super(context);
	}

	@Override
	public void initAction() {
		action = "member/reg.do";
	}
	
	public void setParams(RegisterEntry register) {
		params.put("loginId", register.loginId);
		params.put("confirmPassword", register.confirmPassword);
		params.put("password", register.password);
		params.put("name", register.name);
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
