package com.app.civillife.Service;

import java.util.concurrent.Executors;

import com.CivilLife.Json.PushJson;
import com.CivilLife.Variable.GlobalVariable;
import com.CivilLife.net.Httpurl;
import com.CivilLife.net.RequestTask;

import Requset_getORpost.RequestListener;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
/*
 * 聊天服务
 */
public class MessageChatService extends Service {

	private NotificationManager manager;
	public PushJson savepush;
	Handler handler=new Handler();
	private int Time=5000;
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
				.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.ChatMessageList(GlobalVariable.sendID, 1));
				handler.postDelayed(this, Time);
			}
		}, Time);  
	}
	
	
	
	@Override
	public void onDestroy() {
		Log.e("", "聊天服务关闭");
		super.onDestroy();
	}

}
