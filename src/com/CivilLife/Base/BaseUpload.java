package com.CivilLife.Base;

import java.io.FileNotFoundException;

import Requset_getORpost.ProgressDialog;
import Requset_getORpost.RequestListener;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.oss.OSSService;
import com.alibaba.sdk.android.oss.OSSServiceProvider;
import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.AccessControlList;
import com.alibaba.sdk.android.oss.model.AuthenticationType;
import com.alibaba.sdk.android.oss.model.ClientConfiguration;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.alibaba.sdk.android.oss.model.TokenGenerator;
import com.alibaba.sdk.android.oss.storage.OSSBucket;
import com.alibaba.sdk.android.oss.storage.OSSFile;
import com.alibaba.sdk.android.oss.util.OSSLog;
import com.alibaba.sdk.android.oss.util.OSSToolKit;
import com.aysy_mytool.Qlog;

public class BaseUpload {
	private final int maxUPnum=5;//单次断点上传限制次数
	
	private OSSBucket bucket;

	public static final String bucketName = "civillife";
	public static OSSService ossService = OSSServiceProvider.getService();
	static final String accessKey = "jIbwZ39jSkEPI8wI"; // 测试代码没有考虑AK/SK的安全性
	static final String screctKey = "xlXXDvG306Pa3jP98brUS7PKv0hAqm";
	private long currentTimeMillis;
	private String upname;
	private String pathurl = "http://civillife.oss-cn-hangzhou.aliyuncs.com/";//上传地址
	private String bucketurl = "civillife_video/";
	private String path;
	private RequestListener rl;
	private int UPnum=0;
	private Context context;
	private Boolean isok=false;

	public BaseUpload(Context context) {
		this.context = context;
		OSSLog.enableLog();
		// 初始化设置
		ossService.setApplicationContext(BaseApplication.getContext());
		ossService.setGlobalDefaultHostId("oss-cn-hangzhou.aliyuncs.com"); // 设置region
		ossService.setGlobalDefaultACL(AccessControlList.PUBLIC_READ_WRITE); // 默认为private
		ossService.setAuthenticationType(AuthenticationType.ORIGIN_AKSK); // 设置加签类型为原始AK/SK加签
		ossService.setGlobalDefaultTokenGenerator(new TokenGenerator() { // 设置全局默认加签器
			@Override
			public String generateToken(String httpMethod, String md5, String type, String date, String ossHeaders,
					String resource) {
				String content = httpMethod + "\n" + md5 + "\n" + type + "\n" + date + "\n" + ossHeaders + resource;

				return OSSToolKit.generateToken(accessKey, screctKey, content);
			}
		});
		ossService.setCustomStandardTimeWithEpochSec(System.currentTimeMillis() / 1000);

		ClientConfiguration conf = new ClientConfiguration();
		conf.setConnectTimeout(15 * 1000); // 设置全局网络连接超时时间，默认30s
		conf.setSocketTimeout(15 * 1000); // 设置全局socket超时时间，默认30s
		conf.setMaxConnections(50); // 设置全局最大并发网络链接数, 默认50
		ossService.setClientConfiguration(conf);
	}

	public void Upload(final String path, final RequestListener rl,boolean isdialog,String smg) {
		progressDialog = ProgressDialog.createDialog(context, null, false);
//		Qlog.e("", "path1  "+path);
		if (isdialog) {
			if (TextUtils.isEmpty(smg)) {
				progressDialog.setMessage("请稍候");
			} else {
				progressDialog.setMessage(smg);
			}
			if (progressDialog != null){
//				Qlog.e("", "path11  "+path);
				progressDialog.show();
			}	
		}
		this.path = path;
		this.rl = rl;
		new Thread(new Runnable() {
			public void run() {
				bucket = ossService.getOssBucket(bucketName);  
				currentTimeMillis = System.currentTimeMillis();
//				Qlog.e("", "path2  "+path);
				String[] split = path.split("/");
				upname = path;
				if (split.length > 0) {     
					upname = split[split.length - 1];
				}  
				OSSFile bigfFile = ossService.getOssFile(bucket, bucketurl + currentTimeMillis + upname);
				try {
					bigfFile.setUploadFilePath(path, "raw/binary");  
					bigfFile.ResumableUploadInBackground(new SaveCallback() {


						@Override
						public void onSuccess(String objectKey) {
//							Qlog.e("", "onSuccess  "+pathurl + bucketurl + currentTimeMillis + upname);
							isok=true;
//							Qlog.e("", "progressDialog "+progressDialog);
							if (progressDialog != null) {
//								Qlog.e("", "progressDialog "+progressDialog);
								if (progressDialog.isShowing()){
//									Qlog.e("", "onSuccess dismiss ");   
									progressDialog.dismiss();
								}
							}  
							rl.responseResult(pathurl + bucketurl + currentTimeMillis + upname);
						}  

						@Override
						public void onProgress(String objectKey, int byteCount, int totalSize) {
//							Qlog.e("", "onProgress");
						}  

						@Override
						public void onFailure(String objectKey, OSSException ossException) {
//							Qlog.e("", "onFailure");
							if (!isok) {
								ossException.printStackTrace();
								ResumableTaskOption(bucket, bucketurl + currentTimeMillis + upname, path);
							}
							
						}
					});  
				} catch (FileNotFoundException e) { 
					e.printStackTrace();
					
				}
				
			}
		}).start();

	}

	
	/**  断点续传  **/
	public void ResumableTaskOption(OSSBucket arg0, String arg1 ,final String path){
		
		OSSFile ossFiles = ossService.getOssFile(arg0, arg1);
		try {
			ossFiles.setUploadFilePath(path, "raw/binary");
			ossFiles.ResumableUploadInBackground(new SaveCallback() {
				@Override
				public void onSuccess(String objectKey) {
					isok=true;
//					Qlog.e("", "onSuccess111");
					if (progressDialog != null) {  
						if (progressDialog.isShowing())
							progressDialog.dismiss();
					}
					rl.responseResult(pathurl + bucketurl + currentTimeMillis + upname);
				}

				@Override
				public void onProgress(String objectKey, int byteCount, int totalSize) {
					
				}

				@Override
				public void onFailure(String objectKey, OSSException ossException) {
//					Qlog.e("", "onFailure222");
					if (!isok) {    
//						Qlog.e("", "onFailure谁打啊速度按时打的");
						ossException.printStackTrace();  
						UPnum++;
						if (UPnum>maxUPnum) {  
							rl.responseException("");
							if (progressDialog != null) {
								if (progressDialog.isShowing())
									progressDialog.dismiss();  
							}
						}else{
							ResumableTaskOption(bucket, bucketurl + currentTimeMillis + upname, path);
						}
					}
				}
			});
		} catch (FileNotFoundException e) {
//			rl.responseException(e.toString());
			e.printStackTrace();
		} 
	
	}
	public Handler handler = new Handler(){

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				ResumableTaskOption(bucket, bucketurl + currentTimeMillis + upname, path);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	};

	private ProgressDialog progressDialog;
}
