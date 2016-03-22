package com.app.civillife;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import com.CivilLife.Base.BaseActivity;
import com.CivilLife.Base.BaseUpload;
import com.CivilLife.Entity.HomeEntity;
import com.CivilLife.Entity.HomeonetitleEntity;
import com.CivilLife.Entity.PublicEntity;
import com.CivilLife.Json.HomeJson;
import com.CivilLife.Json.HomeoneTitleJson;
import com.CivilLife.Json.PublicUpJson;
import com.CivilLife.Variable.GlobalVariable;
import com.CivilLife.Widget.GridForScrollView;
import com.CivilLife.Widget.URLImageParser;
import com.CivilLife.net.Httpurl;
import com.CivilLife.net.RequestTask;
import com.CivilLife.net.ReturnAL;
import com.app.civillife.Util.CommonAPI;
import com.app.civillife.Util.GetDistance;
import com.app.civillife.Util.OnGetPhotoListener;
import com.app.civillife.Util.VideoPay;
import com.app.civillife.Util.Videolistener;
import com.app.civillife.Util.ViewLoadManager;
import com.app.civillife.Util.ViewLoadManager.IMAGE_LOAD_TYPE;
import com.king.photo.activity.AlbumActivity;
import com.king.photo.activity.Bimp;
import com.king.photo.activity.GalleryActivity;
import com.king.photo.activity.ImageItem;
import com.king.photo.activity.PublicWay;
import com.king.photo.activity.Res;
import com.king.photo.zoom.FileUtils;
import com.umeng.analytics.MobclickAgent;
import com.yixia.camera.demo.service.MediaRecorderActivity;

import Downloadimage.ImageUtils;
import Requset_getORpost.ProgressDialog;
import Requset_getORpost.RequestListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Thumbnails;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * 发布消息界面
 * 
 * @author Administrator
 * 
 */
@SuppressLint("HandlerLeak")
public class PublishActivity extends BaseActivity {

	private ImageView mIm_Pic, image_play;
	private TextView mTx_Name, tv_spacing;
	private CheckBox mCb_Anonymity;
	private EditText mEd_Content;
	private Button mBt_Location;
	private VideoView mVd_Video;
	private Spinner spinner_title1;
	private Spinner spinner_title2;
	private List<String> data_list = new ArrayList<String>(); // 标题1 内容
	private List<String> data_list1 = new ArrayList<String>(); // 标题2 内容
	private List<String> dataID_list = new ArrayList<String>(); // 标题1 ID
	private List<String> dataID_list1 = new ArrayList<String>(); // 标题2 ID
	private ArrayList<HomeonetitleEntity> titlelist = new ArrayList<HomeonetitleEntity>();
	private String seleortID = "1";//
	private boolean anonymous = false;
	private int type = 0; // 上传类型 1=图片 2=视频
	private GridForScrollView noScrollgridview;
	private GridAdapter adapter;
	public static Bitmap bimap;
	private int TYPE;// 判定跳转
	// private Boolean ClassTITLE= false;// 一级发布类型请求是否完成
	// private Boolean ISEDIT= false;// 草稿或我的文字请求是否完成
	// 是否是网络视频
	private Boolean upvoide = false;
	private int seleorttitle1ID = -1;// 标题1默认选中
	private int seleorttitle2ID = -1;// 标题2默认选中

