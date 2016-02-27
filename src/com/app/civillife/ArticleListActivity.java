package com.app.civillife;

import java.util.Collection;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.CivilLife.Base.BaseActivity;
import com.CivilLife.Entity.HomeEntity;
import com.CivilLife.Json.HomeJson;
import com.CivilLife.MyAdapter.HomeListViewAdapter;
import com.CivilLife.Variable.GlobalVariable;
import com.CivilLife.Variable.RequestCode;
import com.CivilLife.net.Httpurl;
import com.CivilLife.net.RequestTask;
import com.app.civillife.Util.GetDistance;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import Downloadimage.ImageUtils;
import Requset_getORpost.RequestListener;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 师徒社区 同城社区 老乡社区 个人发表文章 好友发表的文章
 * 
 * @author mac
 * 
 */
public class ArticleListActivity extends BaseActivity {
	private TextView mTx_Title;
	private PullToRefreshListView mListView;
	private static final int LOAD_DATA_FINISH = 10;
	private static final int REFRESH_DATA_FINISH = 11;
	// 页数
	private int page = 1;
	int Types;
	boolean isrequest = false;// 请求时否完成
	boolean istotime = false;// 请求时间是否结束
	private int ARTID = -1;// 社区id
	private RelativeLayout mLayout_Hint;
	private LinearLayout mLayout_DataNull;
	private LinearLayout mLayout_NetworkError;
	private Button mBtn_Refresh;
	private String City;
	private HomeListViewAdapter adapter;
	private String userid;
	private ListView listView;
	private ImageView image_article;
	private TextView tv_rolling;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article_list);
		initViews();
		initEvents();
		init();
	}

	@Override
	protected void initViews() {
		mTx_Title = (TextView) findViewById(R.id.tx_title);
		tv_rolling = (TextView) findViewById(R.id.tv_rolling);
		image_article = (ImageView) findViewById(R.id.image_article);
		mListView = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView);

		mLayout_Hint = (RelativeLayout) findViewById(R.id.layout_hint);
		mLayout_DataNull = (LinearLayout) findViewById(R.id.layout_data_null);
		mLayout_NetworkError = (LinearLayout) findViewById(R.id.layout_network_error);
		mBtn_Refresh = (Button) findViewById(R.id.btn_refresh);
		mBtn_Refresh.setOnClickListener(this);
		image_article.setOnClickListener(this);
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void initEvents() {
		findViewById(R.id.image_back).setOnClickListener(this);
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
				HomeEntity item = (HomeEntity) adapter.getDatas().get(arg2 - 1);
				bundle.putBoolean("comment", false);
				bundle.putParcelable("HomeEntity", item);
				startActivityForResult(ContentDataActivity.class, bundle, RequestCode.publiccode);
			}
		});
	}

	@Override
	protected void init() {
		Intent intent = getIntent();
		Types = intent.getIntExtra("type", 0);

		if (Types == 1) {
			image_article.setVisibility(View.VISIBLE);
			mTx_Title.setText(R.string.discover_overman_community);// 师徒
		} else if (Types == 2) {
			tv_rolling.setText("");
			tv_rolling.setVisibility(View.VISIBLE);
			image_article.setVisibility(View.VISIBLE);
			mTx_Title.setText(R.string.discover_fellow_community);// 老乡
			ARTID = 26;
			new RequestTask(this, onlinerlistener, false, false, "").executeOnExecutor(Executors.newCachedThreadPool(),
					Httpurl.OnOnlineUsers(Types));
		} else if (Types == 3) {
			tv_rolling.setText("");
			tv_rolling.setVisibility(View.VISIBLE);
			ARTID = 27;
			image_article.setVisibility(View.VISIBLE);
			mTx_Title.setText(R.string.discover_city_community);// 同城
			new RequestTask(this, onlinerlistener, false, false, "").executeOnExecutor(Executors.newCachedThreadPool(),
					Httpurl.OnOnlineUsers(Types));
		} else if (Types == 4) {
			String UserName = intent.getStringExtra("UserName");// 个人文章
			userid = intent.getStringExtra("Userid");
			mTx_Title.setText(UserName);

		}

		adapter = new HomeListViewAdapter(mApplication, this, null);
		mListView.setAdapter(adapter);
		listView = mListView.getRefreshableView();
		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				if (adapter.ispay != -1) {
					int firstPosition = listView.getFirstVisiblePosition();
					int Lastposition = listView.getLastVisiblePosition();
					if (adapter.ispay + 1 < firstPosition || adapter.ispay + 1 > Lastposition) {
						adapter.InitVideo();
					}
				}
			}

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {

			}
		});

		// 自动下拉刷新
		mListView.postDelayed(new Runnable() {
			@Override
			public void run() {
				mListView.setRefreshing(true);
			}
		}, 1000);

	}

	RequestListener listlistener = new RequestListener() {

		@SuppressWarnings("unchecked")
		@Override
		public void responseResult(String jsonObject) {
			HomeJson homeJson = HomeJson.readJsonToSendmsgObject(ArticleListActivity.this, jsonObject);
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
				homeJson.getAl().addAll(0, (Collection<? extends HomeEntity>) adapter.getDatas());
			}
			mLayout_Hint.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);

			adapter.setmDatas(homeJson.getAl());
			adapter.hm.clear();
			adapter.notifyDataSetChanged();
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.image_back:
			finish();
			break;
		case R.id.image_article:
			if (tologin()) {
				return;
			}
			Bundle bundle = new Bundle();
			bundle.putInt("TYPE", 10);
			bundle.putString("artid", ARTID + "");
			bundle.putString("artname", mTx_Title.getText().toString());
			startActivityForResult(PublishActivity.class, bundle, RequestCode.publiccode);
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

	public void loadData(final int type) {
		new Thread() {
			@Override
			public void run() {
				switch (type) {
				case 0:
					page = 1;
					if (Types == 4) {
						new RequestTask(ArticleListActivity.this, listlistener, false, false, "").executeOnExecutor(
								Executors.newCachedThreadPool(), Httpurl.GetPersonalarticles(page, userid));
					} else {
						new RequestTask(ArticleListActivity.this, listlistener, false, false, "")
								.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.Hometown1(Types, page));
					}
					break;
				case 1:
					page++;
					if (Types == 4) {
						new RequestTask(ArticleListActivity.this, listlistener, false, false, "").executeOnExecutor(
								Executors.newCachedThreadPool(), Httpurl.GetPersonalarticles(page, userid));
					} else {
						new RequestTask(ArticleListActivity.this, listlistener, false, false, "")
								.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.Hometown1(Types, page));
					}
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
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg2 != null) {
			if (arg0 == RequestCode.publiccode && arg1 == 33) {

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

	/** 社区人数统计 **/
	RequestListener onlinerlistener = new RequestListener() {

		@Override
		public void responseResult(String jsonObject) {
			try {
				JSONObject object = new JSONObject(jsonObject);
				JSONArray DataArray = object.getJSONArray("Data");
				JSONObject object2 = DataArray.getJSONObject(0);
				final String OnlineUsers = object2.getString("OnlineUsers");
				TipsVisible(OnlineUsers);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void responseException(String errorMessage) {
			showShortToast(errorMessage);
			TipsVisible("(计算错误)");
		}
	};

	private void TipsVisible(final String OnlineUsers) {
		tv_rolling.setVisibility(View.VISIBLE);
		String city = GlobalVariable.City;
		if (city.equals("")) {
			SetCityTips("???", " 定位中... ");
			// 启动定位 传位置坐标到服务器
			GetDistance.location(ArticleListActivity.this, new Handler() {

				public void handleMessage(android.os.Message msg) {
					if (msg.what == 11) {
						SetCityTips(OnlineUsers, GlobalVariable.City);
					}
				};
			}).start();
		} else {
			SetCityTips(OnlineUsers, city);
		}
	}
	private void SetCityTips(String OnlineUsers, String city) {
		if (Types == 3) {
			City = "同城";
		} else if (Types == 2) {
			City = "老乡";
		}
		tv_rolling.setText("来自" + city + "的" + City + "人数" + OnlineUsers + "人，请遵守法律法规，共同遵维护社区秩序！");
		tv_rolling.setFocusableInTouchMode(true);
		tv_rolling.setFocusable(true);
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
