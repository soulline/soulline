package com.cdd.operater;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.cdd.mode.BaseEntry;
import com.cdd.mode.DynamicCommitEntry;

public class DynamicCommitOp extends BaseOperater {

	public DynamicCommitOp(Context context) {
		super(context);
	}

	@Override
	public void initAction() {
		action = "cof/addNews.do";
	}
	
	public void setParmas(DynamicCommitEntry entry) {
		extra_params.put("photos", entry.photos);
		extra_params.put("content", entry.content);
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
