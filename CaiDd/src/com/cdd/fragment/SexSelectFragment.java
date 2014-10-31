package com.cdd.fragment;

import com.cdd.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class SexSelectFragment extends DialogFragment implements OnClickListener{

	private View view;
	
	private BaseFragmentListener listener;
	
	public SexSelectFragment() {
		super();
	}
	
	public void addFragmentListener(BaseFragmentListener listener) {
		this.listener = listener;
	}
	
	public SexSelectFragment(Context context, Bundle b) {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.sex_select_fragment, null);
		initView();
		return view;
	}
	
	private void initView() {
		view.findViewById(R.id.sex_bg_layout).setOnClickListener(this);
		view.findViewById(R.id.sex_man).setOnClickListener(this);
		view.findViewById(R.id.sex_woman).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sex_man:
			if (listener != null) {
				listener.onCallBack("1");
			}
			dismissAllowingStateLoss();
			break;
			
		case R.id.sex_woman:
			if (listener != null) {
				listener.onCallBack("2");
			}
			dismissAllowingStateLoss();
			break;
			
		case R.id.sex_bg_layout:
			dismissAllowingStateLoss();
			break;

		default:
			break;
		}
		
	}
	
	
}
