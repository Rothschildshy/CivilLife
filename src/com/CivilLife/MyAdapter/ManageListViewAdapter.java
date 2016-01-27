package com.CivilLife.MyAdapter;

import java.util.List;
import java.util.concurrent.Executors;

import com.CivilLife.Base.BaseActivity;
import com.CivilLife.Base.BaseApplication;
import com.CivilLife.Base.BaseListAdapter;
import com.CivilLife.Entity.MyManageEntity;
import com.CivilLife.Entity.PublicEntity;
import com.CivilLife.Json.PublicUpJson;
import com.CivilLife.net.Httpurl;
import com.CivilLife.net.RequestTask;
import com.app.civillife.R;
import com.aysy_mytool.Time;
import com.aysy_mytool.ToastUtil;

import Requset_getORpost.RequestListener;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 管理我的文字 添加修改
 * 
 * @author Administrator
 * 
 */
public class ManageListViewAdapter extends BaseListAdapter {
	private Context context;

	public ManageListViewAdapter(BaseApplication application, Context context,
			List<? extends Object> datas) {
		super(application, context, datas);
		this.context = context;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = mInflater.inflate(R.layout.manage_list_item, null);
			holder = new ViewHolder();
			holder.mTx_Time = (TextView) convertView.findViewById(R.id.tx_time);
			holder.mTx_Content = (TextView) convertView
					.findViewById(R.id.tx_content);
			holder.mImg_Compile = (ImageView) convertView
					.findViewById(R.id.image_compile);
			holder.mImg_Del = (ImageView) convertView
					.findViewById(R.id.image_del);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MyManageEntity item = (MyManageEntity) getItem(position);
		holder.mTx_Content.setText(item.getContent());
//		long time = Time.getTime(item.getAddTime());
//		String formartTime = Time.getFormartTime(time);

		holder.mTx_Time.setText(Time.handTime(item.getAddTime(), "yyyy/MM/dd HH:mm:ss"));
		
		holder.mImg_Del.setOnClickListener(new OnClickListener() {
			RequestListener DELlistener=new RequestListener() {
				
				@Override
				public void responseResult(String jsonObject) {
					PublicUpJson publicjson = PublicUpJson.readJsonToSendmsgObject(
							context, jsonObject);
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
						MyManageEntity item=(MyManageEntity) getItem(position);
						new RequestTask(context, DELlistener, false, true,
								"删除中").executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.DelMyManage(item.getID()));
					}
				});
			}
		});
		
		return convertView;
	}
	

	private static class ViewHolder {
		TextView mTx_Time;
		TextView mTx_Content;
		ImageView mImg_Del;
		ImageView mImg_Compile;
	}
}
