package com.cdd.operater;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.cdd.mode.BaseEntry;
import com.cdd.mode.ForumItem;

public class ExamItemOperater extends BaseOperater {

	private ArrayList<ForumItem> itemList = new ArrayList<ForumItem>();
	
	public ExamItemOperater(Context context) {
		super(context);
	}

	@Override
	public void initAction() {
		action = "base/examItemList.do";
	}

	@Override
	public void initRequest() {

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
		for (int i=0; i < response.length(); i++) {
			JSONObject obj = response.optJSONObject(i);
			ForumItem entry = new ForumItem();
			entry.fatherId = "9";
			entry.id = obj.optString("id");
			entry.name = obj.optString("name");
			itemList.add(entry);
		}
	}
	
	public ArrayList<ForumItem> getForumList() {
		return itemList;
	}

	@Override
	public BaseEntry getData() {
		// TODO Auto-generated method stub
		return null;
	}

}
