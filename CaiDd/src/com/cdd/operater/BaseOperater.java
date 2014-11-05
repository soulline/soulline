package com.cdd.operater;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import com.cdd.app.CddApp;
import com.cdd.base.BaseActivity;
import com.cdd.login.LoginActivity;
import com.cdd.mode.BaseEntry;
import com.cdd.net.RequestCallBack;
import com.cdd.net.RequestListener;
import com.cdd.net.CddException;
import com.cdd.net.CddHttpManager;
import com.cdd.net.CddHttpManager.Request;
import com.cdd.util.CddRequestCode;
import com.cdd.util.CryptAES;
import com.cdd.util.CddConfig;
import com.szzc.volley.utils.CryptAESNew;
import com.szzc.volley.utils.NetworkUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

abstract public class BaseOperater {

	private CddHttpManager manager = null;
	private Request request = null;
	public HttpEntity entity = null;
	public JSONObject response;

	public boolean isGet = true;
	public boolean isCache = false;

	public String action = "";

	public Map<String, String> params = new HashMap<String, String>();

	public Map<String, Object> extra_params = new HashMap<String, Object>();

	public CddApp app;

	public Context context;

	private RequestListener rlistener;

	private String queryContent = "";

	private boolean showLoading = true;

	private boolean showToast = true;

	private String extra_data = "";

	private boolean isEncrypt = false;

	private boolean isShowErrorNetwork = true;

	private CryptAESNew aes;

	public boolean isStartRequest = false;

	public BaseOperater(Context context) {
		this.context = context;
		manager = CddHttpManager.getInstance(context);
		request = new Request();
		app = CddApp.getInstance();
		aes = new CryptAESNew();
	}

	/**
	 * 初始化请求action
	 */
	abstract public void initAction();

	/**
	 * 初始化请求参�??
	 */
	abstract public void initRequest();

	/**
	 * 初始化post实体
	 */
	abstract public void initEntity();

	/**
	 * 解析返回json数据
	 * 
	 * @param response
	 *            JSONObject
	 */
	abstract public void onParser(JSONObject response);

	/**
	 * 解析返回json数据
	 * 
	 * @param response
	 *            JSONArray
	 */
	abstract public void onParser(JSONArray response);

	/**
	 * 回调解析数据
	 * 
	 * @return BaseEntry
	 */
	abstract public BaseEntry getData();

	public void setCrypt(boolean crypt) {
		this.isEncrypt = crypt;
	}

	public void setShowLoading(boolean showLoading) {
		this.showLoading = showLoading;
	}

	public void setShowToast(boolean showToast) {
		this.showToast = showToast;
	}

	public void setShowNetwork(boolean isShowErrorNetwork) {
		this.isShowErrorNetwork = isShowErrorNetwork;
	}

	public void setExtraData(String extra_data) {
		this.extra_data = extra_data;
	}

