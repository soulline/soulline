package com.cdd.operater;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.cdd.mode.BaseEntry;
import com.cdd.util.DataUtils;

public class StartOp extends BaseOperater {

	public StartOp(Context context) {
		super(context);
	}

	@Override
	public void initAction() {
		action = "base/start.do";
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
		String uid = response.optString("uid");
		DataUtils.putPreferences(DataUtils.KEY_UUID, uid);
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
