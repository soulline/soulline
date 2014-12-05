package com.cdd.wxapi;

import com.cdd.R;
import com.cdd.base.BaseActivity;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.content.Intent;
import android.os.Bundle;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler{
	
	private IWXAPI api;
	private static final String WX_APP_ID = "wxa3ed942958805e2c";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this, WX_APP_ID, false);
		api.registerApp(WX_APP_ID);
		api.handleIntent(getIntent(), this);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	
	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onResp(BaseResp resp) {
		String result = "";
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = getString(R.string.share_success);
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = getString(R.string.share_cancel);
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = getString(R.string.share_refuse);
			break;
		default:
			result = getString(R.string.share_return);
			break;
		}
		showToast(result);
		finish();
	}

}
