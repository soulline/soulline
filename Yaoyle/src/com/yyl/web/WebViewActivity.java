package com.yyl.web;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yyl.BaseActivity;
import com.yyl.R;

public class WebViewActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_activity);
		init();
	}

	private void init() {
		String url = getIntent().getStringExtra("web_url");
		WebView web = (WebView) findViewById(R.id.web_content);
		web.getSettings().setJavaScriptEnabled(true);
		web.getSettings().setUseWideViewPort(false);
		web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		web.getSettings().setBuiltInZoomControls(false);
		web.getSettings().setCacheMode(
				android.webkit.WebSettings.LOAD_CACHE_ELSE_NETWORK);
		web.getSettings().setSupportMultipleWindows(true);
		web.getSettings().setDomStorageEnabled(true);
		web.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
		String appCachePath = getApplicationContext().getCacheDir()
				.getAbsolutePath();
		web.getSettings().setAppCachePath(appCachePath);
		web.getSettings().setAllowFileAccess(true);
		web.getSettings().setAppCacheEnabled(true);
		web.setWebChromeClient(new WebChromeClient());
		web.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
		        view.loadUrl(url);
				return true;
			}

		});
		web.loadUrl(url);
	}
}
