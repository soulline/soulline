package com.cdd.minepage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdd.R;
import com.cdd.base.BaseActivity;
import com.cdd.fragment.BaseFragmentListener;
import com.cdd.fragment.CitySelectFragment;
import com.cdd.fragment.EditNickNameFragment;
import com.cdd.fragment.SexSelectFragment;
import com.cdd.fragment.TakePhotoSelectFragment;
import com.cdd.mode.CityItemEntry;
import com.cdd.mode.MemberInfoEntry;
import com.cdd.mode.ModifyMemberEntry;
import com.cdd.net.RequestListener;
import com.cdd.operater.GetMemberInfoOp;
import com.cdd.operater.UpdateMemberInfoOp;
import com.cdd.operater.UploadOperater;
import com.cdd.util.BitmapUtil;
import com.cdd.util.CddConfig;
import com.cdd.util.CddRequestCode;
import com.cdd.util.ImageOperater;

public class MineInfoModifyActivity extends BaseActivity implements
		OnClickListener {

	private ImageView portraitView;

	private TextView nickName, sexType, localCity, simpleTx, levelValue,
			dingdangbiValue;

	private final String IMAGE_TYPE = "image/*";

	private String tempPath = "";
	
	private MemberInfoEntry meberInfo = new MemberInfoEntry();

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.self_info_activity);
		initTitle("个人信息");
		initView();
		initContent();
	}

	private void initView() {
		portraitView = (ImageView) findViewById(R.id.portrait);
		portraitView.setOnClickListener(this);
		nickName = (TextView) findViewById(R.id.nick_name);
		sexType = (TextView) findViewById(R.id.sex_type);
		localCity = (TextView) findViewById(R.id.local_city);
		simpleTx = (TextView) findViewById(R.id.simple_tx);
		levelValue = (TextView) findViewById(R.id.level_value);
		dingdangbiValue = (TextView) findViewById(R.id.dingdangbi_value);
		findViewById(R.id.nick_name_layout).setOnClickListener(this);
		findViewById(R.id.sex_layout).setOnClickListener(this);
		findViewById(R.id.local_layout).setOnClickListener(this);
		findViewById(R.id.simple_tx_layout).setOnClickListener(this);
		findViewById(R.id.bound_phone).setOnClickListener(this);
		findViewById(R.id.modify_password_layout).setOnClickListener(this);
	}

	private void initContent() {
		loadMemberInfo();
	}

	private void initSex(String sex) {
		if (sex.equals("1")) {
			sexType.setText("男");
		} else if (sex.equals("2")) {
			sexType.setText("女");
		}
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
			workImageForCamera(tempPath);
		}
	}
	
	private void workImageForCamera(final String path) {
		showLoading(true);
		new Thread(new Runnable() {

			@Override
			public void run() {
				BitmapUtil.onCompressForUpload(path, 894, 595);
				BitmapFactory.Options options = new BitmapFactory.Options();
				Bitmap bitmap = BitmapUtil.getBitmapByPath(
						CddConfig.IMAGE_CACHE_FILE + "upload.jpg", options, 894,
						595);
				if (bitmap != null) {
					showLoading(true);
					uploadImage(CddConfig.IMAGE_CACHE_FILE + "upload.jpg");
					BitmapDrawable portraitD = new BitmapDrawable(bitmap);
					if (portraitD != null) {
						showImage(portraitD);
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
					File myUploadFile = new File(dir, "upload.jpg");
					try {
						out = new FileOutputStream(myUploadFile);
						out.write(upImageData);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (out != null) {
							try {
								out.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					uploadImage(myUploadFile.getAbsolutePath());
					BitmapFactory.Options options = new BitmapFactory.Options();
					Bitmap bit = BitmapUtil.getBitmapByPath(
							CddConfig.IMAGE_CACHE_FILE + "upload.jpg", options,
							894, 595);
					BitmapDrawable portraitD = new BitmapDrawable(bit);
					if (portraitD != null) {
						showImage(portraitD);
					}
				}

			}
		}).start();
	}
	
	private void showImage(final BitmapDrawable portraitD) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				portraitView.setImageDrawable(portraitD);
			}
		});
	}
	
	private void uploadImage(final String path) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				UploadOperater upload = new UploadOperater();
				upload.uploadFile(path, new UploadOperater.OnUploadListener() {

					@Override
					public void onSuccess(String result) {
						JSONObject response = null;
						try {
							response = new JSONObject(result);
							if (!JSONObject.NULL.equals(response)) {
								int code = response.optInt("status");
								showLoading(false);
								if (code != 200) {
									showToast(response.optString("msg"));
								} else {
									showToast("上传成功");
									JSONObject obj = response
											.optJSONObject("re");
									final String url = obj.optString("url");
									meberInfo.photo = url;
									handler.post(new Runnable() {

										@Override
										public void run() {
											ImageOperater
													.getInstance(context)
													.onLoadImage(url, portraitView);
										}
									});
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
							showLoading(false);
						} finally {
							showLoading(false);
						}

					}

					@Override
					public void onError(String error) {
						showLoading(false);
						showToast(error);
					}
				});
			}
		}).start();

	}
	
	private void initMemberInfo(MemberInfoEntry memberInfo) {
		if (memberInfo.sex.equals("1")) {
			portraitView.setImageResource(R.drawable.default_man_portrait);
		} else if (memberInfo.sex.equals("2")) {
			portraitView.setImageResource(R.drawable.default_woman_portrait);
		} else {
			portraitView.setImageResource(R.drawable.default_woman_portrait);
		}
		if (!TextUtils.isEmpty(memberInfo.photo) && !memberInfo.photo.equals("null")) {
			String ulr = memberInfo.photo;
			ImageOperater.getInstance(context).onLoadImage(ulr,
					portraitView);
		}
		if (!TextUtils.isEmpty(memberInfo.name)
				&& !memberInfo.name.equals("null")) {
			nickName.setText(memberInfo.name);
		}
		sexType.setText("女");
		initSex(memberInfo.sex);
		if (!TextUtils.isEmpty(memberInfo.cityName)
				&& !memberInfo.cityName.equals("null")) {
			localCity.setText(memberInfo.cityName);
		}
		if (!TextUtils.isEmpty(memberInfo.description)
				&& !memberInfo.description.equals("null")) {
			simpleTx.setText(memberInfo.description);
		}
		if (!TextUtils.isEmpty(memberInfo.levelName)
				&& !memberInfo.levelName.equals("null")) {
			levelValue.setText(memberInfo.levelName);
		}
		if (!TextUtils.isEmpty(memberInfo.availableScore)
				&& !memberInfo.availableScore.equals("null")) {
			dingdangbiValue.setText(memberInfo.availableScore);
		} else {
			dingdangbiValue.setText("0");
		}
	}

	private void loadMemberInfo() {
		final GetMemberInfoOp memberOp = new GetMemberInfoOp(context);
		memberOp.onRequest(new RequestListener() {

			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCallBack(Object data) {
				final MemberInfoEntry memberInfo = memberOp.getMemberInfo();
				meberInfo = memberInfo;
				handler.post(new Runnable() {

					@Override
					public void run() {
						initMemberInfo(memberInfo);
					}
				});
			}
		});
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
			takePhotoF.addFragmentListener(listener);
			return takePhotoF;
		} else if (tag.equals("edit_nickname")) {
			EditNickNameFragment nickFragment = new EditNickNameFragment(context, b);
			nickFragment.addFragmentListener(listener);
			return nickFragment;
		} else if (tag.equals("sex_select")) {
			SexSelectFragment sexFragment = new SexSelectFragment(context, b);
			sexFragment.addFragmentListener(listener);
			return sexFragment;
		} else if (tag.equals("city_list")) {
			CitySelectFragment cityFragment = new CitySelectFragment();
			cityFragment.addFragmentListener(listener);
			return cityFragment;
		}
		return null;
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
	
	public void updateMemberInfo(ModifyMemberEntry member) {
		UpdateMemberInfoOp updateMemberOp = new UpdateMemberInfoOp(context);
		updateMemberOp.setParmas(member);
		updateMemberOp.onRequest(new RequestListener() {
			
			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCallBack(Object data) {
				showToast("修改成功");
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.portrait:
			displayFragment(true, "pick_photo", null,
					new BaseFragmentListener() {

						@Override
						public void onCallBack(Object object) {
							if (object instanceof String) {
								String type = (String) object;
								if (type.equals("1")) {
									ModifyMemberEntry member = new ModifyMemberEntry();
									member.defaultPhoto = "1";
									meberInfo.photo = "";
									updateMemberInfo(member);
									if (meberInfo != null && meberInfo.sex.equals("1")) {
										portraitView.setImageResource(R.drawable.default_man_portrait);
									} else if ((meberInfo != null && meberInfo.sex.equals("2"))) {
										portraitView.setImageResource(R.drawable.default_woman_portrait);
									}
								}
							}

						}
					});
			break;

		case R.id.bound_phone:
			showToast("目前暂时无法使用绑定手机功能，敬请期待");
			break;

		case R.id.nick_name_layout:
			Bundle b = new Bundle();
			b.putString("nick_name", nickName.getText().toString());
			b.putString("input_type", "1");
			displayFragment(true, "edit_nickname", b, new BaseFragmentListener() {
				
				@Override
				public void onCallBack(Object object) {
					if (object instanceof String) {
						String nickNamet = (String) object;
						nickName.setText(nickNamet);
						ModifyMemberEntry member = new ModifyMemberEntry();
						member.name = nickNamet;
						meberInfo.name = nickNamet;
						updateMemberInfo(member);
					}
				}
			});

			break;

		case R.id.sex_layout:
			displayFragment(true, "sex_select", null, new BaseFragmentListener() {
				
				@Override
				public void onCallBack(Object object) {
					if (object instanceof String) {
						String sex = (String) object;
						initSex(sex);
						ModifyMemberEntry modify = new ModifyMemberEntry();
						modify.sex = sex;
						meberInfo.sex = sex;
						if (TextUtils.isEmpty(meberInfo.photo) || (meberInfo.photo.equals("null"))) {
							if (meberInfo.sex.equals("1")) {
								portraitView.setImageResource(R.drawable.default_man_portrait);
							} else if (meberInfo.sex.equals("2")) {
								portraitView.setImageResource(R.drawable.default_woman_portrait);
							}
						}
						updateMemberInfo(modify);
					}
					
				}
			});

			break;

		case R.id.local_layout:
			displayFragment(true, "city_list", null, new BaseFragmentListener() {
				
				@Override
				public void onCallBack(Object object) {
					if (object instanceof CityItemEntry) {
						CityItemEntry city = (CityItemEntry) object;
						localCity.setText(city.name);
						ModifyMemberEntry modify1 = new ModifyMemberEntry();
						modify1.cityId = city.id;
						meberInfo.cityId = city.id;
						meberInfo.cityName = city.name;
						updateMemberInfo(modify1);
					}
				}
			});
			break;

		case R.id.simple_tx_layout:
			Bundle bundle = new Bundle();
			bundle.putString("nick_name", simpleTx.getText().toString());
			bundle.putString("input_type", "2");
			displayFragment(true, "edit_nickname", bundle, new BaseFragmentListener() {
				
				@Override
				public void onCallBack(Object object) {
					if (object instanceof String) {
						String nickNamet = (String) object;
						simpleTx.setText(nickNamet);
						ModifyMemberEntry member = new ModifyMemberEntry();
						member.description = nickNamet;
						meberInfo.description = nickNamet;
						updateMemberInfo(member);
					}
				}
			});
			break;
			
		case R.id.modify_password_layout:
			Intent modify = new Intent(context, ModifyPWActivity.class);
			startActivity(modify);
			break;

		default:
			break;
		}

	}

}
