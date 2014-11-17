package com.cdd.operater;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.cdd.mode.BaseEntry;
import com.cdd.mode.FansEntry;

public class FansListOp extends BaseOperater {

	private ArrayList<FansEntry> fansList = new ArrayList<FansEntry>();
	
	public FansListOp(Context context) {
		super(context);
	}

	@Override
	public void initAction() {
		action = "member/getFansList.do";
	}
	
	public void setParams(String pageNum) {
		params.put("pageNum", pageNum);
	}

	public void setMemberId(String memberId) {
		params.put("memberId", memberId);
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
		for (int i=0; i < response.length(); i++) {
			JSONObject obj = response.optJSONObject(i);
			FansEntry entry = new FansEntry();
			entry.availableScore = obj.optString("availableScore");
			entry.cityId = obj.optString("cityId");
			entry.cityName = obj.optString("cityName");
			entry.description = obj.optString("description");
			entry.dynamicInfoCount = obj.optString("dynamicInfoCount");
			entry.email = obj.optString("email");
			entry.fansCount = obj.optString("fansCount");
			entry.id = obj.optString("id");
			entry.idolCount = obj.optString("idolCount");
			entry.isSignIn = obj.optString("isSignIn");
			entry.levelId = obj.optString("levelId");
			entry.levelName = obj.optString("levelName");
			entry.mobile = obj.optString("mobile");
			entry.name = obj.optString("name");
			entry.photo = obj.optString("photo");
			entry.sex = obj.optString("sex");
			entry.relation = obj.optString("relation");
			entry.status = obj.optString("status");
			fansList.add(entry);
		}

	}
	
	public ArrayList<FansEntry> getFansList() {
		return fansList;
	}

	@Override
	public BaseEntry getData() {
		// TODO Auto-generated method stub
		return null;
	}

}
