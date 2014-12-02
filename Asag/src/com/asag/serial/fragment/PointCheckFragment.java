package com.asag.serial.fragment;

import com.asag.serial.MainPageActivity;
import com.asag.serial.R;
import com.asag.serial.utils.CMDCode;
import com.asag.serial.widget.SerialCheck;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

public class PointCheckFragment extends DialogFragment implements OnClickListener{

	private View view;
	
	private SerialCheck check0, check1, check2, check3, check4, check5, check6, check7,
	check8, check9, check10, check11, check12, check13, check14, check15;
	
	private BaseFragmentListener listener;
	
	public PointCheckFragment() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.point_check_fragment, null);
		initView();
		return view;
	}
	
	public void addBaseFragmentListener(BaseFragmentListener listener) {
		this.listener = listener;
	}

	private void initView() {
		check0 = (SerialCheck) view.findViewById(R.id.check_way_0);
		check1 = (SerialCheck) view.findViewById(R.id.check_way_1);
		check2 = (SerialCheck) view.findViewById(R.id.check_way_2);
		check3 = (SerialCheck) view.findViewById(R.id.check_way_3);
		check4 = (SerialCheck) view.findViewById(R.id.check_way_4);
		check5 = (SerialCheck) view.findViewById(R.id.check_way_5);
		check6 = (SerialCheck) view.findViewById(R.id.check_way_6);
		check7 = (SerialCheck) view.findViewById(R.id.check_way_7);
		check8 = (SerialCheck) view.findViewById(R.id.check_way_8);
		check9 = (SerialCheck) view.findViewById(R.id.check_way_9);
		check10 = (SerialCheck) view.findViewById(R.id.check_way_10);
		check11 = (SerialCheck) view.findViewById(R.id.check_way_11);
		check12 = (SerialCheck) view.findViewById(R.id.check_way_12);
		check13 = (SerialCheck) view.findViewById(R.id.check_way_13);
		check14 = (SerialCheck) view.findViewById(R.id.check_way_14);
		check15 = (SerialCheck) view.findViewById(R.id.check_way_15);
		view.findViewById(R.id.btn_ok).setOnClickListener(this);
		view.findViewById(R.id.btn_cancel).setOnClickListener(this);
	}
	
	private String getCheckResult() {
		StringBuilder sb = new StringBuilder();
		if (check15.isCheck()) {
			sb.append("1");
		} else {
			sb.append("0");
		}
		if (check14.isCheck()) {
			sb.append("1");
		} else {
			sb.append("0");
		}
		if (check13.isCheck()) {
			sb.append("1");
		} else {
			sb.append("0");
		}
		if (check12.isCheck()) {
			sb.append("1");
		} else {
			sb.append("0");
		}
		if (check11.isCheck()) {
			sb.append("1");
		} else {
			sb.append("0");
		}
		if (check10.isCheck()) {
			sb.append("1");
		} else {
			sb.append("0");
		}
		if (check9.isCheck()) {
			sb.append("1");
		} else {
			sb.append("0");
		}
		if (check8.isCheck()) {
			sb.append("1");
		} else {
			sb.append("0");
		}
		if (check7.isCheck()) {
			sb.append("1");
		} else {
			sb.append("0");
		}
		if (check6.isCheck()) {
			sb.append("1");
		} else {
			sb.append("0");
		}
		if (check5.isCheck()) {
			sb.append("1");
		} else {
			sb.append("0");
		}
		if (check4.isCheck()) {
			sb.append("1");
		} else {
			sb.append("0");
		}
		if (check3.isCheck()) {
			sb.append("1");
		} else {
			sb.append("0");
		}
		if (check2.isCheck()) {
			sb.append("1");
		} else {
			sb.append("0");
		}
		if (check1.isCheck()) {
			sb.append("1");
		} else {
			sb.append("0");
		}
		if (check0.isCheck()) {
			sb.append("1");
		} else {
			sb.append("0");
		}
		String hexStr = binaryToHex(sb.toString()).toUpperCase();
		sb = new StringBuilder();
		for (int i=0; i < (4 - hexStr.length()); i++) {
			sb.append("0");
		}
		hexStr = sb.toString() + hexStr;
		return hexStr;
	}
	
	public String binaryToHex(String s){
		return Long.toHexString(Long.parseLong(s,2));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			if (listener != null) {
				listener.onCallBack(getCheckResult());
			}
			if (getActivity() instanceof MainPageActivity) {
				((MainPageActivity) getActivity()).sendMessageS(CMDCode.PREPARE_OK);
			}
			dismissAllowingStateLoss();
			break;
			
		case R.id.btn_cancel:
			if (getActivity() instanceof MainPageActivity) {
				((MainPageActivity) getActivity()).sendMessageS(CMDCode.PREPARE_CANCLE);
			}
			dismissAllowingStateLoss();
			break;

		default:
			break;
		}
		
	}
	
}
