package com.app.civillife;

import java.util.Collection;
import java.util.concurrent.Executors;

import com.CivilLife.Base.BaseActivity;
import com.CivilLife.Entity.MyManageEntity;
import com.CivilLife.Entity.PublicEntity;
import com.CivilLife.Json.MyManageJson;
import com.CivilLife.Json.PublicUpJson;
import com.CivilLife.MyAdapter.ManageListViewAdapter;
import com.CivilLife.Variable.RequestCode;
import com.CivilLife.Widget.AlertDialogEx;
import com.CivilLife.Widget.AlertDialogEx.Builder;
import com.CivilLife.net.Httpurl;
import com.CivilLife.net.RequestTask;
import com.aysy_mytool.SpUtils;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.XOnRefreshListener;
import com.baoyz.widget.PullRefreshLayout;
import com.baoyz.widget.PullRefreshLayout.OnRefreshListener;

import Requset_getORpost.RequestListener;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 管理我的文字
 * 
 * @author Administrator
 * 
 */
public class ManageActivity extends BaseActivity implements XOnRefreshListener {
	private ManageListViewAdapter mAdapter;
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage);
		initViews();
		initEvents();
		init();
	}

	@Override
	protected void initViews() {
		mLayout = (PullRefreshLayout) findViewById(R.id.pullrefreshlayout);
		mListView = (SwipeMenuListView) findViewById(R.id.swipemenulistview);

		mLayout_Hint = (RelativeLayout) findViewById(R.id.layout_hint);
		mLayout_DataNull = (LinearLayout) findViewById(R.id.layout_data_null);
		mLayout_NetworkError = (LinearLayout) findViewById(R.id.layout_network_error);
		mBtn_Refresh = (Button) findViewById(R.id.btn_refresh);
		mBtn_Refresh.setOnClickListener(this);
	}

	@Override
	protected void initEvents() {
		findViewById(R.id.image_back).setOnClickListener(this);
	}

	@Override
	protected void init() {
		boolean isFirstdelete = SpUtils.getBoolean(ManageActivity.this, "isFirstdelete");
		if (!isFirstdelete) {
			new AlertDialogEx(this).setBuilder(new Builder(this).setMessage("可以侧滑删除文字，来管理您的文字！").setPositiveButton("知道了，不再提示！", new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					SpUtils.saveBoolean(ManageActivity.this, "isFirstdelete", true);
				}
			}, true).show());
		}
		// 先设置颜色在设置风格，颜色固定4个，否则异常
		mLayout.setColorSchemeColors(new int[] { Color.RED, Color.BLUE,
				Color.YELLOW, Color.GREEN });
		mLayout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
		mAdapter = new ManageListViewAdapter(mApplication, this, null);  
		mListView.setAdapter(mAdapter);
		// 设置是否使用上拉加载功能
		mListView.setPullLoadEnable(false);
		// ListView 行点击跳转
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				MyManageEntity info = (MyManageEntity) mAdapter.getDatas().get(
						arg2);
				String id = info.getID();
				open(id);
			}
		});
		// 下拉布局刷新监听
		mLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				loadData(0);
			}
		});
		mListView.setXOnRefreshListener(this);
		// step 2. listener item click event PublishActivity
		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(final int position, SwipeMenu menu,
					int index) {
				MyManageEntity info = (MyManageEntity) mAdapter.getDatas().get(
						position);
				final String id = info.getID();
				switch (index) {
				case 0:
					// open
					open(id);
					break;
				case 1:
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							// delete
							delete(id);
							mAdapter.notifyDataSetChanged();
							// 菜单回弹事件
							mListView.smoothOpenOrCloseMenu(position);
						}
					});
					break;
				}
			}
		});
		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "open" item
				SwipeMenuItem openItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
						0xCE)));
				// set item width
				openItem.setWidth(dp2px(90));
				// set item title
				openItem.setTitle("编辑");
				// set item title fontsize
				openItem.setTitleSize(18);
				// set item title font color
				openItem.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(openItem);

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
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
		case R.id.image_back:
			finish();
			break;
		case R.id.btn_refresh:
			mLayout_Hint.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);

			page = 1;
			mLayout.setRefreshing(true);
			loadData(0);

			break;
		default:
			break;
		}
	}

	// 获取个人发表的文章信息
	RequestListener listener = new RequestListener() {

		@Override
		public void responseResult(String jsonObject) {
//			Qlog.e("", "jsonObject管理我的文章  "+jsonObject);
			MyManageJson Myjson = MyManageJson.readJsonToSendmsgObject(
					ManageActivity.this, jsonObject);
			if (Myjson == null) {
				isrequest = true;
				stoprequest();
				if (page == 1) {
					mLayout_Hint.setVisibility(View.VISIBLE);
					mLayout_DataNull.setVisibility(View.VISIBLE);
					mLayout_NetworkError.setVisibility(View.GONE);
				}else{
					showShortToast("没有更多数据");
				}
				page -= 1;
				return;
			}
			if (page != 1) {
				Myjson.getAl().addAll(
						0,
						(Collection<? extends MyManageEntity>) mAdapter
								.getDatas());
			}
			if (page == 1 && Myjson.getAl().size() > 9) {
				// 当第一页数据超过九条的时候设置可上拉加载
				mListView.setPullLoadEnable(true);
			}

			mLayout_Hint.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);

			mAdapter.setmDatas(Myjson.getAl());
			mAdapter.notifyDataSetChanged();
			mLayout.setRefreshing(false);
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

	private void delete(final String id) {
		PromptDialogs("确定删除文字吗？", new OnClickListener() {

			@Override
			public void onClick(View v) {
				new RequestTask(ManageActivity.this, DELlistener, false, true,
						"删除中").executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.DelMyManage(id));
			}
		});
	}

	private void open(String id) {
		Bundle bundle = new Bundle();
		bundle.putInt("TYPE", 2);
		bundle.putString("ID", id);
		startActivityForResult(PublishActivity.class, bundle,
				RequestCode.publiccode);
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

	RequestListener DELlistener = new RequestListener() {

		@Override
		public void responseResult(String jsonObject) {
			
			PublicUpJson publicjson = PublicUpJson.readJsonToSendmsgObject(
					ManageActivity.this, jsonObject);
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
					new RequestTask(ManageActivity.this, listener, false,
							false, "数据加载").executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.GetMyManage(page));
					break;
				case 1:
					page++;
					new RequestTask(ManageActivity.this, listener, false,
							false, "数据加载").executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.GetMyManage(page));
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
				mLayout.setRefreshing(false);// 下拉刷新完成
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
