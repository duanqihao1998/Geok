package com.geok.langfang.tools;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

public class PlaySoundPool {

	private Context context;
	private SoundPool soundPool;
	private int StreamVolume;
	private HashMap<Integer, Integer> soundPoolMap;

	public PlaySoundPool(Context context) {
		this.context = context;
		initSound();
	}

	public void initSound() {
		soundPool = new SoundPool(100, AudioManager.STREAM_MUSIC, 100);
		soundPoolMap = new HashMap<Integer, Integer>();
		AudioManager mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		StreamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);

	}

	public void loadSfx(int raw, int ID) {
		// 把资源中的音效加载到指定的ID(播放的时候就对应到这个ID播放就行了)
		soundPoolMap.put(ID, soundPool.load(context, raw, ID));
	}

	public void play(int sound, int uLoop) {
		soundPool.play(soundPoolMap.get(sound), StreamVolume, StreamVolume, 1, uLoop, 1f);
		System.out.println("这一次测试。。。。。。。。。。。。。。。。。");
	}

}
