package com.cdd.fragment;

import com.cdd.R;
import com.cdd.alarmpage.AlarmActivity;
import com.cdd.base.BaseActivity;
import com.cdd.minepage.MineInfoModifyActivity;
import com.cdd.minepage.MyCollectActivity;
import com.cdd.minepage.MyForumActivity;
import com.cdd.minepage.SettingActivity;
import com.cdd.mode.MemberInfoEntry;
import com.cdd.net.RequestListener;
import com.cdd.operater.GetMemberInfoOp;
import com.cdd.util.ImageOperater;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MineFragment extends Fragment implements OnClickListener {

	private View view;

	private ImageView selfPortrait;

	private TextView nickName, levelTx, coinTx, simpleText;

	public MineFragment() {
		super();
	}

	public MineFragment(Context context) {

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.mine_fragment, null);
		initView();
		initContent();
		return view;
	}

	private void initMemberInfo(MemberInfoEntry member) {
		if (!TextUtils.isEmpty(member.photo) && !member.photo.equals("null")) {
			String url = member.photo;
			ImageOperater.getInstance(getActivity()).onLoadImage(url,
					selfPortrait);
		}
		if (!TextUtils.isEmpty(member.name) && !member.name.equals("null")) {
			nickName.setText(member.name);
		}
		levelTx.setText(member.levelName);
		coinTx.setText(member.availableScore);
		simpleText.setText(member.description);
	}
	
	public void initContent() {
		loadMemberInfo();
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
				handler.post(new Runnable() {

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
			Intent mineInfo = new Intent(getActivity(),
					MineInfoModifyActivity.class);
			getActivity().startActivity(mineInfo);
			break;
		case R.id.trends_content_layout:

			break;
		case R.id.listen_content_layout:

			break;
		case R.id.fans_content_layout:

			break;
		case R.id.exam_alarm_layout:
			Intent exam = new Intent(getActivity(), AlarmActivity.class);
			getActivity().startActivity(exam);
			break;
		case R.id.my_question_layout:
			Intent question = new Intent(getActivity(), MyForumActivity.class);
			question.putExtra("content_type", 0);
			getActivity().startActivity(question);
			break;
		case R.id.my_answers_layout:
			Intent answer = new Intent(getActivity(), MyForumActivity.class);
			answer.putExtra("content_type", 1);
			getActivity().startActivity(answer);
			break;
		case R.id.my_collect_layout:
			Intent collect = new Intent(getActivity(), MyCollectActivity.class);
			getActivity().startActivity(collect);
			break;
		case R.id.my_letters_layout:

			break;
		case R.id.share_dingdang_layout:

			break;
		case R.id.setting_layout:
			Intent setting = new Intent(getActivity(), SettingActivity.class);
			getActivity().startActivity(setting);
			break;
		default:
			break;
		}

	}

}
