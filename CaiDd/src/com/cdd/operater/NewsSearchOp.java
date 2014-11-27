package com.cdd.operater;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.cdd.mode.BaseEntry;
import com.cdd.mode.DynamicEntry;
import com.cdd.mode.DynamicReplay;
import com.cdd.mode.PhotosEntry;

public class NewsSearchOp extends BaseOperater {

	private ArrayList<DynamicEntry> dynamicList = new ArrayList<DynamicEntry>();
	
	public NewsSearchOp(Context context) {
		super(context);
	}

	@Override
	public void initAction() {
		action = "cof/search.do";
	}
	
	public void setParams(String pageNum, String keyword) {
		params.put("pageNum", pageNum);
		params.put("keyword", keyword);
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
		JSONArray array = response.optJSONArray("newsList");
		for (int i=0; i < array.length(); i++) {
			JSONObject obj = array.optJSONObject(i);
			DynamicEntry dynamic = new DynamicEntry();
			dynamic.content = obj.optString("content");
			dynamic.createTime = obj.optString("createTime");
			dynamic.favoriteCount = obj.optString("favoriteCount");
			dynamic.id = obj.optString("id");
			dynamic.isHot = obj.optString("isHot");
			dynamic.likeCount = obj.optString("likeCount");
			dynamic.memberId = obj.optString("memberId");
			dynamic.memberLevelName = obj.optString("memberLevelName");
			dynamic.memberName = obj.optString("memberName");
			dynamic.memberPhoto = obj.optString("memberPhoto");
			dynamic.memberSex = obj.optString("memberSex");
			dynamic.shareCount = obj.optString("shareCount");
			dynamic.status = obj.optString("status");
			dynamic.type = obj.optString("type");
			dynamic.isForward = obj.optString("isForward");
			JSONObject objF = obj.optJSONObject("forwardNews");
			if (!JSONObject.NULL.equals(objF) && !objF.toString().equals("null")) {
				dynamic.forward.content = objF.optString("content");
				dynamic.forward.createTime = objF.optString("createTime");
				dynamic.forward.id = objF.optString("id");
				dynamic.forward.memberId = objF.optString("memberId");
				dynamic.forward.memberName = objF.optString("memberName");
				dynamic.forward.memberPhoto = objF.optString("memberPhoto");
				dynamic.forward.memberSex = objF.optString("memberSex");
				dynamic.forward.photo = objF.optString("photo");
			}
			JSONArray arrayPhoto = obj.optJSONArray("photos");
			for (int j=0; j < arrayPhoto.length(); j++) {
				JSONObject objPhoto = arrayPhoto.optJSONObject(j);
				PhotosEntry photoEntry = new PhotosEntry();
				photoEntry.id = objPhoto.optString("id");
				photoEntry.url = objPhoto.optString("url");
				dynamic.photos.add(photoEntry);
			}
			JSONArray replayArray = obj.optJSONArray("replyList");
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
				dynamic.replyList.add(replay);
			}
			dynamicList.add(dynamic);
		}
	}

	@Override
	public void onParser(JSONArray response) {
		// TODO Auto-generated method stub

	}
	
	public ArrayList<DynamicEntry> getDynamicList() {
		return dynamicList;
	}

	@Override
	public BaseEntry getData() {
		// TODO Auto-generated method stub
		return null;
	}

}
