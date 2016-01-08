package com.app.civillife.Service;

import java.util.ArrayList;
import java.util.Collection;

import com.CivilLife.Activity.MainActivity;
import com.CivilLife.Entity.MessagelistEntity;
import com.CivilLife.Entity.PushEntity;
import com.CivilLife.Json.MessageListJson;
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
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

public class MessageChatService extends Service {

	private NotificationManager manager;
	public PushJson savepush;
	Handler handler=new Handler();
	@Override
	public IBinder onBind(Intent intent) {
		Log.e("", "聊天服务绑定");
		return new MyBind();
	}
	@Override
	public void unbindService(ServiceConnection conn) {
		Log.e("", "聊天服务绑定解除");
		super.unbindService(conn);
	}
	public class MyBind extends Binder {
		public void RequestListenerChange(RequestListener messagelisteners) {
			setRequestListener(messagelisteners);
		}
	}

	@Override
	public void onCreate() {
		Log.e("", "聊天服务开启");  
		super.onCreate();  
	}
	
	public void setRequestListener(final RequestListener messagelisteners){
		
		handler.postDelayed(new Runnable() {
			public void run() {
				new RequestTask(MessageChatService.this, messagelisteners, false, false, "")
				.execute(Httpurl.ChatMessageList(GlobalVariable.sendID, 1));
				handler.postDelayed(this, 60000);
			}
		}, 60000);  
	}
	
	
	
	@Override
	public void onDestroy() {
		Log.e("", "聊天服务关闭");
		super.onDestroy();
	}

}