	private boolean isupvoideimage = true;// 视频缩略图上传 是否成功
	private boolean isupvoide = false;// 视频上传 是否成功
	private String upvoideimageurl;// 视频缩略图地址
	private String path = "";// 上传路径
	private int pathnum = 0;
	private ImageView mcamera;
	private ImageView mphoto;
	private VideoPay videoPay;
	private RelativeLayout layout_video;
	private ArrayAdapter<String> arr_adapter2;
	private ProgressBar pb_waiting;
	private String videopath;
	private ImageView video_image;
	private TextView mTx_save;
	private String iD;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish);
		Res.init(this);
		initViews();
		initEvents();
		init();

	}

	@Override
	protected void initViews() {
		mIm_Pic = (ImageView) findViewById(R.id.image_pic);
		image_play = (ImageView) findViewById(R.id.image_play);
		video_image = (ImageView) findViewById(R.id.video_image);
		mTx_Name = (TextView) findViewById(R.id.tx_name);
		tv_spacing = (TextView) findViewById(R.id.tv_spacing);
		mCb_Anonymity = (CheckBox) findViewById(R.id.cb_anonymity);
		mEd_Content = (EditText) findViewById(R.id.ed_content);
		mBt_Location = (Button) findViewById(R.id.btn_location);
		mVd_Video = (VideoView) findViewById(R.id.video_my);
		spinner_title1 = (Spinner) findViewById(R.id.spinner1);
		spinner_title2 = (Spinner) findViewById(R.id.spinner2);
		layout_video = (RelativeLayout) findViewById(R.id.layout_video);
		pb_waiting = (ProgressBar) findViewById(R.id.pb_waiting);
		ImageUtils.loadImage(this, GlobalVariable.UserImage, mIm_Pic, GlobalVariable.WifiDown);
		mTx_Name.setText(GlobalVariable.Nickname);
		mTx_save = (TextView) findViewById(R.id.tx_save);
		mphoto = (ImageView) findViewById(R.id.image_photo);
		mcamera = (ImageView) findViewById(R.id.image_camera);
	}

	@Override
	protected void initEvents() {
		progressDialog = ProgressDialog.createDialog(this, null, false);
		image_play.setOnClickListener(this);
		mIm_Pic.setOnClickListener(this);
		mTx_Name.setOnClickListener(this);
		mBt_Location.setOnClickListener(this);
		layout_video.setOnClickListener(this);
		findViewById(R.id.tx_back).setOnClickListener(this);
		mTx_save.setOnClickListener(this);
		findViewById(R.id.tx_publish).setOnClickListener(this);
		mphoto.setOnClickListener(this);
		mcamera.setOnClickListener(this);
		findViewById(R.id.image_video).setOnClickListener(this);
		// 匿名监听
		mCb_Anonymity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					anonymous = true;
				} else {
					anonymous = false;
				}
			}
		});
		noScrollgridview = (GridForScrollView) findViewById(R.id.gv_pic);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 == Bimp.tempSelectBitmap.size()) {
					if (!TextUtils.isEmpty(mediaPath)) {
						PromptDialogs("是否放弃拍摄的视频？", new OnClickListener() {
							@Override
							public void onClick(View v) {
								noScrollgridview.setVisibility(View.VISIBLE);
								layout_video.setVisibility(View.GONE);
								Intent intent = new Intent(PublishActivity.this, AlbumActivity.class);
								startActivity(intent);
							}
						});
					} else {
						Intent intent = new Intent(PublishActivity.this, AlbumActivity.class);
						startActivity(intent);
					}
				} else {
					Intent intent = new Intent(PublishActivity.this, GalleryActivity.class);
					intent.putExtra("position", "1");
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});
	}

	@Override
	protected void init() {
		mBt_Location.setText(GlobalVariable.address);

		TYPE = getIntent().getIntExtra("TYPE", 0);
		if (TYPE == 10) {// 社区调整过来发布的
			mTx_save.setVisibility(View.GONE);
			String artid = getIntent().getStringExtra("artid");
			String artname = getIntent().getStringExtra("artname");

			titlelist.add(new HomeonetitleEntity(artid, artname));
		} else {
			if (GlobalVariable.Data == null && GlobalVariable.Data.size() == 0) {
				new RequestTask(this, hometitlelistener, true, true, "Loading")
						.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.homeonetitle);
			} else {
				titlelist = GlobalVariable.Data;
			}
		}
		setTitle();
		TackType();
	}

	/** 获取草稿 或编辑我的文章 **/
	private String[] images;
	// 获取我的草稿
	RequestListener listlistener = new RequestListener() {

		@Override
		public void responseResult(String jsonObject) {
			// Log.e("", " jsonObject " + jsonObject);
			HomeJson homeJson = HomeJson.readJsonToSendmsgObject(PublishActivity.this, jsonObject);
			if (homeJson == null) {
				return;
			}
			if (!TextUtils.isEmpty(homeJson.getAl().get(0).getMessage())) {
				// 从首页或社区进来的发布 如果没有草稿就 获取发布的栏目
				String seleorttitle1 = getIntent().getStringExtra("seleorttitle1");
				String seleorttitle2 = getIntent().getStringExtra("seleorttitle2");

				if (!TextUtils.isEmpty(seleorttitle1)) {
					int indexOf = dataID_list.indexOf(seleorttitle1);
					if (-1 != indexOf) {
						spinner_title1.setSelection(indexOf, true);
					}
				}
				if (!TextUtils.isEmpty(seleorttitle2)) {
					try {
						seleorttitle2ID = Integer.valueOf(seleorttitle2);
					} catch (Exception e) {
						seleorttitle2ID = -1;
					}
				}
				return;
			}
			SetData(homeJson.getAl().get(0));
		}

		@Override
		public void responseException(String errorMessage) {
			showMessage(errorMessage);
		}
	};

	@SuppressWarnings("static-access")
	private void SetData(HomeEntity entity) {
		layout_video.setVisibility(View.GONE);
		noScrollgridview.setVisibility(View.GONE);
		// mEd_Content.setText(entity.getContent());
		mEd_Content.setText(Html.fromHtml(entity.getContent(), null, null));
		if (entity.getAnonymous().equals("1")) {
			mCb_Anonymity.setChecked(true);
		} else {
			mCb_Anonymity.setChecked(false);
		}
		if (!TextUtils.isEmpty(entity.getPicUrl())) {
			type = 1;
			images = entity.getPicUrl().split(",");
			ViewLoadManager viewLoadManager = new ViewLoadManager(PublishActivity.this);
			for (int i = 0; i < images.length; i++) {
				viewLoadManager.setViewBackground(IMAGE_LOAD_TYPE.FILE_URL, images[i], ogl);
			}
		}

		if (!TextUtils.isEmpty(entity.getVideoUrl())) {
			upvoide = true;
			type = 2;
			mediaPath = entity.getVideoUrl();
			layout_video.setVisibility(View.VISIBLE);
			noScrollgridview.setVisibility(View.GONE);
			videoPay = new VideoPay(PublishActivity.this, mVd_Video, mediaPath, videolistener);
		}

		try {
			seleorttitle2ID = Integer.valueOf(entity.getArticleClassNameID());
			seleorttitle1ID = Integer.valueOf(entity.getParent_ArticleClassNameID());
		} catch (Exception e) {
			seleorttitle1ID = -1;
			seleorttitle2ID = -1;
		}

		int indexOf = dataID_list.indexOf(entity.getParent_ArticleClassNameID());
		if (-1 != indexOf) {
			spinner_title1.setSelection(indexOf, true);
		}
		new ImageUtils().loadImage3(PublishActivity.this, entity.getVideoThumbnailsPicUrl(), video_image, false);
	}

	// 获取标题列表
	RequestListener hometitlelistener = new RequestListener() {

		@Override
		public void responseResult(String jsonObject) {
			HomeoneTitleJson readJsonToSendmsgObject = HomeoneTitleJson.readJsonToSendmsgObject(PublishActivity.this,
					jsonObject);
			if (readJsonToSendmsgObject == null) {
				return;
			}
			data_list.clear();
			dataID_list.clear();
			titlelist = readJsonToSendmsgObject.getAl();
			if (titlelist != null) {
				GlobalVariable.Data = titlelist;
				setTitle();
			}
			TackType();
		}

		@Override
		public void responseException(String errorMessage) {
			showShortToast(errorMessage);
			ReqEdit();
		}
	};

	private void setTitle() {
		for (int i = 0; i < titlelist.size(); i++) {
			data_list.add(titlelist.get(i).getArticleClassName());
			dataID_list.add(titlelist.get(i).getID());
		}
		seleortID = titlelist.get(0).getID();
	}

	/** 获取草稿的图片的 Bitmap **/
	OnGetPhotoListener ogl = new OnGetPhotoListener() {

		@Override
		public void onGetPhoto(String url, Bitmap bitmap) {

			if (bitmap != null) {
				ImageItem takePhoto = new ImageItem();
				takePhoto.setBitmap(bitmap);
				takePhoto.setIsdown(true);
				Bimp.tempSelectBitmap.add(takePhoto);
			}
			if (Bimp.tempSelectBitmap.size() == images.length) {
				noScrollgridview.setVisibility(View.VISIBLE);
				layout_video.setVisibility(View.GONE);
				adapter.notifyDataSetChanged();
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tx_back:// 取消
			Bimp.tempSelectBitmap.clear();
			finish();
			break;
		case R.id.tx_save:// 保存草稿
			mod = "SaveDraft";
			String cotent = mEd_Content.getText().toString();
			if (TextUtils.isEmpty(cotent)) {
				showMessage("请输入文章内容！");
				return;
			}
			progressDialog.setMessage("保存中");
			progressDialog.show();

			PublishSubit();
			break;
		case R.id.tx_publish:// 发布
			mod = "";
			String cotents = mEd_Content.getText().toString();
			if (TextUtils.isEmpty(cotents)) {
				showMessage("请输入文章内容！");
				return;
			}
			progressDialog.setMessage("发布中");
			progressDialog.show();
			PublishSubit();

			break;
		case R.id.image_pic:// 点击跳转个人中心
			StartInfoHomepage();
			break;
		case R.id.tx_name:// 点击跳转个人中心
			StartInfoHomepage();
			break;
		case R.id.btn_location:// 刷新定位
			mBt_Location.setText("定位中...");
			GetDistance.location(mApplication, mhandler).start();
			break;
		case R.id.image_photo:// 选择相册图片
			if (!TextUtils.isEmpty(mediaPath)) {
				PromptDialogs("是否放弃拍摄的视频？", new OnClickListener() {
					@Override
					public void onClick(View v) {
						layout_video.setVisibility(View.GONE);
						noScrollgridview.setVisibility(View.VISIBLE);
						Intent intent = new Intent(PublishActivity.this, AlbumActivity.class);
						startActivity(intent);
						type = 1;
					}
				});
			} else {
				layout_video.setVisibility(View.GONE);
				noScrollgridview.setVisibility(View.VISIBLE);
				Intent intent = new Intent(PublishActivity.this, AlbumActivity.class);
				startActivity(intent);
				type = 1;
			}

			break;
		case R.id.image_camera:// 选择拍照
			if (!TextUtils.isEmpty(mediaPath)) {
				PromptDialogs("是否放弃拍摄的视频？", new OnClickListener() {
					@Override
					public void onClick(View v) {
						layout_video.setVisibility(View.GONE);
						noScrollgridview.setVisibility(View.VISIBLE);
						photo();
						type = 1;
					}
				});
			} else {
				layout_video.setVisibility(View.GONE);
				noScrollgridview.setVisibility(View.VISIBLE);
				photo();
				type = 1;
			}

			break;
		case R.id.image_video:// 选择录像
			if (Bimp.tempSelectBitmap.size() > 0) {
				PromptDialogs("是否放弃选择的图片？", new OnClickListener() {
					@Override
					public void onClick(View v) {
						noScrollgridview.setVisibility(View.GONE);
						adapter.notifyDataSetChanged();
						Bimp.tempSelectBitmap.clear();
						startActivityForResult(MediaRecorderActivity.class, null, 1021);
						type = 2;
					}
				});
			} else {
				noScrollgridview.setVisibility(View.GONE);
				startActivityForResult(MediaRecorderActivity.class, null, 1021);
				type = 2;
			}

			break;
		case R.id.image_play:// 视频播放
			if (!videoPay.ispay) {
				videoPay.playvideo();
				video_image.setVisibility(View.GONE);
				pb_waiting.setVisibility(View.VISIBLE);
				image_play.setVisibility(View.GONE);
				videoPay.ispay = true;
			}

			break;
		case R.id.layout_video:// 视频暂停
			if (videoPay.ispay) {
				image_play.setVisibility(View.VISIBLE);
				videoPay.setPause();
				videoPay.ispay = false;
				// paying = false;
			}

			break;
		default:
			break;
		}
	}

	private void StartInfoHomepage() {
		Bundle bundle = new Bundle();
		bundle.putInt("type", 5);
		bundle.putString("username", GlobalVariable.Nickname);
		bundle.putString("userid", GlobalVariable.UserID);
		startActivity(InfoHomepageActivity.class, bundle);
	}

	/** 文章提交 **/
	private void PublishSubit() {
		// Log.e("", "anonymous "+anonymous);
		if (type == 1) {
			for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
				if (images != null && images.length > i
						&& Bimp.tempSelectBitmap.get(i).getIsdown()) {/** 草稿的网络图片不在上传 **/
					if (pathnum >= Bimp.tempSelectBitmap.size() - 1) {
						path += images[i];
						new RequestTask(PublishActivity.this,
								ReturnAL.PublishedArticles(anonymous, mEd_Content.getText().toString(), path, null,
										seleortID, mod, null, TYPE, iD),
								publishedarticleslistener, false, false, "发布中")
										.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.URL);
					} else {
						path += images[i] + ",";
					}
					pathnum++;
				} else {

					new BaseUpload(this).Upload(Bimp.tempSelectBitmap.get(i).getImagePath(), uplelistener, false,
							"上传中");
				}
			}
		} else if (type == 2) {
			if (upvoide) {
				new RequestTask(PublishActivity.this,
						ReturnAL.PublishedArticles(anonymous, mEd_Content.getText().toString(), null, mediaPath,
								seleortID, mod, null, TYPE, iD),
						publishedarticleslistener, false, false, "发布中")
								.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.URL);
			} else {
				new BaseUpload(this).Upload(mediaPath, uplelistener, true, "上传中");

				if (!TextUtils.isEmpty(videopath)) {
					isupvoideimage = false;
					new BaseUpload(this).Upload(videopath, uplelistener1, false, "");
				}
			}

		} else {

			new RequestTask(PublishActivity.this,
					ReturnAL.PublishedArticles(anonymous, mEd_Content.getText().toString(), null, null, seleortID, mod,
							null, TYPE, iD),
					publishedarticleslistener, false, false, "发布中").executeOnExecutor(Executors.newCachedThreadPool(),
							Httpurl.URL);
		}
	}

	/** 视频缩略图 上传文件回调 **/
	RequestListener uplelistener1 = new RequestListener() {

		@Override
		public void responseResult(String jsonObject) {
			// Log.e("", "jsonObject回调地址 " + jsonObject);
			upvoideimageurl = jsonObject;
			isupvoideimage = true;
			if (isupvoide && isupvoideimage) {
				new RequestTask(PublishActivity.this,
						ReturnAL.PublishedArticles(anonymous, mEd_Content.getText().toString(), null, path, seleortID,
								mod, upvoideimageurl, TYPE, iD),
						publishedarticleslistener, false, false, "发布中")
								.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.URL);
			}
		}

		@Override
		public void responseException(String errorMessage) {
			isupvoideimage = true;
			if (isupvoide && isupvoideimage) {
				new RequestTask(PublishActivity.this,
						ReturnAL.PublishedArticles(anonymous, mEd_Content.getText().toString(), null, path, seleortID,
								mod, "", TYPE, iD),
						publishedarticleslistener, false, false, "发布中")
								.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.URL);
			}
		}

	};

	/** 上传文件回调 **/
	RequestListener uplelistener = new RequestListener() {

		@Override
		public void responseResult(String jsonObject) {
			// Log.e("", "jsonObject回调地址 " + jsonObject);
			if (type == 1) {
				if (pathnum >= Bimp.tempSelectBitmap.size() - 1) {
					path += jsonObject;
					new RequestTask(PublishActivity.this,
							ReturnAL.PublishedArticles(anonymous, mEd_Content.getText().toString(), path, null,
									seleortID, mod, null, TYPE, iD),
							publishedarticleslistener, false, false, "发布中")
									.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.URL);
				} else {
					path += jsonObject + ",";
				}
				pathnum++;
			} else if (type == 2) {
				path = jsonObject;
				isupvoide = true;
				if (isupvoide && isupvoideimage) {
					new RequestTask(PublishActivity.this,
							ReturnAL.PublishedArticles(anonymous, mEd_Content.getText().toString(), null, path,
									seleortID, mod, upvoideimageurl, TYPE, iD),
							publishedarticleslistener, false, false, "发布中")
									.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.URL);
				}
			}
		}

		@Override
		public void responseException(String errorMessage) {
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			if (TextUtils.isEmpty(errorMessage)) {
				if (type == 1) {
					showMessage("图片" + pathnum + "上传失败");
				} else if (type == 2) {
					showMessage("视频上传失败");
				}
				return;
			}
		}
	};
	// 发布文章
	RequestListener publishedarticleslistener = new RequestListener() {

		@Override
		public void responseResult(String jsonObject) {
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			// Qlog.e("", " 发布文章 " + jsonObject);
			PublicUpJson publicUpJson = PublicUpJson.readJsonToSendmsgObject(PublishActivity.this, jsonObject);
			if (publicUpJson == null) {
				return;
			}
			PublicEntity publicEntity = publicUpJson.getAl().get(0);
			showShortToast(publicEntity.getMessage());
			if (publicEntity.getStatus().equals("1")) {
				Bimp.tempSelectBitmap.clear();
				if (TYPE == 10) {
					setResult(33, new Intent());
				} else if (TYPE == 2) {
					setResult(34, new Intent());
				}
				finish();
			}
		}

		@Override
		public void responseException(String errorMessage) {
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			showMessage(errorMessage);
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Bimp.tempSelectBitmap.clear();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg2 != null) {
			if (arg0 == 1021 && arg1 == 2021) {// 视频转码成功
				upvoide = false;
				mediaPath = arg2.getStringExtra("MediaPath");
				layout_video.setVisibility(View.VISIBLE);
				videoPay = new VideoPay(this, mVd_Video, mediaPath, videolistener);
				Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(mediaPath, Thumbnails.MINI_KIND);
				bitmap = ThumbnailUtils.extractThumbnail(bitmap, 300, 300);
				String fileName = System.currentTimeMillis() + "";
				videopath = FileUtils.saveBitmap(bitmap, fileName);
				video_image.setImageBitmap(bitmap);
			} else if (arg0 == 1021 && arg1 == 2022) {
				showMessage("转码失败！");
			} else if (TAKE_PICTURE == arg0 && Bimp.tempSelectBitmap.size() < PublicWay.num && arg1 == RESULT_OK) {// 获取图片

				String fileName = System.currentTimeMillis() + "";
				Bitmap bm = (Bitmap) arg2.getExtras().get("data");
				// String bm1 = (String)
				// arg2.getExtras().get(MediaStore.EXTRA_OUTPUT);
				ImageItem takePhoto = new ImageItem();
				takePhoto.setBitmap(bm);
				takePhoto.setImagePath(FileUtils.saveBitmap(bm, fileName));
				Bimp.tempSelectBitmap.add(takePhoto);
				noScrollgridview.setVisibility(View.VISIBLE);
			}
		}
	}

	Videolistener videolistener = new Videolistener() {

		@Override
		public void onPrepared(MediaPlayer mediaplayer) {
			pb_waiting.setVisibility(View.GONE);
		}

		@Override
		public void onError(MediaPlayer mediaplayer, int i, int j) {
			showMessage("视频出错！");
			video_image.setVisibility(View.VISIBLE);
			image_play.setVisibility(View.VISIBLE);
			videoPay.setPause();
		}

		@Override
		public void onCompletion(MediaPlayer mediaplayer) {
			image_play.setVisibility(View.VISIBLE);
			video_image.setVisibility(View.VISIBLE);
			videoPay.setPause();
		}
	};
	// 获取二级标题内容
	private ArrayList<HomeonetitleEntity> al;
	RequestListener hometwotitlelistener = new RequestListener() {

		@Override
		public void responseResult(String jsonObject) {
			HomeoneTitleJson readJsonToSendmsgObject = HomeoneTitleJson.readJsonToSendmsgObject(PublishActivity.this,
					jsonObject);
			if (readJsonToSendmsgObject == null) {
				return;
			}
			tv_spacing.setVisibility(View.VISIBLE);
			spinner_title2.setVisibility(View.VISIBLE);
			data_list1.clear();
			dataID_list1.clear();
			al = readJsonToSendmsgObject.getAl();
			al.remove(0);
			seleortID = al.get(0).getID();
			for (int i = 0; i < al.size(); i++) {
				data_list1.add(al.get(i).getArticleClassName());
				dataID_list1.add(al.get(i).getID());
			}
			TackType1();
		}

		@Override
		public void responseException(String errorMessage) {
			showMessage(errorMessage);
		}
	};

	private void TackType() {
		// 适配器
		ArrayAdapter<String> arr_adapter = new ArrayAdapter<String>(PublishActivity.this,
				android.R.layout.simple_spinner_item, data_list);
		// 设置样式
		arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 加载适配器
		spinner_title1.setAdapter(arr_adapter);
		ReqEdit();
		spinner_title1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (titlelist.get(position).getID().equals("3") || titlelist.get(position).getID().equals("5")
						|| titlelist.get(position).getID().equals("6") || titlelist.get(position).getID().equals("4")) {

					new RequestTask(PublishActivity.this, hometwotitlelistener, true, true, "Loading")
							.executeOnExecutor(Executors.newCachedThreadPool(),
									Httpurl.hometwotitle(titlelist.get(position).getID()));
				} else {
					tv_spacing.setVisibility(View.GONE);
					spinner_title2.setVisibility(View.GONE);
					seleortID = titlelist.get(position).getID();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	/** 请求我的草稿或我的文字内容 **/
	private void ReqEdit() {
		if (TYPE == 1) {// 首页栏目进来获取
			new RequestTask(PublishActivity.this, listlistener, false, true, "加载内容中")
					.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.GetDraft());
		} else if (TYPE == 2) {// 管理我的文字进来发布
			HomeEntity homeEntity = getIntent().getParcelableExtra("HomeEntity");
			iD = homeEntity.getID();
			SetData(homeEntity);
			// new RequestTask(PublishActivity.this, listlistener, false, true,
			// "加载内容中")
			// .executeOnExecutor(Executors.newCachedThreadPool(),
			// Httpurl.GetMyDraft(iD));
		}
		// else if (TYPE == 10) {// 从社区进来获取草稿
		// String ID = getIntent().getStringExtra("artid");
		// new RequestTask(PublishActivity.this, listlistener, false, true,
		// "加载内容中")
		// .executeOnExecutor(Executors.newCachedThreadPool(),
		// Httpurl.GetDraft1(ID));
		// }
	}

	@SuppressLint("NewApi")
	private void TackType1() {

		arr_adapter2 = new ArrayAdapter<String>(PublishActivity.this, android.R.layout.simple_spinner_item, data_list1);
		// 设置样式
		arr_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 加载适配器
		spinner_title2.setAdapter(arr_adapter2);
		int indexOf = dataID_list1.indexOf(seleorttitle2ID + "");
		// Log.e("", " seleorttitle2ID "+seleorttitle2ID);
		// Log.e("", " indexOf2 "+indexOf);
		// Log.e("", " dataID_list1 "+dataID_list1.size());
		if (indexOf != -1) {
			// Qlog.e("", " indexOf2 "+indexOf);
			spinner_title2.setSelection(indexOf, true);
			spinner_title2.setGravity(Gravity.CENTER);
			spinner_title2.setPadding(0, 0, 0, CommonAPI.dip2px(PublishActivity.this, 18));
			seleortID = seleorttitle2ID + "";
		}
		spinner_title2.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				seleortID = al.get(position).getID();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			if (Bimp.tempSelectBitmap.size() == 9) {
				return 9;
			}
			return (Bimp.tempSelectBitmap.size() + 1);
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida, parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == Bimp.tempSelectBitmap.size()) {
				holder.image
						.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused));
				if (position == PublicWay.num) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
			}
			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.tempSelectBitmap.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							Bimp.max += 1;
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						}
					}
				}
			}).start();
		}
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}

	private static final int TAKE_PICTURE = 0x000001;
	private String mediaPath;
	// private BaseUpload baseUpload;
	private String mod;
	private ProgressDialog progressDialog;

	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	Handler mhandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			if (msg.what == 10) {
				String Address = (String) msg.obj;
				mBt_Location.setText(Address);
			}
		};
	};

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
