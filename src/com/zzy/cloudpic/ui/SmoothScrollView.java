package com.zzy.cloudpic.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class SmoothScrollView extends ScrollView {

	public SmoothScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void fling(int velocityY) {

		// int speed = (2 * velocityY) / 3;
		super.fling(velocityY);
	}

}
