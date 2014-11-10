package com.cdd.activity.findpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.cdd.R;
import com.cdd.base.BaseActivity;
import com.cdd.net.RequestListener;
import com.cdd.operater.DingdangDynamicListOp;

public class DynamicListActivity extends BaseActivity implements OnClickListener{

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.dynamic_list_activity);
		initTitle("叮当圈");
		initView();
		initContent();
	}
	
	private void initView() {
		findViewById(R.id.add_news_layout).setOnClickListener(this);
	}
	
	private void initContent() {
		requestDynamicList();
	}
	
	private void requestDynamicList() {
		DingdangDynamicListOp dingdangOp = new DingdangDynamicListOp(context);
		dingdangOp.setParams("1");
		dingdangOp.onRequest(new RequestListener() {
			
			@Override
			public void onError(Object error) {
				
			}
			
			@Override
			public void onCallBack(Object data) {
				
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_news_layout:
			Intent addNews = new Intent(context, PulishDynamicActivity.class);
			startActivity(addNews);
			break;

		default:
			break;
		}
		
	}

}
