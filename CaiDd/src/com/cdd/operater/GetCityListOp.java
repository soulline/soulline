package com.cdd.operater;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.cdd.mode.BaseEntry;
import com.cdd.mode.CityItemEntry;

public class GetCityListOp extends BaseOperater {

	private ArrayList<CityItemEntry> cityList = new ArrayList<CityItemEntry>();
	
	public GetCityListOp(Context context) {
		super(context);
	}

	@Override
	public void initAction() {
		action = "base/getCityList.do";
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
		for (int i=0; i < response.length(); i++) {
			JSONObject obj = response.optJSONObject(i);
			CityItemEntry entry = new CityItemEntry();
			entry.enName = obj.optString("enName");
			entry.id = obj.optString("id");
			entry.name = obj.optString("name");
			entry.provinceId = obj.optString("provinceId");
			cityList.add(entry);
		}

	}
	
	public ArrayList<CityItemEntry> getCityList() {
		return cityList;
	}

	@Override
	public BaseEntry getData() {
		// TODO Auto-generated method stub
		return null;
	}

}
