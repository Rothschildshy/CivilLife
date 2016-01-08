package com.CivilLife.Widget;

import android.content.Context;
import android.widget.GridView;
import android.widget.ListView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class GridForScrollView extends GridView {
	// 重新计算GridView高度

	public GridForScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	/**
	 * ��д�÷������ﵽʹ��ӦScrollView��Ч��
	 */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
