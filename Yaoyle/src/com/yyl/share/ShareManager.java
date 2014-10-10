package com.yyl.share;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.exception.WeiboShareException;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yyl.BaseActivity;
import com.yyl.R;
import com.yyl.application.YylApp;
import com.yyl.mode.ShareEntry;
import com.yyl.net.RequestListener;
import com.yyl.operater.ShareOperater;
import com.yyl.utils.AccessTokenKeeper;
import com.yyl.utils.PackageUtil;

public class ShareManager implements IWeiboHandler.Response{
	private Oauth2AccessToken mAccessToken;

	/** 微博 Web 授权类，提供登陆等功能 */
	private WeiboAuth mWeiboAuth;

	public static final String SINA_SCOPE = "email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";

	/** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
	private SsoHandler mSsoHandler;

	/** 微博分享的接口实例 */
	private IWeiboShareAPI mWeiboShareAPI;

	public static final String SINA_APP_KEY = "1876975061";

	private Activity context;

	private IWXAPI wXApi;

	public static final String WX_APP_ID = "wx0efabc4ae60261b1";
	
	private ShareEntry shareEntry = new ShareEntry();

	public ShareManager(Activity context) {
		this.context = context;
	}

	public void initShare() {
		mWeiboAuth = new WeiboAuth(context, SINA_APP_KEY,
				"https://api.weibo.com/oauth2/default.html", SINA_SCOPE);
		wXApi = WXAPIFactory.createWXAPI(context, WX_APP_ID, false);
//		initSinaShare();
		initShareEntry();
	}

	
	private void initShareEntry() {
		final ShareOperater shareOp = new ShareOperater(YylApp.getInstance());
		shareOp.setParams("100000");
		shareOp.onRequest(new RequestListener() {
			
			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCallBack(Object data) {
				shareEntry = shareOp.getShareEntry();
			}
		});
	}

	public void initSinaShare() {
		if (mWeiboShareAPI == null) {
			mWeiboShareAPI = WeiboShareSDK
					.createWeiboAPI(context, SINA_APP_KEY);
			mWeiboShareAPI.registerApp();
		}
	}

	public void handlerWeibo(Intent intent) {
		initSinaShare();
		mWeiboShareAPI.handleWeiboResponse(intent, this);
	}

	public void onNewIntent(Intent intent,
			IWXAPIEventHandler iwxh) {
		if (mWeiboShareAPI != null) {
			mWeiboShareAPI.handleWeiboResponse(intent, this);
		}
		if (wXApi != null) {
			wXApi.handleIntent(context.getIntent(), iwxh);
		}
	}

	private boolean isWXInstalled() {
		return PackageUtil.isInstalled(context, "com.tencent.mm");
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}

	public void sharedMsgToWX(int type) {
		if (!isWXInstalled()) {
			((BaseActivity) context).showToast(context.getString(R.string.not_install_wx));
			return;
		}
		wXApi.registerApp(WX_APP_ID);

		// 用WXTextObject对象初始化一个WXMediaMessage对象

		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = shareEntry.url;
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = shareEntry.msg;
		msg.description = shareEntry.msg;
		Bitmap thumb = getBitmap(shareEntry.picUrl);
		if (thumb == null) return;
		msg.thumbData = bmpToByteArray(thumb, true);

		// 构造一个Req
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage"); // transaction字段用于唯一标识一个请求
		req.message = msg;

		if (type == 0) {
			req.scene = SendMessageToWX.Req.WXSceneSession;
		} else if (type == 1) {
			req.scene = SendMessageToWX.Req.WXSceneTimeline;
		}

		// 调用api接口发送数据到微信
		boolean result = wXApi.sendReq(req);
	}

