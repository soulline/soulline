package com.yyl.operater;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.yyl.mode.AccountInfo;
import com.yyl.mode.YylEntry;

public class ModifyGamerInfoOperater extends BaseOperater {

	public ModifyGamerInfoOperater(Context context) {
		super(context);
	}

	@Override
	public void initAction() {
		action = "updateMember.do";
	}

	public void setParams(AccountInfo account) {
		params.put("name", account.name);
		params.put("sex", account.sex);
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
