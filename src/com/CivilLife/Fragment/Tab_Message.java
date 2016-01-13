package com.CivilLife.Fragment;

import java.util.Collection;

import Requset_getORpost.RequestListener;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.CivilLife.Base.BaseFragment;
import com.CivilLife.Entity.MessageEntity;
import com.CivilLife.Entity.PublicEntity;
import com.CivilLife.Json.MessageJson;
import com.CivilLife.Json.PublicUpJson;
import com.CivilLife.MyAdapter.MessageListViewAdapter;
import com.CivilLife.Variable.GlobalVariable;
import com.CivilLife.net.Httpurl;
import com.CivilLife.net.RequestTask;
import com.app.civillife.MessageActivity;
import com.app.civillife.R;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.XOnRefreshListener;
import com.baoyz.widget.PullRefreshLayout;
import com.baoyz.widget.PullRefreshLayout.OnRefreshListener;

/***
 * 消息片段
 * 
 * @author Administrator
 * 
 */
public class Tab_Message extends BaseFragment implements XOnRefreshListener {
	private MessageListViewAdapter mAdapter;
	private SwipeMenuListView mListView;

	private PullRefreshLayout mLayout;
	private int page = 1;
	private static final int LOAD_DATA_FINISH = 10;
	private static final int REFRESH_DATA_FINISH = 11;
	boolean isrequest = false;// 请求时否完成
	boolean istotime = false;// 请求时间是否结束
	private RelativeLayout mLayout_Hint;
	private LinearLayout mLayout_DataNull;
	private LinearLayout mLayout_NetworkError;
	private Button mBtn_Refresh;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return FragmentCache(R.layout.tab_message, inflater, container);
	}

	@Override
	protected void initViews() {
		mListView = (SwipeMenuListView) findViewById(R.id.swipemenulistview);
		mLayout = (PullRefreshLayout) findViewById(R.id.pullrefreshlayout);

		mLayout_Hint = (RelativeLayout) findViewById(R.id.layout_hint);
		mLayout_DataNull = (LinearLayout) findViewById(R.id.layout_data_null);
		mLayout_NetworkError = (LinearLayout) findViewById(R.id.layout_network_error);
		mBtn_Refresh = (Button) findViewById(R.id.btn_refresh);
		mBtn_Refresh.setOnClickListener(this);
	}

	@Override
	protected void initEvents() {

	}
	@Override
	public void onResume() {  
		super.onResume();
		loadData(0);

	}

	@Override
	protected void init() {
		
		// 先设置颜色在设置风格，颜色固定4个，否则异常
		mLayout.setColorSchemeColors(new int[] { Color.RED, Color.BLUE,
				Color.YELLOW, Color.GREEN });
		mLayout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);

		mAdapter = new MessageListViewAdapter(mApplication, getActivity(), null,this);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
					
					Bundle bundle = new Bundle();
					MessageEntity item = (MessageEntity) mAdapter.getDatas().get(
							arg2);
					bundle.putString("ToUserId", "" + item.getSendID());// 需要传ID
					bundle.putString("ToUserName", ""+item.getTitle());// 需要对方名称
					startActivity(MessageActivity.class, bundle);
				
			}
		});

		// 设置是否使用上拉加载功能
		mListView.setPullLoadEnable(false);
		// 下拉布局刷新监听
		mLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				loadData(0);
			}
		});
		mListView.setXOnRefreshListener(this);
		// step 2. listener item click event
		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				MessageEntity datas = (MessageEntity) mAdapter.getDatas().get(
						position);
				delete(datas.getID());
				// mAppList.remove(position);
				mAdapter.notifyDataSetChanged();
			}
		});
		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity()
						.getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(90));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		mListView.setMenuCreator(creator);

		mLayout.post(new Runnable() {

			@Override
			public void run() {
				mLayout.setRefreshing(true);
				loadData(0);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_refresh:
			mLayout_Hint.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);

			mLayout.setRefreshing(true);
			loadData(0);

			break;
		default:
			break;
		}
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

	// 获取消息列表回调
	RequestListener GetMessageList = new RequestListener() {

		@SuppressWarnings("unchecked")
		@Override
		public void responseResult(String jsonObject) {
			MessageJson messageJson = MessageJson.readJsonToSendmsgObject(
					getActivity(), jsonObject);
			if (messageJson == null) {
				isrequest = true;
				stoprequest();
				if (page == 1) { 
					mAdapter.getmDatas().clear(); 
					mLayout_Hint.setVisibility(View.VISIBLE);
					mLayout_DataNull.setVisibility(View.VISIBLE);
					mLayout_NetworkError.setVisibility(View.GONE);
				}else{
					showShortToast("最后一页");
				}
				page -= 1;
				return;
			}
			if (page != 1) {
				messageJson.getAl().addAll(0,(Collection<? extends MessageEntity>) mAdapter.getDatas());
			}
			if (page == 1 && messageJson.getAl().size() > 9) {
				// 当第一页数据超过九条的时候设置可上拉加载
				mListView.setPullLoadEnable(true);
			}
			mLayout_Hint.setVisibility(View.GONE);
			mAdapter.setmDatas(messageJson.getAl());
			mAdapter.notifyDataSetChanged();
			isrequest = true;
			stoprequest();
		}

		@Override
		public void responseException(String errorMessage) {
			showShortToast(errorMessage);
			isrequest = true;
			stoprequest();
			page -= 1;

			mLayout_Hint.setVisibility(View.VISIBLE);
			mLayout_DataNull.setVisibility(View.GONE);
			mLayout_NetworkError.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
		}
	};

	public void delete(final String id) {
		PromptDialogs("确定删除消息?", new OnClickListener() {

			@Override
			public void onClick(View v) {
				new RequestTask(getActivity(), DELlistener, false, true, "删除中")
						.execute(Httpurl.DelMessage(id));
			}
		});
	}

	RequestListener DELlistener = new RequestListener() {

		@Override
		public void responseResult(String jsonObject) {
			PublicUpJson publicjson = PublicUpJson.readJsonToSendmsgObject(
					getActivity(), jsonObject);
			if (publicjson == null) {
				return;
			}
			PublicEntity publicEntity = publicjson.getAl().get(0);
			String status = publicEntity.getStatus();
			if (status.equals("1")) {
				page = 1;
				mLayout.setRefreshing(true);
				loadData(0);
			} else {
				showShortToast(publicEntity.getMessage());
			}

		}

		@Override
		public void responseException(String errorMessage) {
			showShortToast(errorMessage);
		}
	};

	@Override
	public void onLoadMore() {
		loadData(1);
	}

	public void loadData(final int type) {
		new Thread() {
			@Override
			public void run() {
				switch (type) {
				case 0:
					page = 1;
					new RequestTask(getActivity(), GetMessageList, false,
							false, "Login..")
							.execute(Httpurl.MessageList(page));

					break;
				case 1:
					page++;
					new RequestTask(getActivity(), GetMessageList, false,
							false, "Login..")
							.execute(Httpurl.MessageList(page));

					break;
				}
				
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				istotime = true;
				stoprequest();
			}
		}.start();
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH_DATA_FINISH:
				mLayout.setRefreshing(false);
				mListView.stopLoadMore();// 加载更多完成
				istotime = false;
				isrequest = false;
				break;
			case LOAD_DATA_FINISH:
				mListView.stopLoadMore();// 加载更多完成
				break;
			default:
				break;
			}
		};
	};

	public void stoprequest() {
		if (isrequest && istotime) {
			Message _Msg = mHandler.obtainMessage(REFRESH_DATA_FINISH);
			mHandler.sendMessage(_Msg);
		}
	}

}
