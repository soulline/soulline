package com.cdd.operater;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.cdd.mode.BaseEntry;

public class SqHotAskOp extends BaseOperater {

	public SqHotAskOp(Context context) {
		super(context);
	}

	@Override
	public void initAction() {
		action = "forum/hotSubjectList.do";
	}
	
	public void setParmas(String pageNum) {
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
