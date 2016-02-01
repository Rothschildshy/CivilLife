package com.CivilLife.Activity;

import java.util.Map;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.CivilLife.Base.BaseActivity;
import com.CivilLife.DataInfo.Constants;
import com.CivilLife.Entity.PublicEntity;
import com.CivilLife.Json.PublicUpJson;
import com.CivilLife.Variable.GlobalVariable;
import com.CivilLife.net.Httpurl;
import com.CivilLife.net.RequestTask;
import com.CivilLife.net.ReturnAL;
import com.app.civillife.R;
import com.aysy_mytool.SpUtils;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import Requset_getORpost.RequestListener;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 登陆界面
 * 
 * @author mac
 * 
 */
public class LoginActivity extends BaseActivity {

	// 整个平台的Controller, 负责管理整个SDK的配置、操作等处理
	private UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
	private int code;
	private int mold;
	private String Name;
	private String profile_image_url;
	private EditText mEd_User;
	private EditText mEd_PassWd;
	private String userID;
	private String userNameMD5;
	private String nickName;
	private String userPassWord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initViews();
		initEvents();
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	/** 初始化视图 **/
	@Override
	protected void initViews() {
		mEd_User = (EditText) findViewById(R.id.edit_user);
		mEd_PassWd = (EditText) findViewById(R.id.edit_password);
		mTx_Register = (TextView) findViewById(R.id.tx_register);
		mTx_Forgetpw = (TextView) findViewById(R.id.tx_forgetpw);
	}

	/** 初始化事件 **/
	@Override
	protected void initEvents() {
		findViewById(R.id.tx_back).setOnClickListener(this);
		findViewById(R.id.layout_login_weixin).setOnClickListener(this);
		findViewById(R.id.layout_login_qq).setOnClickListener(this);
		findViewById(R.id.layout_login_weibo).setOnClickListener(this);
		findViewById(R.id.btn_login).setOnClickListener(this);
		mTx_Register.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
		mTx_Register.setOnClickListener(this);
		mTx_Forgetpw.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
		mTx_Forgetpw.setOnClickListener(this);
	}

