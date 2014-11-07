package com.cdd.operater;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.cdd.mode.BaseEntry;
import com.cdd.mode.PhotosEntry;
import com.cdd.mode.SqAnswerDetailEntry;

public class SqAnswerDetailOp extends BaseOperater {

	private ArrayList<SqAnswerDetailEntry> answerList = new ArrayList<SqAnswerDetailEntry>();

	public SqAnswerDetailOp(Context context) {
		super(context);
	}

	@Override
	public void initAction() {
		action = "forum/answerList.do";
	}

	public void setParmas(String subjectId, String pageNum) {
		params.put("subjectId", subjectId);
		params.put("pageNum", pageNum);
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
		JSONArray array = response.optJSONArray("answerList");
		if (array != null || (array != null)
				&& !array.toString().equals("null")) {
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.optJSONObject(i);
				SqAnswerDetailEntry answerDetail = new SqAnswerDetailEntry();
				answerDetail.anonymous = obj.optString("anonymous");
				answerDetail.content = obj.optString("content");
				answerDetail.createTime = obj.optString("createTime");
				answerDetail.id = obj.optString("id");
				answerDetail.likeCount = obj.optString("likeCount");
				answerDetail.memberId = obj.optString("memberId");
				answerDetail.memberName = obj.optString("memberName");
				answerDetail.memberPhoto = obj.optString("memberPhoto");
				answerDetail.memberSex = obj.optString("memberSex");
				answerDetail.memberLevelName = obj.optString("memberLevelName");
				answerDetail.subjectId = obj.optString("subjectId");
				JSONArray photoArray = obj.optJSONArray("photos");
				for (int j = 0; j < photoArray.length(); j++) {
					JSONObject objN = photoArray.optJSONObject(j);
					PhotosEntry entry = new PhotosEntry();
					entry.id = objN.optString("id");
					entry.url = objN.optString("url");
					answerDetail.photos.add(entry);
				}
				answerList.add(answerDetail);
			}
		}

	}

	@Override
	public void onParser(JSONArray response) {
		// TODO Auto-generated method stub

	}

	public ArrayList<SqAnswerDetailEntry> getAnswerList() {
		return answerList;
	}

	@Override
	public BaseEntry getData() {
		// TODO Auto-generated method stub
		return null;
	}

}
