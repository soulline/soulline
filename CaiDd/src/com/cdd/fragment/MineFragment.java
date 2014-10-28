package com.cdd.fragment;

import com.cdd.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class MineFragment extends Fragment implements OnClickListener{

	private Context context;
	
	private View view;
	
	public MineFragment(Context context) {
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.mine_fragment, null);
		initView();
		return view;
	}
	
	
	private void initView() {
		view.findViewById(R.id.self_info_layout).setOnClickListener(this);
		view.findViewById(R.id.trends_content_layout).setOnClickListener(this);
		view.findViewById(R.id.listen_content_layout).setOnClickListener(this);
		view.findViewById(R.id.fans_content_layout).setOnClickListener(this);
		view.findViewById(R.id.exam_alarm_layout).setOnClickListener(this);
		view.findViewById(R.id.my_question_layout).setOnClickListener(this);
		view.findViewById(R.id.my_answers_layout).setOnClickListener(this);
		view.findViewById(R.id.my_collect_layout).setOnClickListener(this);
		view.findViewById(R.id.my_letters_layout).setOnClickListener(this);
		view.findViewById(R.id.share_dingdang_layout).setOnClickListener(this);
		view.findViewById(R.id.setting_layout).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.self_info_layout:
			
			break;
		case R.id.trends_content_layout:
			
			break;
		case R.id.listen_content_layout:
			
			break;
		case R.id.fans_content_layout:
			
			break;
		case R.id.exam_alarm_layout:
			
			break;
		case R.id.my_question_layout:
			
			break;
		case R.id.my_answers_layout:
			
			break;
		case R.id.my_collect_layout:
			
			break;
		case R.id.my_letters_layout:
			
			break;
		case R.id.share_dingdang_layout:
			
			break;
		case R.id.setting_layout:
			
			break;
		default:
			break;
		}
		
	}
	

}
