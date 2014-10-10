package com.yyl.account;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyl.BaseActivity;
import com.yyl.R;
import com.yyl.game.RoomListActivity;
import com.yyl.mode.AccountInfo;
import com.yyl.mode.FirstLoginInfo;
import com.yyl.net.RequestListener;
import com.yyl.operater.ModifyGamerInfoOperater;
import com.yyl.operater.UploadOperater;
import com.yyl.operater.UploadOperater.OnUploadListener;
import com.yyl.utils.ImageOperater;
import com.yyl.utils.ImageUtils;
import com.yyl.utils.YylConfig;
import com.yyl.utils.YylRequestCode;

public class CreatePeopleActivity extends BaseActivity implements
		OnClickListener {

	private int SEX_MODE = 1;

	private final String IMAGE_TYPE = "image/*";

	private TextView sexMan;
	private TextView sexWoman;

	private EditText nameInput;
	private ImageView portraitFrame;

	private boolean isCustom = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_people);
		setFullScreen();
		initView();
		initSex();
		FirstLoginInfo firstL = (FirstLoginInfo) getIntent().getSerializableExtra("first_login");
		if (firstL != null && firstL.isFirstLogin.equals("1")) {
			showTipDialog(getString(R.string.first_login_note).replaceAll("%", firstL.firstLoginScore));
		}
	}

	private void initSex() {
		Drawable drawable1 = getResources().getDrawable(R.drawable.sexual_yes);
		Drawable drawable2 = getResources().getDrawable(R.drawable.sexual_no);
		drawable1.setBounds(0, 0, drawable1.getMinimumWidth(),
				drawable1.getMinimumHeight());
		drawable2.setBounds(0, 0, drawable2.getMinimumWidth(),
				drawable2.getMinimumHeight());
		switch (SEX_MODE) {
		case 1:
			sexMan.setCompoundDrawables(drawable1, null, null, null);
			sexWoman.setCompoundDrawables(drawable2, null, null, null);
			if (!isCustom) {
				portraitFrame.setImageResource(R.drawable.head_boy);
			}
			break;
		case 2:
			sexMan.setCompoundDrawables(drawable2, null, null, null);
			sexWoman.setCompoundDrawables(drawable1, null, null, null);
			if (!isCustom) {
				portraitFrame.setImageResource(R.drawable.head_girl);
			}
			break;
		default:
			break;
		}
	}

	private void uploadImage(final String path) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				UploadOperater upload = new UploadOperater();
				upload.uploadFile("upload.do", path, new OnUploadListener() {
					
					@Override
					public void onSuccess(String result) {
						JSONObject response = null;
						try {
							response = new JSONObject(result);
							if (!JSONObject.NULL.equals(response)) {
								int status = response.optInt("status");
								showLoading(false);
								if (status != 200) {
									showToast(response.optString("msg"));
								} else {
									showToast(getString(R.string.upload_success));
									JSONObject obj = response.optJSONObject("re");
									final String url = obj.optString("url")  + "&" + System.currentTimeMillis();
									runOnUiThread(new Runnable() {
										
										@Override
										public void run() {
											ImageOperater.getInstance(context).onLoadImage(url, portraitFrame);
										}
									});
								}
							}
						} catch (JSONException e) {
							showLoading(false);
							e.printStackTrace();
						} finally {
							showLoading(false);
						}
					}
					
					@Override
					public void onError(String error) {
						showLoading(false);
						showToast(getString(R.string.upload_failed));
					}
				});
			}
		}).start();
		
	}
	
	private void initView() {
		sexMan = (TextView) findViewById(R.id.sex_man);
		sexWoman = (TextView) findViewById(R.id.sex_woman);
		nameInput = (EditText) findViewById(R.id.name_input);
		portraitFrame = (ImageView) findViewById(R.id.portrait_frame);
		sexMan.setOnClickListener(this);
		sexWoman.setOnClickListener(this);
		portraitFrame.setOnClickListener(this);
		findViewById(R.id.create_enter_btn).setOnClickListener(this);
	}

	private void createPeople() {
		if (TextUtils.isEmpty(nameInput.getText().toString().trim())) {
			showToast(getString(R.string.please_input_username));
			return;
		}
		ModifyGamerInfoOperater gamerC = new ModifyGamerInfoOperater(context);
		final AccountInfo account = new AccountInfo();
		account.name = nameInput.getText().toString().trim();
		account.sex = SEX_MODE + "";
		gamerC.setParams(account);
		gamerC.onRequest(new RequestListener() {

			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCallBack(Object data) {
				showToast(getString(R.string.people_create_success));
				saveAccount(account);
				Intent intent = new Intent(context, RoomListActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	private void saveAccount(AccountInfo account) {
		AccountInfo newAccount = app.getAccount();
		newAccount.name = account.name;
		newAccount.sex = account.sex;
		app.setAccount(newAccount);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK
				&& requestCode == YylRequestCode.ACTION_PICK_GALLERY) {
			Uri originalUri = data.getData();
			workForImage(originalUri);
		}
	}
	
	private void workForImage(final Uri originalUri) {
		showLoading(true);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Bitmap bmY = null;
				try {
					Bitmap bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), originalUri);
					if (bm != null) {
						bmY = ImageUtils.comp(bm);
					} else {
						showLoading(false);
					}
				} catch (IOException e) {
					e.printStackTrace();
					showLoading(false);
				}
				if (bmY != null) {
					showLoading(true);
					ImageUtils.writeBitmap(YylConfig.IMAGE_CACHE_FILE, "upload.jpg", bmY);
					uploadImage(YylConfig.IMAGE_CACHE_FILE + "upload.jpg");
					BitmapDrawable portraitD = new BitmapDrawable(bmY);
					if (portraitD != null) {
						showPortrait(portraitD);
					}
				} else {
					showLoading(false);
				}
				
			}
		}).start();
	}

	private void showPortrait(final BitmapDrawable bmD) {
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				isCustom = true;
				portraitFrame.setImageDrawable(bmD);
			}
		});
	}

	private void pickImage() {
		Intent pickGallery = new Intent(Intent.ACTION_GET_CONTENT);
		pickGallery.setType(IMAGE_TYPE);
		startActivityForResult(pickGallery, YylRequestCode.ACTION_PICK_GALLERY);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sex_man:
			if (SEX_MODE == 2) {
				SEX_MODE = 1;
				initSex();
			} else {
				return;
			}
			break;
		case R.id.sex_woman:
			if (SEX_MODE == 1) {
				SEX_MODE = 2;
				initSex();
			} else {
				return;
			}
			break;
		case R.id.create_enter_btn:
			createPeople();
			break;
		case R.id.portrait_frame:
			showLoading(true);
			pickImage();
			break;
		default:
			break;
		}

	}

}
