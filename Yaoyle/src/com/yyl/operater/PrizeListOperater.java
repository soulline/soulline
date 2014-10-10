package com.yyl.operater;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.yyl.mode.PrizeInfo;
import com.yyl.mode.RaffleInfo;
import com.yyl.mode.YylEntry;

public class PrizeListOperater extends BaseOperater {

	private ArrayList<PrizeInfo> prizeList = new ArrayList<PrizeInfo>();
	
	private RaffleInfo raffleInfo = new RaffleInfo();
	
	public PrizeListOperater(Context context) {
		super(context);
	}

	@Override
	public void initAction() {
		action = "prize/list.do";

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
		JSONArray array = response.optJSONArray("prizeList");
		raffleInfo.score = response.optString("score");
		for (int i=0;i < array.length(); i++) {
			JSONObject obj = array.optJSONObject(i);
			PrizeInfo prize = new PrizeInfo();
			prize.id = obj.optString("id");
			prize.name = obj.optString("name");
			prize.photo = obj.optString("photo");
			raffleInfo.prizeList.add(prize);
		}

	}

	@Override
	public void onParser(JSONArray response) {

	}

	@Override
	public YylEntry getData() {
		// TODO Auto-generated method stub
		return raffleInfo;
	}

}
