package com.CivilLife.MyAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.CivilLife.Base.BaseApplication;
import com.CivilLife.Base.BaseListAdapter;
import com.CivilLife.Entity.HomeonetitleEntity;
import com.CivilLife.Fragment.Tab_Homepage;
import com.app.civillife.R;

/**
 * 首页 栏目横向listview片段适配器
 * 
 * @author Administrator
 * 
 */
public class HomehorizontallvAdapter extends BaseListAdapter {
	View convertView1;
	public static int selseortitem = 0;
	private Context context;

	public HomehorizontallvAdapter(BaseApplication application, Context context, List<? extends Object> datas) {
		super(application, context, datas);
		this.context = context;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView1 = convertView;
		ViewHolder holder = null;
		if (convertView1 == null || convertView1.getTag() == null) {
			convertView1 = mInflater.inflate(R.layout.hometitlelayout, null);
			holder = new ViewHolder();
			holder.tv_hometitlename = (TextView) convertView1.findViewById(R.id.tv_hometitlename);
			holder.iv_seleortsubscript = (ImageView) convertView1.findViewById(R.id.iv_seleortsubscript);
			convertView1.setTag(holder);
		} else {
			holder = (ViewHolder) convertView1.getTag();
		}
		HomeonetitleEntity item = (HomeonetitleEntity) getItem(position);
		holder.tv_hometitlename.setText(item.getArticleClassName());
		if (selseortitem == position) {// 状态初始化
			holder.tv_hometitlename.setSelected(true);
			holder.iv_seleortsubscript.setSelected(true);
		} else {
			holder.tv_hometitlename.setSelected(false);
			holder.iv_seleortsubscript.setSelected(false);
		}
		return convertView1;
	}

	private static class ViewHolder {
		TextView tv_hometitlename;
		ImageView iv_seleortsubscript;
	}

}
