package com.cdd.activity.login;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.cdd.app.CddApp;
import com.cdd.mode.QQLoginEntry;
import com.cdd.mode.TencentShareEntry;
import com.cdd.share.BaseUiListener;
import com.cdd.util.DataUtils;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class QQLoginManger {

	public static final String QQ_QZONE_ID = "1103564486";

	public Tencent mTencent;

	private static final String SCOPE = "get_user_info,get_simple_userinfo,get_user_profile,get_app_friends,"
			+ "add_share,add_topic,list_album,upload_pic,add_album,set_user_face,get_vip_info,get_vip_rich_info,get_intimate_friends_weibo,match_nick_tips_weibo";

	private Activity context;

	public QQAuth mQQAuth;
	
	private OnQQLoginListener onQQlistener;
	
	private QQLoginEntry qqEntry = new QQLoginEntry();

	public interface OnQQLoginListener {
		public void onSuccess();
		public void onError();
	}

	public QQLoginManger(Activity context) {
		this.context = context;
	}
	
	public void addOnQQLoginListener(OnQQLoginListener onQQlistener) {
		this.onQQlistener = onQQlistener;
	}

	public void initQQ() {
		mTencent = Tencent.createInstance(QQ_QZONE_ID, CddApp.getInstance());
	}

	public boolean readyQQ() {
		boolean ready = mTencent.isSessionValid()
				&& mTencent.getOpenId() != null;
		return ready;
	}
	
	public void loginOutQQ() {
		mTencent.logout(context);
	}

	public void initTencentShare() {
		TencentShareEntry entry = new TencentShareEntry();
		entry.access_token = DataUtils.getPreferences("tx_access_token", "");
		entry.expires_in = DataUtils.getPreferences("tx_expires_in", "");
		entry.openid = DataUtils.getPreferences("tx_openid", "");
		if (!TextUtils.isEmpty(entry.openid)) {
			mTencent.setOpenId(entry.openid);
		}
		if (!TextUtils.isEmpty(entry.access_token)
				&& !TextUtils.isEmpty(entry.expires_in)) {
			mTencent.setAccessToken(entry.access_token, entry.expires_in);
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		mTencent.onActivityResult(requestCode, resultCode, data);
	}

	public void doLoginQQ() {
		IUiListener listener = new BaseUiListener() {

			@Override
			protected void doComplete(Object values) {
				TencentShareEntry entry = parseTencentShare(values.toString());
				DataUtils.putPreferences("tx_access_token", entry.access_token);
				DataUtils.putPreferences("tx_expires_in", entry.expires_in);
				DataUtils.putPreferences("tx_openid", entry.openid);
				getUserInfo();
			}

		};
		mTencent.login(context, SCOPE, listener);
	}
	
	public QQLoginEntry getQQEntry() {
		return qqEntry;
	}

	public void getUserInfo() {
		mQQAuth = QQAuth.createInstance(QQ_QZONE_ID, context);
		UserInfo info = new UserInfo(context, mQQAuth.getQQToken());
		info.getUserInfo(getUserListener);
	}

	private IUiListener getUserListener = new IUiListener() {
		@Override
		public void onError(UiError e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onComplete(final Object response) {
			JSONObject json = (JSONObject) response;
			if (json != null && !json.equals(JSONObject.NULL)) {
				
				qqEntry.deviceFlag = ((TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE))
						.getDeviceId();
				qqEntry.gender = json.optString("gender");
				qqEntry.nickname = json.optString("nickname");
				qqEntry.openId = mTencent.getOpenId();
				qqEntry.photo = json.optString("figureurl_qq_2");
				if (onQQlistener != null && !TextUtils.isEmpty(qqEntry.openId)) {
					onQQlistener.onSuccess();
				} else if (onQQlistener != null && TextUtils.isEmpty(qqEntry.openId)) {
					onQQlistener.onError();
				}
			} else if (onQQlistener != null) {
				onQQlistener.onError();
			}
		}

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub

		}
	};

	public TencentShareEntry parseTencentShare(String response) {
		TencentShareEntry entry = new TencentShareEntry();
		try {
			JSONObject obj = new JSONObject(response);
			entry.access_token = obj.optString("access_token");
			if (!TextUtils.isEmpty(obj.optString("expires_in"))) {
				long expires_in = System.currentTimeMillis()
						+ Long.parseLong(obj.optString("expires_in")) * 1000;
				entry.expires_in = expires_in + "";
			}
			entry.openid = obj.optString("openid");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return entry;
	}
}
