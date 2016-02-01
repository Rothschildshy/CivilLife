package com.CivilLife.MyAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;

import com.CivilLife.Base.BaseActivity;
import com.CivilLife.Base.BaseApplication;
import com.CivilLife.Base.BaseListAdapter;
import com.CivilLife.Entity.HomeEntity;
import com.CivilLife.Entity.PublicEntity;
import com.CivilLife.Json.PublicUpJson;
import com.CivilLife.Variable.GlobalVariable;
import com.CivilLife.Widget.CircleImageView;
import com.CivilLife.Widget.GridForScrollView;
import com.CivilLife.Widget.URLImageParser;
import com.CivilLife.net.Httpurl;
import com.CivilLife.net.RequestTask;
import com.app.civillife.ContentDataActivity;
import com.app.civillife.InfoHomepageActivity;
import com.app.civillife.R;
import com.app.civillife.ShareActivity;
import com.app.civillife.Util.ImagePagerActivity;
import com.app.civillife.Util.VideoPay;
import com.app.civillife.Util.Videolistener;
import com.aysy_mytool.Time;
import com.aysy_mytool.ToastUtil;

import Downloadimage.ImageUtils;
import Requset_getORpost.RequestListener;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * 首页片段适配器
 * 
 * @author Administrator
 * 
 */
@SuppressLint("UseSparseArrays")
public class HomeListViewAdapter extends BaseListAdapter {

	private Context context;
	ViewHolder holder = null;
	private HomeEntity item;
	public int ispay=-2;//播放视频的位置
	public VideoPay videoPay=null;
	public HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();//点赞存储

	private int seleortpos;
	private int seleortpyte;
	

	public VideoPay getVideoPay() {
		return videoPay;
	}

	public void setVideoPay(VideoPay videoPay) {
		this.videoPay = videoPay;
	}

	public HomeListViewAdapter(BaseApplication application, Context context, List<? extends Object> datas) {
		super(application, context, datas);
		this.context = context;
	}

