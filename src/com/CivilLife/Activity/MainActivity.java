package com.CivilLife.Activity;

import com.CivilLife.Base.BaseActivity;
import com.CivilLife.Fragment.Tab_Discover;
import com.CivilLife.Fragment.Tab_Homepage;
import com.CivilLife.Fragment.Tab_Message;
import com.CivilLife.Fragment.Tab_My;
import com.CivilLife.Variable.GlobalVariable;
import com.app.civillife.R;
import com.app.civillife.Service.MessageService;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTabHost;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

/**
 * APP首页
 * 
 * @author mac
 * 
 */
@SuppressLint("InflateParams")
public class MainActivity extends BaseActivity {
	private int imagelist[] = new int[] { R.drawable.tab_sel_home, R.drawable.tab_sel_discover,
			R.drawable.tab_sel_message, R.drawable.tab_sel_my };
	private FragmentTabHost mTabHost;
	private int backnum = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// GetDistance.location(mApplication,null).start();//开启定位
		UmengUpdateAgent.update(this);// 开启更新检验
		initViews();
		initEvents();
		init();
		GlobalVariable.SetArrayList();
		Intent intent = new Intent(this, MessageService.class);
		startService(intent);
		
	}

	@Override
	protected void initViews() {
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
	}

	@Override
	protected void initEvents() {
		AddTab("1", R.string.tabtext_title, 0, Tab_Homepage.class);
		AddTab("2", R.string.tabtext_discover, 1, Tab_Discover.class);
		AddTab("3", R.string.tabtext_message, 2, Tab_Message.class);
		AddTab("4", R.string.tabtext_my, 3, Tab_My.class);
	}

	@Override
	protected void init() {
		TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		GlobalVariable.IMEI = TelephonyMgr.getDeviceId();
	}

	@Override
	public void onClick(View v) {

	}

	@SuppressWarnings("rawtypes")
	private void AddTab(String tag, int title, int i, Class cls) {
		TabSpec tabSpec = mTabHost.newTabSpec(tag);
		View view = getLayoutInflater().inflate(R.layout.ic_tab_item, null);
		ImageView image = (ImageView) view.findViewById(R.id.tab_image);
		TextView textview = (TextView) view.findViewById(R.id.tab_text);
		textview.setText(title);
		image.setBackgroundResource(imagelist[i]);
		mTabHost.addTab(tabSpec.setIndicator(view), cls, null);
		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				if (tabId.equals("3")) {
					if (TextUtils.isEmpty(GlobalVariable.UserID)) {
						showShortToast("请客官先登录");
						startActivityForResult(LoginActivity.class, null, 343);
					}
				}
			}
		});

	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg2 != null && arg0 == 343 && arg1 == 0) {
			mTabHost.setCurrentTab(0);
		}
	}

	/**
	 * 双击退出应用
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (backnum == 0) {
			backnum++;
			Toast.makeText(this, "双击退出应用", Toast.LENGTH_SHORT).show();
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					backnum = 0;
				}
			}, 2000);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		Intent intent = new Intent(this, MessageService.class);
		stopService(intent);
		super.onDestroy();
	}

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
