package com.app.civillife;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;

import com.CivilLife.Base.BaseActivity;
import com.CivilLife.Entity.HomeEntity;
import com.CivilLife.Entity.PublicEntity;
import com.CivilLife.Json.HomeJson;
import com.CivilLife.Json.PublicUpJson;
import com.CivilLife.MyAdapter.CommentListViewAdapter;
import com.CivilLife.MyAdapter.HomeimagegridviewAdapter;
import com.CivilLife.Variable.GlobalVariable;
import com.CivilLife.Widget.CircleImageView;
import com.CivilLife.Widget.ListViewScrollView;
import com.CivilLife.Widget.URLImageParser;
import com.CivilLife.net.Httpurl;
import com.CivilLife.net.RequestTask;
import com.CivilLife.net.ReturnAL;
import com.app.civillife.Util.CommonAPI;
import com.app.civillife.Util.ImagePagerActivity;
import com.app.civillife.Util.VideoPay;
import com.app.civillife.Util.Videolistener;
import com.aysy_mytool.Time;
import com.aysy_mytool.ToastUtil;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import Downloadimage.ImageUtils;
import Requset_getORpost.RequestListener;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * 发布的文章详情
 * 
 * @author mac
 * 
 */
public class ContentDataActivity extends BaseActivity {
	private ListViewScrollView mListView;
	private static final int LOAD_DATA_FINISH = 10;
	private static final int REFRESH_DATA_FINISH = 11;

