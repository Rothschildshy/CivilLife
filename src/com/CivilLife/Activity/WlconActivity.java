package com.CivilLife.Activity;

import com.CivilLife.Variable.GlobalVariable;
import com.app.civillife.R;
import com.app.civillife.Util.GetDistance;
import com.aysy_mytool.GetAgeOrConstellation;
import com.aysy_mytool.Qlog;
import com.aysy_mytool.SpUtils;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

/**
 * 欢迎界面
 */

public class WlconActivity extends Activity {

	private ImageView mImaeg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wlcon);
		GetDistance.location(this, null).start();// 开启定位

		mImaeg = (ImageView) findViewById(R.id.imageView1);
		new Handler().postDelayed(new Runnable() {
			private Editor edit;

			@Override
			public void run() {
				// 利用数据共享来储存是否第一次登录
				SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
				boolean isFirst = sp.getBoolean("isFirst", true);
				if (isFirst) {
					edit = sp.edit();
					edit.putBoolean("isFirst", false);
					edit.commit();

					InitInfo();// 初始化变量
					// 第一次登录跳转到新手引导界面
					startActivity(new Intent(WlconActivity.this, SplashActivity.class));
				} else {
					InitData();// 初始化数据
					// 不是首次登录跳转到主页面
					startActivity(new Intent(WlconActivity.this, MainActivity.class));
				}
				finish();
				overridePendingTransition(R.anim.start_in, R.anim.start_out);
			}

		}, 2000);
	}

	private void InitInfo() {// 初始化全部
		String Null = "";
		GlobalVariable.UserImage = Null;
		SpUtils.saveString(this, GlobalVariable.USERIMAGE, Null);
		GlobalVariable.UserID = Null;
		SpUtils.saveString(this, GlobalVariable.USERID, Null);
		GlobalVariable.UserName = Null;
		SpUtils.saveString(this, GlobalVariable.USERNAME, Null);
		GlobalVariable.Nickname = Null;
		SpUtils.saveString(this, GlobalVariable.NICKNAME, Null);
		GlobalVariable.UserPassWord = Null;
		SpUtils.saveString(this, GlobalVariable.USERPW, Null);
	}

	private void InitData() {
		// 初始化UserId
		String UserId = SpUtils.getString(this, GlobalVariable.USERID);
		GlobalVariable.UserID = UserId;
		// 初始化头像
		String UserImage = SpUtils.getString(this, GlobalVariable.USERIMAGE);
		GlobalVariable.UserImage = UserImage;
		// 初始化后台返回的username
		String UserName = SpUtils.getString(this, GlobalVariable.USERNAME);
		GlobalVariable.UserName = UserName;
		// 初始化用户名 昵称
		String Nickname = SpUtils.getString(this, GlobalVariable.NICKNAME);
		GlobalVariable.Nickname = Nickname;
		// 初始化密码
		String userPassWord = SpUtils.getString(this, GlobalVariable.USERPW);
		GlobalVariable.UserPassWord = userPassWord;
		// 坐标设置最近的一次定位
		String Longitude = SpUtils.getString(this, "Longitude");
		GlobalVariable.mycoordinates_x = Longitude;
		String Latitude = SpUtils.getString(this, "Latitude");
		GlobalVariable.mycoordinates_y = Latitude;

		Qlog.e("初始化数据", "UserID:" + UserId + "\n头像:" + UserImage + "\n用户名:" + UserName + "\n昵称:" + Nickname
				+ "\n用户密码MD5:" + userPassWord + "\n坐标:" + Longitude + "--" + Latitude);
	}

	/**
	 * 按back无法返回
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
