package com.cdd.util;

import android.content.Context;
import android.widget.ImageView;

import com.szzc.minivolley.RequestQueue;
import com.szzc.minivolley.toolbox.ImageLoader;
import com.szzc.minivolley.toolbox.ImageLoader.ImageListener;
import com.szzc.minivolley.toolbox.Volley;

public class ImageOperater {

	private static ImageOperater operater;

	private ImageLoader imageLoader;
	private RequestQueue queue;

	private ImageOperater(Context context) {
		queue = Volley.newRequestQueue(context);
		imageLoader = new ImageLoader(queue, new CddBitmapCache());
	}

	synchronized public static ImageOperater getInstance(Context context) {
		if (operater == null) {
			operater = new ImageOperater(context);
		}
		return operater;
	}

	public void onLoadImage(String url, ImageView image) {
		ImageListener listener = ImageLoader.getImageListener(image, 0, 0);
		imageLoader.get(url, listener);
	}

	public void onDestory() {
		queue.stop();
		operater = null;
	}
}
