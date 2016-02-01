package com.CivilLife.MyAdapter;

import java.util.List;

import com.CivilLife.Base.BaseApplication;
import com.CivilLife.Base.BaseListAdapter;
import com.CivilLife.Entity.HomeEntity;
import com.CivilLife.Variable.GlobalVariable;
import com.CivilLife.Widget.CircleImageView;
import com.app.civillife.InfoHomepageActivity;
import com.app.civillife.R;

import Downloadimage.ImageUtils;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 评论列表适配器 xiugai
 * 
 * @author Administrator
 * 
 */
public class CommentListViewAdapter extends BaseListAdapter {

	public CommentListViewAdapter(BaseApplication application, Context context, List<? extends Object> datas) {
		super(application, context, datas);
		this.context = context;
	}

	private Context context;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = mInflater.inflate(R.layout.comment_list_item, null);
			holder = new ViewHolder();
			holder.mTx_floor = (TextView) convertView.findViewById(R.id.tx_floor);
			holder.mTx_Name = (TextView) convertView.findViewById(R.id.tx_name);
			holder.mTx_Content = (TextView) convertView.findViewById(R.id.tx_content);
			holder.mIm_Pic = (CircleImageView) convertView.findViewById(R.id.image_pic);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mTx_floor.setText(position + 1 + " 楼");
		final HomeEntity item = (HomeEntity) getItem(position);
		holder.mTx_Name.setText(item.getNickname());
		holder.mTx_Content.setText(item.getContent());
		ImageUtils.loadImage1(context, item.getUserInfoPicUrl(), holder.mIm_Pic, R.drawable.ic_my_nolog_selector, R.drawable.ic_my_nolog_selector, GlobalVariable.WifiDown);
		// 跳转个人主页
		holder.mIm_Pic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				StartInfoHomepage(item.getNickname(),item.getUserID());
			}
		});
		// 跳转个人主页
		holder.mTx_Name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				StartInfoHomepage(item.getNickname(),item.getUserID());
			}

		});
		return convertView;
	}

	// 跳转个人主页
	private void StartInfoHomepage(String username,String userid) {
		Intent intent = new Intent(context, InfoHomepageActivity.class);
		intent.putExtra("type", 5);
		intent.putExtra("username", username);
		intent.putExtra("userid", userid);
		context.startActivity(intent);
	}

	private static class ViewHolder {
		TextView mTx_floor;// 楼层
		TextView mTx_Name;// 评论人名称
		TextView mTx_Content;// 评论内容
		CircleImageView mIm_Pic;// 评论人头像
	}

}
