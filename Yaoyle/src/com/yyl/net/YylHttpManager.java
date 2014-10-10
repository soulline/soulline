package com.yyl.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

public class YylHttpManager {

	public static final int REQUEST_LIMIT = 6;

	public static final int REQUEST_WHAT = 1000;
	public static final int REQUEST_BUSY = 2000;

	private static YylHttpManager manager = null;

	private Handler dispatcherHandler = null;
	private SJMDHttpRequestDispatcher dispatcher = null;
	private SJMDHttpRequestThread[] requestHandlers = new SJMDHttpRequestThread[REQUEST_LIMIT];

	private String USER_AGENT = "";

	private Context context;

	private ArrayList<String> urlList;

	public static class Request {
		public boolean isGet = false;
		public String url = "";
		public HttpEntity entity = null;
		public RequestCallBack callBack = null;
		public String domain = "";
		public boolean isImg = false;

		@Override
		public boolean equals(Object o) {
			return url.equals(((Request) o).url);
		}
	}

	private class SJMDHttpRequestDispatcher extends HandlerThread implements
			Handler.Callback {

		public SJMDHttpRequestDispatcher(String name) {
			super(name);
		}

		@Override
		public boolean quit() {
			Looper l = getLooper();
			if (l != null) {
				l.quit();
				return true;
			}
			return false;
			// return super.quit();
		}

		@Override
		public boolean handleMessage(Message msg) {

			int index = -1;
			if (msg.what == REQUEST_WHAT) {
				for (int i = 0; i < REQUEST_LIMIT; i++) {
					if (!requestHandlers[i].isWork()) {
						requestHandlers[i].isWork = true;
						Message m = requestHandlers[i].getHandler()
								.obtainMessage();
						m.what = msg.what;
						m.obj = msg.obj;
						requestHandlers[i].getHandler().sendMessage(m);
						index = i;
						break;
					}
				}
				if (index == -1) {
					Message mm = new Message();
					mm.copyFrom(msg);
					dispatcherHandler.sendMessageDelayed(mm, 1000);
				}
			}
			return true;
		}
	}

	private class SJMDHttpRequestThread extends HandlerThread implements
			Handler.Callback {

		public boolean isWork = false;

		private Handler handler = null;

		private YylHttpClient client = null;

		private Request requestParms = null;

		private HttpUriRequest request = null;

		public SJMDHttpRequestThread(String name) {
			super(name);
		}

		@Override
		public boolean quit() {
			Looper l = getLooper();
			if (l != null) {
				l.quit();
				return true;
			}
			return false;
			// return super.quit();
		}

		public void init() {
			handler = new Handler(getLooper(), this);
		}

		public boolean isWork() {
			return isWork;
		}

		public Handler getHandler() {
			return handler;
		}

		public boolean isRequest(Request r) {
			return requestParms == r || requestParms.equals(r);
		}

		@Override
		public boolean handleMessage(Message msg) {
			int what = msg.what;
			if (what == REQUEST_WHAT) {
				isWork = true;
				requestParms = (Request) msg.obj;
				client = new YylHttpClient(context);
				request(request, client, requestParms);
				isWork = false;
			}
			return true;
		}

	}

	public void onDestroy() {
		if (dispatcher != null) {
			// dispatcher.quit();
			dispatcher.getLooper().quit();
			dispatcher = null;
		}
		if (requestHandlers != null) {
			for (SJMDHttpRequestThread r : requestHandlers) {
				if (r != null) {
					// r.quit();
					r.getLooper().quit();
					r = null;
				}
			}
		}
		manager = null;
	}

	private YylHttpManager(Context context) {
		this.context = context;
		initDispatcher();
		initRequest();
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		int w = dm.widthPixels;
		int h = dm.heightPixels;

		String p = android.os.Build.MODEL;
		String b = android.os.Build.BRAND;
		int vc = android.os.Build.VERSION.SDK_INT;
		USER_AGENT = b + "-" + p + "-" + vc + "," + w + "x" + h;
		urlList = new ArrayList<String>();
	}

	public static YylHttpManager getInstance(Context context) {
		if (manager == null) {
			manager = new YylHttpManager(context);
		}
		return manager;
	}

