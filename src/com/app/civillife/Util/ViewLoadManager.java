package com.app.civillife.Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import com.app.civillife.R;
import com.aysy_mytool.Qlog;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.view.View;

/**
 * 支持布局View设置网络图片为背景
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class ViewLoadManager {
	/** 图片源类型: 文件,网络,资源ID **/
	public enum IMAGE_LOAD_TYPE {
		FILE_PATH, FILE_URL, FILE_RESOURCE_ID
	}

	private String TAG = "ImageLoadManager...";

	private Context context;

	private Set<ImageLoadTask> taskCollection;

	/** 最大内存 **/
	final static int maxCacheSize = (int) (Runtime.getRuntime().maxMemory() / 8);

	/** 建立线程安全,支持高并发的容器 **/
	private static ConcurrentHashMap<String, SoftReference<Bitmap>> currentHashmap = new ConcurrentHashMap<String, SoftReference<Bitmap>>();

	public ViewLoadManager(Context context) {
		super();
		this.context = context;
		taskCollection = new HashSet<ViewLoadManager.ImageLoadTask>();
	}

	private static LruCache<String, Bitmap> BitmapMemoryCache = new LruCache<String, Bitmap>(maxCacheSize) {
		@Override
		protected int sizeOf(String key, Bitmap value) {
			if (value != null) {
				return value.getByteCount();
				// return value.getRowBytes() * value.getHeight(); //旧版本的方法
			} else {
				return 0;
			}
		}

		// 这个方法当LruCache的内存容量满的时候会调用,将oldValue的元素移除出来腾出空间给新的元素加入
		@Override
		protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
			if (oldValue != null) {
				// 当硬引用缓存容量已满时，会使用LRU算法将最近没有被使用的图片转入软引用缓存
				currentHashmap.put(key, new SoftReference<Bitmap>(oldValue));
			}
		}

	};

	/**
	 * 针对提供图片文件链接或下载链接来显示图片的方法
	 * 
	 * @param loadType
	 *            图片加载类型
	 * @param imageFilePath
	 *            图片文件的本地文件地址或网络URL的下载链接
	 * @param imageView
	 *            显示图片的ImageView
	 */
	public void setViewBackground(IMAGE_LOAD_TYPE loadType, String imageFilePath, View view) {
		if (imageFilePath == null || imageFilePath.trim().equals("")) {
			view.setBackgroundResource(R.drawable.image_default);
		} else {
			Bitmap bitmap = getBitmapFromMemoryCache(imageFilePath);
			if (bitmap != null) {
				Drawable drawable = new BitmapDrawable(bitmap);
				view.setBackgroundDrawable(drawable);
			} else {
				view.setBackgroundResource(R.drawable.image_default);
				ImageLoadTask task = new ImageLoadTask(loadType, view);
				taskCollection.add(task);
				task.execute(imageFilePath);
			}
		}
	}

	/**
	 * 针对提供图片文件链接或下载链接来显示图片的方法
	 * 
	 * @param loadType
	 *            图片加载类型
	 * @param imageFilePath
	 *            图片文件的本地文件地址或网络URL的下载链接
	 * @param imageView
	 *            显示图片的ImageView
	 */
	public void setViewBackground(IMAGE_LOAD_TYPE loadType, String imageFilePath,OnGetPhotoListener opl) {
		if (imageFilePath == null || imageFilePath.trim().equals("")) {
			Bitmap  bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.image_default);
			opl.onGetPhoto(null, bitmap);
		} else {
			Bitmap bitmap = getBitmapFromMemoryCache(imageFilePath);
			if (bitmap != null) {
				opl.onGetPhoto(null, bitmap);
			} else {
				ImageLoadTask task = new ImageLoadTask(loadType, null, opl);
				taskCollection.add(task);
				task.executeOnExecutor(Executors.newCachedThreadPool(), imageFilePath);
			}
		}
	}

	/**
	 * 从LruCache中获取一张图片，如果不存在就返回null
	 * 
	 * @param key
	 *            键值可以是图片文件的filePath,可以是图片URL地址
	 * @return Bitmap对象,或者null
	 */
	public Bitmap getBitmapFromMemoryCache(String key) {
		try {
			if (BitmapMemoryCache.get(key) == null) {
				if (currentHashmap.get(key) != null) {
					return currentHashmap.get(key).get();
				}
			}
			return BitmapMemoryCache.get(key);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return BitmapMemoryCache.get(key);
	}

	/**
	 * 将图片放入缓存
	 * 
	 * @param key
	 * @param bitmap
	 */
	private void addBitmapToCache(String key, Bitmap bitmap) {
		BitmapMemoryCache.put(key, bitmap);
	}

	/**
	 * 图片异步加载
	 * 
	 * @author Mr.Et
	 * 
	 */
	private class ImageLoadTask extends AsyncTask<String, Void, Bitmap> {
		private String imagePath;
		private View view;
		private IMAGE_LOAD_TYPE loadType;
		private OnGetPhotoListener opl;

		public ImageLoadTask(IMAGE_LOAD_TYPE loadType, View view) {
			this.loadType = loadType;
			this.view = view;
		}

		public ImageLoadTask(IMAGE_LOAD_TYPE loadType, View view, OnGetPhotoListener opl) {
			this.loadType = loadType;
			this.view = view;
			this.opl = opl;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			imagePath = params[0];
			try {
				if (loadType == IMAGE_LOAD_TYPE.FILE_PATH) {
					if (new File(imagePath).exists()) { // 从本地FILE读取图片
						BitmapFactory.Options opts = new BitmapFactory.Options();
						opts.inSampleSize = 2;
						Bitmap bitmap = BitmapFactory.decodeFile(imagePath, opts);
						// 将获取的新图片放入缓存
						addBitmapToCache(imagePath, bitmap);
						return bitmap;
					}
					return null;
				} else if (loadType == IMAGE_LOAD_TYPE.FILE_URL) { // 从网络下载图片
					byte[] datas = getBytesOfBitMap(imagePath);
					if (datas != null) {
						// BitmapFactory.Options opts = new
						// BitmapFactory.Options();
						// opts.inSampleSize = 2;
						// Bitmap bitmap = BitmapFactory.decodeByteArray(datas,
						// 0, datas.length, opts);
						Bitmap bitmap = BitmapFactory.decodeByteArray(datas, 0, datas.length);
						addBitmapToCache(imagePath, bitmap);
						return bitmap;
					}
					return null;
				}

			} catch (Exception e) {
				e.printStackTrace();
				// 可自定义其他操作
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (bitmap==null) {
				bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.image_default);
			}
			opl.onGetPhoto(null, bitmap);
			try {
				if (view != null) {
					if (bitmap != null) {
						Drawable drawable = new BitmapDrawable(bitmap);
						view.setBackgroundDrawable(drawable);
					} else {
						Qlog.e(TAG, "The bitmap result is null...");
					}
				} else {
					Qlog.e(TAG, "The imageView is null...");
					// 获取图片失败时显示默认图片
					view.setBackgroundResource(R.drawable.image_default);

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * InputStream转byte[]
	 * 
	 * @param inStream
	 * @return
	 * @throws Exception
	 */
	private byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[2048];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}

	/**
	 * 获取下载图片并转为byte[]
	 * 
	 * @param urlStr
	 * @return
	 */
	private byte[] getBytesOfBitMap(String imgUrl) {
		try {
			URL url = new URL(imgUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(10 * 1000); // 10s
			conn.setReadTimeout(20 * 1000);
			conn.setRequestMethod("GET");
			conn.connect();
			InputStream in = conn.getInputStream();
			return readStream(in);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// /**
	// * 该资源ID是否有效
	// *
	// * @param resourceId
	// * 资源ID
	// * @return
	// */
	// private boolean ifResourceIdExist(int resourceId) {
	// try {
	// Field field = R.drawable.class.getField(String.valueOf(resourceId));
	// Integer.parseInt(field.get(null).toString());
	// return true;
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return false;
	// }

	/**
	 * 取消所有任务
	 */
	public void cancelAllTask() {
		if (taskCollection != null) {
			for (ImageLoadTask task : taskCollection) {
				task.cancel(false);
			}
		}
	}

}
