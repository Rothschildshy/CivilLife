package com.app.civillife.Service;

import java.util.ArrayList;

import com.CivilLife.Activity.MainActivity;
import com.CivilLife.Entity.PushEntity;
import com.CivilLife.Json.PushJson;
import com.CivilLife.Variable.GlobalVariable;
import com.CivilLife.net.Httpurl;
import com.CivilLife.net.RequestTask;
import com.app.civillife.ContentDataActivity;
import com.app.civillife.MessageActivity;
import com.app.civillife.R;
import com.aysy_mytool.Qlog;

import Requset_getORpost.RequestListener;
import android.R.string;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

public class MessageService extends Service {

	private NotificationManager manager;
	public PushJson savepush;

	@Override
	public IBinder onBind(Intent intent) {
		return new MyBind();
	}

	public class MyBind extends Binder {
		public void BindChange(String name) {
			Chenge(name);
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		handler = new Handler();
		if (GlobalVariable.push) {
			new RequestTask(MessageService.this, listener, false, false, "").execute(Httpurl.Push());
		}

	}

	// 延迟请求
	private void Request() {
		if (GlobalVariable.push) {
			handler.postDelayed(new Runnable() {
				public void run() {
//					 Log.e("", "更新数据中...");
					// Notification();  
					// handler.postAtTime(this,4000);
					new RequestTask(MessageService.this, listener, false, false, "").execute(Httpurl.Push());
					// Log.e("", "更新数据中... "+Httpurl.Push());
					// handler.postDelayed(this, 10000);
				}
			}, 60000);
		}
	}

	// 数据推送处理
	private void GetPush(PushJson push) {
		savepush = push;
		for (int i = push.getAl().size() - 1; i >= 0; i--) {
			if (!GlobalVariable.sendID.equals("-1")) {
				if (push.getAl().get(i).getSendID().equals(GlobalVariable.sendID)) {
					push.getAl().remove(i);
					continue;
				}
			}
			PushEntity pushEntity = push.getAl().get(i);
			Notification(pushEntity.getTitle(), pushEntity.getTitle() + ":" + pushEntity.getContent(),
					pushEntity.getSendID(), pushEntity.getType());
		}
	}

	RequestListener listener = new RequestListener() {

		@Override
		public void responseResult(String jsonObject) {
			// Log.e("", "jsonObject推送的消息 "+jsonObject);
			@SuppressWarnings("static-access")
			PushJson push = new PushJson().readJsonToSendmsgObject(MessageService.this, jsonObject);
			Request();
			if (push == null) {
				return;
			}
			if (!TextUtils.isEmpty(push.getAl().get(0).getContent())) {
				if (savepush != null) {
					if (!push.equals(savepush)) {
						GetPush(push);
					}
				} else {
					GetPush(push);
				}
			}
			// Request();
		}

		@Override
		public void responseException(String errorMessage) {
			Request();
			Log.e("", "jsonObject推送的消息失败  " + errorMessage);
		}
	};
	private Handler handler;

	private void Chenge(String name) {
	}

	@Override
	public void onDestroy() {
		Qlog.e("", "服务关闭");
		super.onDestroy();
	}

	@SuppressWarnings("deprecation")
	public void Notification(String title, String content, String id, String type) {
		// 构建一个通知对象(需要传递的参数有三个,分别是图标,标题和 时间)
		@SuppressWarnings("deprecation")
		Notification notification = new Notification(R.drawable.ic_launcher,
				getResources().getString(R.string.app_name), System.currentTimeMillis());
		notification.flags = Notification.FLAG_AUTO_CANCEL;// 点击后自动消失
		notification.defaults = Notification.DEFAULT_SOUND;// 声音默认
		if (type.equals("1")) {
			Intent intent = new Intent(this, MessageActivity.class);
			intent.putExtra("ToUserId", id);
			intent.putExtra("ToUserName", title);
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			notification.setLatestEventInfo(this, title, content, pendingIntent);
			manager.notify(Integer.valueOf(id), notification);// 发动通知,id由自己指定，每一个Notification对应的唯一标志
		} else {
			Intent intent = new Intent(this, ContentDataActivity.class);
			intent.putExtra("TYPE", 0);
			intent.putExtra("comment", false);
			intent.putExtra("ToUserId", id);
			
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			notification.setLatestEventInfo(this, title, content, pendingIntent);
			manager.notify(-Integer.valueOf(id), notification);// 发动通知,id由自己指定，每一个Notification对应的唯一标志
		}
		// 其实这里的id没有必要设置,只是为了下面要用到它才进行了设置
	}

}
