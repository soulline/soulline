package com.cdd.operater;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.cdd.mode.BaseEntry;
import com.cdd.mode.SqAnswerItem;
import com.cdd.mode.SqAskItem;
import com.cdd.mode.SqAskListRequest;

public class SqAskListOp extends BaseOperater {

	private ArrayList<SqAskItem> askList = new ArrayList<SqAskItem>();
	
	public SqAskListOp(Context context) {
		super(context);
	}
	
	public void setParams(SqAskListRequest request) {
		params.put("itemId", request.itemId);
		params.put("subItemId", request.subItemId);
		params.put("pageNum", request.pageNum);
	}

	@Override
	public void initAction() {
		action = "forum/subjectList.do";
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
		JSONArray array = response.optJSONArray("subjectList");
		if (array != null && !TextUtils.isEmpty(array.toString())
				&& !array.toString().equals("null")) {
			for (int i=0; i < array.length(); i++) {
				JSONObject obj = array.optJSONObject(i);
				SqAskItem askItem = new SqAskItem();
				askItem.anonymous = obj.optString("anonymous");
				askItem.content = obj.optString("content");
				askItem.createTime = obj.optString("createTime");
				askItem.favoriteCount = obj.optString("favoriteCount");
				askItem.id = obj.optString("id");
				askItem.itemId = obj.optString("itemId");
				askItem.likeCount = obj.optString("likeCount");
				askItem.memberId = obj.optString("memberId");
				askItem.memberName = obj.optString("memberName");
				askItem.memberPhoto = obj.optString("memberPhoto");
				askItem.shareCount = obj.optString("shareCount");
				askItem.subItemId = obj.optString("subItemId");
				askItem.title = obj.optString("title");
				JSONArray arrayN = obj.optJSONArray("answerList");
				if (arrayN != null && !TextUtils.isEmpty(arrayN.toString())
						&& !arrayN.toString().equals("null")) {
					for (int j=0; j < arrayN.length(); j++) {
						JSONObject objN = new JSONObject();
						SqAnswerItem answerItem = new SqAnswerItem();
						answerItem.anonymous = objN.optString("anonymous");
						answerItem.content = objN.optString("content");
						answerItem.createTime = objN.optString("createTime");
						answerItem.id = objN.optString("id");
						answerItem.likeCount = objN.optString("likeCount");
						answerItem.memberId = objN.optString("memberId");
						answerItem.memberName = objN.optString("memberName");
						answerItem.memberPhoto = objN.optString("memberPhoto");
						answerItem.subjectId = objN.optString("subjectId");
						askItem.answerList.add(answerItem);
					}
				}
				askList.add(askItem);
			}
		}

	}
	
	public ArrayList<SqAskItem> getAskList() {
		return askList;
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
