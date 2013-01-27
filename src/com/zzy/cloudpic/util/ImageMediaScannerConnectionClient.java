package com.zzy.cloudpic.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.zzy.cloudpic.R;
import com.zzy.cloudpic.activity.FolderActivity;

public class ImageMediaScannerConnectionClient implements MediaScannerConnectionClient {

	private final ProgressDialog progress;
	private final Context context;
	private final MediaScannerConnection mConnect;

	public ImageMediaScannerConnectionClient(Context context) {
		this.context = context;
		Resources res = context.getResources();
		this.progress = ProgressDialog.show(context, res.getString(R.string.scaner_alert_title),
				res.getString(R.string.scaner_alert_msg));
		mConnect = new MediaScannerConnection(context, this);
		mConnect.connect();
	}

	@Override
	public void onMediaScannerConnected() {
		// TODO Auto-generated method stub
		String path = "file://" + Environment.getExternalStorageDirectory();
		Log.i("ImageMediaScannerConnectionClient", path);

		if (mConnect.isConnected()) {
			mConnect.scanFile(path, "image/jpeg");
		}
	}

	@Override
	public void onScanCompleted(String path, Uri uri) {
		Log.i("ImageMediaScannerConnectionClient", path + "   " + uri.toString());
		mConnect.disconnect();
		FolderActivity.coverList = MediaStoreUtil.queryCoverInfo(context,
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStoreUtil.COVER_INFO, null,
				null, null);
		progress.dismiss();

	}

}
