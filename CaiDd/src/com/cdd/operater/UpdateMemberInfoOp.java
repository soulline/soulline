package com.cdd.operater;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;

import com.cdd.mode.BaseEntry;
import com.cdd.mode.ModifyMemberEntry;

public class UpdateMemberInfoOp extends BaseOperater {

	public UpdateMemberInfoOp(Context context) {
		super(context);
	}

	@Override
	public void initAction() {
		action = "member/updateMember.do";
	}
	
	public void setParmas(ModifyMemberEntry member) {
		if (!TextUtils.isEmpty(member.name)) {
			params.put("name", member.name);
		}
		if (!TextUtils.isEmpty(member.description)) {
			params.put("description", member.description);
		}
		if (!TextUtils.isEmpty(member.cityId)) {
			params.put("cityId", member.cityId);
		}
		if (!TextUtils.isEmpty(member.sex)) {
			params.put("sex", member.sex);
		}
		if (!TextUtils.isEmpty(member.defaultPhoto)) {
			params.put("defaultPhoto", member.defaultPhoto);
		}
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
