package com.cdd.fragment;

import com.cdd.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class SignSuccessFragment extends DialogFragment implements OnClickListener{

	private View view;
	
	private TextView signCoin;
	
	
	private String signFen = "";
	
	public SignSuccessFragment() {
		super();
	}
	
	public SignSuccessFragment(Context context, Bundle b) {
		if (b != null) {
			signFen = b.getString("sign_coin");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.sign_success_fragment, null);
		initView();
		return view;
	}

	private void initView() {
		view.findViewById(R.id.sign_layout).setOnClickListener(this);
		signCoin = (TextView) view.findViewById(R.id.sign_coin);
//		signCoin.setText(signFen);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sign_layout:
			dismissAllowingStateLoss();
			break;

		default:
			break;
		}
		
	}
	
}
