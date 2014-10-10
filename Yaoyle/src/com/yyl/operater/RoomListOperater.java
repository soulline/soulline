package com.yyl.operater;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.yyl.mode.RoomItemEntry;
import com.yyl.mode.RoomListInfo;
import com.yyl.mode.YylEntry;

public class RoomListOperater extends BaseOperater {

	private RoomListInfo roomList = new RoomListInfo();

	public RoomListOperater(Context context) {
		super(context);
	}

	@Override
	public void initAction() {
		action = "game/roomList.do";
	}

	public void setParmas(String pageNum) {
		params.put("pageNum", pageNum);
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
		roomList.pageNum = response.optInt("pageNum");
		roomList.pageSize = response.optInt("pageSize");
		roomList.totalCount = response.optInt("totalCount");
		roomList.totalPage = response.optInt("totalPage");
		JSONArray array = response.optJSONArray("roomList");
		if (!JSONObject.NULL.equals(array)) {
			for (int i=0; i < array.length(); i++) {
				JSONObject obj = array.optJSONObject(i);
				RoomItemEntry item = new RoomItemEntry();
				item.boardNo = obj.optString("boardNo");
				item.gamerNum = obj.optInt("gamerNum");
				item.id = obj.optString("id");
				item.maxGamerNum = obj.optInt("maxGamerNum");
				item.score = obj.optString("score");
				int status = obj.optInt("status");
				if (status == 0) {
					item.isStarting = false;
				} else {
					item.isStarting = true;
				}
				roomList.roomList.add(item);
			}
		}
	}

	@Override
	public void onParser(JSONArray response) {
		
	}

	@Override
	public YylEntry getData() {
		return roomList;
	}

}
