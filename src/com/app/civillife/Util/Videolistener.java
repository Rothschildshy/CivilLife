package com.app.civillife.Util;

import android.media.MediaPlayer;

public interface Videolistener {
	/**视频准备**/
	public void onPrepared(MediaPlayer mediaplayer);
	/**视频播完**/
	public void onCompletion(MediaPlayer mediaplayer);
	/**视频出错**/
	public void onError(MediaPlayer mediaplayer, int i, int j);
}
