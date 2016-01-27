package com.CivilLife.Activity;

import java.util.concurrent.Executors;

import com.CivilLife.Base.BaseActivity;
import com.CivilLife.Entity.PublicEntity;
import com.CivilLife.Json.PublicUpJson;
import com.CivilLife.net.Httpurl;
import com.CivilLife.net.RequestTask;
import com.CivilLife.net.ReturnAL;
import com.app.civillife.R;
import com.aysy_mytool.ToastUtil;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import Requset_getORpost.RequestListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

/**
 * 找回密码
 * 
 * @author mac
 * 
 */
public class RetrievePassWordActivity extends BaseActivity {
	private EditText mEd_User;
	private EditText mEd_Email;
	private EditText mEd_PassWd;
	private EditText mEd_Re_PassWd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_retrievepassword);
		initViews();
		initEvents();
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	@Override
	protected void initViews() {
		findViewById(R.id.tx_back).setOnClickListener(this);
		findViewById(R.id.btn_submit_to).setOnClickListener(this);
		mEd_User = (EditText) findViewById(R.id.edit_user);
		mEd_Email = (EditText) findViewById(R.id.edit_email);
		mEd_PassWd = (EditText) findViewById(R.id.edit_password);
		mEd_Re_PassWd = (EditText) findViewById(R.id.edit_repetition_password);

	}

	@Override
	protected void initEvents() {

	}

	@Override
	protected void init() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tx_back:// 取消
			finish();
			break;
		case R.id.btn_submit_to:// 提交数据
			RetrievePassWord();
			break;
		default:
			break;
		}
	}

	private boolean Type = true;

	// 提交数据
	private void RetrievePassWord() {
		user = mEd_User.getText().toString();
		email = mEd_Email.getText().toString();
		passWd = mEd_PassWd.getText().toString();
		String Re_PassWd = mEd_Re_PassWd.getText().toString();
		if (TextUtils.isEmpty(user)) {
			YoYo.with(Techniques.Shake).playOn(mEd_User);
			ToastUtil.showToast(this, "用户名不能为空");
			return;
		}
		if (TextUtils.isEmpty(email)) {
			YoYo.with(Techniques.Shake).playOn(mEd_Email);
			ToastUtil.showToast(this, "邮箱不能为空");
			return;
		}
		if (TextUtils.isEmpty(passWd)) {
			YoYo.with(Techniques.Shake).playOn(mEd_PassWd);
			ToastUtil.showToast(this, "密码不能为空");
			return;
		}
		if (TextUtils.isEmpty(Re_PassWd)) {
			YoYo.with(Techniques.Shake).playOn(mEd_Re_PassWd);
			ToastUtil.showToast(this, "确认密码不能为空");
			return;
		}
		if (!passWd.equals(Re_PassWd)) {
			YoYo.with(Techniques.Shake).playOn(mEd_PassWd);
			YoYo.with(Techniques.Shake).playOn(mEd_Re_PassWd);
			ToastUtil.showToast(this, "两次密码不一致");
			return;
		}
		Type = true;
		new RequestTask(RetrievePassWordActivity.this, ReturnAL.VerificationMap(user, "", email), listener, false, true,
				"验证中").executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.URL);
	}

	// 注册回调
	RequestListener listener = new RequestListener() {

		@Override
		public void responseResult(String jsonObject) {
			PublicUpJson publicjson = PublicUpJson.readJsonToSendmsgObject(RetrievePassWordActivity.this, jsonObject);
			if (publicjson == null) {
				return;
			}
			PublicEntity publicEntity = publicjson.getAl().get(0);
			String status = publicEntity.getStatus();
			if (status.equals("1")) {
				if (Type) {// 说明是验证的
					Type = false;
					new RequestTask(RetrievePassWordActivity.this, ReturnAL.VerificationMap(user, passWd, email),
							listener, false, true, "修改中").executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.URL);
				} else {// 修改成功的
					showShortToast("修改成功！");
					Bundle bundle = new Bundle();
					bundle.putString("USER", user);
					bundle.putString("PASSWD", passWd);
					SetResult(10002, bundle);
				}
			} else {
				Type = true;
				if (publicjson.getAl().get(0).getMessage().equals("输入的用户名和电子邮件地址不匹配！")) {
					YoYo.with(Techniques.Shake).playOn(mEd_User);
					YoYo.with(Techniques.Shake).playOn(mEd_Email);
				}
				showShortToast(publicjson.getAl().get(0).getMessage());
			}
		}

		@Override
		public void responseException(String errorMessage) {

		}
	};
	private String user;
	private String email;
	private String passWd;

}
