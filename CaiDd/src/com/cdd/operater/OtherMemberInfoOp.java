package com.cdd.operater;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.cdd.mode.BaseEntry;
import com.cdd.mode.MemberInfoEntry;

public class OtherMemberInfoOp extends BaseOperater {

	private MemberInfoEntry memberInfo = new MemberInfoEntry();
	
	public OtherMemberInfoOp(Context context) {
		super(context);
	}

	public void setParams(String memberId) {
		params.put("memberId", memberId);
	}
	
	@Override
	public void initAction() {
		action = "member/memberInfo.do";
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
		memberInfo.availableScore = response.optString("availableScore");
		memberInfo.cityId = response.optString("cityId");
		memberInfo.cityName = response.optString("cityName");
		memberInfo.description = response.optString("description");
		memberInfo.email = response.optString("email");
		memberInfo.id = response.optString("id");
		memberInfo.levelId = response.optString("levelId");
		memberInfo.levelName = response.optString("levelName");
		memberInfo.isSignIn = response.optString("isSignIn");
		memberInfo.mobile = response.optString("mobile");
		memberInfo.name = response.optString("name");
		memberInfo.photo = response.optString("photo");
		memberInfo.scoreCeiling = response.optString("scoreCeiling");
		memberInfo.sex = response.optString("sex");
		memberInfo.fansCount = response.optString("fansCount");
		memberInfo.dynamicInfoCount = response.optString("dynamicInfoCount");
		memberInfo.idolCount = response.optString("idolCount");
		memberInfo.status = response.optString("status");
	}

	@Override
	public void onParser(JSONArray response) {
		// TODO Auto-generated method stub

	}
	
	public MemberInfoEntry getMemberInfo() {
		return memberInfo;
	}

	@Override
	public BaseEntry getData() {
		// TODO Auto-generated method stub
		return null;
	}

}
