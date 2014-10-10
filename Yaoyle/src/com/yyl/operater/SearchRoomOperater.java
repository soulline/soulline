package com.yyl.operater;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.yyl.mode.RoomItemEntry;
import com.yyl.mode.YylEntry;

public class SearchRoomOperater extends BaseOperater {

	private RoomItemEntry roomItem = new RoomItemEntry();

	public SearchRoomOperater(Context context) {
		super(context);
	}

	@Override
	public void initAction() {
		action = "game/searchRoom.do";
	}

	public void setParams(String roomNo) {
		params.put("roomNo", roomNo);
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
		roomItem.boardNo = response.optString("boardNo");
		roomItem.gamerNum = response.optInt("gamerNum");
		roomItem.id = response.optString("id");
		roomItem.maxGamerNum = response.optInt("maxGamerNum");
		roomItem.score = response.optString("score");
		int status = response.optInt("status");
		if (status == 0) {
			roomItem.isStarting = false;
		} else {
			roomItem.isStarting = true;
		}
	}

	@Override
	public void onParser(JSONArray response) {
		// TODO Auto-generated method stub

	}

	@Override
	public YylEntry getData() {
		return roomItem;
	}

}
