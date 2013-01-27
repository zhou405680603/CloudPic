package com.zzy.cloudpic.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.zzy.cloudpic.bean.ImageInfo;
import com.zzy.cloudpic.ui.BitmapCallback;

/**
 * Image操作工具类，提供对缩略图和原始图片的加载，生成和缓存
 * 
 * @author 周志勇
 * 
 */
public class ImageUtil {

	private static final int OPTIONS_NONE = 0x0;
	private static final int OPTIONS_SCALE_UP = 0x1;
	public static final int OPTIONS_RECYCLE_INPUT = 0x2;
	public Context context;

	// 原始Image图片缓存
	public static HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();
	// 缩略图缓存
	public static HashMap<String, SoftReference<Bitmap>> thumbCache = new HashMap<String, SoftReference<Bitmap>>();
	// 线程池，用于加载图片与缩略图
	private final static ExecutorService executorService = Executors.newFixedThreadPool(5);

	/**
	 * 获取Drawable对象的缩略图
	 * 
	 * @param context
	 *            应用程序上下文
	 * @param url
	 *            缩略图对于的sd卡路径
	 * @param callback
	 *            回调接口
	 * @return
	 */
	public static Bitmap loadThumb(final Context context, final ImageInfo thumbInfo,
			final BitmapCallback callback) {
		final String url = thumbInfo.getData();
		// 首先查找缓存 如果缓存存在直接返回缓存
		if (thumbCache.containsKey(url)) {
			SoftReference<Bitmap> softReference = thumbCache.get(url);
			if (softReference.get() != null) {
				Bitmap bitmap = softReference.get();
				return bitmap;
			}
		}

		// handler对象，用于线程加载完图片后更新主线程UI
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				callback.loadImage((Bitmap) message.obj, url);
			}
		};

		executorService.submit(new Runnable() {

			@Override
			public void run() {
				long start = System.currentTimeMillis();
				Bitmap bitmap = getThumbBitmap(thumbInfo, context);
				Log.e("time", "time" + (System.currentTimeMillis() - start));
				thumbCache.put(url, new SoftReference<Bitmap>(bitmap));
				Message message = handler.obtainMessage(0, bitmap);
				handler.sendMessage(message);
			}
		});

		return null;
	}

	public static Bitmap getThumbBitmap(ImageInfo thumbInfo, Context context) {

		BitmapFactory.Options ops = new Options();
		String path = thumbInfo.getData();
		ops.inJustDecodeBounds = true;
		Bitmap bm = BitmapFactory.decodeFile(path, ops);

		int bitmapWidth = ops.outWidth;
		int bitmapHeight = ops.outHeight;
		int be = 1;
		if (bitmapHeight > bitmapWidth) {
			be = bitmapHeight / 150;
		}
		else {
			be = bitmapWidth / 150;
		}

		if (be <= 0)
			be = 1;
		ops.inSampleSize = be;
		ops.inJustDecodeBounds = false;
		ops.inPreferredConfig = Bitmap.Config.RGB_565;
		ops.inPurgeable = true;
		ops.inInputShareable = true;
		//
		// try {
		// InputStream is = new FileInputStream(new File(thumbInfo.getData()));
		// bm = BitmapFactory.decodeStream(is, null, ops);
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// bm = ThumbnailUtils.extractThumbnail(bm, 154, 154);

		// Bitmap bm =
		// MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(),
		// thumbInfo.getId(), 1, null);
		//
		// return bm;
		try {
			InputStream is = new FileInputStream(new File(thumbInfo.getData()));
			bm = BitmapFactory.decodeStream(is, null, ops);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ImageUtil.extractThumbnail(bm, 120, 120);
	}

	/**
	 * 根据需要的大小截取图片
	 * 
	 * @param source
	 *            源文件
	 * @param width
	 *            目标宽度
	 * @param height
	 *            目标高度
	 * @return 更改后的图片
	 */
	public static Bitmap extractThumbnail(Bitmap source, int width, int height) {
		return extractThumbnail(source, width, height, OPTIONS_NONE);
	}

	/**
	 * 根据需要的大小截取图片
	 * 
	 * @param source
	 *            源文件
	 * @param width
	 *            目标宽度
	 * @param height
	 *            目标高度
	 * @param options
	 *            选项
	 * @return
	 */
	public static Bitmap extractThumbnail(Bitmap source, int width, int height, int options) {
		if (source == null) {
			return null;
		}

		float scale;
		if (source.getWidth() < source.getHeight()) {
			scale = width / (float) source.getWidth();
		}
		else {
			scale = height / (float) source.getHeight();
		}
		Matrix matrix = new Matrix();
		matrix.setScale(scale, scale);
		Bitmap thumbnail = transform(matrix, source, width, height, OPTIONS_SCALE_UP | options);
		return thumbnail;
	}

	private static Bitmap transform(Matrix scaler, Bitmap source, int targetWidth,
			int targetHeight, int options) {
		boolean scaleUp = (options & OPTIONS_SCALE_UP) != 0;
		boolean recycle = (options & OPTIONS_RECYCLE_INPUT) != 0;

		int deltaX = source.getWidth() - targetWidth;
		int deltaY = source.getHeight() - targetHeight;
		if (!scaleUp && (deltaX < 0 || deltaY < 0)) {
			/**
			 * In this case the bitmap is smaller, at least in one dimension,
			 * than the target. Transform it by placing as much of the image as
			 * possible into the target and leaving the top/bottom or left/right
			 * (or both) black.
			 */
			Bitmap b2 = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(b2);

			int deltaXHalf = Math.max(0, deltaX / 2);
			int deltaYHalf = Math.max(0, deltaY / 2);
			Rect src = new Rect(deltaXHalf, deltaYHalf, deltaXHalf
					+ Math.min(targetWidth, source.getWidth()), deltaYHalf
					+ Math.min(targetHeight, source.getHeight()));
			int dstX = (targetWidth - src.width()) / 2;
			int dstY = (targetHeight - src.height()) / 2;
			Rect dst = new Rect(dstX, dstY, targetWidth - dstX, targetHeight - dstY);
			c.drawBitmap(source, src, dst, null);
			if (recycle) {
				source.recycle();
			}
			return b2;
		}
		float bitmapWidthF = source.getWidth();
		float bitmapHeightF = source.getHeight();

		float bitmapAspect = bitmapWidthF / bitmapHeightF;
		float viewAspect = (float) targetWidth / targetHeight;

		if (bitmapAspect > viewAspect) {
			float scale = targetHeight / bitmapHeightF;
			if (scale < .9F || scale > 1F) {
				scaler.setScale(scale, scale);
			}
			else {
				scaler = null;
			}
		}
		else {
			float scale = targetWidth / bitmapWidthF;
			if (scale < .9F || scale > 1F) {
				scaler.setScale(scale, scale);
			}
			else {
				scaler = null;
			}
		}

		Bitmap b1;
		if (scaler != null) {
			// this is used for minithumb and crop, so we want to filter here.
			b1 = Bitmap.createBitmap(source, 0, 0, (int) bitmapWidthF, (int) bitmapHeightF, scaler,
					true);
			recycle = true;
		}
		else {
			b1 = source;
		}

		if (recycle && b1 != source) {
			source.recycle();
		}

		int dx1 = Math.max(0, b1.getWidth() - targetWidth);
		int dy1 = Math.max(0, b1.getHeight() - targetHeight);

		Bitmap b2 = Bitmap.createBitmap(b1, dx1 / 2, dy1 / 2, targetWidth, targetHeight);

		if (b2 != b1) {
			if (recycle || b1 != source) {
				b1.recycle();
			}
		}

		return b2;
	}

}
