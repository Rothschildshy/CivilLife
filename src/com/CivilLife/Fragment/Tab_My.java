package com.CivilLife.Fragment;

import com.CivilLife.Activity.LoginActivity;
import com.CivilLife.Activity.MyinfoActivity;
import com.CivilLife.Base.BaseFragment;
import com.CivilLife.Variable.GlobalVariable;
import com.CivilLife.Widget.CircleImageView;
import com.app.civillife.FeedBackActivity;
import com.app.civillife.HelpActivity;
import com.app.civillife.ManageActivity;
import com.app.civillife.MyFriendActivity;
import com.app.civillife.R;
import com.app.civillife.SearchFriendActivity;
import com.app.civillife.SettingActivity;
import com.app.civillife.Util.GetDistance;
import com.aysy_mytool.SpUtils;
import com.umeng.analytics.MobclickAgent;

import Downloadimage.ImageUtils;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 我的片段
 * 
 * @author Administrator
 * 
 */
@SuppressLint("HandlerLeak")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Tab_My extends BaseFragment {
	private ScrollView mScrollView;
	private RelativeLayout mLa_Lonin;
	private RelativeLayout mLa_LonOut;
	private RelativeLayout mLa_Individual;
	private CircleImageView mIm_Pic;
	private TextView mTx_Naem;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return FragmentCache(R.layout.tab_my, inflater, container);
	}

	@Override
	protected void initViews() {
		mScrollView = (ScrollView) findViewById(R.id.scrollView1);
		mLa_Lonin = (RelativeLayout) findViewById(R.id.layout_login);
		mLa_LonOut = (RelativeLayout) findViewById(R.id.laout_outlogin);
		mLa_Individual = (RelativeLayout) findViewById(R.id.layout_individual);
		mIm_Pic = (CircleImageView) findViewById(R.id.image_pci);
		mTx_Naem = (TextView) findViewById(R.id.tx_name);
	}

	@Override
	protected void initEvents() {
		mLa_Lonin.setOnClickListener(this);
		mLa_LonOut.setOnClickListener(this);
		mLa_Individual.setOnClickListener(this);
		findViewById(R.id.layout_manage).setOnClickListener(this);
		findViewById(R.id.layout_crowd).setOnClickListener(this);
		findViewById(R.id.layout_apprentice).setOnClickListener(this);
		findViewById(R.id.layout_overman).setOnClickListener(this);
		findViewById(R.id.layout_addfriend).setOnClickListener(this);
		findViewById(R.id.layout_addoverman).setOnClickListener(this);
		findViewById(R.id.layout_feedback).setOnClickListener(this);
		findViewById(R.id.laoyut_help).setOnClickListener(this);
		findViewById(R.id.layout_setting).setOnClickListener(this);
	}

	@Override
	protected void init() {
		if (TextUtils.isEmpty(GlobalVariable.UserID)) {
			mLa_Lonin.setVisibility(View.VISIBLE);
			mLa_LonOut.setVisibility(View.GONE);
			mLa_Individual.setVisibility(View.GONE);
		} else {
			ShowLogin();
		}

	}

	@Override
	public void onClick(View v) {
		Bundle bundle = new Bundle();
		switch (v.getId()) {
		case R.id.layout_login:// 登录
			startActivityForResult(LoginActivity.class, null, 10001);
			break;
		case R.id.laout_outlogin:// 退出登录
			ShowOutLogin();
			break;
		case R.id.layout_individual:// 个人中心
			if (tologin()) {
				return;
			}
			startActivityForResult(MyinfoActivity.class, null, 10001);
			break;
		case R.id.layout_manage:// 管理我的文字
			if (tologin()) {
				return;
			}
			startActivity(ManageActivity.class, null);
			break;
		case R.id.layout_crowd:// 我的筑友
			if (tologin()) {
				return;
			}
			bundle.putInt("type", 1);
			startActivity(MyFriendActivity.class, bundle);
			break;
		case R.id.layout_apprentice:// 我的徒弟
			if (tologin()) {
				return;
			}
			bundle.putInt("type", 2);
			startActivity(MyFriendActivity.class, bundle);
			break;
		case R.id.layout_overman:// 我的师傅
			if (tologin()) {
				return;
			}
			bundle.putInt("type", 3);
			startActivity(MyFriendActivity.class, bundle);
			break;
		case R.id.layout_addfriend:// 添加筑友
			if (tologin()) {
				return;
			}
			bundle.putInt("type", 1);
			startActivity(SearchFriendActivity.class, bundle);
			break;
		case R.id.layout_addoverman:// 添加师傅
			if (tologin()) {
				return;
			}
			bundle.putInt("type", 2);
			startActivity(SearchFriendActivity.class, bundle);
			break;
		case R.id.layout_feedback:// 意见反馈
			startActivity(FeedBackActivity.class, null);
			break;
		case R.id.laoyut_help:// 帮助
			startActivity(HelpActivity.class, null);
			break;
		case R.id.layout_setting:// 设置
			startActivity(SettingActivity.class, null);
			break;
		default:
			break;
		}
	}

	private void ShowOutLogin() {
		PromptDialogs("确定要退出登录吗？", new OnClickListener() {

			@Override
			public void onClick(View v) {
				OutLogin();
				showShortToast("退出成功!");

			}
		});
	};

	private void OutLogin() {
		mScrollView.scrollTo(0, 0);
		mLa_Lonin.setVisibility(View.VISIBLE);
		mLa_LonOut.setVisibility(View.GONE);
		mLa_Individual.setVisibility(View.GONE);

		// 退出初始化清空用户信息缓存
		String userID = "";
		String UserName = "";
		String NickName = "";
		String userPassWord = "";
		String UserImage = "";
		GlobalVariable.UserID = userID;
		SpUtils.saveString(getActivity(), GlobalVariable.USERID, userID);
		GlobalVariable.UserName = UserName;
		SpUtils.saveString(getActivity(), GlobalVariable.USERNAME, UserName);
		GlobalVariable.Nickname = NickName;
		SpUtils.saveString(getActivity(), GlobalVariable.NICKNAME, NickName);
		GlobalVariable.UserPassWord = userPassWord;
		SpUtils.saveString(getActivity(), GlobalVariable.USERPW, userPassWord);
		GlobalVariable.UserImage = UserImage;
		SpUtils.saveString(getActivity(), GlobalVariable.USERIMAGE, userPassWord);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {// 登陆成功回调
			if (requestCode == 10001 && resultCode == 10002) {
				ShowLogin();
			}
		}
	}

	private void ShowLogin() {
		// 启动定位 传位置坐标到服务器
		GetDistance.location(getActivity(), mhandler).start();
		mLa_Lonin.setVisibility(View.GONE);
		mLa_LonOut.setVisibility(View.VISIBLE);
		mLa_Individual.setVisibility(View.VISIBLE);
		if (!TextUtils.isEmpty(GlobalVariable.UserImage)) {
			ImageUtils.loadImage1(getActivity(), GlobalVariable.UserImage, mIm_Pic, R.drawable.ic_my_nolog_selector,
					R.drawable.ic_my_nolog_selector, GlobalVariable.WifiDown);
		} else {
			mIm_Pic.setImageResource(R.drawable.ic_my_nolog_selector);
		}
		mTx_Naem.setText(GlobalVariable.Nickname);
	}

	Handler mhandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			if (msg.what == 11) {
				boolean login = (Boolean) msg.obj;
				if (login) {
					if (!TextUtils.isEmpty(GlobalVariable.UserImage)) {
						ImageUtils.loadImage(getActivity(), GlobalVariable.UserImage, mIm_Pic, GlobalVariable.WifiDown);
					} else {
						mIm_Pic.setImageResource(R.drawable.ic_my_nolog_selector);
					}
					mTx_Naem.setText(GlobalVariable.Nickname);
				}
			}
		};
	};

	@Override
	public void onResume() {
		super.onResume();
		if (!TextUtils.isEmpty(GlobalVariable.UserID)) {
			ShowLogin();
		}
		MobclickAgent.onPageStart("Tab_My"); // 统计页面，"MainScreen"为页面名称，可自定义
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("Tab_My");// 统计页面，"MainScreen"为页面名称，可自定义
	}
}
