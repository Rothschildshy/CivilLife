package com.app.civillife;

import java.util.List;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.CivilLife.Base.BaseActivity;
import com.CivilLife.Variable.GlobalVariable;
import com.MyView.Widget.PrDialog;
import com.MyView.Widget.SwitchView;
import com.MyView.Widget.SwitchView.OnStateChangedListener;
import com.aysy_mytool.CleanManager;
import com.aysy_mytool.GetAppVersion;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

/**
 * 设置界面 
 * 
 * @author Administrator
 * 
 */
public class SettingActivity extends BaseActivity {
	private TextView mTx_Memory;
	private TextView mTx_Version;
	private String totalCacheSize;
	private SwitchView mSB_Gain;
	private SwitchView mSB_Notice;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		initViews();
		initEvents();
		init();
	}

	@Override
	protected void initViews() {
		mTx_Memory = (TextView) findViewById(R.id.tx_cache);// 缓存
		mTx_Version = (TextView) findViewById(R.id.version);// 当前版本
		mSB_Gain = (SwitchView) findViewById(R.id.switchView_gain);
		mSB_Notice = (SwitchView) findViewById(R.id.switchView_notice);
	}

	@Override
	protected void initEvents() {
		findViewById(R.id.image_back).setOnClickListener(this);
		findViewById(R.id.layout_cache).setOnClickListener(this);
		findViewById(R.id.layout_review).setOnClickListener(this);
		findViewById(R.id.layout_updata).setOnClickListener(this);
		findViewById(R.id.layout_about).setOnClickListener(this);
	}

	@Override
	protected void init() {
		// 获得当前版本号
		String appVersionName = GetAppVersion.getAppVersionName(this);
		mTx_Version.setText("当前版本： " + appVersionName);
		isupdate();
		// 清除缓存
		// ClearCache.Init_CachSize(this, mTx_Memory);
		try {
			totalCacheSize = CleanManager.getTotalCacheSize(this);
			mTx_Memory.setText(totalCacheSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (GlobalVariable.WifiDown) {
			mSB_Gain.setState(true); // 设置初始状态。true为开;false为关[默认]。
		} else {
			mSB_Gain.setState(false); // 设置初始状态。true为开;false为关[默认]。
		}
		mSB_Gain.setOnStateChangedListener(new OnStateChangedListener() {
			@Override
			public void toggleToOn() {
				GlobalVariable.WifiDown=true;
				mSB_Gain.toggleSwitch(true); // 以动画效果切换到打开的状态
				showShortToast("打开模式");
				
			}

			@Override
			public void toggleToOff() {
				GlobalVariable.WifiDown=false;
				// 原本为打开的状态，被点击后
				mSB_Gain.toggleSwitch(false);
				showShortToast("关闭模式");

			}
		});
		if (GlobalVariable.push) {
			mSB_Notice.setState(true); // 设置初始状态。true为开;false为关[默认]。
		} else {
			mSB_Notice.setState(false); // 设置初始状态。true为开;false为关[默认]。
		}
		mSB_Notice.setOnStateChangedListener(new OnStateChangedListener() {
			@Override
			public void toggleToOn() {
				GlobalVariable.push=true;
				mSB_Notice.toggleSwitch(true); // 以动画效果切换到打开的状态
				showShortToast("打开推送");
				
			}
			
			@Override
			public void toggleToOff() {
				GlobalVariable.push=false;
				// 原本为打开的状态，被点击后
				mSB_Notice.toggleSwitch(false);
				showShortToast("关闭推送");
				
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.image_back:
			finish();
			break;
		case R.id.layout_cache:// 清除缓存  
			Cache();
			break;
		case R.id.layout_review:// 赏个好评
			Review();
			break;
		case R.id.layout_updata:// 检查更新
			PrDialog.ShowProssDialog(this, true, "版本检查中");
			updating();
			break;
		case R.id.layout_about:// 关于我们
			break;
		default:
			break;
		}
	}

	private void Cache() {
		PromptDialogs("已累计 " + totalCacheSize + " 缓存", new OnClickListener() {

			@Override
			public void onClick(View v) {
				CleanManager.clearAllCache(SettingActivity.this);
				try {
					totalCacheSize = CleanManager.getTotalCacheSize(SettingActivity.this);
					mTx_Memory.setText(totalCacheSize);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void isupdate() {
		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
				switch (updateStatus) {
				case UpdateStatus.Yes: // has update
					// mImageUpdate.setVisibility(View.VISIBLE);
					break;
				default:
					// mImageUpdate.setVisibility(View.INVISIBLE);
					break;
				}
			}
		});
		UmengUpdateAgent.update(this);
	}

	// 版本更新
	private void updating() {
		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
				PrDialog.DismssProssDialog();
				switch (updateStatus) {
				case UpdateStatus.Yes: // has update
					UmengUpdateAgent.showUpdateDialog(SettingActivity.this, updateInfo);
					break;
				case UpdateStatus.No: // has no update
					showShortToast("此版本为最新版本！");
					break;
				case UpdateStatus.NoneWifi: // none wifi
					showShortToast("没有wifi连接， 只在wifi下更新");
					break;
				case UpdateStatus.Timeout: // time out
					showShortToast("没有wifi连接， 只在wifi下更新");
					break;
				}
			}
		});
		UmengUpdateAgent.update(SettingActivity.this);
	}

	private void Review() {
		Uri uriObj = Uri.parse("market://details?id=" + "com.app.civillife");
		Intent appStartIntent = new Intent(Intent.ACTION_VIEW);
		PackageManager packageManager = getPackageManager();
		List<ResolveInfo> queryIntentActivities = packageManager.queryIntentActivities(appStartIntent, 0);
		if (queryIntentActivities.size() > 0) {
			appStartIntent.setData(uriObj);
			startActivity(appStartIntent);
		} else {
			showShortToast("当前手机没有安装应用市场");
		}
	}
}
