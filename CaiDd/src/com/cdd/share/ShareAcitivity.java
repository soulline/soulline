package com.cdd.share;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.cdd.R;
import com.cdd.base.BaseActivity;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

public class ShareAcitivity extends BaseActivity implements OnItemClickListener, IWXAPIEventHandler{

	private GridView shareGrid;
	
	private ShareManager shareManager = null;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.share_activity);
		initView();
		initContent();
		initShared();
		if (bundle != null) {
			shareManager.handlerWeibo(getIntent());
		}
	}
	
	private void initView() {
		shareGrid = (GridView) findViewById(R.id.share_grid);
		shareGrid.setOnItemClickListener(this);
	}
	
	private void initShared() {
		shareManager = new ShareManager(this);
		shareManager.initShare();
	}
	
	private void initContent() {
		CddShareAdapter adapter = new CddShareAdapter(context);
		String[] shareTitles = getResources().getStringArray(R.array.share_array);
		adapter.addData(shareTitles);
		shareGrid.setAdapter(adapter);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		shareManager.onNewIntent(intent, this);
	}

	public void shareToSinaWb() {
		shareManager.sharedSinaWeibo();
	}

	public void shareToWX(int code) {
		shareManager.sharedMsgToWX(code);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {
		case 0:
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					shareToSinaWb();
					
				}
			}).start();
			break;
			
		case 1:
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					shareToWX(0);
				}
			}).start();
			break;
			
		case 2:
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					shareToWX(1);
				}
			}).start();
			break;
			
		case 3:
			shareManager.initTencentShare();
			if (shareManager.readyQQ()) {
				shareManager.sharedQQzone();
			} else {
				shareManager.doLoginQQ(0);
			}
			break;
			
		case 4:
			shareManager.initTencentShare();
			if (shareManager.readyQQ()) {
				shareManager.shareToQQ();
			} else {
				shareManager.doLoginQQ(1);
			}
			break;
			
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		shareManager.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub
		Log.d("zhao", "onReq");
	}

	@Override
	public void onResp(BaseResp arg0) {
		// TODO Auto-generated method stub
		Log.d("zhao", "onResp");
	}

}
