package com.yyl.wxapi;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yyl.BaseActivity;
import com.yyl.pay.PayParams;
import com.yyl.utils.DataUtils;
import com.yyl.utils.YylConfig;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler{

	private IWXAPI api;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this, PayParams.WX_APP_ID);
        api.handleIntent(getIntent(), this);
	}
	
	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResp(BaseResp resp) {
		if (YylConfig.IS_DEBUG) {
			Log.i("YYL", "---- mm pay " + resp.errCode + " " + resp.errStr);
		}
		int money = DataUtils.getPreferences(DataUtils.KEY_COIN_VALUE, 0);
		DataUtils.putPreferences(DataUtils.KEY_COIN_VALUE, "");
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			showToast(getMsg(resp.errCode, money));
		}
		finish();
	}

	private String getMsg(int index, int money) {
		String msg = "";
		if (index == BaseResp.ErrCode.ERR_OK) {
			msg = "恭喜您,充值成功" + money + "金币";
		}else if (index == BaseResp.ErrCode.ERR_AUTH_DENIED) {
			msg = "认证被否决，请检查您的客户端版本";
		}else if (index == BaseResp.ErrCode.ERR_COMM) {
			msg = "一般错误，请稍后重试";
		}else if (index == BaseResp.ErrCode.ERR_SENT_FAILED) {
			msg = "发送失败，请稍后重试";
		}else if (index == BaseResp.ErrCode.ERR_UNSUPPORT) {
			msg = "您的微信版本不支持支付功能";
		}else if (index == BaseResp.ErrCode.ERR_USER_CANCEL) {
			msg = "支付取消";
		}
		return msg;
	}
	
}
