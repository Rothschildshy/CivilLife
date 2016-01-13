package com.CivilLife.MyAdapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import com.CivilLife.Base.BaseApplication;
import com.CivilLife.Base.BaseListAdapter;
import com.CivilLife.Entity.MessagelistEntity;
import com.CivilLife.Variable.GlobalVariable;
import com.CivilLife.Widget.CircleImageView;
import com.app.civillife.InfoHomepageActivity;
import com.app.civillife.R;

import Downloadimage.ImageUtils;

/**
 * 聊天界面适配器
 * 
 * @author Administrator
 * 
 */
public class ChatListViewAdapter extends BaseListAdapter {


	private Context context;
	public String tousedid;
	public String tousedname;
	public ViewHolder holder = null;
	public boolean UP_isOK=true;//上传结果
	public boolean UPing=false;//上传中...
	public ChatListViewAdapter(BaseApplication application, Context context1, List<? extends Object> datas) {
		super(application, context1, datas);
		context = context1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		MessagelistEntity item = (MessagelistEntity) getItem(position);
		if (convertView == null ) {
			holder = new ViewHolder();        
			  
			convertView = mInflater.inflate(R.layout.chat_lift_item, null);
			holder.image_pic_other = (CircleImageView) convertView.findViewById(R.id.image_pic_other);
			holder.tx_content_other = (TextView) convertView.findViewById(R.id.tx_content_other);
			
			holder.image_pic_me = (CircleImageView) convertView.findViewById(R.id.image_pic_me);
			holder.tx_content_me = (TextView) convertView.findViewById(R.id.tx_content_me);
			holder.rl_othermessage = (RelativeLayout) convertView.findViewById(R.id.rl_othermessage);
			holder.rl_memessage = (RelativeLayout) convertView.findViewById(R.id.rl_memessage);
			holder.prb_up = (ProgressBar) convertView.findViewById(R.id.prb_up);
			holder.tv_toresend = (TextView) convertView.findViewById(R.id.tv_toresend);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();     
		} 
		if (item.getUserID().equals(GlobalVariable.UserID)) {  
			holder.rl_memessage.setVisibility(View.VISIBLE);
			holder.rl_othermessage.setVisibility(View.GONE);
			ImageUtils.loadImage(context, item.getUserPicUrl(), holder.image_pic_me, false);
			holder.tx_content_me.setText(item.getContent());
		}else{
			holder.rl_memessage.setVisibility(View.GONE);
			holder.rl_othermessage.setVisibility(View.VISIBLE);
			ImageUtils.loadImage(context, item.getUserPicUrl(), holder.image_pic_other, false);
			holder.tx_content_other.setText(item.getContent());
		}
		holder.tv_toresend.setPaintFlags(IGNORE_ITEM_VIEW_TYPE);  
//		ImageUtils.loadImage(context, item.getUserPicUrl(), holder.mIm_Pic, false);
//		holder.mTx_Content.setText(item.getContent());
		// 跳转个人主页
		holder.image_pic_other.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, InfoHomepageActivity.class);
				intent.putExtra("type", 5);
				intent.putExtra("username", tousedname+"主页");
				intent.putExtra("userid", tousedid);
				context.startActivity(intent);  
			}

		});
		
		holder.image_pic_me.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, InfoHomepageActivity.class);
				intent.putExtra("type", 5);
				intent.putExtra("username", "我的主页");
				intent.putExtra("userid", GlobalVariable.UserID);  
				context.startActivity(intent);
			}
		});
		
		if (UPing && position==getCount()-1) {
			holder.prb_up.setVisibility(View.VISIBLE);
		}else{
			holder.prb_up.setVisibility(View.GONE);
		}
		
//		if (UP_isOK && position==getCount()-1) {
//			holder.tv_toresend.setVisibility(View.VISIBLE);
//		}else{  
//			holder.tv_toresend.setVisibility(View.GONE);
//		}
		
		return convertView;
	}
	
	private static class ViewHolder {
		TextView mTx_Time;
		TextView tx_content_other;
		CircleImageView image_pic_other;
		TextView tx_content_me;
		CircleImageView image_pic_me;
		RelativeLayout rl_othermessage;
		RelativeLayout rl_memessage;
		
		public ProgressBar prb_up;
		public TextView tv_toresend;
	}

}
