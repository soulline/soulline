package com.yyl.operater;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.yyl.mode.ShareEntry;
import com.yyl.mode.YylEntry;

public class ShareOperater extends BaseOperater {

	private ShareEntry shareEntry = new ShareEntry();
	
	public ShareOperater(Context context) {
		super(context);
	}

	@Override
	public void initAction() {
		action = "share.do";
	}

	public void setParams(String cid) {
		params.put("cid", cid);
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
		shareEntry.msg = response.optString("msg");
		shareEntry.picUrl = response.optString("picUrl");
		shareEntry.url = response.optString("url");
	}

	@Override
	public void onParser(JSONArray response) {
		// TODO Auto-generated method stub

	}
	
	public ShareEntry getShareEntry() {
		return shareEntry;
	}

	@Override
	public YylEntry getData() {
		// TODO Auto-generated method stub
		return null;
	}

}
