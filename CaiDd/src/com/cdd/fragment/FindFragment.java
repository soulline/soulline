package com.cdd.fragment;

import com.cdd.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class FindFragment extends Fragment implements OnClickListener{


	private Context context;
	
	private View view;
	
	public FindFragment(Context context) {
		this.context = context;
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_layout:
			
			break;
		case R.id.dingdangquan_layout:
			
			break;
		case R.id.hot_news_layout:
			
			break;
		case R.id.group_layout:
			
			break;

		default:
			break;
		}
		
	}

}
