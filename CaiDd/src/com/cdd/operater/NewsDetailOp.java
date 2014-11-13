package com.cdd.operater;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.cdd.mode.BaseEntry;
import com.cdd.mode.DynamicEntry;
import com.cdd.mode.DynamicReplay;
import com.cdd.mode.PhotosEntry;

public class NewsDetailOp extends BaseOperater {

	private DynamicEntry dynamicEntry = new DynamicEntry();
	
	public NewsDetailOp(Context context) {
		super(context);
	}

	public void setParams(String cofId) {
		params.put("cofId", cofId);
	}
	
	@Override
	public void initAction() {
		action = "cof/getNewsInfo.do";
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
		dynamicEntry.content = response.optString("content");
		dynamicEntry.createTime = response.optString("createTime");
		dynamicEntry.favoriteCount = response.optString("favoriteCount");
		dynamicEntry.id = response.optString("id");
		dynamicEntry.isHot = response.optString("isHot");
		dynamicEntry.likeCount = response.optString("likeCount");
		dynamicEntry.memberId = response.optString("memberId");
		dynamicEntry.memberLevelName = response.optString("memberLevelName");
		dynamicEntry.memberName = response.optString("memberName");
		dynamicEntry.memberPhoto = response.optString("memberPhoto");
		dynamicEntry.memberSex = response.optString("memberSex");
		dynamicEntry.shareCount = response.optString("shareCount");
		dynamicEntry.status = response.optString("status");
		dynamicEntry.type = response.optString("type");
		dynamicEntry.isForward = response.optString("isForward");
		JSONObject objF = response.optJSONObject("forwardNews");
		if (!JSONObject.NULL.equals(objF) && !objF.toString().equals("null")) {
			dynamicEntry.forward.content = objF.optString("content");
			dynamicEntry.forward.createTime = objF.optString("createTime");
			dynamicEntry.forward.id = objF.optString("id");
			dynamicEntry.forward.memberId = objF.optString("memberId");
			dynamicEntry.forward.memberName = objF.optString("memberName");
			dynamicEntry.forward.memberPhoto = objF.optString("memberPhoto");
			dynamicEntry.forward.memberSex = objF.optString("memberSex");
			dynamicEntry.forward.photo = objF.optString("photo");
		}
		JSONArray arrayPhoto = response.optJSONArray("photos");
		for (int j=0; j < arrayPhoto.length(); j++) {
			JSONObject objPhoto = arrayPhoto.optJSONObject(j);
			PhotosEntry photoEntry = new PhotosEntry();
			photoEntry.id = objPhoto.optString("id");
			photoEntry.url = objPhoto.optString("url");
			dynamicEntry.photos.add(photoEntry);
		}
		JSONArray replayArray = response.optJSONArray("replyList");
		for (int k=0; k < replayArray.length(); k++) {
			JSONObject replayObj = replayArray.optJSONObject(k);
			DynamicReplay replay = new DynamicReplay();
			replay.cofId = replayObj.optString("cofId");
			replay.createTime = replayObj.optString("createTime");
			replay.id = replayObj.optString("id");
			replay.memberId = replayObj.optString("memberId");
			replay.memberLevelName = replayObj.optString("memberLevelName");
			replay.memberName = replayObj.optString("memberName");
			replay.memberPhoto = replayObj.optString("memberPhoto");
			replay.memberSex = replayObj.optString("memberSex");
			replay.message = replayObj.optString("message");
			dynamicEntry.replyList.add(replay);
		}
	}

	public DynamicEntry getDynamic() {
		return dynamicEntry;
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
