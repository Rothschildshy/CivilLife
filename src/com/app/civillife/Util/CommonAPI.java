package com.app.civillife.Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class CommonAPI {
	private final static String TAG = "CommonAPI";
	private static CommonAPI commonAPI = null;
	private final static boolean DEBUG = false;

	protected CommonAPI() {

	}

	public synchronized static CommonAPI getInstance() {
		if (commonAPI == null) {
			commonAPI = new CommonAPI();
		}
		return commonAPI;

	}

	public String getUserId(Context context) {
		/**
		 * 得到配置参数的类 参数1 配置参数文件的名字,没有后缀名 参数2 文件访问模式 只能是生成这个文件的应用访问
		 */
		SharedPreferences share = context.getSharedPreferences("perference", Activity.MODE_PRIVATE);
		return (share.getString("userId", "null"));
	}

	public void setUserId(Context context, String userId) {
		/**
		 * 得到配置参数的类 参数1 配置参数文件的名字,没有后缀名 参数2 文件访问模式 只能是生成这个文件的应用访问
		 */
		SharedPreferences share = context.getSharedPreferences("perference", Activity.MODE_PRIVATE);
		Editor editor = share.edit();// 取得编辑器
		editor.putString("userId", userId);// 存储配置 参数1 是key 参数2 是值
		editor.commit();// 提交刷新数据
	}

	public String getUserPhone(Context context) {
		/**
		 * 得到配置参数的类 参数1 配置参数文件的名字,没有后缀名 参数2 文件访问模式 只能是生成这个文件的应用访问
		 */
		SharedPreferences share = context.getSharedPreferences("perference", Activity.MODE_PRIVATE);
		return (share.getString("userPhone", "null"));
	}

	public void setUserPhone(Context context, String userPhone) {
		/**
		 * 得到配置参数的类 参数1 配置参数文件的名字,没有后缀名 参数2 文件访问模式 只能是生成这个文件的应用访问
		 */
		SharedPreferences share = context.getSharedPreferences("perference", Activity.MODE_PRIVATE);
		Editor editor = share.edit();// 取得编辑器
		editor.putString("userPhone", userPhone);// 存储配置 参数1 是key 参数2 是值
		editor.commit();// 提交刷新数据
	}

	public String getUserName(Context context) {
		/**
		 * 得到配置参数的类 参数1 配置参数文件的名字,没有后缀名 参数2 文件访问模式 只能是生成这个文件的应用访问
		 */
		SharedPreferences share = context.getSharedPreferences("perference", Activity.MODE_PRIVATE);
		return (share.getString("userName", null));
	}

	public void setUserName(Context context, String userName) {
		/**
		 * 得到配置参数的类 参数1 配置参数文件的名字,没有后缀名 参数2 文件访问模式 只能是生成这个文件的应用访问
		 */
		SharedPreferences share = context.getSharedPreferences("perference", Activity.MODE_PRIVATE);
		Editor editor = share.edit();// 取得编辑器
		editor.putString("userName", userName);// 存储配置 参数1 是key 参数2 是值
		editor.commit();// 提交刷新数据
	}

	public String getPwd(Context context) {
		/**
		 * 得到配置参数的类 参数1 配置参数文件的名字,没有后缀名 参数2 文件访问模式 只能是生成这个文件的应用访问
		 */
		SharedPreferences share = context.getSharedPreferences("perference", Activity.MODE_PRIVATE);
		return (share.getString("passwd", "null"));
	}

	public void setPwd(Context context, String strPwd) {
		/**
		 * 得到配置参数的类 参数1 配置参数文件的名字,没有后缀名 参数2 文件访问模式 只能是生成这个文件的应用访问
		 */
		SharedPreferences share = context.getSharedPreferences("perference", Activity.MODE_PRIVATE);
		Editor editor = share.edit();// 取得编辑器
		editor.putString("passwd", strPwd);// 存储配置 参数1 是key 参数2 是值
		editor.commit();// 提交刷新数据
	}

	/**
	 * 验证手机格式
	 */
	public static boolean isMobileNO(String mobiles) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、152、157、158、159、147、182、183、184、
		 * 187、188 联通：130、131、132、145、155、156、185、186 （145属于联通无线上网卡号段）、176
		 * 电信：133 、153 、180 、181 、189 总结起来就是第一位必定为1，第二位必定为3或4或5或8，其他位置的可以为0-9
		 */
		String telRegex = "[1][3458]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobiles)) {
			return false;
		} else {
			return mobiles.matches(telRegex);
		}
	}

	public static boolean checkDataIsJson(String value) {
		try {
			if (value != null) {
				new JSONObject(value);
			} else {
				return false;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 获取网落图片资源
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getHttpBitmap(String url) {
		URL myFileURL;
		Bitmap bitmap = null;
		try {
			myFileURL = new URL(url);
			// 获得连接
			HttpURLConnection conn = (HttpURLConnection) myFileURL.openConnection();
			// 设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
			conn.setConnectTimeout(6000);
			// 连接设置获得数据流
			conn.setDoInput(true);
			// 不使用缓存
			conn.setUseCaches(false);
			// 这句可有可无，没有影响
			// conn.connect();
			// 得到数据流
			InputStream is = conn.getInputStream();
			// 解析得到图片
			bitmap = BitmapFactory.decodeStream(is);
			// 关闭数据流
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;

	}

	// public static Dialog createDialog(Context context, View view,boolean
	// canceledOnTouchOutside){
	//
	// Dialog dialog = new Dialog(context,R.style.room_dialog);
	// dialog.setCanceledOnTouchOutside(true);
	// Window window = dialog.getWindow();
	// window.setWindowAnimations(R.style.dialogWindowAnim2); //设置窗口弹出动画
	// window.setBackgroundDrawableResource(android.R.color.transparent);
	// window.setGravity(Gravity.BOTTOM);
	// dialog.setContentView(view);//添加布局视图
	// dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
	// WindowManager m = ((Activity) context).getWindowManager();
	// Display d = m.getDefaultDisplay();
	// WindowManager.LayoutParams p = window.getAttributes();
	// p.width = (int) (d.getWidth() )-CommonAPI.getInstance().dip2px(context,
	// 0);
	// window.setAttributes(p);
	// return dialog;
	// }
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * Bitmap转byte[]
	 */
	public byte[] bmpToJPGByteArray(Bitmap bmp) {  
		// Default size is 32 bytes
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		return bos.toByteArray();
	}

	/**
	 * 删除单个文件
	 * 
	 * @param filePath
	 *            被删除文件的文件名
	 * @return 文件删除成功返回true，否则返回false
	 */
	public boolean deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.isFile() && file.exists()) {
			return file.delete();
		}
		return false;
	}

	/*
	 * 将第四个到第七个 替换成" **** "
	 */
	public static String SecretPhone(String phone) {
		if (phone.length() > 7) {
			String substring = phone.substring(3, 7);
			String replace = phone.replace(substring, "****");
			return replace;
		}
		return phone;
	}

	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变
	 * 
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 判断一个字符串是不是能够转化成字符串/同时转化成浮点类型
	 * 
	 * @return
	 */
	public static float tofloat(String strVal) {
		float float1;
		try {
			float1 = Float.valueOf(strVal);
		} catch (Exception e) {
			return 0;
		}
		return float1;

	}

	public static int toInteger(String strVal) {
		int float1;
		try {
			float1 = Integer.valueOf(strVal);
		} catch (Exception e) {
			return 0;
		}
		return float1;

	}
	
}
