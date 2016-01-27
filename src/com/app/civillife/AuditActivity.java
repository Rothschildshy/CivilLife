package com.app.civillife;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

import com.CivilLife.Base.BaseActivity;
import com.CivilLife.Entity.HomeEntity;
import com.CivilLife.Json.HomeJson;
import com.CivilLife.Json.PublicUpJson;
import com.CivilLife.MyAdapter.HomeimagegridviewAdapter;
import com.CivilLife.Variable.GlobalVariable;
import com.CivilLife.Widget.CircleImageView;
import com.CivilLife.Widget.GridForScrollView;
import com.CivilLife.net.Httpurl;
import com.CivilLife.net.RequestTask;
import com.app.civillife.Util.ImagePagerActivity;
import com.app.civillife.Util.VideoPay;
import com.app.civillife.Util.Videolistener;
import com.aysy_mytool.ToastUtil;

import Downloadimage.ImageUtils;
import Requset_getORpost.RequestListener;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * 审核界面
 * 
 * @author mac
 * 
 */
public class AuditActivity extends BaseActivity {

	private RelativeLayout mLa_Start;
	private RelativeLayout mLa_Audit;
	private CircleImageView mIm_Pic;// 头像  
	private TextView mTx_Name;// 昵称
	private TextView mTx_Content;// 评论内容
	private VideoView video;// 视频
	private GridForScrollView mImageGR;// 图标墙
	private RelativeLayout mLa_Video;// 视频布局
	private ImageView mIm_Play;// 视频播放
	private int page = 1;
	private HomeEntity homeEntity;// 文章实体类
	private ArrayList<String> imageurllist = new ArrayList<String>();// 图片地址容器
	private ProgressBar pb_waiting;
	private boolean pays = false;
	private VideoPay videoPay;

