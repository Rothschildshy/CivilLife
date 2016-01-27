package com.app.civillife;

import java.util.HashMap;
import java.util.Map;

import com.CivilLife.Base.BaseActivity;
import com.CivilLife.DataInfo.Constants;
import com.aysy_mytool.Qlog;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class ShareActivity extends BaseActivity {
	// 首先在您的Activity中添加如下成员变量
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.app.civillife");
	Map<String, SHARE_MEDIA> mPlatformsMap = new HashMap<String, SHARE_MEDIA>();
	CharSequence[] items = { "QQ", "QQ空间", "微信好友", "微信朋友圈", "新浪微博" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		initViews();
		initEvents();
		init();
	}

	@Override
	protected void initViews() {

	}

	@Override
	protected void initEvents() {
		findViewById(R.id.popuback).setOnClickListener(this);
		findViewById(R.id.button_share_close).setOnClickListener(this);
		findViewById(R.id.button_share_qqhaoyou).setOnClickListener(this);
		findViewById(R.id.button_share_sinaweibo).setOnClickListener(this);
		findViewById(R.id.button_share_weixin).setOnClickListener(this);
		findViewById(R.id.button_share_pengyouquan).setOnClickListener(this);
	}

	@Override
	protected void init() {
		// 配置需要分享的相关平台
		configPlatforms();
		// 设置分享内容
		String title=getIntent().getStringExtra("Title");
		String Content=getIntent().getStringExtra("Content");
		String shareURL=getIntent().getStringExtra("shareURL");
		String imageurl=getIntent().getStringExtra("imageurl");
		Log.e("", "shareURL  "+shareURL);
		setShareContent(title,Content,shareURL,imageurl);
		// 初始化平台map
		initPlatformMap();    
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.popuback:// 点击空白关闭
			defaultFinish();
			break;
		case R.id.button_share_close:// 点击关闭
			finish();
			break;
		case R.id.button_share_qqhaoyou:// QQ分享
			ShareStart(0);
			break;
		case R.id.button_share_sinaweibo:// 新浪微博
			ShareStart(4);
			break;
		case R.id.button_share_weixin:// 微信分享
			ShareStart(2);
			break;
		case R.id.button_share_pengyouquan:// 朋友圈分享
			ShareStart(3);
			break;
		default:
			break;
		}
	}

	private void ShareStart(int which) {
		// 获取用户点击的平台
		SHARE_MEDIA platform = mPlatformsMap.get(items[which]);
		// 调用直接分享
		mController.directShare(ShareActivity.this, platform, mShareListener);
	}

	/**
	 * 初始化平台map
	 */
	private void initPlatformMap() {
		mPlatformsMap.put("QQ", SHARE_MEDIA.QQ);
		mPlatformsMap.put("QQ空间", SHARE_MEDIA.QZONE);
		mPlatformsMap.put("微信好友", SHARE_MEDIA.WEIXIN);
		mPlatformsMap.put("微信朋友圈", SHARE_MEDIA.WEIXIN_CIRCLE);
		mPlatformsMap.put("新浪微博", SHARE_MEDIA.SINA);
	}

	/**
	 * 配置分享平台参数</br>
	 */
	private void configPlatforms() {
		// 添加新浪SSO授权
		mController.getConfig().setSsoHandler(new SinaSsoHandler());

		// 添加QQ、QZone平台
		addQQQZonePlatform();

		// 添加微信、微信朋友圈平台
		addWXPlatform();
	}

	/**
	 * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
	 *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
	 *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
	 *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
	 * @return
	 */
	private void addQQQZonePlatform() {
		String appId = Constants.QQAPPID;
		String appKey = Constants.QQAPPKEY;
		// 添加QQ支持, 并且设置QQ分享内容的target url
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, appId, appKey);
		qqSsoHandler.addToSocialSDK();
		// 添加QZone平台
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, appId, appKey);
		qZoneSsoHandler.addToSocialSDK();
	}

	/**
	 * @功能描述 : 添加微信平台分享
	 * @return
	 */
	private void addWXPlatform() {
		// 注意：在微信授权的时候，必须传递appSecret
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = Constants.WEIXINAPPID;
		String appSecret = Constants.WEIXINAPPSECRET;
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(this, appId, appSecret);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(this, appId, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

	/**
	 * 根据不同的平台设置不同的分享内容</br>
	 */
	private void setShareContent(String title,String Content,String shareURL,String imageurl) {

//		// 分享后点击的url
//		String shareURL = "http://www.umeng.com/social";
//		// 本地图片
//		// UMImage localImage = new UMImage(this, R.drawable.ic_launcher);
//		// 网络图片
		UMImage urlImage = new UMImage(this, imageurl);
//		// 分享的标题
//		String title = "我是土木分享的标题";
//		// 分享的内容
//		String Content = "哈喽！大家好。我是土木分享的内容哦。这个app真的好。里面帅锅靓女";

		// 设置微信好友分享的内容
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent.setShareContent(Content);
		weixinContent.setTitle(title);
		weixinContent.setTargetUrl(shareURL);
		weixinContent.setShareMedia(urlImage);
		mController.setShareMedia(weixinContent);

		// 设置朋友圈分享的内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(Content);
		circleMedia.setTitle(title);
		circleMedia.setShareMedia(urlImage);
		circleMedia.setTargetUrl(shareURL);
		mController.setShareMedia(circleMedia);

		// 设置QQ空间分享内容
		QZoneShareContent qzone = new QZoneShareContent();
		qzone.setShareContent(Content);
		qzone.setTargetUrl(shareURL);
		qzone.setTitle(title);
		qzone.setShareMedia(urlImage);
		mController.setShareMedia(qzone);

		// 设置QQ好友分享内容
		QQShareContent qqShareContent = new QQShareContent();
		qqShareContent.setShareContent(Content);
		qqShareContent.setShareMedia(urlImage);
		qqShareContent.setTitle(title);
		qqShareContent.setTargetUrl(shareURL);
		mController.setShareMedia(qqShareContent);

		// 设置新浪分享内容
		SinaShareContent sinaContent = new SinaShareContent();
		sinaContent.setShareContent(Content);
		mController.setShareMedia(sinaContent);

	}

	/**
	 * 分享监听器
	 */
	SnsPostListener mShareListener = new SnsPostListener() {

		@Override
		public void onStart() {
			Qlog.e("", "分享开始");
		}

		@Override
		public void onComplete(SHARE_MEDIA platform, int stCode, SocializeEntity entity) {
			if (stCode == 200) {
				Toast.makeText(ShareActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(ShareActivity.this, "分享失败 : error code : " + stCode, Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

}
