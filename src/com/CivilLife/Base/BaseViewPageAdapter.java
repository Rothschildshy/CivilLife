package com.CivilLife.Base;

import java.util.ArrayList;
import java.util.List;

import com.CivilLife.Fragment.MyInFo_Fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class BaseViewPageAdapter extends android.support.v4.app.FragmentPagerAdapter {

	protected Context mContext;
	protected LayoutInflater mInflater;
	protected List<? extends Object> mDatas = new ArrayList<Object>();

	public BaseViewPageAdapter(FragmentManager fm, Context context, List<? extends Object> datas) {
		super(fm);
		mContext = context;
		mInflater = LayoutInflater.from(context);
		if (datas != null) {
			mDatas = datas;
		}
	}

	public List<? extends Object> getmDatas() {
		return mDatas;
	}

	public void setmDatas(List<? extends Object> mDatas) {
		this.mDatas = mDatas;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public android.support.v4.app.Fragment getItem(int position) {
		return null;
	}

	public List<? extends Object> getDatas() {
		return mDatas;
	}
}
