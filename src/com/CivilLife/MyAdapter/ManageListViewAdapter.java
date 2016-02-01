package com.CivilLife.MyAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

import com.CivilLife.Base.BaseActivity;
import com.CivilLife.Base.BaseApplication;
import com.CivilLife.Base.BaseListAdapter;
import com.CivilLife.Entity.HomeEntity;
import com.CivilLife.Entity.PublicEntity;
import com.CivilLife.Json.PublicUpJson;
import com.CivilLife.Variable.RequestCode;
import com.CivilLife.Widget.GridForScrollView;
import com.CivilLife.Widget.URLImageParser;
import com.CivilLife.net.Httpurl;
import com.CivilLife.net.RequestTask;
import com.app.civillife.PublishActivity;
import com.app.civillife.R;
import com.app.civillife.Util.ImagePagerActivity;
import com.app.civillife.Util.VideoPay;
import com.app.civillife.Util.Videolistener;
import com.aysy_mytool.Time;
import com.aysy_mytool.ToastUtil;

import Downloadimage.ImageUtils;
import Requset_getORpost.RequestListener;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 管理我的文字 添加修改
 * 
 * @author Administrator
 * 
 */
public class ManageListViewAdapter extends BaseListAdapter {
	private Context context;
	ViewHolder holder = null;
	// private HomeEntity item;
	public int ispay = -2;// 播放视频的位置
	public VideoPay videoPay = null;
	// public HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();//
	// 点赞存储

	// private int seleortpos;
	// private int seleortpyte;

	public VideoPay getVideoPay() {
		return videoPay;
	}

	public void setVideoPay(VideoPay videoPay) {
		this.videoPay = videoPay;
	}

	public ManageListViewAdapter(BaseApplication application, Context context, List<? extends Object> datas) {
		super(application, context, datas);
		this.context = context;
	}

