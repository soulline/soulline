package com.cdd.operater;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.cdd.mode.BaseEntry;

public class SqAnswerZanOp extends BaseOperater {

	public SqAnswerZanOp(Context context) {
		super(context);
	}

	public void setParmas(String answerId) {
		params.put("answerId", answerId);
	}
	
	@Override
	public void initAction() {
		action = "forum/likeAnswer.do";
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
