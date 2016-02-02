package com.app.civillife;

import java.util.Collection;
import java.util.concurrent.Executors;

import com.CivilLife.Base.BaseActivity;
import com.CivilLife.Entity.HomeEntity;
import com.CivilLife.Json.HomeJson;
import com.CivilLife.MyAdapter.ManageListViewAdapter;
import com.CivilLife.Variable.GlobalVariable;
import com.CivilLife.Variable.RequestCode;
import com.CivilLife.net.Httpurl;
import com.CivilLife.net.RequestTask;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import Requset_getORpost.RequestListener;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * 管理我的文字
 * 
 * @author Administrator
 * 
 */
public class ManageActivity extends BaseActivity {
	private ManageListViewAdapter mAdapter;
	private PullToRefreshListView mListView;
	private ListView listView;
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage);
		initViews();
		initEvents();
		init();
	}

	@Override
	protected void initViews() {
		mListView = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView);

		mLayout_Hint = (RelativeLayout) findViewById(R.id.layout_hint);
		mLayout_DataNull = (LinearLayout) findViewById(R.id.layout_data_null);
		mLayout_NetworkError = (LinearLayout) findViewById(R.id.layout_network_error);
		mBtn_Refresh = (Button) findViewById(R.id.btn_refresh);
		mBtn_Refresh.setOnClickListener(this);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void initEvents() {
		findViewById(R.id.image_back).setOnClickListener(this);
		mAdapter = new ManageListViewAdapter(mApplication, this, null);
		listView = mListView.getRefreshableView();
		mListView.setAdapter(mAdapter);
		mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				loadData(0);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				loadData(1);
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Bundle bundle = new Bundle();
				HomeEntity item = (HomeEntity) mAdapter.getDatas().get(arg2 - 1);
				bundle.putBoolean("comment", false);
				bundle.putParcelable("HomeEntity", item);
				startActivityForResult(ContentDataActivity.class, bundle, RequestCode.publiccode);
			}
		});
		// 设置视频播放的控制
		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				if (mAdapter.ispay != -1) {
					int firstPosition = listView.getFirstVisiblePosition();
					int Lastposition = listView.getLastVisiblePosition();
					if (mAdapter.ispay + 1 < firstPosition || mAdapter.ispay + 1 > Lastposition) {
						mAdapter.InitVideo();
					}
				}
			}

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {

			}
		});
	}

	@Override
	protected void init() {
		// 自动下拉刷新
		mListView.postDelayed(new Runnable() {
			@Override
			public void run() {
				mListView.setRefreshing(true);
			}
		}, 1000);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.image_back:
			finish();
			break;
		case R.id.btn_refresh:
			mLayout_Hint.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);
			page = 1;

			mListView.setRefreshing(true);
			// 自动下拉刷新
			mListView.postDelayed(new Runnable() {
				@Override
				public void run() {
					loadData(0);
				}
			}, 2000);
			break;
		default:
			break;
		}
	}

	// 获取个人发表的文章信息
	RequestListener listener = new RequestListener() {

		@SuppressWarnings("unchecked")
		@Override
		public void responseResult(String jsonObject) {
			HomeJson homeJson = HomeJson.readJsonToSendmsgObject(ManageActivity.this, jsonObject);
			if (homeJson == null) {
				isrequest = true;
				stoprequest();
				if (page == 1) {
					mLayout_Hint.setVisibility(View.VISIBLE);
					mLayout_DataNull.setVisibility(View.VISIBLE);
					mLayout_NetworkError.setVisibility(View.GONE);
				} else {
					showShortToast("没有更多数据");
				}
				page -= 1;
				return;
			}
			if (page != 1) {
				homeJson.getAl().addAll(0, (Collection<? extends HomeEntity>) mAdapter.getDatas());
			}
			mLayout_Hint.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);

			mAdapter.setmDatas(homeJson.getAl());
			// mAdapter.hm.clear();
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

	public void loadData(final int type) {
		new Thread() {
			@Override
			public void run() {
				switch (type) {
				case 0:
					page = 1;
					new RequestTask(ManageActivity.this, listener, false, false, "数据加载").executeOnExecutor(
							Executors.newCachedThreadPool(), Httpurl.GetPersonalarticles(page, GlobalVariable.UserID));
					break;
				case 1:
					page++;
					new RequestTask(ManageActivity.this, listener, false, false, "数据加载").executeOnExecutor(
							Executors.newCachedThreadPool(), Httpurl.GetPersonalarticles(page, GlobalVariable.UserID));
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
				mListView.onRefreshComplete(); // 下拉刷新完成
				istotime = false;
				isrequest = false;
				break;
			case LOAD_DATA_FINISH:
				// mListView.onLoadMoreComplete(); // 加载更多完成
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

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent data) {
		super.onActivityResult(arg0, arg1, data);
		if (data != null) {
			if (arg0 == RequestCode.publiccode && arg1 == 34) {
				// 自动下拉刷新
				mListView.postDelayed(new Runnable() {
					@Override
					public void run() {
						mListView.setRefreshing(true);
					}
				}, 1000);
			}
		}
	}

	// 友盟统计
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	// 友盟统计
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