	// 页数
	private int page = 1;
	private LinearLayout mHeadLyout;// 头部布局
	private CircleImageView mIm_Pic;// 头像
	private TextView mTx_Name;// 昵称
	private TextView mTx_Content;// 评论内容
	private RadioButton mRb_Favour;// 好文
	private RadioButton mRb_Disapprove;// 不好文
	private TextView mTx_CommentNum;// 评论数
	private TextView mTx_FunnyNum;// 好文数
	private VideoView video;// 视频
	private GridView mImageGR;// 图标墙
	private LinearLayout mLa_CommentEdit;// 评论布局
	private ImageView mIm_Play;// 视频播放
	private ImageView video_image;// 视频缩略图
	private EditText mEd_Comment;// 评论输入框
	private boolean isComment = false;// 是否是发表评论
	private boolean isoperation = false;// 当前页面是否做了操作
	private int isstr = -1;// 好文和不好文标记

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content_data);
		initViews();
		initEvents();
		init();
	}

	@Override
	protected void initViews() {

		mListView = (ListViewScrollView) findViewById(R.id.pullToRefreshListView);
		mScrollView = (PullToRefreshScrollView) findViewById(R.id.pullToRefreshScrollView);
		mHeadLyout = (LinearLayout) getLayoutInflater().inflate(R.layout.home_info_list_item2, null);
		mIm_Pic = (CircleImageView) mHeadLyout.findViewById(R.id.image_pic);
		mTx_Name = (TextView) mHeadLyout.findViewById(R.id.tx_name);
		mTx_Content = (TextView) mHeadLyout.findViewById(R.id.tx_content);
		mImageGR = (GridView) mHeadLyout.findViewById(R.id.image_gridView);
		video = (VideoView) mHeadLyout.findViewById(R.id.videoView1);
		mIm_Play = (ImageView) mHeadLyout.findViewById(R.id.image_play);
		video_image = (ImageView) mHeadLyout.findViewById(R.id.video_image);
		mTx_FunnyNum = (TextView) mHeadLyout.findViewById(R.id.tx_funny_num);
		mTx_CommentNum = (TextView) mHeadLyout.findViewById(R.id.tx_comment_num);
		mRb_Favour = (RadioButton) mHeadLyout.findViewById(R.id.radio_favour);
		mRb_Disapprove = (RadioButton) mHeadLyout.findViewById(R.id.radio_disapprove);
		mLa_CommentEdit = (LinearLayout) findViewById(R.id.layout_edit_comment);
		mEd_Comment = (EditText) findViewById(R.id.edit_comment);
		layout_video = (RelativeLayout) mHeadLyout.findViewById(R.id.layout_video);
		pb_waiting = (ProgressBar) mHeadLyout.findViewById(R.id.pb_waiting);
		tv_addtime = (TextView) mHeadLyout.findViewById(R.id.tv_addtime);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void initEvents() {
		findViewById(R.id.image_back).setOnClickListener(this);
		layout_video.setOnClickListener(this);
		mHeadLyout.findViewById(R.id.image_comment).setOnClickListener(this);
		mHeadLyout.findViewById(R.id.image_share).setOnClickListener(this);
		findViewById(R.id.tx_updata).setOnClickListener(this);
		mRb_Favour.setOnClickListener(this);
		mRb_Disapprove.setOnClickListener(this);
		mIm_Pic.setOnClickListener(this);
		mTx_Name.setOnClickListener(this);
		mRb_Favour.setOnClickListener(this);
		mRb_Disapprove.setOnClickListener(this);
		mIm_Play.setOnClickListener(this);
		mImageGR.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(ContentDataActivity.this, ImagePagerActivity.class);
				ArrayList<String> al = new ArrayList<String>();
				al.addAll(asList);
				intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, al);
				intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, arg2);
				startActivity(intent);
			}
		});
		mScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				loadData(0);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				loadData(1);
			}
		});

	}

	RequestListener listener = new RequestListener() {

		@Override
		public void responseResult(String jsonObject) {
			@SuppressWarnings("static-access")
			HomeJson json = new HomeJson().readJsonToSendmsgObject(ContentDataActivity.this, jsonObject);
			if (json != null) {
				homeEntity = json.getAl().get(0);
				setData(json.getAl().get(0));
			} else {
				finish();
			}
		}

		@Override
		public void responseException(String errorMessage) {
			showShortToast(errorMessage);
			finish();
		}
	};

	@Override
	protected void init() {
		Intent intent = getIntent();

		boolean IsComment = intent.getBooleanExtra("comment", false);// 是否直接跳出评论框
		if (IsComment) {
			mLa_CommentEdit.setVisibility(View.VISIBLE);
			mEd_Comment.setFocusable(true);// 直接获取焦点弹出软键盘
		}
		if (intent.getIntExtra("TYPE", -1) == 0) {
			String ToUserId = intent.getStringExtra("ToUserId");
			new RequestTask(this, listener, false, true, "").executeOnExecutor(Executors.newCachedThreadPool(),
					Httpurl.GetIDArticle(ToUserId));
		} else {
			homeEntity = intent.getParcelableExtra("HomeEntity");
			setData(homeEntity);
		}

	}

	@SuppressWarnings("static-access")
	private void setData(HomeEntity homeEntity) {

		if (!TextUtils.isEmpty(homeEntity.getUserInfoPicUrl()) && !homeEntity.getNickname().equals("匿名")) {
			ImageUtils.loadImage1(this, homeEntity.getUserInfoPicUrl(), mIm_Pic, R.drawable.ic_my_nolog_selector,
					R.drawable.ic_my_nolog_selector, GlobalVariable.WifiDown);
		}
		mTx_Name.setText(homeEntity.getNickname());
		// Html解析
		mTx_Content.setText(Html.fromHtml(homeEntity.getContent(), new URLImageParser(this, mTx_Content), null));
		// mTx_Content.setText(homeEntity.getContent());
		mTx_FunnyNum.setText(homeEntity.getPraises() + "好文");
		mTx_CommentNum.setText(homeEntity.getReviews() + "评论");
		tv_addtime.setText(Time.handTime(homeEntity.getAddTime(), "yyyy/MM/dd HH:mm:ss"));
		if (!TextUtils.isEmpty(homeEntity.getPicUrl())) {
			String[] images = homeEntity.getPicUrl().split(",");
			asList = Arrays.asList(images);
			HomeimagegridviewAdapter adapter = new HomeimagegridviewAdapter(mApplication, this, asList);
			mImageGR.setAdapter(adapter);
			mImageGR.setVisibility(View.VISIBLE);
		}
		if (!TextUtils.isEmpty(homeEntity.getVideoUrl())) {
			layout_video.setVisibility(View.VISIBLE);
			videoPay = new VideoPay(this, video, homeEntity.getVideoUrl(), videolistener);
		} else {
			layout_video.setVisibility(View.GONE);
		}
		ArrayList<HomeEntity> datas = new ArrayList<HomeEntity>();
		contentadapter = new CommentListViewAdapter(mApplication, this, datas);
		// listView.setEmptyView(emptyView);
		mListView.addHeaderView(mHeadLyout);
		mListView.setAdapter(contentadapter);
		setRefrsh();
		setRidio();
		new ImageUtils().loadImage3(this, homeEntity.getVideoThumbnailsPicUrl(), video_image, false);
		mScrollView.setMode(Mode.BOTH);
		new RequestTask(this, contentlistener, false, false, "拼命加载中").executeOnExecutor(Executors.newCachedThreadPool(),
				Httpurl.Contenturl(homeEntity.getID(), "1"));
	}

	// 好文不好文判定
	private void setRidio() {
		if (homeEntity.getPraise().equals("1")) {
			isstr = 1;
			mRb_Favour.setChecked(true);
			mRb_Disapprove.setChecked(false);
		} else if (homeEntity.getPraise().equals("0")) {
			isstr = 0;
			mRb_Favour.setChecked(false);
			mRb_Disapprove.setChecked(true);
		} else {
			mRb_Favour.setChecked(false);
			mRb_Disapprove.setChecked(false);
		}
	}

	Videolistener videolistener = new Videolistener() {

		@Override
		public void onPrepared(MediaPlayer mediaplayer) {
			pb_waiting.setVisibility(View.GONE);
		}

		@Override
		public void onError(MediaPlayer mediaplayer, int i, int j) {
			showShortToast("视频出错！");
			videoPay.setPause();
			mIm_Play.setVisibility(View.VISIBLE);
			videoPay.ispay = false;
		}

		@Override
		public void onCompletion(MediaPlayer mediaplayer) {
			videoPay.setPause();
			mIm_Play.setVisibility(View.VISIBLE);
			videoPay.ispay = false;
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.image_back:// 取消
			if (isoperation) {
				setResult(0, new Intent());
				finish();
			}
			finish();
			break;
		case R.id.image_pic:// 跳转个人主页中心
			if (homeEntity.getNickname().equals("匿名")) {
				ToastUtil.showToast(this, "该用户使用匿名发布！无法查看详情");
				return;
			}
			StartInfoHomepage();
			break;
		case R.id.tx_name:// 跳转个人主页中心
			if (homeEntity.getNickname().equals("匿名")) {
				ToastUtil.showToast(this, "该用户使用匿名发布！无法查看详情");
				return;
			}
			StartInfoHomepage();
			break;
		case R.id.image_comment:// 显示评论
			mLa_CommentEdit.setVisibility(View.VISIBLE);
			break;
		case R.id.image_share:// 分享

			Bundle bundle = new Bundle();
			bundle.putString("Title", homeEntity.getNickname());
			bundle.putString("Content", homeEntity.getContent());
			bundle.putString("shareURL", "http://tmssh.conitm.com/share-" + homeEntity.getID() + ".html");
			bundle.putString("imageurl", homeEntity.getUserInfoPicUrl());
			startActivityForResult(ShareActivity.class, bundle, 10001);
			// context.startActivity(new Intent(context,ShareActivity.class));
			overridePendingTransition(R.anim.small_in_big, R.anim.fade_out);
			break;
		case R.id.tx_updata:// 提交评论
			if (tologin()) {
				return;
			}
			String content = mEd_Comment.getText().toString();
			if (TextUtils.isEmpty(content)) {
				YoYo.with(Techniques.Shake).playOn(mEd_Comment);
				showShortToast("请输入评论内容");
				return;
			}
			isoperation = true;
			new RequestTask(this, ReturnAL.ContentMap(homeEntity.getID(), content), Commentlistener, false, true, "发表中")
					.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.URL);
			break;
		case R.id.radio_favour:// 好文
			// if (isstr!=1) {
			isstr = 1;
			new RequestTask(this, Favourlistener, false, true, "打赏中").executeOnExecutor(Executors.newCachedThreadPool(),
					Httpurl.IsSmile(homeEntity.getID(), "1"));
			isoperation = true;
			// }else{
			// ToastUtil.showToast(this, "您已经打赏");
			// }
			break;
		case R.id.radio_disapprove:// 不好文
			// if (isstr!=0) {
			isstr = 0;
			new RequestTask(this, Favourlistener, false, true, "打赏中").executeOnExecutor(Executors.newCachedThreadPool(),
					Httpurl.IsSmile(homeEntity.getID(), "0"));
			isoperation = true;
			// }else{
			// ToastUtil.showToast(this, "您已经打赏");
			// }
			break;
		case R.id.image_play:// 视频播放
			if (!videoPay.ispay) {
				videoPay.playvideo();
				video_image.setVisibility(View.GONE);
				pb_waiting.setVisibility(View.VISIBLE);
				mIm_Play.setVisibility(View.GONE);
				videoPay.ispay = true;
			}

			break;
		case R.id.layout_video:// 视频暂停
			if (videoPay.ispay) {
				mIm_Play.setVisibility(View.VISIBLE);
				videoPay.setPause();
				videoPay.ispay = false;
				// paying = false;
			}
			break;
		default:
			break;
		}
	}

	RequestListener Commentlistener = new RequestListener() {

		@Override
		public void responseResult(String jsonObject) {
			PublicUpJson publicUpJson = PublicUpJson.readJsonToSendmsgObject(ContentDataActivity.this, jsonObject);
			if (publicUpJson == null) {
				setRidio();
				return;
			}
			if (publicUpJson.getAl().get(0).getStatus().equals("1")) {
				homeEntity.setReviews(CommonAPI.toInteger(homeEntity.getReviews()) + 1 + "");
				mTx_CommentNum.setText(homeEntity.getReviews() + "评论");
				mEd_Comment.setText("");
				mLa_CommentEdit.setVisibility(View.GONE);
				isComment = true;
				new RequestTask(ContentDataActivity.this, contentlistener, false, false, "拼命加载中").executeOnExecutor(
						Executors.newCachedThreadPool(), Httpurl.Contenturl(homeEntity.getID(), "1"));
			}
			showShortToast(publicUpJson.getAl().get(0).getMessage());
		}

		@Override
		public void responseException(String errorMessage) {
			setRidio();
			showShortToast(errorMessage);
		}
	};
	RequestListener contentlistener = new RequestListener() {

		@SuppressWarnings("unchecked")
		@Override
		public void responseResult(String jsonObject) {
			HomeJson homeJson = HomeJson.readJsonToSendmsgObject(ContentDataActivity.this, jsonObject);
			if (homeJson == null) {
				isrequest = true;
				stoprequest();
				return;
			}
			if (page != 1 && !isComment) {
				homeJson.getAl().addAll(0, (Collection<? extends HomeEntity>) contentadapter.getDatas());
			}
			isComment = false;
			contentadapter.setmDatas(homeJson.getAl());
			contentadapter.notifyDataSetChanged();
			isrequest = true;
			stoprequest();
		}

		@Override
		public void responseException(String errorMessage) {
			ToastUtil.showMessage(ContentDataActivity.this, errorMessage);
			isrequest = true;
			stoprequest();
		}
	};
	RequestListener Favourlistener = new RequestListener() {

		@Override
		public void responseResult(String jsonObject) {
			PublicUpJson publicUpJson = PublicUpJson.readJsonToSendmsgObject(ContentDataActivity.this, jsonObject);
			if (publicUpJson == null) {
				// isstr=-1;
				setRidio();
				return;
			}
			PublicEntity publicEntity = publicUpJson.getAl().get(0);
			if (publicEntity.getStatus().equals("1")) {
				ToastUtil.showToast(ContentDataActivity.this, "谢谢客官打赏");
				if (isstr == 1) {
					homeEntity.setPraise("1");
					homeEntity.setPraises(Integer.valueOf(homeEntity.getPraises().toString()) + 1 + "");
					mTx_FunnyNum.setText(homeEntity.getPraises() + "好文");

				} else if (isstr == 0) {
					homeEntity.setPraise("0");
					Integer valueOf = Integer.valueOf(homeEntity.getPraises().toString());
					if (valueOf > 0) {
						valueOf = valueOf - 1;
					}
					homeEntity.setPraises(valueOf + "");
					mTx_FunnyNum.setText(homeEntity.getPraises() + "好文");
				}

			} else {
				ToastUtil.showToast(ContentDataActivity.this, "打赏失败");
			}
			// isstr=-1;
		}

		@Override
		public void responseException(String errorMessage) {
			ToastUtil.showMessage(ContentDataActivity.this, errorMessage);
			// isstr=-1;
			setRidio();
		}
	};

	public void setRefrsh() {
		new RequestTask(ContentDataActivity.this, contentlistener, false, true, "加载评论")
				.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.Contenturl(homeEntity.getID(), page + ""));

		// new Handler().postDelayed(new Runnable() {
		// @Override
		// public void run() {
		// mScrollView.setRefreshing(true);
		// }
		// }, 500);
	}

	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH_DATA_FINISH:
				// page = 1;
				if (isrequest && istotime) {
					mScrollView.onRefreshComplete(); // 下拉刷新完成
					isrequest = false;
					istotime = false;
				}
				break;
			case LOAD_DATA_FINISH:
				// mListView.onLoadMoreComplete(); // 加载更多完成
				// page++;
				break;
			default:
				break;
			}
		};
	};
	private RelativeLayout layout_video;
	private HomeEntity homeEntity;
	private CommentListViewAdapter contentadapter;
	boolean isrequest = false;// 请求时否完成
	boolean istotime = false;// 请求时间是否结束
	private VideoPay videoPay;
	private ProgressBar pb_waiting;
	private boolean pays = false;
	private List<String> asList;
	private PullToRefreshScrollView mScrollView;
	private TextView tv_addtime;

	public void loadData(final int type) {
		new Thread() {
			@Override
			public void run() {
				switch (type) {
				case 0:
					page = 1;
					new RequestTask(ContentDataActivity.this, contentlistener, false, false, "拼命加载中").executeOnExecutor(
							Executors.newCachedThreadPool(), Httpurl.Contenturl(homeEntity.getID(), page + ""));
					break;
				case 1:
					page++;
					new RequestTask(ContentDataActivity.this, contentlistener, false, false, "拼命加载中").executeOnExecutor(
							Executors.newCachedThreadPool(), Httpurl.Contenturl(homeEntity.getID(), page + ""));
					break;
				}
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				istotime = true;
				stoprequest();
			}
		}.start();
	}

	// 跳转到个人主页
	private void StartInfoHomepage() {
		Bundle bundle = new Bundle();
		bundle.putInt("type", 5);
		bundle.putString("username", homeEntity.getNickname());
		bundle.putString("userid", homeEntity.getUserID());
		startActivity(InfoHomepageActivity.class, bundle);
	}

	/** 传值关闭请求 **/
	public void stoprequest() {
		Message _Msg = mHandler.obtainMessage(REFRESH_DATA_FINISH);
		mHandler.sendMessage(_Msg);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.KEYCODE_BACK == keyCode && isoperation) {
			setResult(0, new Intent());
		}
		return super.onKeyDown(keyCode, event);
	}
}
