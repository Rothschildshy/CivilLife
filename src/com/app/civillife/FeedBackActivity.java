package com.app.civillife;

import java.util.concurrent.Executors;

import com.CivilLife.Base.BaseActivity;
import com.CivilLife.Entity.PublicEntity;
import com.CivilLife.Json.PublicUpJson;
import com.CivilLife.net.Httpurl;
import com.CivilLife.net.RequestTask;
import com.CivilLife.net.ReturnAL;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import Requset_getORpost.RequestListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

/**
 * 意见反馈
 * 
 * @author Administrator
 * 
 */
public class FeedBackActivity extends BaseActivity {
	private EditText mEd_Content;
	private EditText mEd_Contact;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_back);
		initViews();
		initEvents();
		init();
	}

	// 初始化数据
	@Override
	protected void initViews() {
		mEd_Content = (EditText) findViewById(R.id.content);
		mEd_Contact = (EditText) findViewById(R.id.contact);

	}

	@Override
	protected void initEvents() {
		findViewById(R.id.image_back).setOnClickListener(this);
		findViewById(R.id.tx_submit).setOnClickListener(this);
	}

	@Override
	protected void init() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.image_back:
			finish();
			break;
		case R.id.tx_submit:
			FeedBack();
			break;
		default:
			break;
		}
	}

	private void FeedBack() {
		String Content = mEd_Content.getText().toString();
		String Contact = mEd_Contact.getText().toString();

		if (TextUtils.isEmpty(Content)) {
			YoYo.with(Techniques.Shake).playOn(mEd_Content);
			showShortToast("反馈内容不能为空");
			return;
		}
		if (TextUtils.isEmpty(Contact)) {
			YoYo.with(Techniques.Shake).playOn(mEd_Contact);
			showShortToast("联系方式不能为空");
			return;
		}
		new RequestTask(this, ReturnAL.FeedBack(Content, Contact), listener, false, true, "数据提交中").executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.URL);
	}

	RequestListener listener = new RequestListener() {

		@Override
		public void responseResult(String jsonObject) {
			PublicUpJson publicjson = PublicUpJson.readJsonToSendmsgObject(FeedBackActivity.this, jsonObject);
			if (publicjson == null) {
				return;
			}
			PublicEntity publicEntity = publicjson.getAl().get(0);
			String status = publicEntity.getStatus();
			if (status.equals("1")) {
				showShortToast("感谢您的宝贵意见，我们会尽快处理！");
				finish();
			} else {
				showShortToast(publicjson.getAl().get(0).getMessage());
			}
		}

		@Override
		public void responseException(String errorMessage) {
			showShortToast(errorMessage);
		}
	};

}
