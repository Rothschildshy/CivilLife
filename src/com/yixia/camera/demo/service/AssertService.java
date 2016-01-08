package com.yixia.camera.demo.service;

import java.io.File;

import com.yixia.camera.util.DeviceUtils;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;

/**
 * 专门用来处理解压资源
 * 
 * @author tangjun
 *
 */
public class AssertService extends Service implements Runnable {

	/** 是否正在运行 */
	private static boolean mIsRunning;

	@Override
	public void onCreate() {
		super.onCreate();
		mIsRunning = true;
		new Thread(this).start();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void run() {
		try {
			File mThemeCacheDir;
			// 获取传入参数
			if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
					&& !isExternalStorageRemovable())
				mThemeCacheDir = new File(getExternalCacheDir(), "Theme");
			else
				mThemeCacheDir = new File(getCacheDir(), "Theme");
			// ThemeHelper.prepareTheme(getApplication(), mThemeCacheDir);
		} catch (OutOfMemoryError e) {
		} catch (Exception e) {
		}
		mIsRunning = false;
		stopSelf();
	}

	public static boolean isRunning() {
		return mIsRunning;
	}

	@SuppressLint("NewApi")
	public static boolean isExternalStorageRemovable() {
		if (DeviceUtils.hasGingerbread())
			return Environment.isExternalStorageRemovable();
		else
			return Environment.MEDIA_REMOVED.equals(Environment.getExternalStorageState());
	}
}
