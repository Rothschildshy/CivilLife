package com.app.civillife;

import java.util.ArrayList;
import java.util.Collection;

import com.CivilLife.Base.BaseActivity;
import com.CivilLife.Entity.MessagelistEntity;
import com.CivilLife.Json.MessageListJson;
import com.CivilLife.Json.PublicUpJson;
import com.CivilLife.MyAdapter.ChatListViewAdapter;
import com.CivilLife.Variable.GlobalVariable;
import com.CivilLife.net.Httpurl;
import com.CivilLife.net.RequestTask;
import com.CivilLife.net.ReturnAL;
import com.app.civillife.Service.MessageChatService;
import com.app.civillife.Service.MessageChatService.MyBind;
import com.app.civillife.Service.MessageService;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import Requset_getORpost.RequestListener;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;  
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * 小纸条聊天界面
 * 
 * @author Administrator
 * 
 */
public class MessageActivity extends BaseActivity {
	private PullToRefreshListView mListView;
	private static final int LOAD_DATA_FINISH = 10;
	private static final int REFRESH_DATA_FINISH = 11;
	private int page = 0;// 页数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		initViews();
		initEvents();
		init();
		
	}


	@Override
	protected void initViews() {
		mTx_Title = (TextView) findViewById(R.id.tx_title);
		ed_mymessage = (EditText) findViewById(R.id.ed_mymessage);
		mListView = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView);
		ll_chat = (LinearLayout) findViewById(R.id.ll_chat);
		ll_chat.setVisibility(View.GONE);
	}

	@Override
	protected void initEvents() {
		findViewById(R.id.image_back).setOnClickListener(this);
		findViewById(R.id.image_info).setOnClickListener(this);
		findViewById(R.id.tv_release).setOnClickListener(this);//发送消息
		ed_mymessage.setOnClickListener(this);
		ed_mymessage.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				return false;
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override  
	protected void init() {
		Intent intent = getIntent();
		toUserId = intent.getStringExtra("ToUserId");
		GlobalVariable.sendID=toUserId;
		Intent intent1=new Intent(this,MessageChatService.class);
		startService(intent1);
		bindService(intent1, conn, BIND_AUTO_CREATE);
		toUserName = intent.getStringExtra("ToUserName");
		mTx_Title.setText(toUserName);
		adapter = new ChatListViewAdapter(mApplication, this, null); 
		adapter.tousedid=toUserId;
		adapter.tousedname=toUserName;  
		mListView.setAdapter(adapter);
		listView = mListView.getRefreshableView();
		setRefrsh();
		mListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh(@SuppressWarnings("rawtypes") PullToRefreshBase refreshView) {
				loadData(0);
			}
		});      
	}
	
	private MyBind myBind;
	ServiceConnection conn=new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			myBind =  (MyBind) service;
			myBind.RequestListenerChange(messagelisteners);
		}
	};
	
	RequestListener messagelisteners=new RequestListener() {
		
		@SuppressWarnings("unchecked")
		@Override
		public void responseResult(String jsonObject) {
			ArrayList<MessagelistEntity> Data = new ArrayList<MessagelistEntity>();
			MessageListJson listJson = new MessageListJson().readJsonToSendmsgObject(MessageActivity.this, jsonObject);
			if (listJson==null) {
				return;
			}
			for (int i = 0; i < listJson.getAl().size(); i++) {
				MessagelistEntity messagelistEntity = listJson.getAl().get(i);
				int mid = Integer.valueOf(messagelistEntity.getID());
				
				if (mid>id) {
					id=mid;
					Data.add(messagelistEntity);
				}
			}
			if (Data.size()!=0) {  
				Data.addAll(0,(Collection<? extends MessagelistEntity>) adapter.getDatas());
				adapter.setmDatas(Data);  
				adapter.notifyDataSetChanged(); 
				listView.setSelection(adapter.getCount()-1);
			}
		}
		
		@Override
		public void responseException(String errorMessage) {
			showMessage(errorMessage);  
		}
	};
	
	
	//聊天数据 
	private int id;
	RequestListener messagelistener=new RequestListener() {
		

		@SuppressWarnings({ "static-access", "unchecked", "unused" })  
		@Override
		public void responseResult(String jsonObject) {  
//			Log.e("", "jsonObject聊天数据   "+jsonObject);
			MessageListJson listJson = new MessageListJson().readJsonToSendmsgObject(MessageActivity.this, jsonObject);
			isrequest=true;
			stoprequest();
			CleanLocaData();
			if (listJson==null) {
				showMessage("数据获取失败！");
//				finish();
				return;
			}
			ll_chat.setVisibility(View.VISIBLE);
			listJson.getAl().addAll((Collection<? extends MessagelistEntity>) adapter.getDatas());
			adapter.setmDatas(listJson.getAl());
			MessagelistEntity item = (MessagelistEntity) adapter.getItem(adapter.getCount()-1);
			id = Integer.valueOf(item.getID());
			adapter.notifyDataSetChanged();  
			listView.setSelection(adapter.getCount()-1);
		}
		    
		@Override
		public void responseException(String errorMessage) {
			CleanLocaData();
			isrequest=true;
			stoprequest();
			showMessage(errorMessage);
//			finish();
		}
	};
	//移除本地数据
	private void CleanLocaData() {
		for (int i = adapter.getDatas().size()-1; i >=0 ; i--) {
			MessagelistEntity entity=(MessagelistEntity)adapter.getDatas().get(i);
			if (entity.isIslocal()) {
				adapter.getDatas().remove(i);
			}
		}
		adapter.notifyDataSetChanged();
	}
	
	RequestListener releaselistener=new RequestListener() {  
		
		@SuppressWarnings("static-access")  
		@Override
		public void responseResult(String jsonObject) {  
			PublicUpJson json=new PublicUpJson().readJsonToSendmsgObject(MessageActivity.this, jsonObject);
			adapter.UPing=false;
			if (json==null) {
				CleanLocaData();
				ed_mymessage.setText(entity.getContent());
				id=Integer.valueOf(entity.getID());
				listView.setSelection(adapter.getCount()-1);
				return;
			}
			listView.setSelection(adapter.getCount()-1);    
			page=1;
			new RequestTask(MessageActivity.this, messagelistener, false, false, "").execute(Httpurl.ChatMessageList(toUserId, page));
		}

		
		
		@Override
		public void responseException(String errorMessage) {      
			adapter.UPing=false;
//			adapter.UP_isOK=true;
			ed_mymessage.setText(entity.getContent());
			showMessage(errorMessage+",请重新发送！");  
		}
	};  
	@Override  
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.image_back:
			finish();  
			break;
		case R.id.ed_mymessage:
