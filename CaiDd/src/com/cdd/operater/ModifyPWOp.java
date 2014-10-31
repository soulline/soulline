package com.cdd.operater;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.cdd.mode.BaseEntry;
import com.cdd.mode.ModifyPWEntry;

public class ModifyPWOp extends BaseOperater {

	public ModifyPWOp(Context context) {
		super(context);
	}

	@Override
	public void initAction() {
		action = "member/modifyPassword.do";
	}

	public void setParams(ModifyPWEntry modify) {
		params.put("oldPassword", modify.oldPassword);
		params.put("password", modify.password);
		params.put("confirmPassword", modify.confirmPassword);
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
