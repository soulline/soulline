package com.cdd.operater;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.cdd.mode.BaseEntry;

public class MessageReplyOp extends BaseOperater {

	public MessageReplyOp(Context context) {
		super(context);
	}
	
	public void setParams(String to, String msg) {
		params.put("to", to);
		params.put("msg", msg);
	}

	@Override
	public void initAction() {
		action = "member/sendMsg.do";
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
		// TODO Auto-generated method stub

	}

	@Override
	public BaseEntry getData() {
		// TODO Auto-generated method stub
		return null;
	}

}
