package com.cdd.minepage;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdd.R;
import com.cdd.base.BaseActivity;

public class MyCollectActivity extends BaseActivity implements OnClickListener{

	private ImageView scAskImg, scDongtaiImg;
	
	private TextView scAskTx, scDongtaiTx;
	
	private int checkStatus = 0;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.my_collect_activity);
		initTitle("我的收藏");
		initView();
		initContent();
	}

	private void initView() {
		findViewById(R.id.sc_ask_layout).setOnClickListener(this);
		findViewById(R.id.sc_dongtai_layout).setOnClickListener(this);
		scAskImg = (ImageView) findViewById(R.id.sc_ask_img);
		scDongtaiImg = (ImageView) findViewById(R.id.sc_dongtai_img);
		scAskTx = (TextView) findViewById(R.id.sc_ask_tx);
		scDongtaiTx = (TextView) findViewById(R.id.sc_dongtai_tx);
	}
	
	private void initContent() {
		setCheck(checkStatus);
	}
	
	private void setCheck(int check) {
		switch (check) {
		case 0:
			scAskImg
					.setBackgroundResource(R.drawable.sc_askt_press);
			findViewById(R.id.sc_ask_layout).setBackgroundColor(
					getResources().getColor(R.color.sq_blue));
			scAskTx.setTextColor(getResources().getColor(R.color.white));
			scDongtaiImg.setBackgroundResource(R.drawable.sc_dongtai);
			findViewById(R.id.sc_dongtai_layout).setBackgroundColor(
					getResources().getColor(R.color.white));
			scDongtaiTx.setTextColor(getResources().getColor(R.color.sq_blue));
			break;
		case 1:
			scAskImg
					.setBackgroundResource(R.drawable.sc_ask);
			findViewById(R.id.sc_ask_layout).setBackgroundColor(
					getResources().getColor(R.color.white));
			scAskTx
					.setTextColor(getResources().getColor(R.color.sq_blue));
			scDongtaiImg.setBackgroundResource(R.drawable.sc_dongtai_press);
			findViewById(R.id.sc_dongtai_layout).setBackgroundColor(
					getResources().getColor(R.color.sq_blue));
			scDongtaiTx.setTextColor(getResources().getColor(R.color.white));
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sc_ask_layout:
			checkStatus = 0;
			setCheck(checkStatus);
			break;

		case R.id.sc_dongtai_layout:
			checkStatus = 1;
			setCheck(checkStatus);
			break;
		default:
			break;
		}
		
	}
}