	private Bitmap getBitmap(String url) {
	    URL fileUrl = null;
	    Bitmap bitmap = null;
	 
	    try {
	        fileUrl = new URL(url);
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    }
	 
	    try {
	        HttpURLConnection conn = (HttpURLConnection) fileUrl
	                .openConnection();
	        conn.setDoInput(true);
	        conn.connect();
	        InputStream is = conn.getInputStream();
	        bitmap = BitmapFactory.decodeStream(is);
	        is.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return bitmap;
	}
	
	public void sharedSinaWeibo() {
		if (!PackageUtil.isInstalled(context, "com.sina.weibo")) {
			((BaseActivity) context).showToast(context.getString(R.string.not_install_sina));
			return;
		}
		if (mWeiboShareAPI == null) {
			mWeiboShareAPI = WeiboShareSDK
					.createWeiboAPI(context, SINA_APP_KEY);
			mWeiboShareAPI.registerApp();
		}
		mAccessToken = AccessTokenKeeper.readAccessToken(context);
		if (mAccessToken != null && mAccessToken.isSessionValid()) {
			sendSinaShare();
		} else {
			/*
			 * if (TextUtils.isEmpty(shareEntry.logo)) { return; }
			 */
			mSsoHandler = new SsoHandler(context, mWeiboAuth);
			mSsoHandler.authorize(new AuthListener());
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	private void sendSinaShare() {
		mWeiboShareAPI.registerApp();
		if (mWeiboShareAPI.checkEnvironment(true)) {
			try {
//				TextObject textObj = new TextObject();
//				textObj.text = "我正在玩夜店骰王，大家来一起玩吧";
//				textObj.actionUrl = "http";
//				textObj.thumbData = bmpToByteArray(getIconBitmap(), true);
				// textObj.thumbData =
				// bmpToByteArray(getBitmap(shareEntry.logo), true);
				WebpageObject webObject = new WebpageObject();
				webObject.identify = Utility.generateGUID();
				webObject.title = shareEntry.msg;
				webObject.description = shareEntry.msg;
				Bitmap bitmp = getBitmap(shareEntry.picUrl);
				if (bitmp == null) return;
				webObject.setThumbImage(bitmp);
				webObject.actionUrl = shareEntry.url;
				webObject.defaultText = shareEntry.msg;
				WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
//				weiboMessage.textObject = textObj;
				weiboMessage.mediaObject = webObject;
				
				SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
				// 用transaction唯一标识一个请求
				request.transaction = String
						.valueOf(System.currentTimeMillis());
				request.multiMessage = weiboMessage;

				// 3. 发送请求消息到微博，唤起微博分享界面
				mWeiboShareAPI.sendRequest(request);
			} catch (WeiboShareException e) {
				e.printStackTrace();
			}
		}
	}

	private byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	private Bitmap getIconBitmap() {
		return BitmapFactory.decodeResource(context.getResources(),
				R.drawable.icon);
	}

	/**
	 * 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用
	 * {@link SsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
	 * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
	 * SharedPreferences 中。
	 */
	class AuthListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			if (values == null) {
				return;
			}
			// 从 Bundle 中解析 Token
			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			if (mAccessToken.isSessionValid()) {
				// 显示 Token

				// 保存 Token 到 SharedPreferences
				AccessTokenKeeper.writeAccessToken(context, mAccessToken);
				((BaseActivity) context).showToast(context.getString(R.string.auth_success));
				new Thread(new Runnable() {

					@Override
					public void run() {
						sendSinaShare();
					}
				}).start();
			}
		}

		@Override
		public void onCancel() {
			System.out.println("cancel");
		}

		@Override
		public void onWeiboException(WeiboException e) {
			System.out.println(e);
		}
	}

	@Override
	public void onResponse(BaseResponse baseResp) {
		if (baseResp == null) {
			return;
		}
		switch (baseResp.errCode) {
        case WBConstants.ErrorCode.ERR_OK:
        	((BaseActivity) context).showToast(context.getString(R.string.share_success));
            break;
        case WBConstants.ErrorCode.ERR_CANCEL:
            break;
        case WBConstants.ErrorCode.ERR_FAIL:
            break;
        }
		
	}
}