	@SuppressWarnings("static-access")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final int mposition = position;
		if (convertView == null || convertView.getTag() == null) {
			convertView = mInflater.inflate(R.layout.manage_list_item, null);
			holder = new ViewHolder();
			holder.mTx_Time = (TextView) convertView.findViewById(R.id.tx_time);
			holder.mImg_Compile = (ImageView) convertView.findViewById(R.id.image_compile);
			holder.mImg_Del = (ImageView) convertView.findViewById(R.id.image_del);

			holder.mTx_Content = (TextView) convertView.findViewById(R.id.tx_content);
			holder.mImageGR = (GridForScrollView) convertView.findViewById(R.id.image_gridView);
			holder.video = (VideoView) convertView.findViewById(R.id.videoView1);
			holder.mTx_FunnyNum = (TextView) convertView.findViewById(R.id.tx_funny_num);
			holder.mTx_CommentNum = (TextView) convertView.findViewById(R.id.tx_comment_num);
			holder.mIm_Play = (ImageButton) convertView.findViewById(R.id.image_play);
			holder.layout_video = (RelativeLayout) convertView.findViewById(R.id.layout_video);
			holder.pb_waiting = (ProgressBar) convertView.findViewById(R.id.pb_waiting);
			holder.video_image = (ImageView) convertView.findViewById(R.id.video_image);
			holder.tv_addtime = (TextView) convertView.findViewById(R.id.tv_addtime);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final HomeEntity item = (HomeEntity) getItem(position);
		holder.mImageGR.setVisibility(View.GONE);
		holder.layout_video.setVisibility(View.GONE);
		holder.mTx_Content.setText(item.getContent());
		holder.mTx_Time.setText(Time.handTime(item.getAddTime(), "yyyy/MM/dd HH:mm:ss"));
		holder.tv_addtime.setText(Time.handTime(item.getAddTime(), "yyyy/MM/dd HH:mm:ss"));
		holder.mTx_Content
				.setText(Html.fromHtml(item.getContent(), new URLImageParser(context, holder.mTx_Content), null));
		holder.mTx_FunnyNum.setText(item.getPraises() + "好文");
		holder.mTx_CommentNum.setText(item.getReviews() + "评论");

		if (!TextUtils.isEmpty(item.getPicUrl())) {
			String[] images = item.getPicUrl().split(",");
			List<String> asList = Arrays.asList(images);
			HomeimagegridviewAdapter adapter = new HomeimagegridviewAdapter(mApplication, context, asList);
			holder.mImageGR.setAdapter(adapter);
			holder.mImageGR.setVisibility(View.VISIBLE);
		}

		holder.mImageGR.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(context, ImagePagerActivity.class);
				String[] split = ((HomeEntity) getItem(position)).getPicUrl().split(",");
				List<String> asList = Arrays.asList(split);
				ArrayList<String> al = new ArrayList<String>();
				al.addAll(asList);
				intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, al);
				intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, arg2);
				context.startActivity(intent);
			}
		});

		if (!TextUtils.isEmpty(item.getVideoUrl())) {
			holder.layout_video.setVisibility(View.VISIBLE);
			holder.mImageGR.setVisibility(View.GONE);
		} else {
			holder.layout_video.setVisibility(View.GONE);
		}

		holder.mIm_Play.setVisibility(View.VISIBLE);
		holder.video.setVisibility(View.INVISIBLE);
		holder.video_image.setVisibility(View.VISIBLE);
		new ImageUtils().loadImage3(context, item.getVideoThumbnailsPicUrl(), holder.video_image, false);
		if (position == ispay && videoPay.ispay) {
			holder.video.setVisibility(View.VISIBLE);
			holder.mIm_Play.setVisibility(View.GONE);
			holder.video_image.setVisibility(View.GONE);
		}
		// 视频播放
		holder.mIm_Play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (ispay != mposition) {
					if (videoPay != null && videoPay.ispay) {
						videoPay.setPause();
					}
					holder.pb_waiting.setVisibility(View.VISIBLE);
					// Log.e("", "((HomeEntity)
					// getItem(mposition)).getVideoUrl() "+((HomeEntity)
					// getItem(mposition)).getVideoUrl());
					// Log.e("", " 123mposition "+mposition);
					videoPay = new VideoPay(context, holder.video, ((HomeEntity) getItem(mposition)).getVideoUrl(),
							videolistener);
					videoPay.playvideo();
					holder.video.setVisibility(View.VISIBLE);
					holder.mIm_Play.setVisibility(View.GONE);
					// Log.e("", "holder.pb_waiting1
					// "+holder.pb_waiting.getVisibility());
					ispay = mposition;
				} else {
					if (videoPay != null) {
						videoPay.playvideo();
						holder.pb_waiting.setVisibility(View.GONE);
						holder.mIm_Play.setVisibility(View.GONE);
						holder.video.setVisibility(View.VISIBLE);

					} else {
						videoPay = new VideoPay(context, holder.video, ((HomeEntity) getItem(mposition)).getVideoUrl(),
								videolistener);
						videoPay.playvideo();
						holder.video.setVisibility(View.VISIBLE);
						holder.pb_waiting.setVisibility(View.VISIBLE);
						holder.mIm_Play.setVisibility(View.GONE);
						ispay = mposition;
					}
				}
				notifyDataSetChanged();
			}
		});
		holder.layout_video.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (videoPay != null && videoPay.ispay) {
					videoPay.setPause();
					holder.mIm_Play.setVisibility(View.VISIBLE);
				}
			}
		});
		if (ispay == position) {
			holder.pb_waiting.setVisibility(View.VISIBLE);
		} else {
			holder.pb_waiting.setVisibility(View.GONE);
		}
		// 编辑文字
		holder.mImg_Compile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putInt("TYPE", 2);
				bundle.putParcelable("HomeEntity", item);
				((BaseActivity) context).startActivityForResult(PublishActivity.class, bundle, RequestCode.publiccode);
			}

		});
		// 删除文字
		holder.mImg_Del.setOnClickListener(new OnClickListener() {
			RequestListener DELlistener = new RequestListener() {

				@Override
				public void responseResult(String jsonObject) {
					PublicUpJson publicjson = PublicUpJson.readJsonToSendmsgObject(context, jsonObject);
					if (publicjson == null) {
						return;
					}
					PublicEntity publicEntity = publicjson.getAl().get(0);
					String status = publicEntity.getStatus();
					if (status.equals("1")) {
						mDatas.remove(position);
						notifyDataSetChanged();
					} else {
						ToastUtil.showToast(context, publicEntity.getMessage());
					}
				}

				@Override
				public void responseException(String errorMessage) {
					ToastUtil.showToast(context, errorMessage);
				}
			};

			@Override
			public void onClick(View v) {
				((BaseActivity) context).PromptDialogs("确定删除文字吗？", new OnClickListener() {

					@Override
					public void onClick(View v) {
						HomeEntity item = (HomeEntity) getItem(position);
						new RequestTask(context, DELlistener, false, true, "删除中")
								.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.DelMyManage(item.getID()));
					}
				});
			}
		});

		return convertView;
	}

	Videolistener videolistener = new Videolistener() {

		@Override
		public void onPrepared(MediaPlayer mediaplayer) {
			holder.pb_waiting.setVisibility(View.GONE);
			holder.video.setVisibility(View.VISIBLE);
		}

		@Override
		public void onError(MediaPlayer mediaplayer, int i, int j) {
			ispay = -1;
			videoPay = null;
			holder.mIm_Play.setVisibility(View.VISIBLE);
			holder.video.setVisibility(View.INVISIBLE);
			notifyDataSetChanged();
		}

		@Override
		public void onCompletion(MediaPlayer mediaplayer) {
			ispay = -1;
			videoPay = null;
			holder.mIm_Play.setVisibility(View.VISIBLE);
			holder.video.setVisibility(View.INVISIBLE);
			notifyDataSetChanged();
		}
	};

	public void InitVideo() {
		if (videoPay != null) {
			// Log.e("","2 暂停 ");
			videoPay.setPause();
		}
		// Log.e("","1 清空 ");
		ispay = -1;
		holder.mIm_Play.setVisibility(View.VISIBLE);
		videoPay = null;
		notifyDataSetChanged();
	}

	private static class ViewHolder {
		TextView mTx_Time;
		ImageView mImg_Del;
		ImageView mImg_Compile;

		RelativeLayout layout_video;// 视频外布局
		TextView tv_addtime;// 时间
		TextView mTx_Content;// 内容
		TextView mTx_FunnyNum;// 好文数
		TextView mTx_CommentNum;// 评论数
		ImageButton mIm_Play;// 视频播放
		GridForScrollView mImageGR;// 图片墙
		VideoView video;// 视频
		ProgressBar pb_waiting;// 加载条
		ImageView video_image;
	}
}
