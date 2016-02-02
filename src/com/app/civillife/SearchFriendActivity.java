package com.app.civillife;

import com.CivilLife.Base.BaseActivity;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 搜索 好友 师傅
 * 
 * @author Administrator
 * 
 */
public class SearchFriendActivity extends BaseActivity {
	private TextView mTx_Title;
	private EditText mEd_Content;
	private int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_friend);
		initViews();
		initEvents();
		init();
	}

	@Override
	protected void initViews() {
		mTx_Title = (TextView) findViewById(R.id.tx_title);
		mEd_Content = (EditText) findViewById(R.id.edit_user);

	}

	@Override
	protected void initEvents() {
		findViewById(R.id.image_back).setOnClickListener(this);
		findViewById(R.id.btn_login).setOnClickListener(this);
	}

	@Override
	protected void init() {
		type = getIntent().getIntExtra("type", 0);
		if (type == 1) {
			mTx_Title.setText("查找好友");
		} else if (type == 2) {
			mTx_Title.setText("查找师傅");
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.image_back:
			finish();
			break;
		case R.id.btn_login:
			String Content = mEd_Content.getText().toString();
			if (TextUtils.isEmpty(Content)) {
				YoYo.with(Techniques.Shake).playOn(mEd_Content);
				showShortToast("搜索内容不能为空");
				return;
			}
			if (tologin()) {
				return;
			}
			Bundle bundle = new Bundle();
			if (type == 1) {
				bundle.putInt("type", 6);
			} else if (type == 2) {
				bundle.putInt("type", 7);
			}
			bundle.putString("content", Content);
			startActivity(InfoHomepageActivity.class, bundle);
			break;
		default:
			break;
		}
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
