package com.yyl.operater;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.yyl.mode.LotteryInfo;
import com.yyl.mode.YylEntry;

public class PrizeLotteryOperater extends BaseOperater {

	private LotteryInfo lottery = new LotteryInfo();
	
	public PrizeLotteryOperater(Context context) {
		super(context);
	}
	
	@Override
	public void initAction() {
		action = "prize/lottery.do";

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
		lottery.id = response.optString("id");
		lottery.isorder = response.optString("isorder");
		lottery.name = response.optString("name");
		lottery.photo = response.optString("photo");
		lottery.receiveKey = response.optString("receiveKey");
		lottery.type = response.optString("type");

	}

	@Override
	public void onParser(JSONArray response) {
		// TODO Auto-generated method stub

	}

	@Override
	public YylEntry getData() {
		return lottery;
	}

}
