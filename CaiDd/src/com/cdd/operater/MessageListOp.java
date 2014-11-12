package com.cdd.operater;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.cdd.mode.BaseEntry;
import com.cdd.mode.MessageEntry;

public class MessageListOp extends BaseOperater {

	private ArrayList<MessageEntry> messageList = new ArrayList<MessageEntry>();
	
	public MessageListOp(Context context) {
		super(context);
	}

	public void setParams(String pageNum) {
		params.put("pageNum", pageNum);
	}
	
	@Override
	public void initAction() {
		action = "member/letterList.do";
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
		JSONArray array = response.optJSONArray("letterList");
		for (int i=0; i < array.length(); i++) {
			MessageEntry entry = new MessageEntry();
			JSONObject obj = array.optJSONObject(i);
			entry.createTime = obj.optString("createTime");
			entry.fromMemberId = obj.optString("fromMemberId");
			entry.fromMemberName = obj.optString("fromMemberName");
			entry.fromMemberPhoto = obj.optString("fromMemberPhoto");
			entry.id = obj.optString("id");
			entry.isRead = obj.optString("isRead");
			entry.msg = obj.optString("msg");
			entry.replyId = obj.optString("replyId");
			entry.toMemberId = obj.optString("toMemberId");
			messageList.add(entry);
		}

	}

	@Override
	public void onParser(JSONArray response) {
		// TODO Auto-generated method stub

	}

	public ArrayList<MessageEntry> getMessageList() {
		return messageList;
	}
	
	@Override
	public BaseEntry getData() {
		// TODO Auto-generated method stub
		return null;
	}

}
