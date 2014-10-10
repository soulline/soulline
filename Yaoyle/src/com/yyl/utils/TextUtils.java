package com.yyl.utils;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

public class TextUtils {

	/**
	 * 
	 * @param s
	 *            起始�?
	 * @param e
	 *            结束�?
	 * @param newSize
	 *            新字体大�?
	 * @param content
	 *            内容
	 * @return
	 */
	public static SpannableStringBuilder changeSize(int s, int e, int newSize,
			String content) {
		SpannableStringBuilder builder = new SpannableStringBuilder(content);
		AbsoluteSizeSpan span = new AbsoluteSizeSpan(newSize);
		builder.setSpan(span, s, e, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return builder;
	}

	/**
	 * 
	 * @param s
	 *            起始�?
	 * @param e
	 *            结束�?
	 * @param newColor
	 *            新字体颜�?
	 * @param content
	 *            内容
	 * @return
	 */
	public static SpannableStringBuilder changeColor(int s, int e,
			int newColor, String content) {
		SpannableStringBuilder builder = new SpannableStringBuilder(content);
		ForegroundColorSpan span = new ForegroundColorSpan(newColor);
		builder.setSpan(span, s, e, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		return builder;
	}

	/**
	 * 
	 * @param cs 颜色起始�?
	 * @param ce 颜色结束�?
	 * @param ss 字体大小起始�?
	 * @param se 字体大小结束�?
	 * @param newColor 新字体颜�?
	 * @param newSize 新字体大�?
	 * @param content 文字内容
	 * @return
	 */
	public static SpannableStringBuilder changeColorSize(int cs, int ce,
			int ss, int se, int newColor, int newSize, String content) {
		SpannableStringBuilder builder = new SpannableStringBuilder(content);
		ForegroundColorSpan span = new ForegroundColorSpan(newColor);
		builder.setSpan(span, cs, ce, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		AbsoluteSizeSpan span_ = new AbsoluteSizeSpan(newSize);
		builder.setSpan(span_, ss, se, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return builder;
	}
}