	public String initUrl() {
		initAction();
		String url = app.getDomain() + action + initBaseInfo();
		;
		initRequest();
		try {
			url = url + "&params=" + URLEncoder.encode(initJsonUrl(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}

	public String initBaseInfo() {
		StringBuffer sb = new StringBuffer();
		sb.append("?");
		StringBuffer bbi = new StringBuffer();
		// bbi.append("&cid=" + app.base_cid);
		bbi.append("uid=" + app.getUID());
		sb.append(bbi.toString());
		return sb.toString();
	}

	public String initDataUrl() {
		StringBuffer data = new StringBuffer();
		data.append("?");
		if (!isStartRequest) {
			data.append("&uid=").append(app.getUID());
		}
		for (String key : params.keySet()) {
			data.append("&").append(key).append("=").append(params.get(key));
		}
		for (String s : extra_params.keySet()) {
			data.append("&").append(s).append("=").append(params.get(s));
		}
		return data.toString();
	}

	private String initJsonUrl() {
		String data = "";
		try {
			JSONObject jo = new JSONObject();
			for (String key : params.keySet()) {
				jo.put(key, params.get(key));
			}
			for (String s : extra_params.keySet()) {
				Object obj =  extra_params.get(s);
				if(obj != null && obj instanceof ArrayList){
					jo.put(s,new JSONArray((ArrayList<String>)obj));
				}else{
					jo.put(s,obj);
				}
			}
			data = jo.toString();
			if (CddConfig.IS_DEBUG) {
				Log.i("CDD", data);
				queryContent = data;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (TextUtils.isEmpty(data)) {
			return "";
		}
		/*
		 * String dataNew = data; try { dataNew = URLEncoder.encode(data,
		 * "utf-8"); } catch (UnsupportedEncodingException e) {
		 * e.printStackTrace(); }
		 */
		// return dataNew;
		return isEncrypt ? CryptAES.getInstance().onEncrypt(data) : data;
		// return DesEncrypter.getInstance().encrypt(data);
	}

	public boolean checkJson() {
		int code = response.optInt("status", -1);
		if (code != 200) {
			// if (ZCConfig.IS_DEBUG) {
			if (context instanceof BaseActivity) {
				String msg = response.optString("msg", "");
				if (!TextUtils.isEmpty(msg)) {
					if (showToast) {
						/*if (WqConfig.CODE_ERROR_UID != code
								&& WqConfig.CODE_ERROR_REQUEST_QUICKY != code
								&& WqConfig.CODE_ERROR_LOGIN != code) {*/
							((BaseActivity) context).showToast(msg);
//						}
					}

				}
			}
			// }
			/*if (WqConfig.CODE_ERROR_UID == code) {
				
				 * InitAppOperate init = new InitAppOperate(context);
				 * init.onRequest(new RequestListener() {
				 * 
				 * @Override public void onError(Object error) { }
				 * 
				 * @Override public void onCallBack(Object data) {
				 * onRequest(rlistener); } });
				 
			}*/ if (CddConfig.CODE_ERROR_LOGIN == code) {
				app.clearLogin();
				Intent i = new Intent(context, LoginActivity.class);
				i.putExtra("rebuild", true);
				app.isRebuild = true;
				if (context instanceof Activity) {
					((Activity) context).startActivityForResult(i,
							CddRequestCode.ACTION_LOGIN_REQUEST_CODE);
				} else {
					context.startActivity(i);
				}

			}/* else if (WqConfig.CODE_ERROR_REQUEST_QUICKY == code) {
				if (context instanceof BaseActivity) {
					((BaseActivity) context).showToast("请求太快");
				}
			}*/
		}
		return code == 200;
	}

/*	private void onStartOp() {
		StartOperater startOp = new StartOperater(context);
		startOp.onRequest(new RequestListener() {

			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCallBack(Object data) {

			}
		});
	}*/

	public void showMsg() {
		if (!showToast) {
			return;
		}
		if (context instanceof BaseActivity) {
			((BaseActivity) context).showToast(response.optString("msg", ""));
		}
	}

	public void reRequest() {
		if (rlistener != null) {
			onRequest(rlistener);
		}
	}

	/**
	 * 发起数据请求
	 * 
	 * @param callback
	 */
	public void onRequest(final RequestListener callback) {

		if (!NetworkUtil.isNetworkAvailable(context)) {
			if (CddConfig.IS_DEBUG) {
				if (context instanceof BaseActivity) {
					((BaseActivity) context).showToast("网络连接错误");
				}
			}
			if (isShowErrorNetwork) {
				if (context instanceof BaseActivity) {
					((BaseActivity) context).showNetworkError();
				}
				/*
				 * if (context instanceof ActivityIndex) { ((ActivityIndex)
				 * context).showNetworkError(); }
				 */
			}
			callback.onError(null);
			return;
		}

		rlistener = callback;
		request.domain = CddConfig.DOMAIN;
		request.isGet = isGet;
		request.url = initUrl();
		if (CddConfig.IS_DEBUG) {
			Log.i("CDD", request.url);
		}
		initEntity();
		if (null != entity) {
			request.entity = entity;
		}
		request.callBack = new RequestCallBack() {

			@Override
			public void onFetch(boolean success, Object data) {
				if (context instanceof BaseActivity && showLoading) {
					((BaseActivity) context).showLoading(false);
				}
				if (success && data != null) {
					try {

						if (data instanceof CddException) {
							if (CddConfig.IS_DEBUG) {
								CddException z = (CddException) data;
								/*
								 * RecordUtil.recordDataLog(queryContent,
								 * request.url, getExceptionInfo(z));
								 */
								((BaseActivity) context)
										.showToast(getExceptionInfo(z));
							}
							((BaseActivity) context).showToast("网络请求错误");
							callback.onError(data);
							return;
						}

						String d = (String) data;

						
						/*String ed = isEncrypt ? CryptAES.getInstance()
								.onDecrypt(d) : d;*/
						// String ed = aes.onDecrypt(d);
						if (CddConfig.IS_DEBUG) {
							// RecordUtil.recordDataLog(queryContent,
							// request.url,
							// d);
							// RecordUtil.recordDataLog(queryContent,
							// request.url,
							// "");
							Log.d("CDD", d);
//							Log.d("CDD", ed);
						}
						response = new JSONObject(d);
						if (app.isRebuild)
							return;
						if (checkJson()) {
							if (response.has("re")) {
								Object obj = response.get("re");
								if (obj instanceof JSONObject) {
									onParser((JSONObject) obj);
								} else if (obj instanceof JSONArray) {
									onParser((JSONArray) obj);
								} else {
									onParser(response);
								}
							}
							callback.onCallBack(d);
						} else {
							callback.onError(response.optString("msg", ""));
						}

					} catch (Exception e) {
						callback.onError(data);
						e.printStackTrace();
						/*
						 * RecordUtil.recordDataLog(queryContent, request.url,
						 * getExceptionInfo(e));
						 */
					}

				} else {
					callback.onError(data);
					if (CddConfig.IS_DEBUG) {
						if (context instanceof BaseActivity && data != null) {
							if (data instanceof CddException) {
								CddException e = (CddException) data;
								// ((BaseFragmentActivity) context)
								// .showToast(getExceptionInfo(e));
								/*
								 * RecordUtil.recordDataLog(queryContent,
								 * request.url, data == null ? "null" :
								 * getExceptionInfo(e));
								 */
							}

						}
					}
				}
			}
		};
		manager.onRequest(request);
		if (context instanceof BaseActivity && showLoading) {
			((BaseActivity) context).showLoading(true);
		}
	}

	private String getExceptionInfo(Throwable ex) {
		if (ex == null) {
			return null;
		}
		StringWriter info = null;
		PrintWriter printWriter = null;
		try {
			info = new StringWriter();
			printWriter = new PrintWriter(info);
			ex.printStackTrace(printWriter);
			Throwable cause = ex.getCause();
			while (cause != null) {
				cause.printStackTrace(printWriter);
				cause = cause.getCause();
			}
			return info.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (printWriter != null) {
					printWriter.close();
				}
				if (info != null) {
					info.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
