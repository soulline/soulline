package com.cdd.activity.findpage;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdd.R;
import com.cdd.base.BaseActivity;
import com.cdd.mode.MemberInfoEntry;
import com.cdd.net.RequestListener;
import com.cdd.operater.OtherMemberInfoOp;
import com.cdd.util.ImageOperater;

public class UserInfoActivity extends BaseActivity implements OnClickListener{

	private String memberId = "";
	
	private MemberInfoEntry memberInfo = new MemberInfoEntry();
	
	private ImageView userPortrait;
	
	private TextView userName, userLevel, userCoin, listenCount, fansCount;
	
	private TextView nickName, sexType, localCity, simpleTx, levelValue, dingdangbiValue;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.user_info_activity);
		initTitle("个人详情");
		initView();
		initContent();
	}
	
	private void initContent() {
		memberId = getIntent().getStringExtra("memberId");
		setChecked(0);
		requestMemberInfo();
	}
	
	private void requestMemberInfo() {
		final OtherMemberInfoOp otherOp = new OtherMemberInfoOp(context);
		otherOp.setParams(memberId);
		otherOp.onRequest(new RequestListener() {
			
			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCallBack(Object data) {
				memberInfo = otherOp.getMemberInfo();
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						loadMemberInfo(memberInfo);
					}
				});
			}
		});
	}
	
	private void initSex(String sex) {
		if (sex.equals("1")) {
			sexType.setText("男");
		} else if (sex.equals("2")) {
			sexType.setText("女");
		}
	}
	
	private void loadMemberInfo(MemberInfoEntry member) {

		if (member.sex.equals("1")) {
			userPortrait.setImageResource(R.drawable.default_man_portrait);
		} else if (member.sex.equals("2")) {
			userPortrait.setImageResource(R.drawable.default_woman_portrait);
		} else {
			userPortrait.setImageResource(R.drawable.default_woman_portrait);
		}
		if (!TextUtils.isEmpty(member.photo) && !member.photo.equals("null")) {
			String ulr = member.photo;
			ImageOperater.getInstance(context).onLoadImage(ulr,
					userPortrait);
		}
		if (!TextUtils.isEmpty(member.name)
				&& !member.name.equals("null")) {
			nickName.setText(member.name);
			userName.setText(member.name);
		}
		sexType.setText("女");
		initSex(member.sex);
		if (!TextUtils.isEmpty(member.cityName)
				&& !member.cityName.equals("null")) {
			localCity.setText(member.cityName);
		}
		if (!TextUtils.isEmpty(member.description)
				&& !member.description.equals("null")) {
			simpleTx.setText(member.description);
		}
		if (!TextUtils.isEmpty(member.levelName)
				&& !member.levelName.equals("null")) {
			levelValue.setText(member.levelName);
			userLevel.setText(member.levelName);
		}
		if (!TextUtils.isEmpty(member.availableScore)
				&& !member.availableScore.equals("null")) {
			dingdangbiValue.setText(member.availableScore);
			userCoin.setText(member.availableScore);
		} else {
			dingdangbiValue.setText("0");
			userCoin.setText("0");
		}
		listenCount.setText(member.idolCount);
		fansCount.setText(member.fansCount);
	}
	
	private void initView() {
		findViewById(R.id.ask_tv).setOnClickListener(this);
		findViewById(R.id.dynamic_tv).setOnClickListener(this);
		findViewById(R.id.detail_info_tv).setOnClickListener(this);
		userPortrait = (ImageView) findViewById(R.id.user_portrait);
		userName = (TextView) findViewById(R.id.user_name);
		userLevel = (TextView) findViewById(R.id.user_level);
		userCoin = (TextView) findViewById(R.id.user_coin);
		listenCount = (TextView) findViewById(R.id.listen_count);
		fansCount = (TextView) findViewById(R.id.fans_count);
		nickName = (TextView) findViewById(R.id.nick_name);
		sexType = (TextView) findViewById(R.id.sex_type);
		localCity = (TextView) findViewById(R.id.local_city);
		simpleTx = (TextView) findViewById(R.id.simple_tx);
		levelValue = (TextView) findViewById(R.id.level_value);
		dingdangbiValue = (TextView) findViewById(R.id.dingdangbi_value);
	}
	
	private void setChecked(int type) {
		switch (type) {
		case 0:
			findViewById(R.id.ask_bottom).setVisibility(View.VISIBLE);
			findViewById(R.id.dynamic_bottom).setVisibility(View.GONE);
			findViewById(R.id.detail_info_bottom).setVisibility(View.GONE);
			findViewById(R.id.detail_info_layout).setVisibility(View.GONE);
			findViewById(R.id.listview_layout).setVisibility(View.VISIBLE);
			break;
			
		case 1:
			findViewById(R.id.ask_bottom).setVisibility(View.GONE);
			findViewById(R.id.dynamic_bottom).setVisibility(View.VISIBLE);
			findViewById(R.id.detail_info_bottom).setVisibility(View.GONE);
			findViewById(R.id.detail_info_layout).setVisibility(View.GONE);
			findViewById(R.id.listview_layout).setVisibility(View.VISIBLE);
			break;
			
		case 2:
			findViewById(R.id.ask_bottom).setVisibility(View.GONE);
			findViewById(R.id.dynamic_bottom).setVisibility(View.GONE);
			findViewById(R.id.detail_info_bottom).setVisibility(View.VISIBLE);
			findViewById(R.id.detail_info_layout).setVisibility(View.VISIBLE);
			findViewById(R.id.listview_layout).setVisibility(View.GONE);
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ask_tv:
			setChecked(0);
			break;
			
		case R.id.dynamic_tv:
			setChecked(1);
			break;
			
		case R.id.detail_info_tv:
			setChecked(2);
			break;

		default:
			break;
		}
		
	}

}
