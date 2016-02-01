package com.CivilLife.MyAdapter;

import java.util.List;

import com.CivilLife.Base.BaseApplication;
import com.CivilLife.Base.BaseListAdapter;
import com.CivilLife.Entity.MessageEntity;
import com.CivilLife.Fragment.Tab_Message;
import com.CivilLife.Variable.GlobalVariable;
import com.CivilLife.Widget.CircleImageView;
import com.app.civillife.R;

import Downloadimage.ImageUtils;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 小纸条列表片段适配器
 * 
 * @author Administrator
 * 
 */
public class MessageListViewAdapter extends BaseListAdapter implements OnClickListener{
	protected static final Context Tab_Message = null;
	private Context context;
	private Tab_Message mess;

	public MessageListViewAdapter(BaseApplication application, Context context, List<? extends Object> datas,Tab_Message mess) {
		super(application, context, datas);
		this.context = context;
		this.mess = mess;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = mInflater.inflate(R.layout.message_list_item, null);
			holder = new ViewHolder();
			holder.mImage_pic = (CircleImageView) convertView.findViewById(R.id.image_pic);
			holder.mTx_Name = (TextView) convertView.findViewById(R.id.tx_name);
			holder.mTx_Content = (TextView) convertView.findViewById(R.id.tx_content);
			holder.image_del = (ImageView) convertView.findViewById(R.id.image_del);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MessageEntity item = (MessageEntity) getItem(position);
		holder.mTx_Name.setText(item.getTitle());
		holder.mTx_Content.setText(item.getContent()); 
		holder.image_del.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MessageEntity item = (MessageEntity) getItem(position);
				mess.delete(item.getID());
				// mAppList.remove(position);
				notifyDataSetChanged();
				
			}
		});
		ImageUtils.loadImage1(context, item.getPicUrl(), holder.mImage_pic, R.drawable.ic_my_nolog_selector, R.drawable.ic_my_nolog_selector, GlobalVariable.WifiDown);
		return convertView;
	}

	private static class ViewHolder {
		TextView mTx_Name;
		TextView mTx_Content;
		CircleImageView mImage_pic;
		ImageView image_del;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
