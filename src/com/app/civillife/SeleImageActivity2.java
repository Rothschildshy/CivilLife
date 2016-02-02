package com.app.civillife;

import com.umeng.analytics.MobclickAgent;

import Downloadimage.SelectPicActivity2;

/**
 * 选择图片界面 大罩是长方形图片
 * 
 * @author Administrator
 *
 */
public class SeleImageActivity2 extends SelectPicActivity2 {
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