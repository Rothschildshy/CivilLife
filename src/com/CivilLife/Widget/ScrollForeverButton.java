package com.CivilLife.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;

/**
 * 走马灯的按钮
 * 
 * @author Administrator
 * 
 */
public class ScrollForeverButton extends Button {

	public ScrollForeverButton(Context context) {
		super(context);
	}

	public ScrollForeverButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScrollForeverButton(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle); 
	}

	@Override
	public boolean isFocused() {
		return true;
	}

}
