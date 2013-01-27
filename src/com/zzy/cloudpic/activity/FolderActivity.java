package com.zzy.cloudpic.activity;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.GridView;

import com.zzy.cloudpic.R;
import com.zzy.cloudpic.bean.CoverInfo;
import com.zzy.cloudpic.ui.theme.SimpleGridViewAdapter;
import com.zzy.cloudpic.util.MediaStoreUtil;

import dalvik.system.VMRuntime;

/**
 * 首页文件夹界面
 * 
 * @author 周志勇
 * 
 */
public class FolderActivity extends IActivity {
	public static List<CoverInfo> coverList = null;

	public static int SHOW_STYLE = 1; // NO_UCD (use private)
	public final static int SIMPLE_GRIDVIEW = 1;
	public final static int LISTVIEW = 2;
	public static int screenWidth = 0;
	public static int screenHeight = 0;
	private final ProgressDialog pod = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int size = 16 * 1024 * 1024;
		VMRuntime.getRuntime().setMinimumHeapSize(size);

		// 获取屏的宽度和高度
		this.getDisplaySize();
		this.scanerImage();

		// 此处将通过Swtich选取首页界面
		// 通过冲数据库中读取到的默认设置
		switch (FolderActivity.SHOW_STYLE) {
		case FolderActivity.SIMPLE_GRIDVIEW:
			setContentView(R.layout.folder_ac_sgv);
			simpleGridViewLoader();
			break;

		default:
			setContentView(R.layout.folder_ac_sgv);
			simpleGridViewLoader();
			break;
		}

	}

	private void getDisplaySize() {
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
	}

	/**
	 * 获取系统表中的数据
	 * 
	 * @return
	 */
	private List<CoverInfo> getCoverList() {
		return coverList = MediaStoreUtil.queryCoverInfo(this,
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStoreUtil.COVER_INFO, null,
				null, null);
	}

	/**
	 * 简单GridView布局模式，显示一张相册图片 默认显示模式
	 */
	public void simpleGridViewLoader() {
		GridView gridView = (GridView) findViewById(R.id.mainactivity_gridview);
		SimpleGridViewAdapter gva = new SimpleGridViewAdapter(this, coverList, gridView);
		gridView.setAdapter(gva);
		gridView.setOnItemClickListener(gva);

		// gridView.setOnItemLongClickListener(gva);
	}

	public void scanerImage() {
		if (MediaStoreUtil.hasThumb(this) && MediaStoreUtil.hasImage(this)) {
			coverList = this.getCoverList();
		}
		else {
			Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
					+ Environment.getExternalStorageDirectory()));
			this.sendBroadcast(intent);
			Log.i("FolderActivity.scanerImae", "MediaStore is null ,start to scaner image");

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
