package com.CivilLife.Widget;

import com.umeng.socialize.utils.Log;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 重写，添加了判定滑动方向的方法
 * 
 * @author ysy
 * 
 */
public class CustomViewPager extends ViewPager {
	private boolean left = false;
	private boolean right = false;
	private boolean isScrolling = false;
	private int lastValue = -1;
	private ChangeViewCallback changeViewCallback = null;

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CustomViewPager(Context context) {
		super(context);
		init();
	}

	private void init() {
		setOnPageChangeListener(listener);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isright || isleft) {
			return super.onTouchEvent(event);
		}
		return false;
	}

	private float x1;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		switch (event.getAction()) { // MotionEvent 整体事件
		case MotionEvent.ACTION_DOWN:
			x1 = event.getX();
			return super.onInterceptTouchEvent(event);
		case MotionEvent.ACTION_MOVE:// 滑动方向判定
			float x2 = event.getX();
			if (isright && x2 - x1 > 0) {
				return super.onInterceptTouchEvent(event);
			} else if (isleft && x2 - x1 < 0) {
				return super.onInterceptTouchEvent(event);
			} else {
				return false;
			}
		default:
			return false;
		}

	}

	public OnPageChangeListener listener = new OnPageChangeListener() {
		@Override
		public void onPageScrollStateChanged(int arg0) {
			if (arg0 == 1) {
				isScrolling = true;
			} else {
				isScrolling = false;
			}
			if (arg0 == 2) {

				if (changeViewCallback != null) {
					changeViewCallback.changeView(left, right);
				}
				right = left = false;
			}

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			if (isScrolling) {
				if (lastValue > arg2) {
					// 递减，向右侧滑动
					right = true;
					left = false;
				} else if (lastValue < arg2) {
					// 递减，向右侧滑动
					right = false;
					left = true;
				} else if (lastValue == arg2) {
					right = left = false;
				}
			}
			lastValue = arg2;
		}

		@Override
		public void onPageSelected(int arg0) {
			if (changeViewCallback != null) {
				changeViewCallback.getCurrentPageIndex(arg0);
			}
		}
	};
	private boolean isright = true;
	private boolean isleft = true;

	/**
	 * 设置是否左右滑动
	 */
	public void setMove(boolean isleft, boolean isright) {
		this.isright = isright;
		this.isleft = isleft;
	}

	/**
	 * 得到是否向右侧滑动 true 为右滑动
	 */
	public boolean getMoveRight() {
		return right;
	}

	/**
	 * 得到是否向左侧滑动 true 为左做滑动
	 */
	public boolean getMoveLeft() {
		return left;
	}

	/**
	 * 滑动状态监听回调
	 * 
	 */
	public interface ChangeViewCallback {

		public void changeView(boolean left, boolean right);

		public void getCurrentPageIndex(int index);
	}

	public void setChangeViewCallback(ChangeViewCallback callback) {
		changeViewCallback = callback;
	}
}