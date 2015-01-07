package com.asag.serial.utils;

public class ButtonUtils {

	 private static long lastClickTime;
	    public static boolean isFastClick() {
	        long time = System.currentTimeMillis();
	        long timeD = time - lastClickTime;
	        if ( 0 < timeD && timeD < 800) {   
	            return true;   
	        }   
	        lastClickTime = time;   
	        return false;   
	    }

}
