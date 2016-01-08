package com.CivilLife.Activity;

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
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

/**
 * 注册
 * 
 * @author mac
 * 
 */
public class RegisterActivity extends BaseActivity {
	private EditText mEd_User;
	private EditText mEd_PassWd;
	private EditText mEd_Re_PassWd;
	private String user;
	private String passWd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initViews();
		initEvents();
		init();
	}


	@Override
	protected void initViews() {
		findViewById(R.id.tx_back).setOnClickListener(this);
		findViewById(R.id.btn_submit_to).setOnClickListener(this);
		mEd_User = (EditText) findViewById(R.id.edit_user);
		mEd_PassWd = (EditText) findViewById(R.id.edit_password);
		mEd_Re_PassWd = (EditText) findViewById(R.id.edit_repetition_password);
		edit_nickname = (EditText) findViewById(R.id.edit_nickname);//昵称
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
		case R.id.btn_submit_to:// 提交注册
			Register();
			break;
		default:
			break;
		}
	}

	// 提交数据
	private void Register() {
		user = mEd_User.getText().toString();
		nickname = edit_nickname.getText().toString();
		passWd = mEd_PassWd.getText().toString();
		String Re_PassWd = mEd_Re_PassWd.getText().toString();
		if (TextUtils.isEmpty(user)) {
			YoYo.with(Techniques.Shake).playOn(mEd_User);
			ToastUtil.showToast(this, "用户名不能为空");
			return;
		}
		if (TextUtils.isEmpty(nickname)) {
			YoYo.with(Techniques.Shake).playOn(edit_nickname);
			ToastUtil.showToast(this, "昵称不能为空");
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
		// 下面开始进行请求操作
		new RequestTask(RegisterActivity.this, ReturnAL.RegisterMap(user, passWd, nickname), listener, false, true, "注册中")
				.execute(Httpurl.URL);
	}

	// 注册回调
	RequestListener listener = new RequestListener() {

		@Override
		public void responseResult(String jsonObject) {
			PublicUpJson publicjson = PublicUpJson.readJsonToSendmsgObject(RegisterActivity.this, jsonObject);
			if (publicjson == null) {
				return;
			}
			PublicEntity publicEntity = publicjson.getAl().get(0);
			String status = publicEntity.getStatus();
			if (status.equals("1")) {
				showShortToast("注册成功！");
				Bundle bundle = new Bundle();
				bundle.putString("USER", user);
				bundle.putString("PASSWD", passWd);
				SetResult(10002, bundle);
			} else {
				if (publicjson.getAl().get(0).getMessage().equals("用户名已存在，请输入其它用户名！")) {
					YoYo.with(Techniques.Shake).playOn(mEd_User);
				}
				showShortToast(publicjson.getAl().get(0).getMessage());
			}
		}

		@Override
		public void responseException(String errorMessage) {

		}
	};
	private EditText edit_nickname;
	private String nickname;

}
