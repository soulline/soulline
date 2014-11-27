package com.cdd.fragment;

import com.cdd.R;
import com.cdd.activity.findpage.DynamicListActivity;
import com.cdd.activity.findpage.DynamicSearchActivity;
import com.cdd.activity.findpage.HotNewsListActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class FindFragment extends Fragment implements OnClickListener{


	
	private View view;
	
	public FindFragment() {
		super();
	}
	
	public FindFragment(Context context) {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.find_fragment, null);
		initView();
		return view;
	}
	
	private void initView() {
		view.findViewById(R.id.search_layout).setOnClickListener(this);
		view.findViewById(R.id.dingdangquan_layout).setOnClickListener(this);
		view.findViewById(R.id.hot_news_layout).setOnClickListener(this);
		view.findViewById(R.id.group_layout).setOnClickListener(this);
	}
	
	public void initContent() {
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_layout:
			Intent intent = new Intent(getActivity(), DynamicSearchActivity.class);
			startActivity(intent);
			break;
		case R.id.dingdangquan_layout:
			Intent dingdangquan = new Intent(getActivity(), DynamicListActivity.class);
			getActivity().startActivity(dingdangquan);
			break;
		case R.id.hot_news_layout:
			Intent hotNews = new Intent(getActivity(), HotNewsListActivity.class);
			getActivity().startActivity(hotNews);
			break;
		case R.id.group_layout:
			
			break;

		default:
			break;
		}
		
	}

}
