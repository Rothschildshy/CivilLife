package com.CivilLife.Base;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.LinkedList;
import java.util.List;

import com.CivilLife.Activity.WlconActivity;
import com.CivilLife.net.RequestTask;
import com.aysy_mytool.Qlog;
import com.yixia.camera.VCamera;
import com.yixia.camera.demo.service.AssertService;
import com.yixia.camera.util.DeviceUtils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import cn.waps.AppConnect;

public class BaseApplication extends Application {
	// private List<Activity> activities = new ArrayList<Activity>();
	public static List<Activity> activityList = new LinkedList();
	private static BaseApplication application;

	@Override
	public void onCreate() {
		super.onCreate();

		application = this;

		// 设置拍摄视频缓存路径
		File dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		if (DeviceUtils.isZte()) {
			if (dcim.exists()) {
				VCamera.setVideoCachePath(dcim + "/WeChatJuns/");
			} else {
				VCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/", "/sdcard-ext/") + "/WeChatJuns/");
			}
		} else {
			VCamera.setVideoCachePath(dcim + "/WeChatJuns/");
		}

		// 设置开发debug模式 true为调试模式 打印日志 输出日志
		boolean IsDebug = false;
		RequestTask.IsLog = IsDebug;
		Qlog.DEBUG = IsDebug;
		// 开启log输出,ffmpeg输出到logcat
		VCamera.setDebugMode(IsDebug);

		// 初始化拍摄SDK，必须
		VCamera.initialize(this);

		// 解压assert里面的文件
		startService(new Intent(this, AssertService.class));

		// 万普通过代码设置 APP_ID 和 APP_PID
		AppConnect.getInstance("883f4f98e738071a8df26e1213df254e", "default", this);

		// 以下用来捕获程序崩溃异常
		// 程序崩溃时触发线程
		// 调试模式抛出异常 不然看不到错误日志log
		if (!IsDebug) {
			Thread.setDefaultUncaughtExceptionHandler(restartHandler);
		}
	}

	/**
	 * 添加到Activity容器中
	 */
	public static void addActivity1(Activity activity) {
		if (!activityList.contains(activity)) {
			activityList.add(activity);
		}
	}

	/**
	 * 便利所有Activigty并finish
	 */
	public static void finishActivity() {
		for (Activity activity : activityList) {
			activity.finish();
		}
	}

	/**
	 * 结束指定的Activity
	 */
	@SuppressWarnings("static-access")
	public static void finishSingleActivity(Activity activity) {
		if (activity != null) {
			if (activityList.contains(activity)) {
				activityList.remove(activity);
			}
			activity.setResult(activity.RESULT_CANCELED);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity 在遍历一个列表的时候不能执行删除操作， 所有我们先记住要删除的对象，遍历之后才去删除。
	 */
	public static void finishSingleActivityByClass(Class<?> cls) {
		Activity tempActivity = null;
		for (Activity activity : activityList) {
			if (activity.getClass().equals(cls)) {
				tempActivity = activity;
			}
		}
		finishSingleActivity(tempActivity);
	}

	/**
	 * 结束 除 指定类名的Activity 在遍历一个列表的时候不能执行删除操作， 所有我们先记住要删除的对象，遍历之后才去删除。
	 */
	public static void finishAllActivityByClass(Class<?> cls) {
		Activity tempActivity = null;
		for (Activity activity : activityList) {
			if (!activity.getClass().equals(cls)) {
				finishSingleActivity(activity);
			}
		}
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public static Context getContext() {
		return application;
	}

	// 运用list来保存们每一个activity是关键
	private List<Activity> mList = new LinkedList<Activity>();
	private static BaseApplication instance;

	// 构造方法
	// 实例化一次
	public synchronized static BaseApplication getInstance() {
		if (null == instance) {
			instance = new BaseApplication();
		}
		return instance;
	}

	// add Activity
	public void addActivity(Activity activity) {
		mList.add(activity);
	}

	// 关闭每一个list内的activity
	public void exit() {
		try {
			for (Activity activity : mList) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	// 创建服务用于捕获崩溃异常
	private UncaughtExceptionHandler restartHandler = new UncaughtExceptionHandler() {
		public void uncaughtException(Thread thread, Throwable ex) {
			restartApp();// 发生崩溃异常时,重启应用
		}
	};

	public void restartApp() {
		Qlog.e("", "<--程序异常退出-->");
		Intent intent = new Intent(this, WlconActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.startActivity(intent);
		// 结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
