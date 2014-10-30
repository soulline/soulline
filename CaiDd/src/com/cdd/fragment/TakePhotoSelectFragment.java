package com.cdd.fragment;

import com.cdd.R;
import com.cdd.sqpage.PulishActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class TakePhotoSelectFragment extends DialogFragment implements
		OnClickListener {

	private View view;

	private boolean hasDefault = true;
	
	public TakePhotoSelectFragment() {
		super();
	}

	public TakePhotoSelectFragment(Context context, Bundle b) {
		if (b != null) {
			hasDefault = b.getBoolean("has_default", true);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (hasDefault) {
			view = inflater.inflate(R.layout.take_photo_choose_fragment, null);
		} else {
			view = inflater.inflate(R.layout.insert_pic_layout, null);
		}
		initView();
		return view;
	}

	private void initView() {
		if (hasDefault) {
			view.findViewById(R.id.from_default).setOnClickListener(this);
		}
		view.findViewById(R.id.from_camera).setOnClickListener(this);
		view.findViewById(R.id.from_gallery).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.from_default:

			dismissAllowingStateLoss();
			break;
		case R.id.from_camera:
			if (hasDefault) {

			} else {
				((PulishActivity) getActivity()).pickImageFromCamera();
			}
			dismissAllowingStateLoss();
			break;
		case R.id.from_gallery:
			if (hasDefault) {

			} else {
				((PulishActivity) getActivity()).pickImageFromGallery();
			}
			dismissAllowingStateLoss();
			break;

		default:
			break;
		}
	}

}
