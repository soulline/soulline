package com.yyl.operater;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.yyl.mode.WxInfo;
import com.yyl.mode.YylEntry;

public class PayOperater extends BaseOperater {

	private WxInfo wxInfo = new WxInfo();
	
	public PayOperater(Context context) {
		super(context);
	}

	public void setParams(String value) {
		params.put("fee", value);
	}
	
	@Override
	public void initAction() {
		action = "recharge/createOrder.do";
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
		wxInfo.noncestr = response.optString("noncestr");
		wxInfo.prepayid = response.optString("prepayid");
		wxInfo.sign = response.optString("sign");
		wxInfo.timestamp = response.optString("timestamp");
	}

	@Override
	public void onParser(JSONArray response) {
		// TODO Auto-generated method stub

	}

	public WxInfo getWxInfo() {
		return wxInfo;
	}
	
	@Override
	public YylEntry getData() {
		// TODO Auto-generated method stub
		return null;
	}

}