	private RelativeLayout mLayout_Hint;
	private LinearLayout mLayout_DataNull;
	private LinearLayout mLayout_NetworkError;
	private Button mBtn_Refresh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audit);
		initViews();
		initEvents();
		init();
	}

	@Override
	protected void initViews() {
		mLa_Start = (RelativeLayout) findViewById(R.id.layout_startaudit);
		mLa_Audit = (RelativeLayout) findViewById(R.id.layout_audit);
		mIm_Pic = (CircleImageView) findViewById(R.id.image_pic);
		mTx_Name = (TextView) findViewById(R.id.tx_name);
		mTx_Content = (TextView) findViewById(R.id.tx_content);
		mImageGR = (GridForScrollView) findViewById(R.id.image_gridView);
		video = (VideoView) findViewById(R.id.videoView1);
		mLa_Video = (RelativeLayout) findViewById(R.id.layout_video);
		mIm_Play = (ImageView) findViewById(R.id.image_play);
		video_image = (ImageView) findViewById(R.id.video_image);
		layout_video = (RelativeLayout) findViewById(R.id.layout_video);
		sv_auditlayout = (ScrollView) findViewById(R.id.sv_auditlayout);
		pb_waiting = (ProgressBar) findViewById(R.id.pb_waiting);

		mLayout_Hint = (RelativeLayout) findViewById(R.id.layout_hint);
		mLayout_DataNull = (LinearLayout) findViewById(R.id.layout_data_null);
		mLayout_NetworkError = (LinearLayout) findViewById(R.id.layout_network_error);
		mBtn_Refresh = (Button) findViewById(R.id.btn_refresh);
		TextView mTx_DataNull = (TextView) findViewById(R.id.tx_datanull);
		mTx_DataNull.setText("没有可以审核的文章!");
		mBtn_Refresh.setOnClickListener(this);
	}

	@Override
	protected void initEvents() {
		findViewById(R.id.image_back).setOnClickListener(this);  
		findViewById(R.id.btn_start).setOnClickListener(this);
		findViewById(R.id.btn_skip).setOnClickListener(this);
		findViewById(R.id.btn_funny).setOnClickListener(this);
		findViewById(R.id.btn_boring).setOnClickListener(this);
		findViewById(R.id.btn_report).setOnClickListener(this);
		mIm_Pic.setOnClickListener(this); 
		mTx_Name.setOnClickListener(this);  
		layout_video.setOnClickListener(this);  
		mIm_Play.setOnClickListener(this);  
		mImageGR.setOnItemClickListener(new OnItemClickListener() { // 图片查看

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(AuditActivity.this,
						ImagePagerActivity.class);
				intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS,
						imageurllist);
				intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, arg2);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void init() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.image_back:// 返回
			finish();
			break;
		case R.id.btn_start:// 开始审核
			mLa_Start.setVisibility(View.GONE);
			mLa_Audit.setVisibility(View.VISIBLE);
			new RequestTask(this, getauditlistener, false, true, "努力加载中")
					.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.GetAuditArticle(page));
			break;
		case R.id.btn_skip:// 跳过
			page++;
			new RequestTask(this, getauditlistener, false, true, "努力加载中")
					.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.GetAuditArticle(page));
			break;
		case R.id.btn_funny:// 好文
			new RequestTask(this, Submititlistener, false, true, "努力加载中")
					.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.SubmitAuditArticle(homeEntity.getID(), 1));
			break;
		case R.id.btn_boring:// 没劲
			new RequestTask(this, Submititlistener, false, true, "努力加载中")
					.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.SubmitAuditArticle(homeEntity.getID(), 0));
			break;
		case R.id.btn_report:// 举报
			new RequestTask(this, Reporttitlistener, false, true, "举报中")
					.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.ReportAuditArticle(homeEntity.getID()));
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
//				paying = false;
			}

			break;
		case R.id.btn_refresh:
			mLayout_Hint.setVisibility(View.GONE);
			sv_auditlayout.setVisibility(View.VISIBLE);

			new RequestTask(this, getauditlistener, false, true, "努力加载中")
					.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.GetAuditArticle(page));

			break;

		default:
			break;
		}
	}

	/** 跳转到个人主页 **/
	private void StartInfoHomepage() {
		Bundle bundle = new Bundle();
		bundle.putInt("type", 5);
		bundle.putString("username", homeEntity.getNickname());
		bundle.putString("userid", homeEntity.getUserID());
		startActivity(InfoHomepageActivity.class, bundle);
	}

	/** 获取审核文章 **/
	RequestListener getauditlistener = new RequestListener() {

		@Override
		public void responseResult(String jsonObject) {
			imageurllist.clear();
			layout_video.setVisibility(View.GONE);
			mImageGR.setVisibility(View.GONE);
			sv_auditlayout.setVisibility(View.VISIBLE);
			mIm_Pic.setImageResource(R.drawable.ic_my_nolog_selector);
			HomeJson json = HomeJson.readJsonToSendmsgObject(
					AuditActivity.this, jsonObject);
			if (json == null) {
				findViewById(R.id.btn_skip).setEnabled(false);
				findViewById(R.id.btn_funny).setEnabled(false);
				findViewById(R.id.btn_boring).setEnabled(false);
				findViewById(R.id.btn_report).setEnabled(false);
				sv_auditlayout.setVisibility(View.GONE);
				mLayout_Hint.setVisibility(View.VISIBLE);  
				mLayout_DataNull.setVisibility(View.VISIBLE);
				mLayout_NetworkError.setVisibility(View.GONE);
				return;
			}
			homeEntity = json.getAl().get(0);
			if (!homeEntity.getNickname().equals("匿名")
					&& !TextUtils.isEmpty(homeEntity.getUserInfoPicUrl())) {
				ImageUtils.loadImage1(AuditActivity.this,
						homeEntity.getUserInfoPicUrl(), mIm_Pic,
						R.drawable.ic_my_nolog_selector,
						R.drawable.ic_my_nolog_selector,
						GlobalVariable.WifiDown);
			}  
			mTx_Name.setText(homeEntity.getNickname());
			mTx_Content.setText(homeEntity.getContent());
			if (!TextUtils.isEmpty(homeEntity.getPicUrl())) {
				String[] images = homeEntity.getPicUrl().split(",");
				List<String> list = Arrays.asList(images);
				imageurllist.addAll(list);
				HomeimagegridviewAdapter adapter = new HomeimagegridviewAdapter(
						mApplication, AuditActivity.this, list);
				mImageGR.setAdapter(adapter);
				mImageGR.setVisibility(View.VISIBLE);
			}
			if (!TextUtils.isEmpty(homeEntity.getVideoUrl())) {
				layout_video.setVisibility(View.VISIBLE);
				videoPay = new VideoPay(AuditActivity.this, video,
						homeEntity.getVideoUrl(), videolistener);
			} else {
				layout_video.setVisibility(View.GONE);
			}
			new ImageUtils().loadImage3(AuditActivity.this, homeEntity.getVideoThumbnailsPicUrl(), video_image, false);
		}

		@Override
		public void responseException(String errorMessage) {
			mLayout_Hint.setVisibility(View.VISIBLE);
			mLayout_DataNull.setVisibility(View.GONE);
			mLayout_NetworkError.setVisibility(View.VISIBLE);
			sv_auditlayout.setVisibility(View.GONE);
			showShortToast(errorMessage);
		}
	};
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

	private RelativeLayout layout_video;
	/** 提交审核结果 **/
	RequestListener Submititlistener = new RequestListener() {

		@Override
		public void responseResult(String jsonObject) {
			PublicUpJson publicUpJson = PublicUpJson.readJsonToSendmsgObject(
					AuditActivity.this, jsonObject);
			if (publicUpJson == null) {
				showShortToast("审核失败！请稍后在试！");
				return;
			}
			if (publicUpJson.getAl().get(0).getStatus().equals("1")) {
				showShortToast("审核成功");
				page++;
				new RequestTask(AuditActivity.this, getauditlistener, false,
						true, "努力加载中...")
						.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.GetAuditArticle(page));
			} else {
				showShortToast("审核失败！请稍后在试！");
			}
		}

		@Override
		public void responseException(String errorMessage) {
			showShortToast(errorMessage);
		}
	};
	/** 举报文章 **/
	RequestListener Reporttitlistener = new RequestListener() {

		@Override
		public void responseResult(String jsonObject) {
			PublicUpJson publicUpJson = PublicUpJson.readJsonToSendmsgObject(
					AuditActivity.this, jsonObject);
			if (publicUpJson == null) {
				showShortToast("举报失败！请稍后在试！");
				return;
			}
			showShortToast(publicUpJson.getAl().get(0).getMessage());
			if (publicUpJson.getAl().get(0).getStatus().equals("1")) {
				page++;
				new RequestTask(AuditActivity.this, getauditlistener, false,
						true, "努力加载中...")
						.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.GetAuditArticle(page));
			}
		}

		@Override
		public void responseException(String errorMessage) {
			showShortToast(errorMessage);
		}
	};
	private ScrollView sv_auditlayout;
	private ImageView video_image;
}
