package com.CivilLife.Activity;

import com.app.civillife.R;
import com.umeng.analytics.MobclickAgent;
import com.zhy.guidepagerlib.GuideContoler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 引导页
 * 
 * @author Administrator
 *
 */
public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置无标题  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        //设置全屏  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
                WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		setContentView(R.layout.activity_splash);
		initViewPager();
	}

	/** 使用写好的库初始化引导页面 **/
	private void initViewPager() {
		GuideContoler contoler = new GuideContoler(this);
		// contoler.setmShapeType(ShapeType.RECT);//设置指示器的形状为矩形，默认是圆形
		int[] imgIds = { R.drawable.guide_1, R.drawable.guide_2 };
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.pager_four, null);
		contoler.init(imgIds, view);
		view.findViewById(R.id.bt_login).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(SplashActivity.this, MainActivity.class));
				finish();
				overridePendingTransition(R.anim.start_in, R.anim.start_out);
			}
		});

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
