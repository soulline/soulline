package com.cdd.fragment;

import com.cdd.R;
import com.cdd.base.BaseActivity;
import com.cdd.minepage.MineInfoModifyActivity;
import com.cdd.mode.MemberInfoEntry;
import com.cdd.net.RequestListener;
import com.cdd.operater.GetMemberInfoOp;
import com.cdd.util.ImageOperater;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MineFragment extends Fragment implements OnClickListener{

	
	private View view;
	
	private ImageView selfPortrait;
	
	private TextView nickName, levelTx, coinTx, simpleText;
	
	public MineFragment() {
		super();
	}
	
	public MineFragment(Context context) {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.mine_fragment, null);
		initView();
		if (getActivity() instanceof BaseActivity) {
			((BaseActivity) getActivity()).handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					loadMemberInfo();
				}
			}, 800);
		}
		return view;
	}
	
	private void initMemberInfo(MemberInfoEntry member) {
		if (!TextUtils.isEmpty(member.photo) && !member.photo.equals("null")) {
			String url = member.photo + "&" + System.currentTimeMillis();
			ImageOperater.getInstance(getActivity()).onLoadImage(url, selfPortrait);
		}
		if (!TextUtils.isEmpty(member.name)
				&& !member.name.equals("null")) {
			nickName.setText(member.name);
		}
		levelTx.setText(member.levelName);
		coinTx.setText(member.availableScore);
		simpleText.setText(member.description);
	}
	
	private void loadMemberInfo() {
		final GetMemberInfoOp memberOp = new GetMemberInfoOp(getActivity());
		memberOp.onRequest(new RequestListener() {

			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCallBack(Object data) {
				final MemberInfoEntry memberInfo = memberOp.getMemberInfo();
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						initMemberInfo(memberInfo);
						
					}
				});
			}
		});
	}
	
	private void initView() {
		selfPortrait = (ImageView) view.findViewById(R.id.self_portrait);
		nickName = (TextView) view.findViewById(R.id.nick_name);
		levelTx = (TextView) view.findViewById(R.id.level_tx);
		coinTx = (TextView) view.findViewById(R.id.coin_tx);
		simpleText = (TextView) view.findViewById(R.id.simple_text);
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
			Intent mineInfo = new Intent(getActivity(), MineInfoModifyActivity.class);
			getActivity().startActivity(mineInfo);
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
