package com.cdd.operater;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.cdd.mode.BaseEntry;
import com.cdd.mode.PhotosEntry;
import com.cdd.mode.SqAskDetailEntry;

public class SqAskDetailOp extends BaseOperater {

	private SqAskDetailEntry askDetail = new SqAskDetailEntry();
	
	public SqAskDetailOp(Context context) {
		super(context);
	}

	@Override
	public void initAction() {
		action = "forum/subjectInfo.do";
	}
	
	public void setParams(String subjectId) {
		params.put("subjectId", subjectId);
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
		askDetail.anonymous = response.optString("anonymous");
		askDetail.content = response.optString("content");
		askDetail.createTime = response.optString("createTime");
		askDetail.favoriteCount = response.optString("favoriteCount");
		askDetail.id = response.optString("id");
		askDetail.itemId = response.optString("itemId");
		askDetail.likeCount = response.optString("likeCount");
		askDetail.memberId = response.optString("memberId");
		askDetail.memberName = response.optString("memberName");
		askDetail.memberPhoto = response.optString("memberPhoto");
		askDetail.shareCount = response.optString("shareCount");
		askDetail.subItemId = response.optString("subItemId");
		askDetail.title = response.optString("title");
		JSONArray array = response.optJSONArray("photos");
		for (int i=0; i < array.length(); i++) {
			JSONObject obj = array.optJSONObject(i);
			PhotosEntry entry = new PhotosEntry();
			entry.id = obj.optString("id");
			entry.url = obj.optString("url");
			askDetail.photos.add(entry);
		}
	}

	@Override
	public void onParser(JSONArray response) {
		// TODO Auto-generated method stub

	}
	
	public SqAskDetailEntry getAskDetail() {
		return askDetail;
	}

	@Override
	public BaseEntry getData() {
		// TODO Auto-generated method stub
		return null;
	}

}
