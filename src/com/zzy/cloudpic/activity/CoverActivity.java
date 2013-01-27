package com.zzy.cloudpic.activity;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.ScrollView;

import com.zzy.cloudpic.R;
import com.zzy.cloudpic.bean.CoverInfo;
import com.zzy.cloudpic.ui.CoverGridViewAdapter;

import dalvik.system.VMRuntime;

public class CoverActivity extends IActivity {

	private final static float TARGET_HEAP_UTILIZATION = 0.75f;
	private final int scrollY = 0;
	private GridView gridView = null;
	private ScrollView scrollView = null;
	private int position;
	private CoverInfo coverInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.cover_ac);
		position = this.getIntent().getIntExtra("coverPos", 0);
		coverInfo = FolderActivity.coverList.get(position);

		scrollView = (ScrollView) findViewById(R.id.cover_ac_scroll);
		this.scrollTo(scrollView, 0, coverInfo.getScrollY());
		gridView = (GridView) findViewById(R.id.coveractivity_gridview);
		CoverGridViewAdapter cgv = new CoverGridViewAdapter(this, coverInfo, gridView);
		gridView.setAdapter(cgv);
		gridView.setOnItemClickListener(cgv);
	}

	/**
	 * 指定scrollView到指定的位置
	 * 
	 * @param sv
	 */
	private void scrollTo(ScrollView sv, final int x, final int y) {
		// scrollTo() 是直接指定滚动条的位置, 但是由于这个动作不是单纯关于 ScrollView 而已, 还要根据 ScrollView
		// 里面包含的View 的实际信息. 所以这动作必须在页面加载完成以后才能执行.
		sv.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				scrollView.scrollTo(x, y);
			}
		});
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		FolderActivity.coverList.get(position).setScrollY(scrollView.getScrollY());
		System.out.println("onPause:scrollY" + scrollView.getScrollY());
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		VMRuntime.getRuntime().gcSoftReferences();
		super.onDestroy();
	}
}
