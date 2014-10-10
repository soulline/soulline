package com.yyl.utils;

public class PhoneNumUtil {

	public static boolean isPhoneNum(String num){
		String mather = "^(13|14|15|18)\\d{9}$";
		if (num.matches(mather)) {
			return true;
		}
		return false;
	}
}
