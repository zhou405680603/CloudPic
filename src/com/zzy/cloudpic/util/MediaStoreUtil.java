package com.zzy.cloudpic.util;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.zzy.cloudpic.bean.CoverInfo;
import com.zzy.cloudpic.bean.ImageInfo;
import com.zzy.cloudpic.bean.ThumbInfo;

public class MediaStoreUtil {

	public final static String[] IMAGE_INFO = { MediaStore.Images.Media._ID,
			MediaStore.Images.Media.ORIENTATION, MediaStore.Images.Media.DATE_ADDED,
			MediaStore.Images.Media.DATE_MODIFIED, MediaStore.Images.Media.DATE_TAKEN,
			MediaStore.Images.Media.LATITUDE, MediaStore.Images.Media.LONGITUDE,
			MediaStore.Images.Media.DATA, MediaStore.Images.Media.MIME_TYPE,
			MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
			MediaStore.Images.Media.TITLE };

	public final static String[] THUMB_INFO = { MediaStore.Images.Thumbnails._ID,
			MediaStore.Images.Thumbnails.DATA, MediaStore.Images.Thumbnails.IMAGE_ID,
			MediaStore.Images.Thumbnails.KIND, MediaStore.Images.Thumbnails.WIDTH,
			MediaStore.Images.Thumbnails.HEIGHT };

	// 此处在需要查询的属性前面拼接上 distinct去除相同项
	public final static String[] COVER_INFO = { "distinct  "
			+ MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

	public final static Uri IMAGE_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	public final static Uri THUMB_URI = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;

	/**
	 * 查询图片信息
	 * 
	 * @param context
	 *            上下文信息
	 * @param uri
	 *            数据库uri
	 * @param projection
	 *            需要查询的列名
	 * @param selection
	 *            查询条件
	 * @param selectionArgs
	 *            占位符信息
	 * @param sortOrder
	 *            排序
	 * @return
	 */
	public static List<ImageInfo> queryImageInfo(Context context, Uri uri, String[] projection,
			String selection, String[] selectionArgs, String sortOrder) {
		// 用来保存ImageInfo信息，以image_id为key
		List<ImageInfo> imageList = new ArrayList<ImageInfo>();
		ImageInfo imageInfo = null;

		ContentResolver cr = context.getContentResolver();
		Cursor cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
		while (cursor.moveToNext()) {
			imageInfo = new ImageInfo();
			imageInfo.setId(cursor.getInt(0));
			imageInfo.setOrientation(cursor.getInt(1));
			imageInfo.setDate_add(cursor.getLong(2));
			imageInfo.setDate_modified(cursor.getLong(3));
			imageInfo.setDate_taken(cursor.getLong(4));
			imageInfo.setLatitude(cursor.getDouble(5));
			imageInfo.setLongitude(cursor.getDouble(6));
			imageInfo.setData(cursor.getString(7));
			imageInfo.setMime_type(cursor.getString(8));
			imageInfo.setDisplay_name(cursor.getString(9));
			imageInfo.setParent_name(cursor.getString(10));
			imageInfo.setTitle(cursor.getString(11));

			imageList.add(imageInfo);
		}
		cursor.close();
		return imageList;
	}

	/**
	 * 查询缩略图信息
	 * 
	 * @param context
	 *            上下文信息
	 * @param uri
	 *            数据库uri
	 * @param projection
	 *            需要查询的列名
	 * @param selection
	 *            查询条件
	 * @param selectionArgs
	 *            占位符信息
	 * @param sortOrder
	 *            排序
	 * @return
	 */
	public static List<ThumbInfo> queryThumbInfo(Context context, Uri uri, String[] projection,
			String selection, String[] selectionArgs, String sortOrder) {
		// Map<Integer, ThumbInfo> map = new HashMap<Integer, ThumbInfo>();
		List<ThumbInfo> thumbMap = new ArrayList<ThumbInfo>();
		ThumbInfo thumbInfo = null;
		ContentResolver cr = context.getContentResolver();
		Cursor cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
		while (cursor.moveToNext()) {
			thumbInfo = new ThumbInfo();
			thumbInfo.setId(cursor.getInt(0));
			thumbInfo.setData(cursor.getString(1));
			thumbInfo.setImage_id(cursor.getInt(2));
			thumbInfo.setKind(cursor.getInt(3));
			thumbInfo.setWidth(cursor.getInt(4));
			thumbInfo.setHeight(cursor.getInt(5));

			thumbMap.add(thumbInfo);
		}
		cursor.close();
		return thumbMap;
	}

	/**
	 * 在Android中系统提供了两张表来存储SD卡中的图片信息，分别是images(图片的信息)和thumbnails(图片的缩略图信息)
	 * 分别使用MediaStore.Image. Media和MediaStore.Image.Thumbnails来操作，以image_id为外键
	 * 。这里在images表有一个字段
	 * bucket_dispaly_name是图片所在的文件夹，也就是图片所在的相册。CoverInfo封装了属于同一个相册的所有图片的信息
	 * ，包括缩略图。这里查询就需要使用到distinct取出相同项和外键查询thumbnails表
	 * 
	 * @param context
	 *            上下文信息
	 * @param uri
	 *            数据库uri
	 * @param projection
	 *            需要查询的列名
	 * @param selection
	 *            查询条件
	 * @param selectionArgs
	 *            占位符信息
	 * @param sortOrder
	 *            排序
	 * @return
	 */
	public static List<CoverInfo> queryCoverInfo(Context context, Uri uri, String[] projection,
			String selection, String[] selectionArgs, String sortOrder) {
		List<CoverInfo> list = new ArrayList<CoverInfo>();
		CoverInfo coverInfo = null;
		ContentResolver cr = context.getContentResolver();
		// 首先在image表中查询出相册，因为可能有的很多图片在同一个文件夹下，也就是在同一个相册，此处就需要使用distinct去除相同项
		Cursor cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
		while (cursor.moveToNext()) {
			coverInfo = new CoverInfo(cursor.getString(0));
			// select为sql语句查询的where部分，此处为外键查询的关键，整个的意思就是:
			// where image_id in (select _id from images
			// where_bucket_display_name='covername')
			String where = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + "='" + coverInfo.getName()
					+ "'";

			coverInfo.setImageList(MediaStoreUtil.queryImageInfo(context, MediaStoreUtil.IMAGE_URI,
					MediaStoreUtil.IMAGE_INFO, where, null, null));

			where = MediaStore.Images.Thumbnails.IMAGE_ID + " in (select _id from images where "
					+ MediaStore.Images.Media.BUCKET_DISPLAY_NAME + "='" + coverInfo.getName()
					+ "')";

			coverInfo.setThumbList(MediaStoreUtil.queryThumbInfo(context,
					MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, MediaStoreUtil.THUMB_INFO,
					where, null, null));

			list.add(coverInfo);
		}
		cursor.close();
		return list;
	}

	/**
	 * 判断是否包含缩略图
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasThumb(Context context) {
		ContentResolver cr = context.getContentResolver();
		Cursor cursor = cr.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Images.Thumbnails._ID }, null, null, null);
		if (cursor == null) {
			return false;
		}
		return cursor.moveToNext();
	}

	/**
	 * 判断是否包含缩略图
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasImage(Context context) {
		ContentResolver cr = context.getContentResolver();
		Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Images.Media._ID }, null, null, null);
		if (cursor == null) {
			return false;
		}
		return cursor.moveToNext();
	}

}
