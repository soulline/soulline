package com.cdd.operater;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.cdd.mode.BaseEntry;

public class DynamicReplyOp extends BaseOperater {

	public DynamicReplyOp(Context context) {
		super(context);
	}

	@Override
	public void initAction() {
		action = "cof/reply.do";
	}
	
	public void setParams(String cofId, String message) {
		params.put("cofId", cofId);
		params.put("message", message);
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
