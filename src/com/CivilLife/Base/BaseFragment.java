package com.CivilLife.Base;

import java.util.ArrayList;
import java.util.List;

import com.CivilLife.Activity.LoginActivity;
import com.CivilLife.Variable.GlobalVariable;
import com.CivilLife.Widget.AlertDialogEx;
import com.CivilLife.Widget.AlertDialogEx.Builder;
import com.MyView.Widget.PrDialog;
import com.app.civillife.R;
import com.aysy_mytool.ToastUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment implements OnClickListener {

	protected View mView;

	protected List<AsyncTask<Void, Void, Object>> mAsyncTasks = new ArrayList<AsyncTask<Void, Void, Object>>();
	public BaseApplication mApplication;
	private com.CivilLife.Base.BaseApplication application;

	public BaseFragment() {
		super();
	}

	public BaseFragment(BaseApplication application, Activity activity, Context context) {
		this.application = application;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mApplication = application;

		return mView;
	}

	/** 初始化视图 **/
	protected abstract void initViews();

	/** 初始化事件 **/
	protected abstract void initEvents();

	/** 初始化数据 **/
	protected abstract void init();

	/** 点击事件初始化 **/
	public abstract void onClick(View v);

	public View findViewById(int id) {
		return mView.findViewById(id);
	}

	// 缓存片段
	public View FragmentCache(int id, LayoutInflater inflater, ViewGroup container) {
		if (mView == null) {
			mView = inflater.inflate(id, container, false);
			initViews();
			initEvents();
			init();
		}
		ViewGroup parent = (ViewGroup) mView.getParent();
		if (parent != null) {
			parent.removeView(mView);
		}
		return mView;
	}

	// 片段
	public View NoFragmentCache(int id, LayoutInflater inflater, ViewGroup container) {

		mView = inflater.inflate(id, container, false);
		initViews();
		initEvents();
		init();

		return mView;
	}

	/** Toast提示(来自res) **/
	protected void showShortToast(int resId) {
		ToastUtil.showToast(getActivity(), getString(resId));
	}

	/** Toast提示(来自string) **/
	protected void showShortToast(String str) {
		ToastUtil.showToast(getActivity(), str);
	}

	/** Toast提示(来自res) **/
	protected void showmsg(int resId) {
		ToastUtil.showMessage(getActivity(), getString(resId));
	}

	/** Toast提示(来自string) **/
	protected void showmsg(String str) {
		ToastUtil.showMessage(getActivity(), str);
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
		getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.start);
	}

	/** 含有Bundle通过Class跳转界面 **/
	protected void startActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(getActivity(), cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.start);
	}

	/** 含有Bundle带返回结果通过Class跳转界面 **/
	public void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
		Intent intent = new Intent();
		intent.setClass(getActivity(), cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivityForResult(intent, requestCode);
		getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.start);
	}

	/** 含有Bundle带返回结果通过Class跳转界面 同时自定义进出动画 **/
	public void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode, int in, int out) {
		Intent intent = new Intent();
		intent.setClass(getActivity(), cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivityForResult(intent, requestCode);
		getActivity().overridePendingTransition(in, out);
	}

	/** 带有右出动画的退出 **/
	public void finish() {
		super.getActivity().finish();
		getActivity().overridePendingTransition(R.anim.start, R.anim.push_right_out);
	}

	/** 默认退出 **/
	protected void defaultFinish() {
		super.getActivity().finish();
	}

	/** Dialogs1 **/
	public void PromptDialogs(String meg, View.OnClickListener cl) {
		new AlertDialogEx(getActivity()).setBuilder(new Builder(getActivity()).setMessage(meg)
				.setNegativeButton("确定", cl, true).setPositiveButton("取消", null, true).show());
	}

	/** Dialogs2 **/
	public void PromptDialogs(String meg, View.OnClickListener cl, View.OnClickListener cl1) {
		new AlertDialogEx(getActivity()).setBuilder(new Builder(getActivity()).setMessage(meg)
				.setNegativeButton("确定", cl, true).setPositiveButton("取消", cl1, true).show());
	}

	/** Dialogs3 **/
	public void PromptDialogs(String meg, String yesstr, String nostr, View.OnClickListener cl,
			View.OnClickListener cl1) {
		new AlertDialogEx(getActivity()).setBuilder(new Builder(getActivity()).setMessage(meg)
				.setNegativeButton(yesstr, cl, true).setPositiveButton(nostr, cl1, true).show());
	}

	/** 弹出请求提示框 **/
	public void ShowProgressDialog(String meg) {
		new PrDialog().ShowProssDialog(getActivity(), true, meg);
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
			showShortToast("请看官先登录");
			startActivity(LoginActivity.class);
			return true;
		}
		return false;
	}
}
