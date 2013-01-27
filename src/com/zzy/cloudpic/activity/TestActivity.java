package com.zzy.cloudpic.activity;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.zzy.cloudpic.R;
import com.zzy.cloudpic.bean.CoverInfo;

public class TestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.test1);
		CoverInfo coverInfo = (CoverInfo) this.getIntent().getBundleExtra("bundle")
				.getSerializable("coverName");
		ImageView image = (ImageView) findViewById(R.id.image);
		ImageView image1 = (ImageView) findViewById(R.id.image1);

		image1.setImageBitmap(BitmapFactory.decodeFile(coverInfo.getThumbList().get(0).getData()));
	}
}
