package com.yyl.operater;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.yyl.mode.YylEntry;

public class ModifyPWOperater extends BaseOperater {

	public ModifyPWOperater(Context context) {
		super(context);
	}

	public void setParams(String oldPassword, String newPassword) {
		params.put("oldPassword", oldPassword);
		params.put("newPassword", newPassword);
	}
	
	@Override
	public void initAction() {
		action = "modifyPassword.do";
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
	public YylEntry getData() {
		// TODO Auto-generated method stub
		return null;
	}

}
