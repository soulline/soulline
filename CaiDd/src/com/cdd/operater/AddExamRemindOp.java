package com.cdd.operater;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.cdd.mode.AddExamRemindEntry;
import com.cdd.mode.BaseEntry;

public class AddExamRemindOp extends BaseOperater {

	public AddExamRemindOp(Context context) {
		super(context);
	}

	@Override
	public void initAction() {
		action = "remind/addExamRemind.do";
	}
	
	public void setParams(AddExamRemindEntry entry) {
		params.put("cityId", entry.cityId);
		params.put("itemId", entry.itemId);
		params.put("type", entry.type);
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
	public BaseEntry getData() {
		// TODO Auto-generated method stub
		return null;
	}

}
