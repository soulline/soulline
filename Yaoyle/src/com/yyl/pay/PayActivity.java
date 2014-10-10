package com.yyl.pay;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yyl.BaseActivity;
import com.yyl.R;
import com.yyl.mode.WxInfo;
import com.yyl.net.RequestListener;
import com.yyl.operater.PayOperater;
import com.yyl.utils.DataUtils;
import com.yyl.utils.PackageUtil;

public class PayActivity extends BaseActivity implements OnClickListener{

	private TextView payTaobao;
	private TextView payWx;
	
	private EditText payMoney;
	
	private int PAY_MODE = 2;
	
	private IWXAPI api;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_activity);
		setFullScreen();
		initView();
		api = WXAPIFactory.createWXAPI(context, PayParams.WX_APP_ID);
	}

	private void initView() {
//		payTaobao = (TextView) findViewById(R.id.pay_taobao);
		payWx = (TextView) findViewById(R.id.pay_wx);
		payMoney = (EditText) findViewById(R.id.pay_money);
//		payTaobao.setOnClickListener(this);
		payWx.setOnClickListener(this);
		findViewById(R.id.pay_ok_btn).setOnClickListener(this);
	}

	private void initPayChoose() {
		Drawable drawable1 = getResources().getDrawable(R.drawable.sexual_yes);
		Drawable drawable2 = getResources().getDrawable(R.drawable.sexual_no);
		Drawable drawableTb = getResources().getDrawable(R.drawable.payment_tb);
		Drawable drawableWx = getResources().getDrawable(R.drawable.payment_wx);
		drawable1.setBounds(0, 0, drawable1.getMinimumWidth(),
				drawable1.getMinimumHeight());
		drawable2.setBounds(0, 0, drawable2.getMinimumWidth(),
				drawable2.getMinimumHeight());
		drawableTb.setBounds(0, 0, drawableTb.getMinimumWidth(),
				drawableTb.getMinimumHeight());
		drawableWx.setBounds(0, 0, drawableWx.getMinimumWidth(),
				drawableWx.getMinimumHeight());
		switch (PAY_MODE) {
		case 1:
//			payTaobao.setCompoundDrawables(drawable1, null, drawableTb, null);
			payWx.setCompoundDrawables(drawable2, null, drawableWx, null);
			break;
		case 2:
//			payTaobao.setCompoundDrawables(drawable2, null, drawableTb, null);
			payWx.setCompoundDrawables(drawable1, null, drawableWx, null);
			break;
		default:
			break;
		}
	}

	private boolean checkInpuMoney() {
		if (TextUtils.isEmpty(payMoney.getText().toString().trim())) {
			showToast(getString(R.string.please_input_money));
			return false;
		}
		return true;
	}
	private void doWXpay() {
		if (!checkInpuMoney()) return;
		if (!isWXInstalled()) {
			showToast(getString(R.string.not_install_wx));
			return;
		}
		boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
		if (!isPaySupported) {
			showToast(getString(R.string.wxversion_not_support_pay));
			return;
		}
		final PayOperater payOp = new PayOperater(context);
		int money = Integer.valueOf(payMoney.getText().toString().trim());
		if (money < 1) {
			showToast(getString(R.string.money_not_less_one));
			return;
		}
		money = money * 100;
		payOp.setParams(money + "");
		DataUtils.putPreferences(DataUtils.KEY_COIN_VALUE, money);
		payOp.onRequest(new RequestListener() {
			
			@Override
			public void onError(Object error) {
				
			}
			
			@Override
			public void onCallBack(Object data) {
				WxInfo wxInfo = payOp.getWxInfo();
				onWxPay(wxInfo);
			}
		});
	}
	
	private boolean isWXInstalled() {
		return PackageUtil.isInstalled(context, "com.tencent.mm");
	}
	
	private void onWxPay(WxInfo wxInfo) {
		api.registerApp(PayParams.WX_APP_ID);
		PayReq f = new PayReq();
		f.appId = PayParams.WX_APP_ID;
		f.partnerId = PayParams.WX_PARTNER_ID;
		f.prepayId = wxInfo.prepayid;
		f.nonceStr = wxInfo.noncestr;
		f.timeStamp = wxInfo.timestamp;
		f.packageValue = /* "Sign=" + wx.packge */"Sign=WXPay";
		f.sign = wxInfo.sign;
		api.sendReq(f);
	
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*case R.id.pay_taobao:
			if (PAY_MODE == 2) {
				PAY_MODE = 1;
				initPayChoose();
			} else {
				return;
			}
			break;*/

		case R.id.pay_wx:
			if (PAY_MODE == 1) {
				PAY_MODE = 2;
				initPayChoose();
			} else {
				return;
			}
			break;
		case R.id.pay_ok_btn:
			doWXpay();
			break;
		default:
			break;
		}
		
	}

}
