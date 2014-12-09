package com.cdd.share;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

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

import com.cdd.R;
import com.cdd.app.CddApp;
import com.cdd.base.BaseActivity;
import com.cdd.mode.ShareEntry;
import com.cdd.mode.TencentShareEntry;
import com.cdd.net.RequestListener;
import com.cdd.operater.GetShareInfoOp;
import com.cdd.util.AccessTokenKeeper;
import com.cdd.util.CddConfig;
import com.cdd.util.DataUtils;
import com.cdd.util.PackageUtil;
import com.sina.weibo.sdk.api.ImageObject;
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
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class ShareManager implements IWeiboHandler.Response{
	private Oauth2AccessToken mAccessToken;

	/** 微博 Web 授权类，提供登陆等功能 */
	private WeiboAuth mWeiboAuth;

	public static final String SINA_SCOPE = "email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";
	
	public static final String QQ_QZONE_ID = "1103564486";
	
	public Tencent mTencent;
	private static final String SCOPE = "get_user_info,get_simple_userinfo,get_user_profile,get_app_friends,"
            + "add_share,add_topic,list_album,upload_pic,add_album,set_user_face,get_vip_info,get_vip_rich_info,get_intimate_friends_weibo,match_nick_tips_weibo";

	/** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
	private SsoHandler mSsoHandler;

	/** 微博分享的接口实例 */
	private IWeiboShareAPI mWeiboShareAPI;

	public static final String SINA_APP_KEY = "1326821158";

	private Activity context;

	private IWXAPI wXApi;

	public static final String WX_APP_ID = "wx82b3718805053aa5";
	
	private ShareEntry shareEntry = new ShareEntry();

	public ShareManager(Activity context) {
		this.context = context;
	}

	public void initShare(IWXAPIEventHandler iwxh) {
		mWeiboAuth = new WeiboAuth(context, SINA_APP_KEY,
				"https://api.weibo.com/oauth2/default.html", SINA_SCOPE);
		wXApi = WXAPIFactory.createWXAPI(context, WX_APP_ID, false);
		if (wXApi != null) {
			wXApi.handleIntent(context.getIntent(), iwxh);
		}
//		initSinaShare();
		initShareEntry();
		mTencent = Tencent.createInstance(QQ_QZONE_ID, CddApp.getInstance());
	}

	
	private void initShareEntry() {
		final GetShareInfoOp shareOp = new GetShareInfoOp(CddApp.getInstance());
		shareOp.setParams(CddConfig.CID_CODE + "");
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
	
	public boolean readyQQ() {
        boolean ready = mTencent.isSessionValid()
                && mTencent.getOpenId() != null;
        return ready;
    }

	public void initTencentShare() {
		TencentShareEntry entry = new TencentShareEntry();
		entry.access_token = DataUtils.getPreferences("tx_access_token", "");
		entry.expires_in = DataUtils.getPreferences("tx_expires_in", "");
		entry.openid = DataUtils.getPreferences("tx_openid", "");
		if (!TextUtils.isEmpty(entry.openid)) {
			mTencent.setOpenId(entry.openid);
		}
		if (!TextUtils.isEmpty(entry.access_token) && !TextUtils.isEmpty(entry.expires_in)) {
			mTencent.setAccessToken(entry.access_token, entry.expires_in);
		}
	}
	
	public TencentShareEntry parseTencentShare(String response) {
		TencentShareEntry entry = new TencentShareEntry();
		try {
			JSONObject obj = new JSONObject(response);
			entry.access_token = obj.optString("access_token");
			if (!TextUtils.isEmpty(obj.optString("expires_in"))) {
				long expires_in = System.currentTimeMillis() + Long.parseLong(obj.optString("expires_in")) * 1000;
				entry.expires_in = expires_in + "";
			}
			entry.openid = obj.optString("openid");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return entry;
	}
	
	public void doLoginQQ(final int type) {
		IUiListener listener = new BaseUiListener() {
			
			@Override
			protected void doComplete(Object values) {
				TencentShareEntry entry = parseTencentShare(values.toString());
				DataUtils.putPreferences("tx_access_token", entry.access_token);
				DataUtils.putPreferences("tx_expires_in", entry.expires_in);
				DataUtils.putPreferences("tx_openid", entry.openid);
				if (type == 0) {
					sharedQQzone();
				} else if (type == 1) {
					shareToQQ();
				}
			}

		};
		mTencent.login(context, SCOPE, listener);
	}
	
	public void shareToQQ() { 
	    final Bundle params = new Bundle();
	    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
	    params.putString(QQShare.SHARE_TO_QQ_TITLE, shareEntry.msg);
	    params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  shareEntry.msg);
	    params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareEntry.url);
	    params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareEntry.picUrl);
	    params.putString(QQShare.SHARE_TO_QQ_APP_NAME, context.getString(R.string.app_name));
	    mTencent.shareToQQ(context, params, new BaseUiListener());
	}
	
	public void sharedQQzone() {
		if (readyQQ()) {
			final Bundle params = new Bundle();
			params.putLong(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
					QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);// 分享的标题。
			params.putString(QzoneShare.SHARE_TO_QQ_TITLE,
					shareEntry.msg);// 用户分享时的评论内容，可由用户输入。
			params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,
					shareEntry.msg);// 分享内容
			params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,
					shareEntry.url);
			ArrayList<String> imageUrls = new ArrayList<String>();
			imageUrls.add(shareEntry.picUrl);
			params.putString(QQShare.SHARE_TO_QQ_APP_NAME, context.getString(R.string.app_name));
			params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL,
					imageUrls);
			new Thread(new Runnable() {

				@Override
				public void run() {
					mTencent.shareToQzone(context, params,
							new IUiListener() {

								@Override
								public void onCancel() {
									// TODO Auto-generated method stub

								}

								@Override
								public void onComplete(Object arg0) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onError(UiError arg0) {
									if (arg0 == null) {
										return;
									}
									if (context instanceof BaseActivity) {
										((BaseActivity) context).showToast(context.getString(R.string.share_faided_try_later));
									}
								}

							});
				}
			}).start();

		}
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
		context.setIntent(intent);
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
		msg.description = shareEntry.msg + shareEntry.url;
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
		mTencent.onActivityResult(requestCode, resultCode, data);
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	private void sendSinaShare() {
		mWeiboShareAPI.registerApp();
		if (mWeiboShareAPI.checkEnvironment(true)) {
			try {
				TextObject textObj = new TextObject();
				textObj.text = shareEntry.msg + shareEntry.url;
				ImageObject imageObject = new ImageObject();
				imageObject.setImageObject(getIconBitmap());
				WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
				// textObj.thumbData =
				// bmpToByteArray(getBitmap(shareEntry.logo), true);
				weiboMessage.textObject = textObj;
				weiboMessage.imageObject = imageObject;
				SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
				// 用transaction唯一标识一个请求
				request.transaction = String
						.valueOf(System.currentTimeMillis());
				request.multiMessage = weiboMessage;
				
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
