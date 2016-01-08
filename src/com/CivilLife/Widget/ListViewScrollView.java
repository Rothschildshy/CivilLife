package com.CivilLife.Widget;

import android.content.Context;
import android.widget.ListView;

import android.util.AttributeSet;

public class ListViewScrollView extends ListView {
	// 重新计算GridView高度

	public ListViewScrollView(Context context, AttributeSet attrs) {
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
