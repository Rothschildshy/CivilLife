package com.CivilLife.Fragment;

import com.CivilLife.Base.BaseFragment;
import com.app.civillife.ArticleListActivity;
import com.app.civillife.BookStoreActivity;
import com.app.civillife.InfoHomepageActivity;
import com.app.civillife.R;
import com.app.civillife.ToolActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

/***
 * 发现片段
 * 
 * @author Administrator
 * 
 */
public class Tab_Discover extends BaseFragment {
	private View layout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (layout == null) {
			layout = inflater.inflate(R.layout.tab_discover, container, false);
			initViews();
			initEvents();
			init();
		}
		ViewGroup parent = (ViewGroup) layout.getParent();
		if (parent != null) {
			parent.removeView(layout);
		}
		return layout;
	}

	@Override
	protected void initViews() {

	}

	@Override
	protected void initEvents() {
		layout.findViewById(R.id.layout_overman).setOnClickListener(this);
		layout.findViewById(R.id.layout_tool).setOnClickListener(this);
		layout.findViewById(R.id.layout_master_community).setOnClickListener(
				this);
		layout.findViewById(R.id.layout_nearby).setOnClickListener(this);
		layout.findViewById(R.id.layout_fellow).setOnClickListener(this);
		layout.findViewById(R.id.layout_ellow_community).setOnClickListener(
				this);
		layout.findViewById(R.id.layout_city_peer).setOnClickListener(this);
		layout.findViewById(R.id.layout_city_community)
				.setOnClickListener(this);
		layout.findViewById(R.id.layout_bookstore).setOnClickListener(this);
	}

	@Override
	protected void init() {

	}

	@Override
	public void onClick(View v) {
		Bundle bundle = new Bundle();
		switch (v.getId()) {
		case R.id.layout_overman:// 拜师傅
			if (tologin()) {
				return;
			}
			bundle.putInt("type", 1);
			startActivity(InfoHomepageActivity.class, bundle);
			break;
		case R.id.layout_master_community:// 师徒社区
			if (tologin()) {
				return;
			}
			bundle.putInt("type", 1);
			startActivity(ArticleListActivity.class, bundle);
			break;
		case R.id.layout_nearby:// 附近
			if (tologin()) {
				return;
			}
			bundle.putInt("type", 2);
			startActivity(InfoHomepageActivity.class, bundle);
			break;
		case R.id.layout_fellow:// 寻找同城老乡
			if (tologin()) {
				return;
			}
			bundle.putInt("type", 3);
			startActivity(InfoHomepageActivity.class, bundle);
			break;
		case R.id.layout_ellow_community:// 老乡社区
			if (tologin()) {
				return;
			}
			bundle.putInt("type", 2);
			startActivity(ArticleListActivity.class, bundle);
			break;
		case R.id.layout_city_peer:// 找同城同行
			if (tologin()) {
				return;
			}
			bundle.putInt("type", 4);
			startActivity(InfoHomepageActivity.class, bundle);
			break;
		case R.id.layout_city_community:// 同城社区
			if (tologin()) {
				return;
			}
			bundle.putInt("type", 3);
			startActivity(ArticleListActivity.class, bundle);
			break;
		case R.id.layout_bookstore:// 建筑书城  
			// if (tologin()) {
			// return;
			// }
			startActivity(BookStoreActivity.class);
			break;  
		case R.id.layout_tool:// 工具
			// if (tologin()) {
			// return;
			// }
			startActivity(ToolActivity.class);
			break;
		default:
			break;
		}
	}

}
