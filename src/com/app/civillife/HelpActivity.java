package com.app.civillife;

import com.CivilLife.Base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * 使用帮助
 * 
 * @author Administrator
 *
 */
public class HelpActivity extends BaseActivity {
	private ProgressBar mProgressBar;
	private WebView webview;
	private WebSettings webSettings;
	private String Url = "http://tmssh.conitm.com/help.html";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		initViews();
		initEvents();
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.help, menu);
		return true;
	}

	@Override
	protected void initViews() {
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
		webview = (WebView) findViewById(R.id.webView1);

	}

	@Override
	protected void initEvents() {
		findViewById(R.id.image_back).setOnClickListener(this);

	}

	@Override
	protected void init() {
		webSettings = webview.getSettings();
		// 设置支持JavaScript脚本
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDomStorageEnabled(true);
		// 设置支持缩放
		// webSettings.setBuiltInZoomControls(true);
		webview.setWebViewClient(new MyWebView());
		// 设置https请求的非法证书跳过
		// webview.setWebViewClient(new WebViewClient(){
		// @Override
		// public void onReceivedSslError(WebView view,
		// SslErrorHandler handler, SslError error) {
		// handler.proceed();
		// }
		// });
		webview.setWebChromeClient(new WebChrome());
		webview.loadUrl(Url);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.image_back:
			finish();
			break;
		default:
			break;
		}
	}

	class MyWebView extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			webview.loadUrl(url);
			return true;
		}
	}

	class WebChrome extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
				mProgressBar.setVisibility(View.GONE);
			} else {
				mProgressBar.setVisibility(View.VISIBLE);
				mProgressBar.setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
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
