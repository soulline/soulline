package com.asag.serial.widget;


import com.asag.serial.R;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SerialCheck extends LinearLayout {

	private boolean isCheck = false;
	
	private TextView checkBtn;
	
	public SerialCheck(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.serial_check_item, this);
		checkBtn = (TextView) findViewById(R.id.check_btn);
		checkBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (isCheck) {
					setCheck(false);
				} else {
					setCheck(true);
				}
			}
		});
		String number = attrs.getAttributeValue(null, "Text");
		if (!TextUtils.isEmpty(number)) {
			checkBtn.setText(number);
		}
	}
	
	public boolean isCheck() {
		return isCheck;
	}
	
	public void setCheck(boolean check) {
		isCheck = check;
		if (isCheck) {
			checkBtn.setBackgroundResource(R.drawable.point_press);
		} else {
			checkBtn.setBackgroundResource(R.drawable.point_normal);
		}
	}

}
