package com.asag.serial.fragment;

import com.asag.serial.R;
import com.asag.serial.app.SerialApp;
import com.asag.serial.base.BaseActivity;
import com.asag.serial.mode.TimeSetEntry;
import com.asag.serial.utils.CMDCode;
import com.asag.serial.utils.DataUtils;
import com.asag.serial.utils.SerialBroadCode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class PointSetFragment extends DialogFragment implements OnClickListener{

	private View view;
	
	private EditText paikongInput, jianceInput;
	
	private BaseFragmentListener listener;
	
	private LocalBroadcastManager lbm = LocalBroadcastManager
			.getInstance(SerialApp.getInstance());
	
	public PointSetFragment() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.point_set_fragment, null);
		initView();
		loadCache();
		return view;
	}
	
	public void addBaseFragmentListener(BaseFragmentListener listener) {
		this.listener = listener;
	}
	
	private void initView() {
		paikongInput = (EditText) view.findViewById(R.id.paikong_input);
		jianceInput = (EditText) view.findViewById(R.id.jiance_input);
		view.findViewById(R.id.btn_ok).setOnClickListener(this);
		view.findViewById(R.id.btn_cancel).setOnClickListener(this);
	}
	
	private boolean checkInput() {
		if (TextUtils.isEmpty(paikongInput.getText().toString().trim())) {
			if (getActivity() instanceof BaseActivity) {
				((BaseActivity) getActivity()).showToast("请输入排空时间");
				return false;
			}
		}
		if (TextUtils.isEmpty(jianceInput.getText().toString().trim())) {
			if (getActivity() instanceof BaseActivity) {
				((BaseActivity) getActivity()).showToast("请输入检测时间");
				return false;
			} 
		}
		return true;
	}

	private void sendMessageS(String message) {
		Intent intent = new Intent(SerialBroadCode.ACTION_SEND_MESSAGE);
		intent.putExtra("send_message", message);
		lbm.sendBroadcast(intent);
	}
	
	private void loadCache() {
		String checkTime = DataUtils.getPreferences("check_minute_input", "");
		if (!TextUtils.isEmpty(checkTime)) {
			jianceInput.setText(checkTime);
		}
		String paikongTime = DataUtils.getPreferences("paikong_minute_input", "");
		if (!TextUtils.isEmpty(paikongTime)) {
			paikongInput.setText(paikongTime);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			if (!checkInput()) {
				return;
			} else {
				TimeSetEntry entry = new TimeSetEntry();
				if (!TextUtils.isEmpty(jianceInput.getText().toString()
						.trim())) {
					entry.checkTime = Integer.valueOf(jianceInput.getText()
							.toString().trim());
					DataUtils.putPreferences("check_minute_input", entry.checkTime);
				}
				if (!TextUtils.isEmpty(paikongInput.getText().toString()
						.trim())) {
					entry.paikongTime = Integer.valueOf(paikongInput.getText()
							.toString().trim());
					DataUtils.putPreferences("paikong_minute_input", entry.paikongTime);
				}
				if (listener != null) {
					listener.onCallBack(entry);
				}
				sendMessageS(CMDCode.PREPARE_OK);
			}
			dismissAllowingStateLoss();
			break;

		case R.id.btn_cancel:
			sendMessageS(CMDCode.PREPARE_CANCLE);
			dismissAllowingStateLoss();
			break;

		default:
			break;
		}
		
	}

}