	private void initDispatcher() {
		dispatcher = new SJMDHttpRequestDispatcher("SJMD_Dispatcher");
		dispatcher.setDaemon(true);
		dispatcher.start();
		dispatcherHandler = new Handler(dispatcher.getLooper(), dispatcher);
	}

	private void initRequest() {
		for (int i = 0; i < REQUEST_LIMIT; i++) {
			requestHandlers[i] = new SJMDHttpRequestThread("SJMD_Requester-" + i);
			requestHandlers[i].setDaemon(true);
			requestHandlers[i].start();
			requestHandlers[i].init();
		}
	}

	public void onRequest(Request request) {
		String url = request.url;
		if (!urlList.contains(url)) {
			urlList.add(url);
			Message msg = dispatcherHandler.obtainMessage();
			msg.what = REQUEST_WHAT;
			msg.obj = request;
			dispatcherHandler.sendMessage(msg);
		}else {
			Log.i("YYL", "-----------------------dup url-----------------");
		}
	}

	public void onStop(Request request) {
		if (dispatcherHandler.hasMessages(REQUEST_WHAT, request)) {
			dispatcherHandler.removeMessages(REQUEST_WHAT, request);
			return;
		}

		for (SJMDHttpRequestThread t : requestHandlers) {
			if (t.isRequest(request)) {
				// t.onStop(request);
				break;
			}
		}
	}

	private void request(HttpUriRequest request, YylHttpClient client,
			Request runnable) {
		InputStream is = null;
		HttpResponse httpResponse = null;
		ByteArrayOutputStream baos = null;
		try {
			if (runnable.isGet) {
				HttpGet get = new HttpGet(runnable.url);
				request = get;
			} else {
				HttpPost post = new HttpPost(runnable.url);
				if (null != runnable.entity) {
					post.setEntity(runnable.entity);
				}
				request = post;
			}
			/*request.addHeader("Accept-Encoding", "gzip");
			request.addHeader("User-Agent", USER_AGENT);
			request.addHeader("X-Online-Host", runnable.domain);*/
			httpResponse = client.execute(request);
			int code = httpResponse.getStatusLine().getStatusCode();
			if (code == HttpStatus.SC_OK) {
				Header encodeHeader;
				String encode = "";
				if ((encodeHeader = httpResponse
						.getFirstHeader("Content-Encoding")) != null) {
					encode = encodeHeader.getValue();
				}
				if (encode.equals("gzip")) {
					is = new GZIPInputStream(httpResponse.getEntity()
							.getContent());
				} else {
					is = httpResponse.getEntity().getContent();
				}
				baos = new ByteArrayOutputStream();
				byte[] data = toByteArray(is, baos);
				if (data == null || data.length < 1) {
					runnable.callBack.onFetch(false, null);
					return;
				}
				if (runnable.isImg) {
					runnable.callBack.onFetch(true, data);
					return;
				}
				String d = new String(data);
				runnable.callBack.onFetch(true, d);
			} else {
				YylException ze = new YylException(code + "");
				runnable.callBack.onFetch(false, ze);
			}
		} catch (Exception e) {
			YylException ze = new YylException(e);
			runnable.callBack.onFetch(false, ze);
			e.printStackTrace();
		} finally {
			try {
				if (urlList.contains(runnable.url)) {
					urlList.remove(runnable.url);
				}
				if (is != null) {
					is.close();
				}
				if (baos != null) {
					baos.close();
				}
				if (request != null) {
					request.abort();
				}
				if (client != null) {
					client.getConnectionManager().shutdown();
				}
			} catch (Exception e) {
				YylException ze = new YylException(e);
				runnable.callBack.onFetch(false, ze);
				e.printStackTrace();
			}
		}
	}

	private byte[] toByteArray(InputStream is, ByteArrayOutputStream baos)
			throws IOException {
		if (is == null) {
			return null;
		}
		byte[] buffer = new byte[4096];
		int n = 0;
		while (-1 != (n = is.read(buffer))) {
			baos.write(buffer, 0, n);
		}
		return baos.toByteArray();
	}
}
