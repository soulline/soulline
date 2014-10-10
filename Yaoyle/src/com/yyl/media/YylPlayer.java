package com.yyl.media;

import java.util.HashMap;

import com.yyl.R;
import com.yyl.application.YylApp;
import com.yyl.utils.DataUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class YylPlayer {

	private SoundPool spool;
	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

	public YylPlayer() {
		spool = new SoundPool(5, AudioManager.STREAM_SYSTEM, 0);
		initSoundPool();
	}

	public void initSoundPool() {
		map.put(1, spool.load(YylApp.getInstance(), R.raw.shake_sound, 1));
		map.put(2, spool.load(YylApp.getInstance(), R.raw.pai, 1));
		map.put(3, spool.load(YylApp.getInstance(), R.raw.win, 1));
		map.put(4, spool.load(YylApp.getInstance(), R.raw.fail, 1));
	}

	/**
	 * 
	 * @param sound
	 *            文件
	 * @param number
	 *            循环次数
	 */
	public void playSound(int sound, int number) {
		if (!DataUtils.getPreferences(DataUtils.KEY_SOUND_ONOFF, true)) {
			return;
		}
		AudioManager am = (AudioManager) YylApp.getInstance().getSystemService(
				Context.AUDIO_SERVICE);// 实例化
		float audioMaxVolum = am.getStreamMaxVolume(AudioManager.STREAM_RING);// 音效最大值
		float audioCurrentVolum = am.getStreamVolume(AudioManager.STREAM_MUSIC);
		float audioRatio = audioCurrentVolum / audioMaxVolum;
		spool.play(map.get(sound), audioMaxVolum,// 左声道音量
				audioMaxVolum,// 右声道音量
				1, // 优先级
				number,// 循环播放次数
				1f);// 回放速度，该值在0.5-2.0之间 1为正常速度
	}
}
