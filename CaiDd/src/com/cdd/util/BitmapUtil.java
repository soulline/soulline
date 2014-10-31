package com.cdd.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;

public class BitmapUtil {

	public static final int UNCONSTRAINED = -1;

	private static final int S_LIMIT_SIZE = 100;

	/**
	 * 压缩图片
	 * <p>
	 * 先进行图片尺寸压缩，后在质量压缩。
	 * 
	 * @param bimap
	 * @return
	 */
	public static byte[] onCompress(String path) {
		return onCompressSize(path, 130, 80, true);
	}

	public static byte[] onCompress_(String path) {
		return onCompressSize(path, 480, 480, true);
	}
	public static byte[] onCompress_(String path,String type) {
		return onCompressSize(path, 894, 595, true);
	}
	public static String onCompressForUpload(String path, int w, int h) {
		byte[] os = onCompressSize(path, w, h, true);
		Bitmap bitmap = BitmapFactory.decodeByteArray(os, 0, os.length);
		return onCompressQualityUpload(bitmap);
	}

	public static byte[] onCompressForUploadbyte(String path, int w, int h) {
		byte[] os = onCompressSize(path, w, h, true);
		return os;
	}

	public static String onCompressForWriteReview(String path, int w, int h) {
		String middle = path.substring(0, path.lastIndexOf("."));
		String end = path.substring(path.lastIndexOf(".") + 1, path.length());
		String newPath = middle + "_compress." + end;
		int[] wh = new int[] { w, h };
		Bitmap bitmap = createNewBitmapAndCompressByFile(path, wh);
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			int options = 100;
			while (baos.toByteArray().length / 1024 > S_LIMIT_SIZE) {

				options -= 10;
				if (options < 10) {
					break;
				}
				baos.reset();
				bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
			}
			baos.close();
			ByteArrayInputStream isBm = new ByteArrayInputStream(
					baos.toByteArray());
			bitmap = BitmapFactory.decodeStream(isBm, null, null);
			isBm.close();
			FileOutputStream fos = new FileOutputStream(newPath);
			bitmap.compress(Bitmap.CompressFormat.JPEG, options, fos);
			fos.close();
			if (!bitmap.isRecycled()) {
				bitmap.recycle();
			}
			return newPath;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static byte[] createNewBitmapAndCompressByFile(byte[] bytes,
			int wh[]) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 为true里只读图片的信息，如果长宽，返回的bitmap为null
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		options.inDither = false;
		/**
		 * 计算图片尺寸 //TODO 按比例缩放尺寸
		 */
		BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

		int bmpheight = options.outHeight;
		int bmpWidth = options.outWidth;
		int inSampleSize = bmpheight / wh[1] > bmpWidth / wh[0] ? bmpheight
				/ wh[1] : bmpWidth / wh[0];
		if (inSampleSize > 1)
			options.inSampleSize = inSampleSize;// 设置缩放比例
		options.inJustDecodeBounds = false;
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
					options);
		} catch (OutOfMemoryError e) {
			System.gc();
			bitmap = null;
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] buffer = baos.toByteArray();
		return buffer;
	}

	public static Bitmap createNewBitmapAndCompressByFile(String filePath,
			int wh[]) {
		int offset = 100;
		File file = new File(filePath);
		long fileSize = file.length();
		if (200 * 1024 < fileSize && fileSize <= 1024 * 1024)
			offset = 90;
		else if (1024 * 1024 < fileSize)
			offset = 85;

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 为true里只读图片的信息，如果长宽，返回的bitmap为null
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		options.inDither = false;
		/**
		 * 计算图片尺寸 //TODO 按比例缩放尺寸
		 */
		BitmapFactory.decodeFile(filePath, options);

		int bmpheight = options.outHeight;
		int bmpWidth = options.outWidth;
		int inSampleSize = bmpheight / wh[1] > bmpWidth / wh[0] ? bmpheight
				/ wh[1] : bmpWidth / wh[0];
		// if(bmpheight / wh[1] < bmpWidth / wh[0]) inSampleSize = inSampleSize
		// * 2 / 3;//TODO 如果图片太宽而高度太小，则压缩比例太大。所以乘以2/3
		if (inSampleSize > 1)
			options.inSampleSize = inSampleSize;// 设置缩放比例
		options.inJustDecodeBounds = false;

		InputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			return null;
		}
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(is, null, options);
		} catch (OutOfMemoryError e) {

			System.gc();
			bitmap = null;
		}
		if (offset == 100)
			return bitmap;// 缩小质量
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, offset, baos);
		byte[] buffer = baos.toByteArray();
		options = null;
		if (buffer.length >= fileSize)
			return bitmap;
		return BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
	}
	/**
	 * 
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * 
	 * @return degree旋转的角度
	 */

	public static int readPictureDegree(String path) {

		int degree = 0;

		try {

			ExifInterface exifInterface = new ExifInterface(path);

			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
      
			switch (orientation) {

			case ExifInterface.ORIENTATION_ROTATE_90:

				degree = 90;

				break;

			case ExifInterface.ORIENTATION_ROTATE_180:

				degree = 180;

				break;

			case ExifInterface.ORIENTATION_ROTATE_270:

				degree = 270;

				break;

			}

		} catch (IOException e) {

			e.printStackTrace();

		}

		return degree;

	}
	/**
	 * 图片旋转度数
	 * 
	 * @param bitmap
	 * @param degree
	 * @return
	 */
	public static Bitmap degreeBitmap(Bitmap bitmap, float degree) {
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		return bitmap;
	}
	/**
	 * 进行图片尺寸压缩
	 * 
	 * @param bimap
	 * @return
	 */
	private static byte[] onCompressSize(String path, int w, int h,
			boolean upload) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = false;

		File file = new File(path);
		try {
			if (file.length() > 110 * 1024) {
				opts.inSampleSize = 4;
			}
			// opts.inPreferredConfig = Bitmap.Config.RGB_565;
		} catch (Exception e) {
			// TODO: handle exception
			opts.inSampleSize = 4;
		}
	
		
		Bitmap bitmap = BitmapFactory.decodeFile(path, opts);
		bitmap = degreeBitmap(bitmap,readPictureDegree(path));
		if (upload) {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			int type = h;
			if(h>w){
				h = w;
				w = type;
			}
			
			float scaleWidth = ((float) w) / width;
			float scaleHeight = ((float) h) / height;
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
					true);
		}
		return onCompressQuality(bitmap);
	}
	

	/**
	 * 进行图片质量压缩
	 * 
	 * @param bimap
	 * @return
	 */
	private static byte[] onCompressQuality(Bitmap bitmap) {
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			int options = 100;
			while (baos.toByteArray().length / 1024 > S_LIMIT_SIZE) {
				options -= 10;
				if (options < 10) {
					break;
				}
				baos.reset();
				bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
			}
			return baos.toByteArray();
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if (baos != null) {
					baos.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 进行图片质量压缩
	 * 
	 * @param bimap
	 * @return
	 */
	private static String onCompressQualityUpload(Bitmap bitmap) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			int options = 100;
			while (baos.toByteArray().length / 1024 > S_LIMIT_SIZE) {

				options -= 10;
				if (options < 10) {
					break;
				}
				baos.reset();
				bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
			}
			baos.close();
			ByteArrayInputStream isBm = new ByteArrayInputStream(
					baos.toByteArray());
			bitmap = BitmapFactory.decodeStream(isBm, null, null);
			isBm.close();
			FileOutputStream fos = new FileOutputStream(
					CddConfig.IMAGE_CACHE_FILE + "upload.jpg");
			bitmap.compress(Bitmap.CompressFormat.JPEG, options, fos);
			fos.close();
			if (!bitmap.isRecycled()) {
				bitmap.recycle();
			}
			return CddConfig.IMAGE_CACHE_FILE + "upload.jpg";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/*
	 * 
	 * 获得设置信息
	 */

	public static Options getOptions(String path) {

		Options options = new Options();

		options.inJustDecodeBounds = true;// 只描边，不读取数据

		BitmapFactory.decodeFile(path, options);

		return options;

	}

	/**
	 * 
	 * 获得图像
	 * 
	 * @param path
	 * 
	 * @param options
	 * 
	 * @return
	 * 
	 * @throws FileNotFoundException
	 */

	public static Bitmap getBitmapByPath(String path, Options options,
			int screenWidth, int screenHeight) {

		Bitmap b = null;
		FileInputStream in = null;

		try {
			File file = new File(path);
			if (!file.exists()) {
				throw new FileNotFoundException();
			}

			in = new FileInputStream(file);

			if (options != null) {

				Rect r = getScreenRegion(screenWidth, screenHeight);
				int w = r.width();
				int h = r.height();
				int maxSize = w > h ? w : h;
				int inSimpleSize = computeSampleSize(options, maxSize, w * h);
				options.inSampleSize = inSimpleSize; // 设置缩放比例
				options.inJustDecodeBounds = false;
			}

			b = BitmapFactory.decodeStream(in, null, options);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return b;

	}

	private static Rect getScreenRegion(int width, int height) {

		return new Rect(0, 0, width, height);

	}

	/**
	 * 
	 * 获取需要进行缩放的比例，即options.inSampleSize
	 * 
	 * @param options
	 * 
	 * @param minSideLength
	 * 
	 * @param maxNumOfPixels
	 * 
	 * @return
	 */

	public static int computeSampleSize(BitmapFactory.Options options,

	int minSideLength, int maxNumOfPixels) {

		int initialSize = computeInitialSampleSize(options, minSideLength,

		maxNumOfPixels);

		int roundedSize;

		if (initialSize <= 8) {

			roundedSize = 1;

			while (roundedSize < initialSize) {

				roundedSize <<= 1;

			}

		} else {

			roundedSize = (initialSize + 7) / 8 * 8;

		}

		return roundedSize;

	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,

	int minSideLength, int maxNumOfPixels) {

		double w = options.outWidth;

		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == UNCONSTRAINED) ? 1 :

		(int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));

		int upperBound = (minSideLength == UNCONSTRAINED) ? 128 :

		(int) Math.min(Math.floor(w / minSideLength),

		Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {

			// return the larger one when there is no overlapping zone.

			return lowerBound;

		}

		if ((maxNumOfPixels == UNCONSTRAINED) &&

		(minSideLength == UNCONSTRAINED)) {

			return 1;

		} else if (minSideLength == UNCONSTRAINED) {

			return lowerBound;

		} else {

			return upperBound;

		}

	}

	/**
	 * 将图片转换为灰度图片
	 * 
	 * @param img
	 *            图片文件
	 * @return Bitmap
	 */
	public static Bitmap convertGrayImg(Bitmap img) {
		int w = img.getWidth(), h = img.getHeight();
		int[] pix = new int[w * h];
		img.getPixels(pix, 0, w, 0, 0, w, h);
		int alpha = 0xFF << 24;
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				// 获得像素的颜色
				int color = pix[w * i + j];
				int red = ((color & 0x00FF0000) >> 16);
				int green = ((color & 0x0000FF00) >> 8);
				int blue = color & 0x000000FF;
				color = (red + green + blue) / 3;
				color = alpha | (color << 16) | (color << 8) | color;
				pix[w * i + j] = color;
			}
		}
		if (!img.isRecycled()) {
			img.recycle();
		}
		img = Bitmap.createBitmap(w, h, Config.RGB_565);
		img.setPixels(pix, 0, w, 0, 0, w, h);
		return img;

	}
}
