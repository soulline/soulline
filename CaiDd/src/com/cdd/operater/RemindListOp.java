package com.cdd.operater;


import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.cdd.mode.BaseEntry;
import com.cdd.mode.NoticeEntry;
import com.cdd.mode.RemindEntry;
import com.cdd.mode.RemindInfo;

public class RemindListOp extends BaseOperater {

	private RemindInfo remindInfo = new RemindInfo();
	
	public RemindListOp(Context context) {
		super(context);
	}

	@Override
	public void initAction() {
		action = "remind/getRemindList.do";
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
		JSONArray noticeArray = response.optJSONArray("notice");
		for (int i=0; i < noticeArray.length(); i++) {
			JSONObject nObj = noticeArray.optJSONObject(i);
			NoticeEntry noticeEntry = new NoticeEntry();
			noticeEntry.createTime = nObj.optString("createTime");
			noticeEntry.id = nObj.optString("id");
			noticeEntry.message = nObj.optString("message");
			noticeEntry.type = nObj.optString("type");
			remindInfo.noticeList.add(noticeEntry);
		}
		JSONArray array = response.optJSONArray("list");
		for (int i=0; i < array.length(); i++) {
			JSONObject obj = array.optJSONObject(i);
			RemindEntry entry = new RemindEntry();
			entry.cityId = obj.optString("cityId");
			entry.createTime = obj.optString("createTime");
			entry.des = obj.optString("des");
			entry.id = obj.optString("id");
			entry.itemId = obj.optString("itemId");
			entry.memberId = obj.optString("memberId");
			entry.remindTime = obj.optString("remindTime");
			entry.title = obj.optString("title");
			entry.type = obj.optString("type");
			remindInfo.remindList.add(entry);
		}

	}

	@Override
	public void onParser(JSONArray response) {
		// TODO Auto-generated method stub

	}

	public RemindInfo getRemind() {
		return remindInfo;
	}
	
	@Override
	public BaseEntry getData() {
		// TODO Auto-generated method stub
		return null;
	}

}
