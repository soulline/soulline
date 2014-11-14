package com.cdd.activity.sqpage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.cdd.R;
import com.cdd.activity.image.ImageDrawablePageActivity;
import com.cdd.activity.login.LoginActivity;
import com.cdd.base.BaseActivity;
import com.cdd.base.MainActivity;
import com.cdd.fragment.BaseFragmentListener;
import com.cdd.fragment.SignSuccessFragment;
import com.cdd.fragment.TakePhotoSelectFragment;
import com.cdd.mode.ForumItem;
import com.cdd.mode.SqAnswerCommitEntry;
import com.cdd.mode.SqAskCommitEntry;
import com.cdd.net.RequestListener;
import com.cdd.operater.SqAnswerCommitOp;
import com.cdd.operater.SqAskCommitOp;
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

	private EditText inputEdtx;
	
	private ForumItem forumItem = new ForumItem();
	
	private int pulishType = 1;
	
	private String askId = "";
	
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
		inputEdtx = (EditText) findViewById(R.id.input_edtx);
		findViewById(R.id.insert_pic).setOnClickListener(this);
		findViewById(R.id.insert_bq).setOnClickListener(this);
		findViewById(R.id.pulish_cancel).setOnClickListener(this);
		findViewById(R.id.pulish_ok).setOnClickListener(this);
		setNimingOrNot(isNimingS);
		pulishType = getIntent().getIntExtra("pulish_type", 1);
		if ((pulishType == 1) && getIntent().getSerializableExtra("forum_item") != null
				&& getIntent().getSerializableExtra("forum_item") instanceof ForumItem) {
			forumItem = (ForumItem) getIntent().getSerializableExtra(
					"forum_item");
		} else if (pulishType == 2) {
			askId = getIntent().getStringExtra("ask_id");
		}
		picGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ArrayList<String> photoList = new ArrayList<String>();
				for (int i = 0; i < picList.size(); i++) {
					String picUrl = picList.get(i).getAbsolutePath();
					photoList.add(picUrl);
				}
				Intent intent = new Intent(context, ImageDrawablePageActivity.class);
				intent.putExtra("drawable_list", photoList);
				intent.putExtra("image_index", position);
				startActivity(intent);
			}
		});
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
	
	private void doRequestForAskCommit(ArrayList<String> photoList) {
		SqAskCommitEntry askEntry = new SqAskCommitEntry();
		askEntry.title = inputEdtx.getText().toString().trim();
		if (photoList.size() > 0) {
			askEntry.photos = photoList;
		}
		if (forumItem != null && !TextUtils.isEmpty(forumItem.fatherId)
				&& !TextUtils.isEmpty(forumItem.id)) {
			askEntry.itemId = forumItem.fatherId;
			askEntry.subItemId = forumItem.id;
		} else if (forumItem != null && TextUtils.isEmpty(forumItem.fatherId)
				&& !TextUtils.isEmpty(forumItem.id)) {
			askEntry.itemId = forumItem.id;
		}
		if (isNimingS) {
			askEntry.anonymous = "1";
		} else {
			askEntry.anonymous = "0";
		}
		sqAskCommit(askEntry);
	}
	
	private void doRequestForAnswerCommit(ArrayList<String> photoList) {
		SqAnswerCommitEntry answerEntry = new SqAnswerCommitEntry();
		answerEntry.content = inputEdtx.getText().toString().trim();
		if (photoList.size() > 0) {
			answerEntry.photos = photoList;
		}
		answerEntry.subjectId = askId;
		if (isNimingS) {
			answerEntry.anonymous = "1";
		} else {
			answerEntry.anonymous = "0";
		}
		sqAnswerCommit(answerEntry);
	}
	
	private void sqAnswerCommit(SqAnswerCommitEntry entry) {
		SqAnswerCommitOp answerCommitOp = new SqAnswerCommitOp(context);
		answerCommitOp.setParmas(entry);
		answerCommitOp.onRequest(new RequestListener() {

			@Override
			public void onError(Object error) {

			}

			@Override
			public void onCallBack(Object data) {
				showToast("发表成功");
				setResult(RESULT_OK);
				finish();
			}
		});
	}

	private void uploadFiles(ArrayList<File> list) {
		showLoading(true);
		UploadForMultiOp uploadMultiOp = new UploadForMultiOp();
		uploadMultiOp.uploadFile(list, new OnUploadListener() {

			@Override
			public void onSuccess(String result) {
				try {
					JSONObject obj = new JSONObject(result);
					int status = obj.optInt("status");
					if (status == 200) {
						JSONObject objN = obj.optJSONObject("re");
						JSONArray array = objN.optJSONArray("urls");
						ArrayList<String> photoList = new ArrayList<String>();
						for (int i = 0; i < array.length(); i++) {
							String picUrl = (String) array.opt(i);
							photoList.add(picUrl);
						}
						if (pulishType == 1) {
							doRequestForAskCommit(photoList);
						} else if (pulishType == 2) {
							doRequestForAnswerCommit(photoList);
						}

					} else {
						showToast(obj.optString("msg"));
					}
				} catch (JSONException e) {
					showLoading(false);
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String error) {
				showLoading(false);
			}
		});
	}

	private boolean checkInput() {
		if (TextUtils.isEmpty(inputEdtx.getText().toString())) {
			showToast("请输入发表内容");
			return false;
		}
		return true;
	}

	private void sqAskCommit(SqAskCommitEntry entry) {
		SqAskCommitOp askCommitOp = new SqAskCommitOp(context);
		askCommitOp.setParmas(entry);
		askCommitOp.onRequest(new RequestListener() {

			@Override
			public void onError(Object error) {

			}

			@Override
			public void onCallBack(Object data) {
				showToast("发表成功");
				setResult(RESULT_OK);
				finish();
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
			if (adapter != null && adapter.getCount() == 9) {
				showToast("图片一次最多只能上传9张");
			} else {
				Bundle b = new Bundle();
				b.putBoolean("has_default", false);
				displayFragment(true, "pick_photo", b,
						new BaseFragmentListener() {
					
		  			@Override
					public void onCallBack(Object object) {
						
					}
				});
			}
			break;

		case R.id.insert_bq:

			break;

		case R.id.pulish_cancel:
			showTipNoteDialog("真的要取消编辑的内容吗？");
			break;

		case R.id.pulish_ok:
			if (checkInput() && adapter != null && adapter.getCount() > 0) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						uploadFiles(picList);
					}
				}).start();
			} else if (checkInput()) {
				ArrayList<String> photos = new ArrayList<String>();
				if (pulishType == 1) {
					doRequestForAskCommit(photos);
				} else if (pulishType == 2) {
					doRequestForAnswerCommit(photos);
				}
			}
			break;

		default:
			break;
		}

	}

}