	@SuppressWarnings("static-access")
	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final int mposition=position;
		if (convertView == null || convertView.getTag() == null) {  
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.home_info_list_item, null);
			holder.mIm_Pic = (CircleImageView) convertView.findViewById(R.id.image_pic);
			holder.mTx_Name = (TextView) convertView.findViewById(R.id.tx_name);  
			holder.mTx_Content = (TextView) convertView.findViewById(R.id.tx_content);
			holder.mImageGR = (GridForScrollView) convertView.findViewById(R.id.image_gridView);
			holder.video = (VideoView) convertView.findViewById(R.id.videoView1);
			holder.mTx_FunnyNum = (TextView) convertView.findViewById(R.id.tx_funny_num);
			holder.mTx_CommentNum = (TextView) convertView.findViewById(R.id.tx_comment_num);
			holder.mRb_Favour = (RadioButton) convertView.findViewById(R.id.radio_favour);
			holder.mRb_Disapprove = (RadioButton) convertView.findViewById(R.id.radio_disapprove);
			holder.mIm_Comment = (ImageView) convertView.findViewById(R.id.image_comment);
			holder.mIm_Share = (ImageView) convertView.findViewById(R.id.image_share);
			holder.mIm_Play = (ImageButton) convertView.findViewById(R.id.image_play);
			holder.layout_video = (RelativeLayout) convertView.findViewById(R.id.layout_video);
			holder.pb_waiting = (ProgressBar) convertView.findViewById(R.id.pb_waiting);
			holder.video_image = (ImageView) convertView.findViewById(R.id.video_image);
			holder.tv_addtime = (TextView) convertView.findViewById(R.id.tv_addtime);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();  
		}
		item = (HomeEntity) getItem(position);
		holder.mImageGR.setVisibility(View.GONE);  
		holder.layout_video.setVisibility(View.GONE);
		String userInfoPicUrl = item.getUserInfoPicUrl();
		if (item.getNickname().equals("匿名")) {
			holder.mIm_Pic.setImageResource(R.drawable.icon_apprentice);
			userInfoPicUrl="";
		}
		ImageUtils.loadImage1(context, userInfoPicUrl, holder.mIm_Pic, R.drawable.ic_my_nolog_selector, R.drawable.ic_my_nolog_selector,GlobalVariable.WifiDown);
		holder.mTx_Name.setText(item.getNickname());    
		holder.mTx_Content.setText(Html.fromHtml(item.getContent(), new URLImageParser(context, holder.mTx_Content), null));    
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
				Intent intent = new Intent(context,ImagePagerActivity.class);
				String[] split = ((HomeEntity) getItem(position)).getPicUrl().split(",");
				List<String> asList = Arrays.asList(split);  
				ArrayList<String> al=new ArrayList<String>();
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
		if (hm.containsKey(position)) { 
			if (hm.get(position) == 1) {
				holder.mRb_Favour.setChecked(true);
				holder.mRb_Disapprove.setChecked(false);
			} else if (hm.get(position) == 0) {
				holder.mRb_Disapprove.setChecked(true);
				holder.mRb_Favour.setChecked(false);
			} else {
				holder.mRb_Favour.setChecked(false);
				holder.mRb_Disapprove.setChecked(false);
			}
		} else if (((HomeEntity) getDatas().get(position)).getPraise().equals("1")) {
			hm.put(position, 1);
			holder.mRb_Favour.setChecked(true);
		} else if (((HomeEntity) getDatas().get(position)).getPraise().equals("0")) {
			hm.put(position, 0);
			holder.mRb_Disapprove.setChecked(true);
		} else {
			hm.put(position, -1);  
			holder.mRb_Favour.setChecked(false);
			holder.mRb_Disapprove.setChecked(false);  
		}

		holder.mTx_FunnyNum.setText(item.getPraises() + "好文");  
		holder.mTx_CommentNum.setText(item.getReviews() + "评论");
		
		holder.tv_addtime.setText(Time.handTime(item.getAddTime(), "yyyy/MM/dd HH:mm:ss"));
		// 跳转个人主页
		holder.mIm_Pic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				HomeEntity item = (HomeEntity) getItem(mposition);
				if (item.getNickname().equals("匿名")) {
					ToastUtil.showToast(context, "该用户使用匿名发布！无法查看详情");
					return;
				}
				StartInfoHomepage(item.getNickname(),item.getUserID());
			}
		});
		
		// 跳转个人主页
		holder.mTx_Name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (item.getNickname().equals("匿名")) {
					ToastUtil.showToast(context, "该用户使用匿名发布！无法查看详情");
					return;
				}
				StartInfoHomepage(item.getNickname(),item.getUserID());
			}
		});
		
		// 点击跳转过去直接评论
		holder.mIm_Comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putBoolean("comment", true);
				bundle.putParcelable("HomeEntity", (HomeEntity) getItem(mposition));
				((BaseActivity) context).startActivityForResult(ContentDataActivity.class, bundle, 10001);
			}
		});  
		
		// 分享
		holder.mIm_Share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				HomeEntity  item2 = (HomeEntity) getItem(mposition);
				Bundle bundle = new Bundle();
				bundle.putString("Title", item2.getNickname());
				bundle.putString("Content", item2.getContent());
				bundle.putString("shareURL", "http://tmssh.conitm.com/share-"+item2.getID()+".html");
				bundle.putString("imageurl", item2.getUserInfoPicUrl());
				((BaseActivity) context).startActivityForResult(ShareActivity.class, bundle, 10001);
