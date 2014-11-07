package com.cdd.operater;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.cdd.mode.BaseEntry;
import com.cdd.mode.SqAnswerCommitEntry;

public class SqAnswerCommitOp extends BaseOperater {

	public SqAnswerCommitOp(Context context) {
		super(context);
	}
	
	public void setParmas(SqAnswerCommitEntry entry) {
		extra_params.put("photos", entry.photos);
		extra_params.put("content", entry.content);
		extra_params.put("subjectId", entry.subjectId);
		extra_params.put("anonymous", entry.anonymous);
	}

	@Override
	public void initAction() {
		action = "forum/addAnswer.do";
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
