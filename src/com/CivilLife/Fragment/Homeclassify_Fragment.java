package com.CivilLife.Fragment;

import java.util.Collection;
import java.util.concurrent.Executors;

import com.CivilLife.Base.BaseFragment;
import com.CivilLife.Entity.HomeEntity;
import com.CivilLife.Json.HomeJson;
import com.CivilLife.MyAdapter.HomeListViewAdapter;
import com.CivilLife.Variable.RequestCode;
import com.CivilLife.net.Httpurl;
import com.CivilLife.net.RequestTask;
import com.app.civillife.ContentDataActivity;
import com.app.civillife.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import Requset_getORpost.RequestListener;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

/***
 * 首页栏目
 * 
 * @author Administrator
 * 
 */
@SuppressLint({ "ValidFragment", "NewApi" })
public class Homeclassify_Fragment extends BaseFragment {
	private PullToRefreshListView mListView;
	private static final int LOAD_DATA_FINISH = 10;
	private static final int REFRESH_DATA_FINISH = 11;
	// 页数
	private int page = 1;
	// 请求地址
	private String str;

	boolean isrequest = false;// 请求时否完成
	boolean istotime = false;// 请求时间是否结束

	private RelativeLayout mLayout_Hint;
	private LinearLayout mLayout_DataNull;
	private LinearLayout mLayout_NetworkError;
	private Button mBtn_Refresh;

	public Homeclassify_Fragment(String str) {

		this.str = str;
	}

	public String RequesturlUrl(int page) {
		String requesturl = Httpurl.newtitle(page);
		if (str.equals("-1")) {
			requesturl = Httpurl.newtitle(page);
		} else if (str.equals("-2")) {
			requesturl = Httpurl.essencetitle(page);
		} else {
			requesturl = Httpurl.nofixedtitle(str, page + "", null);
		}
		return requesturl;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return FragmentCache(R.layout.layout_homeclassify, inflater, container);
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
		adapter = new HomeListViewAdapter(mApplication, getActivity(), null);
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
			HomeJson homeJson = HomeJson.readJsonToSendmsgObject(getActivity(), jsonObject);
			if (homeJson == null) {
				isrequest = true;
				stoprequest();
				if (page == 1) {
					mLayout_Hint.setVisibility(View.VISIBLE);
					mLayout_DataNull.setVisibility(View.VISIBLE);
					mLayout_NetworkError.setVisibility(View.GONE);
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
			}, 1000);
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
					//Executors.newCachedThreadPool()  无限制的线程池
					new RequestTask(getActivity(), listlistener, false, false, "加载内容")
							.executeOnExecutor(Executors.newCachedThreadPool(), RequesturlUrl(page));
					//AsyncTask.THREAD_POOL_EXECUTOR   调用默认设置好的5个线程池
//					new RequestTask(getActivity(), listlistener, false, false, "加载内容")
//							.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, RequesturlUrl(page));
					break;
				case 1:
					page++;
					new RequestTask(getActivity(), listlistener, false, false, "加载内容").executeOnExecutor(Executors.newCachedThreadPool(), RequesturlUrl(page));
					break;
				}
				try {
					Thread.sleep(1000);
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
				// mListView.onRefreshComplete(); // 加载更多完成
				break;
			default:
				break;
			}
		};
	};
	private HomeListViewAdapter adapter;
	private ListView listView;

	public void stoprequest() {
		if (isrequest && istotime) {
			Message _Msg = mHandler.obtainMessage(REFRESH_DATA_FINISH);
			mHandler.sendMessage(_Msg);
		}
	}

	@Override
	public void onPause() {
		if (adapter.videoPay != null) {
			adapter.videoPay.setPause();
			adapter.setVideoPay(null);
			adapter.ispay = -1;
			adapter.notifyDataSetChanged();
		}
		super.onPause();
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
	}
}