//				context.startActivity(new Intent(context,ShareActivity.class));
				((Activity) context).overridePendingTransition(R.anim.small_in_big, R.anim.fade_out);
			}
		});
		
		// 好文
		holder.mRb_Favour.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Qlog.e("好文", ""+Httpurl.IsSmile(item.getID(), "0"));
				HomeEntity item1 = (HomeEntity) getItem(mposition);
				seleortpos = mposition;
				seleortpyte = 1;
				new RequestTask(context, Favourlistener, false, true, "打赏中")
						.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.IsSmile(item1.getID(), "1"));
			}
		});
		
		// 不好文
		holder.mRb_Disapprove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Qlog.e("不好文", ""+Httpurl.IsSmile(item.getID(), "0"));
				HomeEntity item1 = (HomeEntity) getItem(mposition);
				seleortpos=mposition;
				seleortpyte=0;
				
				new RequestTask(context, Favourlistener, false, true, "打赏中")
						.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.IsSmile(item1.getID(), "0"));
			}
		});
		
//		holder.pb_waiting.setVisibility(View.GONE);
//		Qlog.e("", "123   "+holder.pb_waiting.getVisibility());
		holder.mIm_Play.setVisibility(View.VISIBLE);
		holder.video.setVisibility(View.INVISIBLE);
		holder.video_image.setVisibility(View.VISIBLE);    
		new ImageUtils().loadImage3(context, item.getVideoThumbnailsPicUrl(), holder.video_image, false);
		if (position==ispay && videoPay.ispay) {
			holder.video.setVisibility(View.VISIBLE);
			holder.mIm_Play.setVisibility(View.GONE);
			holder.video_image.setVisibility(View.GONE);
		}
