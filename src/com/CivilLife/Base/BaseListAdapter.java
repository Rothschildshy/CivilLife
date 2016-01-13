package com.CivilLife.Base;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class BaseListAdapter extends BaseAdapter {

	protected BaseApplication mApplication;
	protected Context mContext;
	protected LayoutInflater mInflater;
	protected List<? extends Object> mDatas = new ArrayList<Object>();

	public BaseListAdapter(BaseApplication application, Context context, List<? extends Object> datas) {
		mApplication = application;
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
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

	public List<? extends Object> getDatas() {
		return mDatas;
	}

}
