package com.cdd.minepage;

import java.io.File;

import android.content.Intent;
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
import com.cdd.util.CddRequestCode;
import com.cdd.util.ImageOperater;

public class MineInfoModifyActivity extends BaseActivity implements
		OnClickListener {

	private ImageView portraitView;

	private TextView nickName, sexType, localCity, simpleTx, levelValue,
			dingdangbiValue;

	private final String IMAGE_TYPE = "image/*";

	private String tempPath = "";

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
	
	private void initMemberInfo(MemberInfoEntry memberInfo) {
		if (!TextUtils.isEmpty(memberInfo.photo) && !memberInfo.photo.equals("null")) {
			String ulr = memberInfo.photo + "&" + System.currentTimeMillis();
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
