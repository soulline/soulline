package com.yyl.utils;

import java.util.ArrayList;

public class RandomUtils {

	public static int getRollDice() {
		return (int)(Math.random()*6+1);
	}
	
	public static ArrayList<Integer> getRollDices(int count) {
		ArrayList<Integer> randoms = new ArrayList<Integer>();
		for (int i=0;i < count; i++) {
			randoms.add(getRollDice());
		}
		return randoms;
	}
}
