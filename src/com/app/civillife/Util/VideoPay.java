package com.app.civillife.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.aysy_mytool.Qlog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoPay {
	private Context context;
	private VideoView mVideoView;
	private String remoteUrl;
	private static final int READY_BUFF = 2000 * 1024;
	private static final int CACHE_BUFF = 500 * 1024;
	
	
	private ProgressDialog progressDialog = null;
	private boolean isready = false;
	private boolean iserror = false;
	private int errorCnt = 0;
	private int curPosition = 0;
	private long mediaLength = 0;
	private long readSize = 0;
//	String localUrl=Environment.getExternalStorageDirectory().getAbsolutePath()
//	+ "/VideoCache/" + System.currentTimeMillis() + ".mp4";
	private Videolistener videolistener;
	
	public VideoPay(Context context,VideoView mVideoView,String remoteUrl,Videolistener videolistener){
		this.context = context;
		this.mVideoView = mVideoView;
		this.remoteUrl = remoteUrl;
		this.videolistener = videolistener;
//		File FileOrDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
//				+ "/VideoCache");
//		DeleteFileOrDirectory(FileOrDirectory);  
		init();
	}
	// 删除指定的文件或目录（无限递归删除，返回是否删除成功）
    public static boolean DeleteFileOrDirectory(java.io.File FileOrDirectory) {
        if (FileOrDirectory.exists()) {
//            if (FileOrDirectory.isFile()) {
//                FileOrDirectory.delete();
//            }else 
        	if (FileOrDirectory.isDirectory()) {
                java.io.File files[] = FileOrDirectory.listFiles();
                for (int i = 0; i < files.length; i++) {
                    DeleteFileOrDirectory(files[i]);
                }
            }
        }
        return FileOrDirectory.exists();
    }

	private void init() {
		
		if (this.remoteUrl == null) {
			return;
		}

		mediaController = new MediaController(context);
		mediaController.setVisibility(View.INVISIBLE);  
		mVideoView.setMediaController(mediaController);
		mVideoView.setOnPreparedListener(new OnPreparedListener() {

			public void onPrepared(MediaPlayer mediaplayer) {//准备完
				mediaplayer.seekTo(100);
				videolistener.onPrepared(mediaplayer);
				mediaplayer.start();  
			}    
		});

		mVideoView.setOnCompletionListener(new OnCompletionListener() {//播放完

			public void onCompletion(MediaPlayer mediaplayer) {
				videolistener.onCompletion(mediaplayer);
				ispay=false;
				curPosition = 0;
				mVideoView.pause();
			}
		});

		mVideoView.setOnErrorListener(new OnErrorListener() {//错误

			public boolean onError(MediaPlayer mediaplayer, int i, int j) {
				videolistener.onError(mediaplayer, i, j);
				ispay=false;
				iserror = true;
				errorCnt++;
				mVideoView.pause();
				return true;
			}
		});
	}
	
	public void setPause(){//暂停
    	if (mVideoView!=null) {
    		mVideoView.pause();
    		mVideoView.requestFocus();
		}
    }
	
	public boolean ispay=false;
	public void playvideo() {//开始
		
		if (ispay) {
			mVideoView.start();
			mVideoView.requestFocus();
			return;
		}
		ispay=true;
		
		if (!URLUtil.isNetworkUrl(this.remoteUrl)) {//不是网络视频,直接播放
			mVideoView.setVideoPath(this.remoteUrl);
			mVideoView.start();
			return;  
		}else{
			Log.e("", "localUrl  "+remoteUrl);
			Uri uri = Uri.parse(this.remoteUrl);  
			mVideoView.setMediaController(mediaController);  
			mVideoView.setVideoURI(uri);  
	        //videoView.start();  
			mVideoView.requestFocus();  
		}
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				FileOutputStream out = null;
//				InputStream is = null;
//
//				try {
//					URL url = new URL(remoteUrl);
//					HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
//					Log.e("", "localUrl  "+localUrl);
//					if (localUrl == null) {
//						localUrl = Environment.getExternalStorageDirectory().getAbsolutePath()
//								+ "/VideoCache/" + System.currentTimeMillis() + ".mp4";
//					}
//
//					File cacheFile = new File(localUrl);
//
//					if (!cacheFile.exists()) {
//						cacheFile.getParentFile().mkdirs();
//						cacheFile.createNewFile();
//					}
//  
//					readSize = cacheFile.length();
//					out = new FileOutputStream(cacheFile, true);
//
//					httpConnection.setRequestProperty("User-Agent", "NetFox");
//					httpConnection.setRequestProperty("RANGE", "bytes=" + readSize + "-");//从readSize处请求读取
//					is = httpConnection.getInputStream();
//
//					mediaLength = httpConnection.getContentLength();
//					if (mediaLength == -1) {
//						return;  
//					}
//
//					mediaLength += readSize;
//					byte buf[] = new byte[10 * 1024];
//					int size = 0;
//					long lastReadSize = 0;
//
//					mHandler.sendEmptyMessage(VIDEO_STATE_UPDATE); //视频缓存状态开始更新
//					while ((size = is.read(buf)) != -1) { //缓存文件
//						try {
//							out.write(buf, 0, size);
//							readSize += size;
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//
//						if (!isready) {
//							if ((readSize - lastReadSize) > READY_BUFF) {
//								lastReadSize = readSize;
//								mHandler.sendEmptyMessage(CACHE_VIDEO_READY);//视频已经准备好
//							}
//						} else {
//							if ((readSize - lastReadSize) > CACHE_BUFF * (errorCnt + 1)) {  
//								lastReadSize = readSize;
//								mHandler.sendEmptyMessage(CACHE_VIDEO_UPDATE);//视频有更新
//							}
//						}
//					}
//					mHandler.sendEmptyMessage(CACHE_VIDEO_END);//视频结束
//					
//				} catch (Exception e) {
//					e.printStackTrace();
//				} finally {   
//					if (out != null) {
//						try {
//							out.close();
//						} catch (IOException e) {
//							//
//						}
//					}
//					if (is != null) {
//						try {
//							is.close();
//						} catch (IOException e) {  
//						}
//					}
//				}
//			}
//		}).start();
	}

	private final static int VIDEO_STATE_UPDATE = 0;
	private final static int CACHE_VIDEO_READY = 1;
	private final static int CACHE_VIDEO_UPDATE = 2;
	private final static int CACHE_VIDEO_END = 3;

//	private final Handler mHandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case VIDEO_STATE_UPDATE:
//
//				mHandler.sendEmptyMessageDelayed(VIDEO_STATE_UPDATE, 1000);//视频缓存状态更新
//				break;
//			case CACHE_VIDEO_READY://视频已经准备好
////				System.out.println("----CACHE_VIDEO_READY---------------");
//				isready = true;
//				mVideoView.setVideoPath(localUrl);
//				mVideoView.start();  
//				mVideoView.requestFocus();    
//				
//				/*File file = new File(videoUrl);     
//            	FileInputStream fis = new FileInputStream(file);
//            	mediaPlayer.setDataSource(fis.getFD());
//            	mediaPlayer.prepare();*/
//				break;  
//			case CACHE_VIDEO_UPDATE://播放
//				if (iserror || !mVideoView.isPlaying()) {
//					mVideoView.setVideoPath(localUrl);  
//					mVideoView.start();
//					iserror = false;
//				}
//				break;
//			case CACHE_VIDEO_END://结束
//				if (iserror || !mVideoView.isPlaying()) {
//					mVideoView.setVideoPath(localUrl);
//					mVideoView.start();
//					iserror = false;
//				}
//				break;
//			}
//
//			super.handleMessage(msg);
//		}
//	};
	private MediaController mediaController;
	
}
