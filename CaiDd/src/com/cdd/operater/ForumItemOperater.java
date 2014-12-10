package com.cdd.operater;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.cdd.mode.BaseEntry;
import com.cdd.mode.ForumItem;
import com.cdd.mode.SubForumItem;
import com.cdd.util.DataUtils;

public class ForumItemOperater extends BaseOperater {

	private ArrayList<ForumItem> itemList = new ArrayList<ForumItem>();
	
	public ForumItemOperater(Context context) {
		super(context);
	}

	@Override
	public void initAction() {
		action = "base/forumItemList.do";
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
		itemList.clear();
		for (int i=0; i < response.length(); i++) {
			JSONObject obj = response.optJSONObject(i);
			ForumItem entry = new ForumItem();
			entry.id = obj.optString("id");
			entry.name = obj.optString("name");
			JSONArray array = obj.optJSONArray("subItems");
			if (array != null && !array.equals(JSONObject.NULL) && !array.toString().equals("null")) {
				for (int j=0; j < array.length(); j++) {
					JSONObject objN = array.optJSONObject(j);
					SubForumItem item = new SubForumItem();
					item.fatherId = entry.id;
					item.id = objN.optString("id");
					item.name = objN.optString("name");
					entry.subItems.add(item);
				}
			}
			itemList.add(entry);
		}
		if (response.length() > 0) {
			DataUtils.putPreferences(DataUtils.KEY_SQMENU_LIST, response.toString());
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