	/** 初始化数据 **/
	@Override
	protected void init() {
		// 添加新浪sso授权
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		// 添加QQ支持
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, Constants.QQAPPID, Constants.QQAPPKEY);
		qqSsoHandler.addToSocialSDK();

		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(this, Constants.WEIXINAPPID, Constants.WEIXINAPPSECRET);
		// 设置是否每次都拉去授权页面 false为每次都会跳转 true为第一次授权会 第二次就不会跳转了
		wxHandler.setRefreshTokenAvailable(false);
		wxHandler.addToSocialSDK();

	}

	/** 点击事件初始化 **/
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tx_back:// 取消
			setResult(0, new Intent());
			finish();
			break;
		case R.id.layout_login_weixin:// 微信登陆
			mold = 0;
			login(SHARE_MEDIA.WEIXIN);
			break;
		case R.id.layout_login_qq:// QQ登陆
			mold = 1;
			login(SHARE_MEDIA.QQ);
			break;
		case R.id.layout_login_weibo:// 微博登陆
			mold = 2;
			login(SHARE_MEDIA.SINA);
			break;
		case R.id.btn_login:// 登陆
			Login();
			break;
		case R.id.tx_register:// 注册
			startActivityForResult(RegisterActivity.class, null, 10001);
			break;
		case R.id.tx_forgetpw:// 忘记密码
			startActivityForResult(RetrievePassWordActivity.class, null, 10001);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == event.KEYCODE_BACK) {
			setResult(0, new Intent());
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	// 登录
	private void Login() {
		String User = mEd_User.getText().toString();
		String PassWd = mEd_PassWd.getText().toString();
		if (TextUtils.isEmpty(User)) {
			YoYo.with(Techniques.Shake).playOn(mEd_User);
			showShortToast("用户名不能为空");
			return;
		}
		if (TextUtils.isEmpty(PassWd)) {
			YoYo.with(Techniques.Shake).playOn(mEd_PassWd);
			showShortToast("密码不能为空");
			return;
		}
		new RequestTask(LoginActivity.this, ReturnAL.LoginMap(User, PassWd), listener, false, true, "登陆中")
				.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.URL);
	}

	// 普通登陆回调
	RequestListener listener = new RequestListener() {

		@Override
		public void responseResult(String jsonObject) {
			PublicUpJson publicjson = PublicUpJson.readJsonToSendmsgObject(LoginActivity.this, jsonObject);
			if (publicjson == null) {
				return;
			}
			PublicEntity publicEntity = publicjson.getAl().get(0);
			String status = publicEntity.getStatus();
			if (status.equals("1")) {
				userID = publicEntity.getUserId();
				userNameMD5 = publicEntity.getUserName();
				nickName = userNameMD5;
				userPassWord = publicEntity.getUserPassWord();
				// 普通登录是没头像的需要清空头像
				profile_image_url = "";
				GlobalVariable.UserImage = profile_image_url;
				SpUtils.saveString(LoginActivity.this, GlobalVariable.USERIMAGE, profile_image_url);
				LoginSucceed();
			} else {
				showShortToast(publicjson.getAl().get(0).getMessage());
				YoYo.with(Techniques.Shake).playOn(mEd_User);
				YoYo.with(Techniques.Shake).playOn(mEd_PassWd);
			}
		}

		@Override
		public void responseException(String errorMessage) {
			showShortToast(errorMessage);
		}
	};
	private TextView mTx_Register;
	private TextView mTx_Forgetpw;

	private void LoginSucceed() {
		// 缓存用户信息
		GlobalVariable.UserID = userID;
		SpUtils.saveString(this, GlobalVariable.USERID, userID);
		GlobalVariable.UserName = userNameMD5;
		SpUtils.saveString(this, GlobalVariable.USERNAME, userNameMD5);
		GlobalVariable.Nickname = nickName;
		SpUtils.saveString(this, GlobalVariable.NICKNAME, nickName);
		GlobalVariable.UserPassWord = userPassWord;
		SpUtils.saveString(this, GlobalVariable.USERPW, userPassWord);

		// Log.e("", "");
		// Qlog.e("登录数据", "UserID:" + userID + "\nUserImage:" +
		// profile_image_url + "\nuserNameMD5:" + userNameMD5 + "\nuserName:"
		// + nickName + "\nuserPassWord:" + userPassWord);
		showShortToast("登陆成功！");
		SetResult(10002, null);
	}

	/**
	 * 授权。如果授权成功，则获取用户信息</br>
	 * 
	 * @param i
	 */
	private void login(final SHARE_MEDIA platform) {
		mController.doOauthVerify(this, platform, new UMAuthListener() {

			@Override
			public void onStart(SHARE_MEDIA platform) {
				ShowProgressDialog("授权开始");
			}

			@Override
			public void onError(SocializeException e, SHARE_MEDIA platform) {
				DismssProssDialog();
				showShortToast("授权失败");
			}

			@Override
			public void onComplete(Bundle value, SHARE_MEDIA platform) {
				DismssProssDialog();
				// Qlog.e("===", "value:" + value.toString());
				String uid = value.getString("uid");
				if (!TextUtils.isEmpty(uid)) {
					ShowProgressDialog("获取资料");
					getUserInfo(platform, value);
				} else {
					showShortToast("授权失败");
				}
			}

			@Override
			public void onCancel(SHARE_MEDIA platform) {
				DismssProssDialog();
				showShortToast("授权取消");
			}
		});
	}

	/**
	 * 获取授权平台的用户信息</br>
	 * 
	 * @param value
	 * @param
	 */
	private void getUserInfo(SHARE_MEDIA platform, final Bundle value) {
		mController.getPlatformInfo(this, platform, new UMDataListener() {
			@Override
			public void onStart() {
			}

			@Override
			public void onComplete(int status, Map<String, Object> info) {
				if (info != null) {
					// Qlog.e("", "回调信息：" + info.toString());
					String access_token = null;
					switch (mold) {
					case 0:// WeiXin的token
						access_token = value.getString("access_token");
						nickName = (String) info.get("nickname");
						profile_image_url = (String) info.get("headimgurl");
						// Qlog.e("", "WX Token:" + access_token + "\n" + "名称:"
						// + nickName + "\n" + "图片：" + profile_image_url);
						break;
					case 1:// QQ的token
						access_token = value.getString("access_token");
						nickName = (String) info.get("screen_name");
						profile_image_url = (String) info.get("profile_image_url");
						// Qlog.e("", "QQ Token:" + access_token + "\n" + "名称:"
						// + nickName + "\n" + "图片：" + profile_image_url);
						break;
					case 2:// Sina的token
						access_token = value.getString("access_key");
						nickName = (String) info.get("screen_name");
						profile_image_url = (String) info.get("profile_image_url");
						// Qlog.e("", "WB Token:" + access_token + "\n" + "名称:"
						// + nickName + "\n" + "图片：" + profile_image_url);
						break;
					default:
						break;
					}
					DismssProssDialog();
					getHttpUtils(access_token, mold, nickName);
				}
			}

		});
	}

	// 第三方授权登陆回调
	private void getHttpUtils(String access_token, int Type, String nickName) {
		new RequestTask(this, new RequestListener() {
			private String message;

			@Override
			public void responseResult(String jsonObject) {
				try {
					JSONObject jo = new JSONObject(jsonObject);
					JSONArray jsonArray = jo.getJSONArray("Data");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject object = jsonArray.getJSONObject(i);
						code = object.getInt("Status");
						userID = object.getString("UserID");
						userNameMD5 = object.getString("UserName");
						if (object.has("UserPassWord")) {
							userPassWord = object.getString("UserPassWord");
						} else if (object.has("Message")) {
							userPassWord = object.getString("PassWord");
						}
					}
					if (code == 1) {// 登陆成功
						// 保存图片
						GlobalVariable.UserImage = profile_image_url;
						SpUtils.saveString(LoginActivity.this, GlobalVariable.USERIMAGE, profile_image_url);
						LoginSucceed();

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void responseException(String errorMessage) {

			}
		}, false, true, "登陆中").executeOnExecutor(Executors.newCachedThreadPool(),
				Httpurl.ToKenLogin(access_token, Type, nickName));

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
		if (data != null) {
			if (requestCode == 10001 && resultCode == 10002) {// 注册成功回调 找回密码成功回调
				String User = data.getStringExtra("USER");
				String PassWD = data.getStringExtra("PASSWD");
				mEd_User.setText(User);
				mEd_PassWd.setText(PassWD);
			}
		}
	}

}
