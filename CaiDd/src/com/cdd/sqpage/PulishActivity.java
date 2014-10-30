package com.cdd.sqpage;

import java.io.File;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.cdd.R;
import com.cdd.base.BaseActivity;
import com.cdd.base.MainActivity;
import com.cdd.fragment.BaseFragmentListener;
import com.cdd.fragment.SignSuccessFragment;
import com.cdd.fragment.TakePhotoSelectFragment;
import com.cdd.login.LoginActivity;
import com.cdd.util.CddRequestCode;

public class PulishActivity extends BaseActivity implements OnClickListener{

	private TextView nimingSend;
	
	private boolean isNimingS = false;
	
	private final String IMAGE_TYPE = "image/*";
	
	private String tempPath = "";
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.publish_activity);
		initView();
	}
	
	private void initView() {
		nimingSend = (TextView) findViewById(R.id.niming_send);
		nimingSend.setOnClickListener(this);
		findViewById(R.id.insert_pic).setOnClickListener(this);
		findViewById(R.id.insert_bq).setOnClickListener(this);
		findViewById(R.id.pulish_cancel).setOnClickListener(this);
		findViewById(R.id.pulish_ok).setOnClickListener(this);
		setNimingOrNot(isNimingS);
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
			TakePhotoSelectFragment takePhotoF = new TakePhotoSelectFragment(context, b);
			return takePhotoF;
		}
		return null;
	}
		
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
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
		File myCaptureFile = new File(dir, "upload.jpg");
		tempPath = myCaptureFile.getAbsolutePath();
		Uri uri = Uri.fromFile(myCaptureFile);
		intent1.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent1.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		startActivityForResult(intent1, CddRequestCode.ACTION_PICK_CAMERA);
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
			
			break;

		default:
			break;
		}
		
	}

}
