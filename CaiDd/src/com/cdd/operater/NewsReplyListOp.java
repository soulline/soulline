package com.cdd.operater;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.cdd.mode.BaseEntry;
import com.cdd.mode.DynamicReplay;

public class NewsReplyListOp extends BaseOperater {

	private ArrayList<DynamicReplay> replyList = new ArrayList<DynamicReplay>();
	
	public NewsReplyListOp(Context context) {
		super(context);
	}
	
	public void setParams(String cofId, String pageNum) {
		params.put("cofId", cofId);
		params.put("pageNum", pageNum);
	}

	@Override
	public void initAction() {
		action = "cof/replyList.do";
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
			DynamicReplay reply = new DynamicReplay();
			reply.cofId = obj.optString("cofId");
			reply.createTime = obj.optString("createTime");
			reply.id = obj.optString("id");
			reply.memberId = obj.optString("memberId");
			reply.memberLevelName = obj.optString("memberLevelName");
			reply.memberName = obj.optString("memberName");
			reply.memberPhoto = obj.optString("memberPhoto");
			reply.memberSex = obj.optString("memberSex");
			reply.message = obj.optString("message");
			replyList.add(reply);
		}

	}
	
	public ArrayList<DynamicReplay> getReplyList() {
		return replyList;
	}

	@Override
	public BaseEntry getData() {
		// TODO Auto-generated method stub
		return null;
	}

}
