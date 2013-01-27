package com.zzy.cloudpic.ui;

import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.zzy.cloudpic.R;
import com.zzy.cloudpic.bean.CoverInfo;
import com.zzy.cloudpic.bean.ImageInfo;
import com.zzy.cloudpic.util.ImageUtil;

/**
 * @author 周志勇
 * 
 */
public class CoverGridViewAdapter extends BaseAdapter implements OnItemClickListener,
		OnItemLongClickListener {

	private Context context = null;
	private GridView gridView = null;
	private LayoutInflater inflater = null;
	CoverInfo coverInfo = null;
	HashMap<Integer, BorderImageView> imageMap = new HashMap<Integer, BorderImageView>();
	public int startSelect = -1;// 双击多选开始位
	public int endSelect = -1; // 双击多选结束位
	public final static int DOUBLE_CLICK_TIME = 500; // 双击延迟时间
	public boolean multiSelect = true; // 是否是多选模式

	public CoverGridViewAdapter(Context context, CoverInfo coverInfo, GridView gridView) {

		this.context = context;
		this.gridView = gridView;
		this.coverInfo = coverInfo;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return coverInfo.getSize();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return coverInfo.getImageList().get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		ImageInfo imageInfo = coverInfo.getImageList().get(position);
		// ThumbInfo thumbInfo = coverInfo.getThumbList().get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.cover_ac_gv_item, null);
			holder.image = (BorderImageView) convertView.findViewById(R.id.cover_ac_gv_item);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 将ImageView添加到map
		imageMap.put(position, holder.image);

		Bitmap thumbCache = ImageUtil.loadThumb(context, imageInfo, new BitmapCallback() {

			@Override
			public void loadImage(Bitmap bitmap, String tag) {
				// TODO Auto-generated method stub
				ImageView image = imageMap.get(position);
				image.setImageBitmap(bitmap);
			}
		});

		if (thumbCache != null) {
			holder.image.setImageBitmap(thumbCache);
		}
		return convertView;
	}

	private class ViewHolder {
		public BorderImageView image = null;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub

		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		if (multiSelect) {
			this.multiSelect(view, position);
		}
		else {
			// 非多选状态
		}

	}

	/**
	 * 双击Item执行
	 * 
	 * @param view
	 * @param position
	 */
	public void multiSelect(View view, int position) {
		ViewHolder holder = (ViewHolder) view.getTag();
		long time = System.currentTimeMillis();
		long lastTime = holder.image.getLastClickTime();
		// System.out.println("onItemClick====" + position + "time===" + (time -
		// lastTime));
		if (holder.image.isClick()) { // 如果前面已经单击过一次
			if ((time - lastTime) < DOUBLE_CLICK_TIME) { // 判断两次的点击的时间间隔
				holder.image.setBorderColor(Color.RED); // 符合双击事件,则修改iamge边框颜色
				holder.image.invalidate(); // 重绘
				if (this.startSelect == -1) {
					this.startSelect = position;
				}
				else {
					this.endSelect = position;
					int length = Math.abs((startSelect - endSelect));
					int index = 0;
					BorderImageView biv = null;
					if (startSelect < endSelect) {
						index = startSelect;
					}
					else {
						index = endSelect;
					}
					// 设置双击区间内的图片为选中状态
					for (int i = 0; i <= length; i++) {
						biv = imageMap.get((i + index));
						this.click(biv, time);
					}
					// 重置双击状态
					startSelect = -1;
					endSelect = -1;
				}
			}
			else {
				holder.image.setSelect(false); // 不符合双击事件，则取消上一次的选中
				holder.image.setClick(false);
				holder.image.invalidate();
			}
		}
		else {
			this.click(holder.image, time);// 如果上一次没有点击，则设置选中状态，并记录下本次点击时间
		}

	}

	public void click(BorderImageView biv, long time) {
		biv.setClick(true);
		biv.setSelect(true);
		biv.setLastClickTime(time);
		biv.setBorderColor(Color.BLUE);
		biv.invalidate();
	}
}
