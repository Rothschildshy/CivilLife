package com.CivilLife.MyAdapter;

import java.util.List;

import com.CivilLife.Base.BaseApplication;
import com.CivilLife.Base.BaseListAdapter;
import com.CivilLife.Entity.MyFriendEntity;
import com.CivilLife.Variable.GlobalVariable;
import com.CivilLife.Widget.CircleImageView;
import com.app.civillife.R;
import com.aysy_mytool.GetAgeOrConstellation;

import Downloadimage.ImageUtils;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 我的筑友 我的徒弟 我的师傅 列表片段适配器
 * 
 * @author Administrator
 * 
 */
public class MyFriendListViewAdapter extends BaseListAdapter {
	private Context context;
	private int Type;

	public MyFriendListViewAdapter(BaseApplication application, Context context, List<? extends Object> datas,
			int type) {
		super(application, context, datas);
		this.context = context;
		this.Type = type;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = mInflater.inflate(R.layout.myfriend_list_item, null);
			holder = new ViewHolder();
			holder.mTx_Address = (TextView) convertView.findViewById(R.id.tx_address);
			holder.mTx_Age = (TextView) convertView.findViewById(R.id.tx_age);
			holder.mTx_Name = (TextView) convertView.findViewById(R.id.tx_name);
			holder.mImg_Poto = (CircleImageView) convertView.findViewById(R.id.image_poto);
			holder.mImg_Time = (ImageView) convertView.findViewById(R.id.image_time);
			holder.mLa_Gender = (RelativeLayout) convertView.findViewById(R.id.layout_gender);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MyFriendEntity item = (MyFriendEntity) getItem(position);
		if (Type == 1) {
			holder.mTx_Address.setVisibility(View.GONE);
			holder.mImg_Time.setVisibility(View.VISIBLE);
		} else {
			holder.mImg_Time.setVisibility(View.GONE);
			holder.mTx_Address.setVisibility(View.VISIBLE);
		}

		try {
			String age = GetAgeOrConstellation.getAge(item.getBirthday());
			holder.mTx_Age.setText(age);
			holder.mTx_Name.setText(item.getNickname());
			ImageUtils.loadImage1(context, item.getPicUrl(), holder.mImg_Poto, R.drawable.ic_my_nolog_selector, R.drawable.ic_my_nolog_selector, GlobalVariable.WifiDown);
			if (item.getSex().equals("1")) {
				holder.mLa_Gender.setBackgroundResource(R.drawable.nearby_gender_male);
			} else {
				holder.mLa_Gender.setBackgroundResource(R.drawable.nearby_gender_female);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}    
		return convertView;
	}

	private static class ViewHolder {
		TextView mTx_Address;
		TextView mTx_Age;
		TextView mTx_Name;
		CircleImageView mImg_Poto;
		ImageView mImg_Time;
		RelativeLayout mLa_Gender;
	}
}
