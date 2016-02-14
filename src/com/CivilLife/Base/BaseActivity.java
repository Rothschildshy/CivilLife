package com.CivilLife.Base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.CivilLife.Activity.LoginActivity;
import com.CivilLife.Variable.GlobalVariable;
import com.CivilLife.Widget.AlertDialogEx;
import com.CivilLife.Widget.AlertDialogEx.Builder;
import com.MyView.Widget.PrDialog;
import com.app.civillife.R;
import com.aysy_mytool.ToastUtil;

public abstract class BaseActivity extends FragmentActivity implements OnClickListener {

	public BaseApplication mApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 竖屏

		mApplication = (BaseApplication) getApplication();
		mApplication.addActivity(this);
	}

	/** 初始化视图 **/
	protected abstract void initViews();

	/** 初始化事件 **/
	protected abstract void initEvents();

	/** 初始化数据 **/
	protected abstract void init();

	/** 点击事件初始化 **/
	public abstract void onClick(View v);

	/** Toast提示(来自res) **/
	protected void showShortToast(int resId) {
		ToastUtil.showToast(this, getString(resId));
	}

	/** Toast提示(来自string) **/
	protected void showShortToast(String str) {
		ToastUtil.showToast(this, str);
	}

	/** Toast提示(来自string) **/
	protected void showMessage(String str) {
		ToastUtil.showMessage(this, str);
	}

	/** 通过Class跳转界面 **/
	protected void startActivity(Class<?> cls) {
		startActivity(cls, null);
	}

	/** 通过Action跳转界面 **/
	protected void startActivity(String action) {
		startActivity(action, null);
	}

	/** 含有Bundle通过Action跳转界面 **/
	protected void startActivity(String action, Bundle bundle) {
		Intent intent = new Intent();
		intent.setAction(action);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
		overridePendingTransition(R.anim.push_left_in, R.anim.start);
	}

	/** 含有Bundle通过Class跳转界面 **/
	protected void startActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
		overridePendingTransition(R.anim.push_left_in, R.anim.start);
	}

	/** 含有Bundle带返回结果通过Class跳转界面 **/
	public void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivityForResult(intent, requestCode);
		overridePendingTransition(R.anim.push_left_in, R.anim.start);
	}

	/** 含有Bundle带返回结果通过Class跳转界面 同时自定义进出动画 **/
	public void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode, int in, int out) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivityForResult(intent, requestCode);
		overridePendingTransition(in, out);
	}

	/** 含有Bundle 的返回 **/
	public void SetResult(int requestCode, Bundle bundle) {
		Intent intent = new Intent();
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		setResult(requestCode, intent);
		defaultFinish();
	}

	/** 带有右出动画的退出 **/
	public void finish() {
		super.finish();
//		overridePendingTransition(R.anim.start, R.anim.push_right_out);
	}

	/** 默认退出 **/
	protected void defaultFinish() {
		super.finish();
	}

	/** Dialogs1 **/
	public void PromptDialogs(String meg, View.OnClickListener cl) {
		new AlertDialogEx(this).setBuilder(new Builder(this).setMessage(meg).setNegativeButton("确定", cl, true)
				.setPositiveButton("取消", null, true).show());
	}

	/** Dialogs2 **/
	public void PromptDialogs(String meg, View.OnClickListener cl, View.OnClickListener cl1) {
		new AlertDialogEx(this).setBuilder(new Builder(this).setMessage(meg).setNegativeButton("确定", cl, true)
				.setPositiveButton("取消", cl1, true).show());
	}

	/** Dialogs3 **/
	public void PromptDialogs(String meg, String yesstr, String nostr, View.OnClickListener cl,
			View.OnClickListener cl1) {
		new AlertDialogEx(this).setBuilder(new Builder(this).setMessage(meg).setNegativeButton(yesstr, cl, true)
				.setPositiveButton(nostr, cl1, true).show());
	}

	/** 弹出请求提示框 **/
	public void ShowProgressDialog(String meg) {
		new PrDialog().ShowProssDialog(this, true, meg);
	}

	/** 关闭请求提示框 **/
	public void DismssProssDialog() {
		new PrDialog().DismssProssDialog();
	}

	/** 获取手机唯一标识 **/
	public String getIMEI() {
		return GlobalVariable.IMEI;
	}

	/** 是否登录处理 **/
	public boolean tologin() {
		if (TextUtils.isEmpty(GlobalVariable.UserID)) {
			showShortToast("请客官先登录");
			startActivity(LoginActivity.class);
			return true;
		}
		return false;
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}

}
