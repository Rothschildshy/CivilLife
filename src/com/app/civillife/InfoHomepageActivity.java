package com.app.civillife;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;

import com.CivilLife.Base.BaseActivity;
import com.CivilLife.Base.BaseViewPageAdapter;
import com.CivilLife.Entity.InfoHomeEntity;
import com.CivilLife.Fragment.MyInFo_Fragment;
import com.CivilLife.Json.InfoHomeJson;
import com.CivilLife.net.Httpurl;
import com.CivilLife.net.RequestTask;
import com.handmark.pulltorefresh.extras.viewpager.PullToRefreshViewPager;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;

import Requset_getORpost.RequestListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 拜师傅 附近的人 寻同城老乡 寻同城同行 个人主页 好友主页 共用片段
 */
public class InfoHomepageActivity extends BaseActivity {
	private TextView mTx_Title;
	private PullToRefreshViewPager mViewPager;
	private static final int LOAD_DATA_FINISH = 10;
	private static final int REFRESH_DATA_FINISH = 11;
	// 页数
	private int page = 1;
	// 请求地址
	@SuppressWarnings("unused")
	private String str;

	private boolean isrequest = false;// 请求时否完成
	private boolean istotime = false;// 请求时间是否结束
	private String userid = "";

	private RelativeLayout mLayout_Hint;
	private LinearLayout mLayout_DataNull;
	private LinearLayout mLayout_NetworkError;
	private Button mBtn_Refresh;

	private boolean Master_Apprentice = false;// 是否是师徒

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_homepage);
		initViews();
		initEvents();
		init();
	}

	@Override
	protected void initViews() {
		mTx_Title = (TextView) findViewById(R.id.tx_title);
		mViewPager = (PullToRefreshViewPager) findViewById(R.id.pull_refresh_viewpager);

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
		ViewPager vp = mViewPager.getRefreshableView();
		// 获取片段管理器 上面一个是Activity的 下面一个是在片段中获取片段管理器的
		FragmentManager fm = getSupportFragmentManager();
		myAdpter = new MyAdpter(fm, this, null);
		vp.setAdapter(myAdpter);

		mViewPager
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase refreshView) {
						LeftOrRight = 0;
						loadData(LeftOrRight);
					}

					@Override
					public void onPullUpToRefresh(PullToRefreshBase refreshView) {
						LeftOrRight = 1;
						loadData(LeftOrRight);
					}
				});
	}

	private int LeftOrRight = 0;

	@Override
	protected void init() {
		Intent intent = getIntent();
		Type = intent.getIntExtra("type", 0);
		if (Type == 1) {// 拜师
			Master_Apprentice=true;
			mTx_Title.setText(R.string.discover_overman);
		} else if (Type == 2) {// 附近
			mTx_Title.setText(R.string.discover_nearby);
		} else if (Type == 3) {// 同城老乡
			mTx_Title.setText(R.string.discover_fellow_townsman);
		} else if (Type == 4) {// 同城同行
			mTx_Title.setText(R.string.discover_city_peer);
		} else if (Type == 5) {// 个人主页
			// 关闭侧滑刷新
			mViewPager.setMode(Mode.DISABLED);
			userid = intent.getStringExtra("userid");
			mTx_Title.setText(intent.getStringExtra("username"));
		} else if (Type == 6) {// 搜索筑友
			userid = intent.getStringExtra("content");
			mTx_Title.setText("搜索筑友:" + userid);
		} else if (Type == 7) {// 搜索师傅
			userid = intent.getStringExtra("content");
			Master_Apprentice=true;
			mTx_Title.setText("搜索师傅:" + userid);
		}
		// loadData(0);// 默认请求第一页数据

		new RequestTask(InfoHomepageActivity.this, listlistener, false, true,
				"加载内容").executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.Hometown(Type, page, userid));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.image_back:
			finish();
			break;
		case R.id.btn_refresh:
			mLayout_Hint.setVisibility(View.GONE);
			mViewPager.setVisibility(View.VISIBLE);

			// loadData(0);// 默认请求第一页数据
			new RequestTask(InfoHomepageActivity.this, listlistener, false,
					true, "加载内容").executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.Hometown(Type, page, userid));

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
					new RequestTask(InfoHomepageActivity.this, listlistener,
							false, false, "加载内容").executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.Hometown(
							Type, page, userid));
					break;
				case 1:
					page++;
					new RequestTask(InfoHomepageActivity.this, listlistener,
							false, false, "加载内容").executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.Hometown(
							Type, page, userid));
					break;
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				istotime = true;
				stoprequest(type);
			}
		}.start();
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH_DATA_FINISH:
				mViewPager.onRefreshComplete(); // 左拉刷新完成
				break;
			case LOAD_DATA_FINISH:
				mViewPager.onRefreshComplete(); // 右拉刷新完成
				break;
			default:
				break;
			}
			istotime = false;
			isrequest = false;
		};
	};
	private int Type;

	public void stoprequest(int type2) {
		if (isrequest && istotime) {
			if (type2 == 0) {
				_Msg = mHandler.obtainMessage(REFRESH_DATA_FINISH);
			} else {
				_Msg = mHandler.obtainMessage(LOAD_DATA_FINISH);
			}
			mHandler.sendMessage(_Msg);
		}
	}

	// 请求成功数据回调
	RequestListener listlistener = new RequestListener() {

		@SuppressWarnings("unchecked")
		@Override
		public void responseResult(String jsonObject) {
			InfoHomeJson homeJson = InfoHomeJson.readJsonToSendmsgObject(
					InfoHomepageActivity.this, jsonObject);
			if (homeJson == null) {
				isrequest = true;
				stoprequest(LeftOrRight);
				if (page == 1) {
					mLayout_Hint.setVisibility(View.VISIBLE);
					mLayout_DataNull.setVisibility(View.VISIBLE);
					mLayout_NetworkError.setVisibility(View.GONE);
					mViewPager.setVisibility(View.GONE);
				}
				page -= 1;

				return;
			}
			if (page != 1) {
				homeJson.getAl().addAll(0,
						(Collection<? extends InfoHomeEntity>) myAdpter
								.getDatas());
			}

			mLayout_Hint.setVisibility(View.GONE);
			mViewPager.setVisibility(View.VISIBLE);

			myAdpter.setmDatas(homeJson.getAl());
			myAdpter.notifyDataSetChanged();

			isrequest = true;
			stoprequest(LeftOrRight);
		}

		@Override
		public void responseException(String errorMessage) {
			showShortToast(errorMessage);
			isrequest = true;
			stoprequest(LeftOrRight);
			page -= 1;

			mLayout_Hint.setVisibility(View.VISIBLE);
			mLayout_DataNull.setVisibility(View.GONE);
			mLayout_NetworkError.setVisibility(View.VISIBLE);
			mViewPager.setVisibility(View.GONE);
		}
	};
	private Message _Msg;
	private MyAdpter myAdpter;

	// 一级 viewpager 适配器
	class MyAdpter extends BaseViewPageAdapter {

		public MyAdpter(FragmentManager fm, Context context,
				List<? extends Object> datas) {
			super(fm, context, datas);
		}

		@Override
		public android.support.v4.app.Fragment getItem(int position) {
			InfoHomeEntity info = (InfoHomeEntity) myAdpter.getDatas().get(
					position);
			return new MyInFo_Fragment(info,Master_Apprentice);
		}
	}
}