//			Log.e("", "12123");
			listView.setSelection(adapter.getCount()-1);
			break;
		case R.id.tv_release://发送
//			Log.e("", "2222");
			String mes = ed_mymessage.getText().toString();
			if (TextUtils.isEmpty(mes)) {  
				showMessage("对不起，小纸条内容不能为空！");
				return;
			}  
			 // 关闭软键盘  
			 InputMethodManager imm = ( InputMethodManager ) v.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );     
		     if ( imm.isActive( ) ) {     
		           imm.hideSoftInputFromWindow( v.getApplicationWindowToken( ) , 0 );   
		     }  
			ed_mymessage.setText("");
			ed_mymessage.setHint("输入你想说的吧！");
			MessagelistEntity item = (MessagelistEntity) adapter.getItem(adapter.getCount()-1);
			entity = new MessagelistEntity(Integer.valueOf(item.getDID())+1+"", Integer.valueOf(item.getID())+1+"", GlobalVariable.UserID, GlobalVariable.UserImage, toUserId, "", mes,true);
			adapter.UPing=true;
			@SuppressWarnings("unchecked")
			ArrayList<MessagelistEntity> al=(ArrayList<MessagelistEntity>) adapter.getDatas();
			al.add(entity);
			adapter.setmDatas(al);
			adapter.notifyDataSetChanged();  
			listView.setSelection(adapter.getCount()-1);  
			new RequestTask(this,ReturnAL.ToMessageList(mes, toUserId),releaselistener, false, false, "").execute(Httpurl.URL);
			break;
		case R.id.image_info:
			Bundle bundle = new Bundle();
			bundle.putInt("type", 5);
			bundle.putString("username", "个人主页");  
			bundle.putString("userid", "2");  
			startActivity(InfoHomepageActivity.class, bundle);  
			break;  
		default:    
			break;  
		}  
	}

	public void setRefrsh() {
		new Handler().postDelayed(new Runnable() {

			@Override  
			public void run() {
				mListView.setRefreshing(true);    
			}
		}, 500);
	}
	
	boolean isrequest = false;// 请求时否完成
	boolean istotime = false;// 请求时间是否结束
	
	public void loadData(final int type) {
		new Thread() {
			@Override
			public void run() {  
				
				switch (type) {
				case 0:
					page++;
					new RequestTask(MessageActivity.this, messagelistener, false, false, "").execute(Httpurl.ChatMessageList(toUserId, page));
					break;       
				case 1:
					break;
				}
				
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}  
				istotime=true;
				stoprequest();
				
			}
		}.start();
	}

	public void stoprequest() {
		if (isrequest && istotime) {  
//			if (type == 0) { // 下拉刷新
				Message _Msg = mHandler.obtainMessage(REFRESH_DATA_FINISH);
				mHandler.sendMessage(_Msg);
//			} else if (type == 1) {
//				Message _Msg = mHandler.obtainMessage(LOAD_DATA_FINISH);
//				mHandler.sendMessage(_Msg);
//			}
		}
	}
	
	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH_DATA_FINISH:
				mListView.onRefreshComplete(); // 下拉刷新完成
				
				break;    
			case LOAD_DATA_FINISH:
				// mListView.onLoadMoreComplete(); // 加载更多完成
				// page++; 
				break;
			default:
				break;
			}  
			if (page==1) {  
				listView.setSelection(adapter.getCount()-1);  
			}
		};
	};  
	
	private TextView mTx_Title;
	private String toUserId;
	private EditText ed_mymessage;
	private ChatListViewAdapter adapter;
	private String toUserName;
	private ListView listView;
	private MessagelistEntity entity;//临时
	private LinearLayout ll_chat;
	@Override
	protected void onPause() {
		GlobalVariable.sendID="-1";
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		GlobalVariable.sendID=toUserId;
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		Intent intent=new Intent(this,MessageChatService.class);
		unbindService(conn);
		stopService(intent);
		super.onDestroy();
	}
}
