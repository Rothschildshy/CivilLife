package com.CivilLife.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class LazyViewPager extends CustomViewPager {

	public LazyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private boolean isCanScroll = true;

	public boolean isCanScroll() {
		return isCanScroll;
	}

	public void setCanScroll(boolean isCanScroll) {
		this.isCanScroll = isCanScroll;
	}

	private static final String TAG = "LazyViewPager";
	private static final boolean DEBUG = false;

	private static final boolean USE_CACHE = false;

	private static final int DEFAULT_OFFSCREEN_PAGES = 0;// 默认的加载页面,ViewPager是1个,所以会加载两个Fragment</span>
	private static final int MAX_SETTLE_DURATION = 600; // ms

}