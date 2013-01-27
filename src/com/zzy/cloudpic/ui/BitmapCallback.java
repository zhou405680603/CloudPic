package com.zzy.cloudpic.ui;

import android.graphics.Bitmap;

/**
 * 加载图片后的回调窗口
 * 
 * @author 周志勇
 * 
 */
public interface BitmapCallback {
	public void loadImage(Bitmap bitmap, String tag);
}
