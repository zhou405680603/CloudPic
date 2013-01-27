package com.zzy.cloudpic.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.zzy.cloudpic.R;

public class BorderImageView extends ImageView {

	private float borderWidth = 0;
	private int borderColor = Color.TRANSPARENT;
	private boolean isSelect = false;
	private boolean isClick = false;
	private long lastClickTime = 0;

	public BorderImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BorderImage);
		this.borderWidth = ta.getDimension(R.styleable.BorderImage_borderWidth, 0);
		this.borderColor = ta.getColor(R.styleable.BorderImage_borderColor, Color.WHITE);
		this.isSelect = ta.getBoolean(R.styleable.BorderImage_isSelect, false);
		ta.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		// rec.right--;
		// rec.bottom--;
		Rect rec = canvas.getClipBounds();
		Paint paint = new Paint();
		if (!isSelect) {
			paint.setColor(Color.WHITE);
		}
		else {
			paint.setColor(borderColor);
		}
		paint.setStrokeWidth(borderWidth);
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawRect(rec, paint);

	}

	public float getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(float borderWidth) {
		this.borderWidth = borderWidth;
	}

	public int getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(int borderColor) {
		this.borderColor = borderColor;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

	public boolean isClick() {
		return isClick;
	}

	public void setClick(boolean isClick) {
		this.isClick = isClick;
	}

	public long getLastClickTime() {
		return lastClickTime;
	}

	public void setLastClickTime(long lastClickTime) {
		this.lastClickTime = lastClickTime;
	}

}
