package com.asag.serial.fragment;

import com.asag.serial.R;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class PointSetFragment extends DialogFragment implements OnClickListener{

	private View view;
	
	private EditText paikongInput, jianceInput;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.point_set_fragment, null);
		initView();
		return view;
	}
	
	private void initView() {
		paikongInput = (EditText) view.findViewById(R.id.paikong_input);
		jianceInput = (EditText) view.findViewById(R.id.jiance_input);
		view.findViewById(R.id.btn_ok).setOnClickListener(this);
		view.findViewById(R.id.btn_cancel).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			
			break;

		case R.id.btn_cancel:
			
			break;

		default:
			break;
		}
		
	}

}
