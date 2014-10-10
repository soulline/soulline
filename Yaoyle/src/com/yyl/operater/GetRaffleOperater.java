package com.yyl.operater;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.yyl.mode.GetPrize;
import com.yyl.mode.YylEntry;

public class GetRaffleOperater extends BaseOperater {

	public GetRaffleOperater(Context context) {
		super(context);
	}

	@Override
	public void initAction() {
		action = "prize/updateReceivePrizeInfo.do";
	}
	
	public void setParams(GetPrize prize) {
		params.put("receiveKey", prize.receiveKey);
		params.put("mobile", prize.mobile);
		params.put("address", prize.address);
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
	public YylEntry getData() {
		// TODO Auto-generated method stub
		return null;
	}

}
