package com.yyl.account;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyl.BaseActivity;
import com.yyl.R;
import com.yyl.mode.AccountInfo;
import com.yyl.net.RequestListener;
import com.yyl.operater.LoadAccountOperater;
import com.yyl.operater.UploadOperater;
import com.yyl.operater.UploadOperater.OnUploadListener;
import com.yyl.pay.PayActivity;
import com.yyl.utils.BaseActivityCloseListener;
import com.yyl.utils.ImageOperater;
import com.yyl.utils.ImageUtils;
import com.yyl.utils.YylConfig;
import com.yyl.utils.YylRequestCode;

public class AccountActivity extends BaseActivity implements OnClickListener{

	private ImageView portraitFrame;
	
	private TextView accountName, accountSex, accountMoney;
	
	private final String IMAGE_TYPE = "image/*";
	
	private AccountInfo account = new AccountInfo();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_activity);
		setFullScreen();
		initView();
		app.putClosePath(YylConfig.PATH_KEY, new BaseActivityCloseListener() {
			
			@Override
			public void onFinish() {
				finish();
			}
		});
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		loadAccountInfo();
	}


	private void initView() {
		portraitFrame = (ImageView) findViewById(R.id.portrait_frame);
		portraitFrame.setOnClickListener(this);
		findViewById(R.id.exit_login_btn).setOnClickListener(this);
		findViewById(R.id.pay_btn).setOnClickListener(this);
		accountName = (TextView) findViewById(R.id.account_name);
		accountSex = (TextView) findViewById(R.id.account_sex);
		accountMoney = (TextView) findViewById(R.id.account_money);
		findViewById(R.id.modify_password_btn).setOnClickListener(this);
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
				portraitFrame.setImageDrawable(bmD);
			}
		});
	}
	
	private void loadAccountInfo() {
		final LoadAccountOperater loadAccount = new LoadAccountOperater(context);
		loadAccount.onRequest(new RequestListener() {
			
			@Override
			public void onError(Object error) {
				
			}
			
			@Override
			public void onCallBack(Object data) {
				account = (AccountInfo) loadAccount.getData();
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						if (account != null) {
							accountName.setText(account.name);
							accountMoney.setText(account.scoreAmount);
							if (account.sex.equals("1")) {
								portraitFrame.setBackgroundResource(R.drawable.head_boy);
								accountSex.setText(R.string.sex_man);
							} else if (account.sex.equals("2")) {
								portraitFrame.setBackgroundResource(R.drawable.head_girl);
								accountSex.setText(R.string.sex_woman);
							}
							if (!TextUtils.isEmpty(account.photoUrl)) {
								String url = account.photoUrl + "&" + System.currentTimeMillis();
								ImageOperater.getInstance(context).onLoadImage(url, portraitFrame);
							}
						}
					}
				});
				
			}
		});
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
									account.photoUrl = url;
									runOnUiThread(new Runnable() {
										
										@Override
										public void run() {
											ImageOperater.getInstance(context).onLoadImage(url, portraitFrame);
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
						showToast(getString(R.string.upload_failed));
					}
				});
			}
		}).start();
		
	}

	private void pickImage() {
		Intent pickGallery = new Intent(Intent.ACTION_GET_CONTENT);
		pickGallery.setType(IMAGE_TYPE);
		startActivityForResult(pickGallery, YylRequestCode.ACTION_PICK_GALLERY);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		app.popClosePath(false, YylConfig.PATH_KEY);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.portrait_frame:
			showLoading(true);
			pickImage();
			break;

		case R.id.exit_login_btn:
			app.clearAccountInfo();
			showToast(getString(R.string.already_exit_account));
			Intent intent = new Intent(context, LoginActivity.class);
			startActivity(intent);
			app.popClosePath(true, YylConfig.PATH_KEY);
			break;
		case R.id.pay_btn:
			Intent pay = new Intent(context, PayActivity.class);
			startActivity(pay);
			break;
		case R.id.modify_password_btn:
			Intent mpw = new Intent(context, ModifyPWActivity.class);
			startActivity(mpw);
			break;
		default:
			break;
		}
		
	}
}
