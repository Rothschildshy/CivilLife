package com.app.civillife;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.CivilLife.Base.BaseActivity;

/**
 * 建筑书城 webview
 * 
 * @author mac
 * 
 */
public class ToolActivity extends BaseActivity {
	private ProgressBar mProgressBar;
	private WebView webview;
	private WebSettings webSettings;
	private String Url = "http://www.conitm.com/gj/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);    
		setContentView(R.layout.activity_tool);
		initViews();
		initEvents();
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.book_store, menu);
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
		
		
		webview.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK
							&& webview.canGoBack()) { // 表示按返回键 时的操作
						webview.goBack(); // 后退
						return true; // 已处理
					}
				}
				return false;
			}

		});
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
}
