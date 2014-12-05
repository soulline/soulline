package com.cdd.share;

import android.util.Log;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

public class BaseUiListener implements IUiListener{


	protected void doComplete(Object values) {

	}

	@Override
	public void onError(UiError e) {
		Log.d("CDD", "onError e: " + e.errorMessage);
	}
	@Override
	public void onCancel() {
		Log.d("CDD", "onCancel");
	}

	@Override
	public void onComplete(Object response) {
		doComplete(response);
	}

}
