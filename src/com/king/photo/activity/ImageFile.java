package com.king.photo.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;


/**
 * 这个类主要是用来进行显示包含图片的文件夹
 *
 * @author king
 * @version 2014年10月18日  下午11:48:06
 */
public class ImageFile extends Activity {

	private FolderAdapter folderAdapter;
	private Button bt_cancel;
	private Context mContext;

	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		Res.init(this);
		setContentView(Res.getLayoutID("plugin_camera_image_file"));
		PublicWay.activityList.add(this);
		mContext = this;
		bt_cancel = (Button) findViewById(Res.getWidgetID("cancel"));
		bt_cancel.setOnClickListener(new CancelListener());
		GridView gridView = (GridView) findViewById(Res.getWidgetID("fileGridView"));
		TextView textView = (TextView) findViewById(Res.getWidgetID("headerTitle"));
		textView.setText(Res.getString("photo"));
		folderAdapter = new FolderAdapter(this);
		gridView.setAdapter(folderAdapter);
	}

	private class CancelListener implements OnClickListener {// 取消按钮的监听
		public void onClick(View v) {
			//清空选择的图片
			setResult(0,new Intent());
			finish();
//			Bimp.tempSelectBitmap.clear();
//			Intent intent = new Intent();
//			intent.setClass(mContext, MainActivity.class);
//			startActivity(intent);
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		setResult(0,new Intent());
		finish();
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			Intent intent = new Intent();
//			intent.setClass(mContext, MainActivity.class);
//			startActivity(intent);
//		}
//		
		return true;
	}

}
