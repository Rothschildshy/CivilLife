package com.CivilLife.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.app.civillife.R;
import com.aysy_mytool.Network;
import com.aysy_mytool.TimeCompare;

import Loading.dialog.SpotsDialog;
import Requset_getORpost.DBHelper;
import Requset_getORpost.HttpStatus_Code;
import Requset_getORpost.Myjsonlog;
import Requset_getORpost.NetException;
import Requset_getORpost.RequestListener;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

/**
 * 网络请求 后台任务
 * 
 * @author PWY
 * 
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RequestTask extends AsyncTask<String, Integer, Object> {
	/** 是否开启打印日志 调试模式为true 上线模式为false */
	public static boolean IsLog = true;
	private static String path;
	/** 调用者上下文 */
	private Context ct;
	/** 请求回调 */
	private RequestListener listener;
	/** 是否显示进度条 */
	private boolean isShowProssDialog = true;
	/** 进度条对话框 */
	// private ProgressDialog progressDialog;
	private SpotsDialog progressDialog;
	/** 进度条提示 */
	private String dlgNote;
	/** 请求类型 是否是get */
	private boolean isget;
	/** 请求 */
	private static HttpClient mHttpClient;
	/** 缓存大小 */
	private static final int BUFFER_SIZE = 1024 * 10;
	/** 链接超时时间 */
	private static int CONNECTION_TIME_OUT = 10 * 1000;
	/** 数据请求超时时间 */
	private static int SO_TIME_OUT = 15 * 1000;
	private static int DEFAULT_HOST_CONNECTIONS = 300;
	private static int DEFAULT_MAX_CONNECTIONS = 600;
	private List<? extends BasicNameValuePair> requestlist = null;
	private String url;
	private boolean isCache = true;// 默认设置缓存
	private boolean isGetCache = false;// 默认设置不从缓存获取
	/** wifi下设置缓存时间3小时 超过就删除原来的记录 */
	private static final int WIFI_CACHE_PAST_DUR = 3;
	/** 非wifi下设置缓存时间24个小时 超过就删除原来的记录 */
	private static final int NOWIFI_CACHE_PAST_DUR = 24;

	/**
	 * 
	 * @param ct
	 * @param requestListenr
	 *            通知更新ui实现方法
	 * @param isShowProssDialog
	 *            是否显示进度条提示框
	 * @param note
	 *            进度框显示文件
	 */

	// get
	public RequestTask(Context ct, RequestListener listener, boolean isCache, boolean isShowProssDialog, String note) {
		this.ct = ct;
		this.listener = listener;
		this.isCache = isCache;
		this.isShowProssDialog = isShowProssDialog;
		this.dlgNote = note;
		isget = false;

	}

	// post
	public RequestTask(Context ct, ArrayList<? extends BasicNameValuePair> requestlist, RequestListener listener,
			boolean isCache, boolean isShowProssDialog, String note) {
		this.ct = ct;
		this.requestlist = requestlist;
		this.listener = listener;
		this.isCache = isCache;
		this.isShowProssDialog = isShowProssDialog;
		this.dlgNote = note;
		isget = true;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();

	}

	// 异步开始执行 这里是最终用户调用Excute时的接口
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		// Log.e("", "异步运行onPreExecute " + System.currentTimeMillis());
		if (isShowProssDialog) {
			// progressDialog = ProgressDialog.createDialog(ct, this, false);
			if (TextUtils.isEmpty(dlgNote)) {
				progressDialog = new SpotsDialog(ct, "请稍候");
				// progressDialog.setMessage("请稍候");
			} else {
				progressDialog = new SpotsDialog(ct, dlgNote);
			}
			if (progressDialog != null)
				progressDialog.show();
		}
	}

	// 异步后台执行 比较耗时的操作都可以放在这里
	@Override
	protected Object doInBackground(String... params) {
		url = params[0];
		Object result = null;
		// Log.e("", "异步开始doInBackground " + System.currentTimeMillis());
		/** 进行缓存操作 start **/
		String[] data = null;
		if (isCache) { // 如果是缓存 取数据库缓存中数据
			data = DBHelper.getInstance(ct).getURLData(url);
		}
		/** 判断网络 */
		switch (Network.checkNetWorkType(ct)) {
		case /** 网络不可用 */
		Network.NONETWORK:
			if (data == null) {
				// 发送广播 通知网络不可用
				sendBroadcastReceiverMessage(ct, R.string.network_show_connectionless);
				return data;
			}
			break;
		case /** 是wifi连接 */
		Network.WIFI: // 设置缓存时间三个小时 超过就删除原来的记录
			if (data != null && data.length > 0 && TimeCompare.compareHourTime(data[1]) > WIFI_CACHE_PAST_DUR) {
				data = null;
				DBHelper.getInstance(ct).deleteURLData(url);
			}
			break;
		case /** 不是wifi连接 */
		Network.NOWIFI: // 设置缓存时间1天 超过就删除原来的记录
			if (data != null && data.length > 0 && TimeCompare.compareHourTime(data[1]) > NOWIFI_CACHE_PAST_DUR) {
				data = null;
				DBHelper.getInstance(ct).deleteURLData(url);
			}
			break;
		default:
			break;
		}
		if (isCache && data != null && data.length > 0) { // 如果是缓存 取数据库缓存中数据
			isGetCache = false;
			return data[0];
		}
		/** 进行缓存操作 end **/

		try {
			// 新建HttpClient对象
			HttpClient httpClient = getHttpClient();
			HttpResponse response = null;
			if (isget) {// post请求
				HttpPost post = new HttpPost(params[0]);
				HttpEntity entity = null;
				try {
					entity = new UrlEncodedFormEntity(requestlist, HTTP.UTF_8);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					result = new NetException(e, e.getMessage());
				}
				post.setEntity(entity);
				response = httpClient.execute(post);

			} else {// get请求
				HttpGet get = new HttpGet(params[0]);
				response = httpClient.execute(get);
			}
			new HttpStatus_Code();
			result = HttpStatus_Code.GetResult(response);
		} catch (Exception e) {
			e.printStackTrace();
			result = new NetException(e, e.getMessage());
		}
		isGetCache = true;
		return result;
	}

	// 相当于Handler 处理结果的方式，在这里面可以使用在doInBackground 得到的结果处理操作数据
	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		if (progressDialog != null) {
			if (progressDialog.isShowing())
				progressDialog.dismiss();
		}
		// 返回为空
		if (result == null) {
			listener.responseException("请求网络异常");
			if (!isCache) {
				sendBroadcastReceiverMessage(ct, R.string.network_show_error);
			}
			// 返回错误
		} else if (result instanceof NetException) {
			listener.responseException(((NetException) result).getMessage());
			if (!isCache) {
				sendBroadcastReceiverMessage(ct, R.string.network_show_error);
			}
		} else {// 正确返回数据
			if (!checkDataIsJson((String) result)) {
				listener.responseException((String) result);
				return;
			}
			listener.responseResult((String) result);
			if (isGetCache && IsLog) {// 是网络请求的数据才 且调试模式下 打印网络log
				Myjsonlog.MyLog(ct, requestlist, (String) result, url);
			}
			if (isCache) {// 把数据缓存到本地
				DBHelper.getInstance(ct).addOrUpdateURLData(url, (String) result);
			}
		}
	}

	public boolean checkDataIsJson(String value) {
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

	// 可以使用进度条增加用户体验度
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

	// 用户调用取消时，要做的操作
	@Override
	protected void onCancelled(Object result) {
		super.onCancelled(result);
	}

	/**
	 * 通过广播发送网络信息
	 * 
	 * @param msg
	 */
	public static void sendBroadcastReceiverMessage(Context context, int msg) {
		// 发送显示消息广播
		Intent intent = new Intent("com.fc.apps.vjmenu.app.showmgs");
		intent.putExtra("msg", msg);
		context.sendBroadcast(intent);
		// BaseActivity.activityList.get(BaseActivity.activityList.size()-1).sendBroadcast(intent);
	}

	private synchronized HttpClient getHttpClient() {
		if (mHttpClient == null) {
			try {
				// 设置请求头超时请求参数
				final HttpParams httpParams = new BasicHttpParams();
				// timeout: get connections from connection pool
				// 超时:从连接池中获得连接
				ConnManagerParams.setTimeout(httpParams, 1000);
				// timeout: connect to the server
				// 超时:连接到服务器
				HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIME_OUT);
				// timeout: transfer data from server
				// 超时:从服务器传输数据
				HttpConnectionParams.setSoTimeout(httpParams, SO_TIME_OUT);
				// set max connections per host
				// 设置最大连接每个主机
				ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(DEFAULT_HOST_CONNECTIONS));
				// set max total connections
				// 设置最大总连接
				ConnManagerParams.setMaxTotalConnections(httpParams, DEFAULT_MAX_CONNECTIONS);
				// 使用HTTPS绕过证书验证
				KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
				trustStore.load(null, null);
				SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
				// 允许所有主机的验证
				sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
				// use expect-continue handshake 使用expect-continue握手
				HttpProtocolParams.setUseExpectContinue(httpParams, true);
				// disable stale check 禁用过期支票
				HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);

				HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);

				HttpClientParams.setRedirecting(httpParams, false);

				// set user agent 设置用户代理
				String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
				HttpProtocolParams.setUserAgent(httpParams, userAgent);

				// disable Nagle algorithm 禁用Nagle算法
				HttpConnectionParams.setTcpNoDelay(httpParams, true);
				HttpConnectionParams.setSocketBufferSize(httpParams, BUFFER_SIZE);

				// scheme: http and https 方案:http和https
				SchemeRegistry schemeRegistry = new SchemeRegistry();
				schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
				schemeRegistry.register(new Scheme("https", sf, 443));

				ClientConnectionManager manager = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
				mHttpClient = new DefaultHttpClient(manager, httpParams);

			} catch (Exception e) {
				e.printStackTrace();
				return new DefaultHttpClient();
			}
		}

		return mHttpClient;
	}

	// 设置https请求证书认证绕过
	class SSLSocketFactoryEx extends SSLSocketFactory {

		SSLContext sslContext = SSLContext.getInstance("TLS");

		public SSLSocketFactoryEx(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {

				@Override
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
						throws java.security.cert.CertificateException {

				}

				@Override
				public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
						throws java.security.cert.CertificateException {

				}
			};
			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
				throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}

}
