package com.zzy.cloudpic.ui.theme;

import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zzy.cloudpic.R;
import com.zzy.cloudpic.activity.CoverActivity;
import com.zzy.cloudpic.bean.CoverInfo;
import com.zzy.cloudpic.bean.ImageInfo;
import com.zzy.cloudpic.ui.BitmapCallback;
import com.zzy.cloudpic.util.ImageUtil;

public class SimpleGridViewAdapter extends BaseAdapter implements OnItemClickListener,
		OnItemLongClickListener {

	// 将xml转化为布局
	private LayoutInflater mInflater = null;
	List<CoverInfo> coverList = null;
	private final Context context;
	private GridView gridView = null;

	public SimpleGridViewAdapter(Context context, List<CoverInfo> coverList, GridView gridView) {
		mInflater = LayoutInflater.from(context);
		this.gridView = gridView;
		this.context = context;
		this.coverList = coverList;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return coverList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return coverList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		CoverInfo coverInfo = coverList.get(position);
		ImageInfo imageInfo = coverInfo.getImageList().get(0);
		// ThumbInfo thumbInfo = coverInfo.getThumbList().get(0);
		CoverFolderHolder holder = null;
		if (convertView == null) {
			holder = new CoverFolderHolder();
			convertView = mInflater.inflate(R.layout.folder_ac_sgv_item, null);
			holder.item = (ImageView) convertView.findViewById(R.id.gridViewItemImage);
			holder.tx = (TextView) convertView.findViewById(R.id.title);
			holder.ll = (LinearLayout) convertView.findViewById(R.id.itme_layout);
			convertView.setTag(holder);
		}
		else {
			holder = (CoverFolderHolder) convertView.getTag();
		}
		holder.item.setTag(imageInfo.getData());

		// 获取item的图片
		Bitmap thumbCache = ImageUtil.loadThumb(context, imageInfo, new BitmapCallback() {

			@Override
			public void loadImage(Bitmap bitmap, String tag) {
				// 根据tag值获取相应的ImageView
				ImageView item = (ImageView) gridView.findViewWithTag(tag);
				if (item != null)
					item.setImageBitmap(bitmap);
			}
		});
		// 当从缓存中获取到了图片不为空则为Item设置图片
		if (thumbCache != null) {
			holder.item.setImageBitmap(thumbCache);
		}

		holder.tx.setText(coverInfo.getName() + "(" + coverInfo.getSize() + ")");

		return convertView;
	}

	private class CoverFolderHolder {
		ImageView item = null;
		TextView tx = null;
		LinearLayout ll = null;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		// new Thread(new
		// AyncLoadImage(coverList.get(position).getThumbList())).start();
		Intent intent = new Intent(context, CoverActivity.class);
		intent.putExtra("coverPos", position);
		context.startActivity(intent);

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(context);
		CoverInfo coverInfo = coverList.get(position);
		builder.setTitle("详细信息");
		builder.setMessage("相册名：" + coverInfo.getName() + "\n大小:" + coverInfo.getSize());
		builder.setPositiveButton("确认", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
		Toast.makeText(SimpleGridViewAdapter.this.context, "long", 1000).show();
		return true;
	}

}
