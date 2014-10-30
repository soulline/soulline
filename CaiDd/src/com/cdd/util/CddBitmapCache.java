package com.cdd.util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.szzc.minivolley.toolbox.ImageLoader.ImageCache;

public class CddBitmapCache implements ImageCache {

	private LruCache<String, Bitmap> mCache;

	public CddBitmapCache() {
		int maxSize = 10 * 1024 * 1024;
		mCache = new LruCache<String, Bitmap>(maxSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}

		};
	}

	@Override
	public Bitmap getBitmap(String url) {
		return mCache.get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		mCache.put(url, bitmap);
	}

}
