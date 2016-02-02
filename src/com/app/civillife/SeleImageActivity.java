package com.app.civillife;

import com.umeng.analytics.MobclickAgent;

import Downloadimage.SelectPicActivity3;

/**
 * 选择图片界面 头像是正方形头像
 * 
 * @author Administrator
 *
 */
public class SeleImageActivity extends SelectPicActivity3 {
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