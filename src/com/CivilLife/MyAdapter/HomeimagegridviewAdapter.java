package com.CivilLife.MyAdapter;

import java.util.List;

import com.CivilLife.Base.BaseApplication;
import com.CivilLife.Base.BaseListAdapter;
import com.CivilLife.Variable.GlobalVariable;
import com.app.civillife.R;
import com.aysy_mytool.ScreenUtils;

import Downloadimage.ImageUtils;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 首页片段适配器
 * 
 * @author Administrator
 * 
 */
public class HomeimagegridviewAdapter extends BaseListAdapter {
	private Context context;
	private int screenHeight;
	private int screenWitdh;

	public HomeimagegridviewAdapter(BaseApplication application, Context context, List<? extends Object> datas) {
		super(application, context, datas);
		this.context = context;
		screenHeight = ScreenUtils.getScreenHeight(context)/5;
		screenWitdh = ScreenUtils.getScreenWitdh(context)/5;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.homeimagegridviewitemlayout, null);
			holder.mIm_Pic = (ImageView) convertView.findViewById(R.id.image_pic);
			convertView.setTag(holder);  
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String item = (String) getItem(position);
		ImageUtils.loadImage(context, item, holder.mIm_Pic, GlobalVariable.WifiDown);
		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) holder.mIm_Pic.getLayoutParams(); 
		linearParams.height=screenWitdh;
		linearParams.width=screenWitdh;
		holder.mIm_Pic.setLayoutParams(linearParams);
		return convertView;  
	}

	private static class ViewHolder {
		ImageView mIm_Pic;// 图片
	}

}
