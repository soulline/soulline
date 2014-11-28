package com.cdd.operater;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.cdd.mode.BaseEntry;
import com.cdd.mode.VersionEntry;

public class CheckVersionOp extends BaseOperater {

	private VersionEntry version = new VersionEntry();
	
	public CheckVersionOp(Context context) {
		super(context);
	}

	@Override
	public void initAction() {
		action = "base/checkVersion.do";

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
		version.address = response.optString("address");
		version.msg = response.optString("msg");
		version.upgrade = response.optString("upgrade");
		version.ver = response.optString("ver");
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
