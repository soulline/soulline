package com.yyl.operater;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.yyl.mode.VersionEntry;
import com.yyl.mode.YylEntry;

public class CheckVersionOperater extends BaseOperater {

	private VersionEntry version = new VersionEntry();
	
	public CheckVersionOperater(Context context) {
		super(context);
	}

	public void setParams(String cid) {
		params.put("cid", cid);
	}

	@Override
	public void initAction() {
		action = "checkVersion.do";
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
		version.upgrade = response.optString("upgrade");
		version.ver = response.optString("ver");
	}

	@Override
	public void onParser(JSONArray response) {
		// TODO Auto-generated method stub

	}

	@Override
	public YylEntry getData() {
		return version;
	}

}