//		holder.mIm_Play.GET
//		Bitmap videoThumbnail = CommonAPI.createVideoThumbnail(item.getVideoUrl()); 
//		Drawable drawable = new BitmapDrawable(videoThumbnail);
//		holder.mIm_Play.setBackgroundColor(R.color.color_black); 
//		Log.e("", "第一贞drawable  "+item.getVideoUrl());
//		holder.video_image.setImageBitmap(videoThumbnail);
		// 视频播放
		holder.mIm_Play.setOnClickListener(new OnClickListener() {  

			@Override
			public void onClick(View arg0) {
				if (ispay!=mposition) {
					if (videoPay!=null && videoPay.ispay) {  
						videoPay.setPause();
					}
					holder.pb_waiting.setVisibility(View.VISIBLE);
//					Log.e("", "((HomeEntity) getItem(mposition)).getVideoUrl()  "+((HomeEntity) getItem(mposition)).getVideoUrl());
//					Log.e("", " 123mposition  "+mposition);
					videoPay = new VideoPay(context, holder.video, ((HomeEntity) getItem(mposition)).getVideoUrl(), videolistener);
					videoPay.playvideo();  
					holder.video.setVisibility(View.VISIBLE);  
					holder.mIm_Play.setVisibility(View.GONE);
//					Log.e("", "holder.pb_waiting1 "+holder.pb_waiting.getVisibility());  
					ispay=mposition;
				}else {  
					if (videoPay!=null) {
						videoPay.playvideo();  
						holder.pb_waiting.setVisibility(View.GONE);
						holder.mIm_Play.setVisibility(View.GONE);
						holder.video.setVisibility(View.VISIBLE);
						
					}else{
						videoPay = new VideoPay(context, holder.video, ((HomeEntity) getItem(mposition)).getVideoUrl(), videolistener);
						videoPay.playvideo();  
						holder.video.setVisibility(View.VISIBLE);
						holder.pb_waiting.setVisibility(View.VISIBLE);
						holder.mIm_Play.setVisibility(View.GONE);
						ispay=mposition;
					}  
				}
				notifyDataSetChanged();
			}
		});
		holder.layout_video.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (videoPay!=null&&videoPay.ispay) {
					videoPay.setPause();  
					holder.mIm_Play.setVisibility(View.VISIBLE);
				}  
			}
		});
		if (ispay==position) {
			holder.pb_waiting.setVisibility(View.VISIBLE);
		}else{
			holder.pb_waiting.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	Videolistener videolistener=new Videolistener() {  
		
		@Override
		public void onPrepared(MediaPlayer mediaplayer) {      
			holder.pb_waiting.setVisibility(View.GONE);
			holder.video.setVisibility(View.VISIBLE);  
		}
		
		@Override
		public void onError(MediaPlayer mediaplayer, int i, int j) {
			ispay=-1;
			videoPay=null;
			holder.mIm_Play.setVisibility(View.VISIBLE);
			holder.video.setVisibility(View.INVISIBLE);
			notifyDataSetChanged();
		}  
		
		@Override
		public void onCompletion(MediaPlayer mediaplayer) {
			ispay=-1;
			videoPay=null;
			holder.mIm_Play.setVisibility(View.VISIBLE);
			holder.video.setVisibility(View.INVISIBLE);
			notifyDataSetChanged();  
		}
	};
	
	public void InitVideo(){
		if (videoPay!=null ) {
//			Log.e("","2  暂停 ");
			videoPay.setPause();   
		}
//		Log.e("","1 清空 ");
		ispay=-1;
		holder.mIm_Play.setVisibility(View.VISIBLE);
		videoPay=null;
		notifyDataSetChanged();
	}
	
	// 跳转个人主页
	private void StartInfoHomepage(String username,String userid) {
//		Qlog.e("", "username "+username+"  userid"+userid);
		Intent intent = new Intent(context, InfoHomepageActivity.class);
		intent.putExtra("type", 5);
		intent.putExtra("username", username);
		intent.putExtra("userid", userid);
		context.startActivity(intent);
	}

	private static class ViewHolder {
		RelativeLayout layout_video;// 视频外布局
		TextView mTx_Name;// 名称
		TextView tv_addtime;// 时间
		TextView mTx_Content;// 内容
		TextView mTx_FunnyNum;// 好文数
		TextView mTx_CommentNum;// 评论数
		CircleImageView mIm_Pic;// 发布者头像
		ImageView mIm_Comment;// 评论
		ImageView mIm_Share;// 分享
		ImageButton mIm_Play;// 视频播放  
		GridForScrollView mImageGR;// 图片墙
		VideoView video;// 视频
		RadioButton mRb_Favour;// 好文
		RadioButton mRb_Disapprove;// 不好文
		ProgressBar pb_waiting;//加载条
		ImageView video_image;
	}
	RequestListener Favourlistener = new RequestListener() {

		@Override
		public void responseResult(String jsonObject) {
			PublicUpJson publicUpJson = PublicUpJson.readJsonToSendmsgObject(context, jsonObject);
			if (publicUpJson == null) {
				return;
			}
			PublicEntity publicEntity = publicUpJson.getAl().get(0);
			if (publicEntity.getStatus().equals("1")) {
				ArrayList<HomeEntity> al=(ArrayList<HomeEntity>) mDatas;
				HomeEntity homeEntity = (HomeEntity) mDatas.get(seleortpos);
				homeEntity.setPraise(seleortpyte+"");
				if (seleortpyte==1) {
					homeEntity.setPraises(Integer.valueOf(homeEntity.getPraises().toString())+1+"");
				}else if(seleortpyte==0){
					Integer valueOf = Integer.valueOf(homeEntity.getPraises().toString());
					if (valueOf>0) {
						valueOf=valueOf-1;
					}
					homeEntity.setPraises(valueOf+"");
				}
				al.set(seleortpos, homeEntity);
				setmDatas(al);
				hm.put(seleortpos, seleortpyte);
				notifyDataSetChanged();
				ToastUtil.showToast(context, "谢谢客官打赏");
			} else {
				ToastUtil.showToast(context, "打赏失败");
			}
		}

		@Override
		public void responseException(String errorMessage) {
			ToastUtil.showMessage(context, errorMessage);
		}
	};
}
