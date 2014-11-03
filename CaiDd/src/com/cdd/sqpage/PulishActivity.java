package com.cdd.sqpage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.cdd.R;
import com.cdd.base.BaseActivity;
import com.cdd.base.MainActivity;
import com.cdd.fragment.BaseFragmentListener;
import com.cdd.fragment.SignSuccessFragment;
import com.cdd.fragment.TakePhotoSelectFragment;
import com.cdd.login.LoginActivity;
import com.cdd.operater.UploadForMultiOp;
import com.cdd.operater.UploadForMultiOp.OnUploadListener;
import com.cdd.util.BitmapUtil;
import com.cdd.util.CddConfig;
import com.cdd.util.CddRequestCode;

public class PulishActivity extends BaseActivity implements OnClickListener {

	private TextView nimingSend;

	private boolean isNimingS = false;

	private final String IMAGE_TYPE = "image/*";

	private String tempPath = "";

	private ArrayList<File> picList = new ArrayList<File>();

	private ArrayList<Drawable> drawableList = new ArrayList<Drawable>();

	private GridView picGrid;

	private PulishImgAdapter adapter;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.publish_activity);
		initView();
	}

	private void initView() {
		nimingSend = (TextView) findViewById(R.id.niming_send);
		nimingSend.setOnClickListener(this);
		picGrid = (GridView) findViewById(R.id.pic_grid);
		findViewById(R.id.insert_pic).setOnClickListener(this);
		findViewById(R.id.insert_bq).setOnClickListener(this);
		findViewById(R.id.pulish_cancel).setOnClickListener(this);
		findViewById(R.id.pulish_ok).setOnClickListener(this);
		setNimingOrNot(isNimingS);
	}

	private void initGridAdapter(ArrayList<Drawable> list) {
		if (adapter == null) {
			adapter = new PulishImgAdapter(context);
			adapter.addData(list);
			picGrid.setAdapter(adapter);
		} else {
			adapter.clear();
			adapter.addData(list);
			adapter.notifyDataSetChanged();
		}
	}

	private void setNimingOrNot(boolean isNiming) {
		Drawable drawable1 = getResources().getDrawable(R.drawable.nm_check);
		Drawable drawable2 = getResources().getDrawable(R.drawable.nm_normal);
		drawable1.setBounds(0, 0, drawable1.getMinimumWidth(),
				drawable1.getMinimumHeight());
		drawable2.setBounds(0, 0, drawable2.getMinimumWidth(),
				drawable2.getMinimumHeight());
		if (isNiming) {
			nimingSend.setCompoundDrawables(drawable1, null, null, null);
		} else {
			nimingSend.setCompoundDrawables(drawable2, null, null, null);
		}

	}

	public void displayFragment(boolean isOpen, String tag, Bundle bundle,
			BaseFragmentListener listener) {
		if (isOpen) {
			showFragment(tag, -1, createFragment(tag, bundle, listener));
		} else {
			closeFragment(tag);
		}
	}

	public DialogFragment createFragment(final String tag, Bundle b,
			BaseFragmentListener listener) {
		if (tag.equals("pick_photo")) {
			TakePhotoSelectFragment takePhotoF = new TakePhotoSelectFragment(
					context, b);
			return takePhotoF;
		}
		return null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK
				&& requestCode == CddRequestCode.ACTION_PICK_GALLERY) {
			Uri originalUri = data.getData();
			ContentResolver resolver = getContentResolver();
			Cursor cursor = resolver.query(originalUri, null, null, null, null);
			if (cursor.moveToFirst()) {
				String path = cursor.getString(cursor.getColumnIndex("_data"));
				workForImage(path);
			}
		} else if (resultCode == RESULT_OK
				&& requestCode == CddRequestCode.ACTION_PICK_CAMERA) {
			if (!TextUtils.isEmpty(tempPath)) {
				File file = new File(tempPath);
				picList.add(file);
				workImageForCamera(tempPath);
				tempPath = "";
			}
		}
	}

	private void workImageForCamera(final String path) {
		showLoading(true);
		new Thread(new Runnable() {

			@Override
			public void run() {
				BitmapUtil.onCompressForUpload(path, 894, 595);
				BitmapFactory.Options options = new BitmapFactory.Options();
				Bitmap bitmap = BitmapUtil.getBitmapByPath(path, options, 894,
						595);
				if (bitmap != null) {
					showLoading(true);
					// uploadImage(CddConfig.IMAGE_CACHE_FILE + "upload.jpg");
					BitmapDrawable portraitD = new BitmapDrawable(bitmap);
					if (portraitD != null) {
						// showImage(portraitD);
						showLoading(false);
						drawableList.add(portraitD);
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								initGridAdapter(drawableList);
							}
						});
					}
				} else {
					showLoading(false);
				}

			}
		}).start();
	}

	private void workForImage(final String pathN) {
		showLoading(true);
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (pathN != null) {
					OutputStream out = null;
					byte[] upImageData = BitmapUtil.onCompress_(pathN,
							"comment");
					String dir = CddConfig.IMAGE_CACHE_FILE;
					File dir_ = new File(dir);
					if (!dir_.exists() || !dir_.isDirectory()) {
						dir_.mkdirs();
					}
					String picPath = System.currentTimeMillis() + ".jpg";
					File myUploadFile = new File(dir, picPath);
					picList.add(myUploadFile);
					try {
						out = new FileOutputStream(myUploadFile);
						out.write(upImageData);
					} catch (Exception e) {
						e.printStackTrace();
						showLoading(false);
					} finally {
						if (out != null) {
							try {
								out.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					BitmapFactory.Options options = new BitmapFactory.Options();
					Bitmap bit = BitmapUtil.getBitmapByPath(
							myUploadFile.getAbsolutePath(), options, 894, 595);
					BitmapDrawable portraitD = new BitmapDrawable(bit);
					if (portraitD != null) {
						// showImage(portraitD);
						showLoading(false);
						drawableList.add(portraitD);
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								initGridAdapter(drawableList);
							}
						});
					}
				}

			}
		}).start();
	}

	private void showTipNoteDialog(final String msg) {
		((BaseActivity) context).handler.post(new Runnable() {

			@Override
			public void run() {
				Builder b = new Builder(context);
				b.setMessage(msg);
				b.setNegativeButton(getString(R.string.action_cancel),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				b.setPositiveButton(R.string.action_sure,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int arg1) {
								finish();
								dialog.dismiss();
							}
						});
				b.show();
			}
		});
	}

	public void pickImageFromGallery() {
		String dir = Environment.getExternalStorageDirectory().toString() + "/"
				+ "agent";
		File dir_ = new File(dir);
		if (!dir_.exists() || !dir_.isDirectory()) {
			dir_.mkdirs();
		}
		Intent pickGallery = new Intent(Intent.ACTION_GET_CONTENT);
		pickGallery.setType(IMAGE_TYPE);
		startActivityForResult(pickGallery, CddRequestCode.ACTION_PICK_GALLERY);
	}

	public void pickImageFromCamera() {

		String dir = Environment.getExternalStorageDirectory().toString() + "/"
				+ "caidingdang";
		File dir_ = new File(dir);
		if (!dir_.exists() || !dir_.isDirectory()) {
			dir_.mkdirs();
		}
		Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String picPath = System.currentTimeMillis() + ".jpg";
		File myCaptureFile = new File(dir, picPath);
		tempPath = myCaptureFile.getAbsolutePath();
		Uri uri = Uri.fromFile(myCaptureFile);
		intent1.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent1.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		startActivityForResult(intent1, CddRequestCode.ACTION_PICK_CAMERA);
	}

	private void uploadFiles(ArrayList<File> list) {
		showLoading(true);
		UploadForMultiOp uploadMultiOp = new UploadForMultiOp();
		uploadMultiOp.uploadFile(list, new OnUploadListener() {
			
			@Override
			public void onSuccess(String result) {
				showLoading(false);
			}
			
			@Override
			public void onError(String error) {
				showLoading(false);
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.niming_send:
			if (isNimingS) {
				isNimingS = false;
				setNimingOrNot(false);
			} else {
				isNimingS = true;
				setNimingOrNot(true);
			}
			break;

		case R.id.insert_pic:
			Bundle b = new Bundle();
			b.putBoolean("has_default", false);
			displayFragment(true, "pick_photo", b, new BaseFragmentListener() {

				@Override
				public void onCallBack(Object object) {

				}
			});
			break;

		case R.id.insert_bq:

			break;

		case R.id.pulish_cancel:
			showTipNoteDialog("真的要取消编辑的内容吗？");
			break;

		case R.id.pulish_ok:
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					uploadFiles(picList);
				}
			}).start();
			break;

		default:
			break;
		}

	}

}
