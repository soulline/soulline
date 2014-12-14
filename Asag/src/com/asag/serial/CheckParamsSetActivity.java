package com.asag.serial;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.asag.serial.base.BaseActivity;
import com.asag.serial.utils.DataUtils;

public class CheckParamsSetActivity extends BaseActivity implements OnClickListener{

	private EditText co2Input, ph3Input, o2Input;
	
	private EditText t0Input, t1Input, t2Input, t3Input, t4Input, t5Input,
	                 t6Input, t7Input, t8Input, t9Input, t10Input, t11Input,
	                 t12Input, t13Input, t14Input, t15Input;
	
	private EditText r0Input, r1Input, r2Input, r3Input, r4Input, r5Input,
	                 r6Input, r7Input, r8Input, r9Input, r10Input, r11Input, 
	                 r12Input, r13Input, r14Input, r15Input;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_params_setting);
		initView();
	}
	
	private void initView() {
		co2Input = (EditText) findViewById(R.id.co2_input);
		ph3Input = (EditText) findViewById(R.id.ph3_input);
		o2Input = (EditText) findViewById(R.id.o2_input);
		t0Input = (EditText) findViewById(R.id.t_0_input);
		t1Input = (EditText) findViewById(R.id.t_1_input);
		t2Input = (EditText) findViewById(R.id.t_2_input);
		t3Input = (EditText) findViewById(R.id.t_3_input);
		t4Input = (EditText) findViewById(R.id.t_4_input);
		t5Input = (EditText) findViewById(R.id.t_5_input);
		t6Input = (EditText) findViewById(R.id.t_6_input);
		t7Input = (EditText) findViewById(R.id.t_7_input);
		t8Input = (EditText) findViewById(R.id.t_8_input);
		t9Input = (EditText) findViewById(R.id.t_9_input);
		t10Input = (EditText) findViewById(R.id.t_10_input);
		t11Input = (EditText) findViewById(R.id.t_11_input);
		t12Input = (EditText) findViewById(R.id.t_12_input);
		t13Input = (EditText) findViewById(R.id.t_13_input);
		t14Input = (EditText) findViewById(R.id.t_14_input);
		t15Input = (EditText) findViewById(R.id.t_15_input);
		
		r0Input = (EditText) findViewById(R.id.r_0_input);
		r1Input = (EditText) findViewById(R.id.r_1_input);
		r2Input = (EditText) findViewById(R.id.r_2_input);
		r3Input = (EditText) findViewById(R.id.r_3_input);
		r4Input = (EditText) findViewById(R.id.r_4_input);
		r5Input = (EditText) findViewById(R.id.r_5_input);
		r6Input = (EditText) findViewById(R.id.r_6_input);
		r7Input = (EditText) findViewById(R.id.r_7_input);
		r8Input = (EditText) findViewById(R.id.r_8_input);
		r9Input = (EditText) findViewById(R.id.r_9_input);
		r10Input = (EditText) findViewById(R.id.r_10_input);
		r11Input = (EditText) findViewById(R.id.r_11_input);
		r12Input = (EditText) findViewById(R.id.r_12_input);
		r13Input = (EditText) findViewById(R.id.r_13_input);
		r14Input = (EditText) findViewById(R.id.r_14_input);
		r15Input = (EditText) findViewById(R.id.r_15_input);
		
		findViewById(R.id.btn_ok).setOnClickListener(this);
		findViewById(R.id.btn_cancel).setOnClickListener(this);
	}
	
	private void saveInput() {
		if (!TextUtils.isEmpty(co2Input.getText().toString().trim())) {
			DataUtils.putPreferences("co2_input", co2Input.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(ph3Input.getText().toString().trim())) {
			DataUtils.putPreferences("ph3_input", ph3Input.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(o2Input.getText().toString().trim())) {
			DataUtils.putPreferences("o2_input", o2Input.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(t0Input.getText().toString().trim())) {
			DataUtils.putPreferences("t_0_input", t0Input.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(t1Input.getText().toString().trim())) {
			DataUtils.putPreferences("t_1_input", t1Input.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(t2Input.getText().toString().trim())) {
			DataUtils.putPreferences("t_2_input", t2Input.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(t3Input.getText().toString().trim())) {
			DataUtils.putPreferences("t_3_input", t3Input.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(t4Input.getText().toString().trim())) {
			DataUtils.putPreferences("t_4_input", t4Input.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(t5Input.getText().toString().trim())) {
			DataUtils.putPreferences("t_5_input", t5Input.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(t6Input.getText().toString().trim())) {
			DataUtils.putPreferences("t_6_input", t6Input.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(t7Input.getText().toString().trim())) {
			DataUtils.putPreferences("t_7_input", t7Input.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(t8Input.getText().toString().trim())) {
			DataUtils.putPreferences("t_8_input", t8Input.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(t9Input.getText().toString().trim())) {
			DataUtils.putPreferences("t_9_input", t9Input.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(t10Input.getText().toString().trim())) {
			DataUtils.putPreferences("t_10_input", t10Input.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(t11Input.getText().toString().trim())) {
			DataUtils.putPreferences("t_11_input", t11Input.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(t12Input.getText().toString().trim())) {
			DataUtils.putPreferences("t_12_input", t12Input.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(t13Input.getText().toString().trim())) {
			DataUtils.putPreferences("t_13_input", t13Input.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(t14Input.getText().toString().trim())) {
			DataUtils.putPreferences("t_14_input", t14Input.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(t15Input.getText().toString().trim())) {
			DataUtils.putPreferences("t_15_input", t15Input.getText().toString().trim());
		}
		
		if (!TextUtils.isEmpty(r0Input.getText().toString().trim())) {
			DataUtils.putPreferences("r_0_input", r0Input.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(r1Input.getText().toString().trim())) {
			DataUtils.putPreferences("r_1_input", r1Input.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(r2Input.getText().toString().trim())) {
			DataUtils.putPreferences("r_2_input", r2Input.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(r3Input.getText().toString().trim())) {
			DataUtils.putPreferences("r_3_input", r3Input.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(r4Input.getText().toString().trim())) {
			DataUtils.putPreferences("r_4_input", r4Input.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(r5Input.getText().toString().trim())) {
			DataUtils.putPreferences("r_5_input", r5Input.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(r6Input.getText().toString().trim())) {
			DataUtils.putPreferences("r_6_input", r6Input.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(r7Input.getText().toString().trim())) {
			DataUtils.putPreferences("r_7_input", r7Input.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(r8Input.getText().toString().trim())) {
			DataUtils.putPreferences("r_8_input", r8Input.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(r9Input.getText().toString().trim())) {
			DataUtils.putPreferences("r_9_input", r9Input.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(r10Input.getText().toString().trim())) {
			DataUtils.putPreferences("r_10_input", r10Input.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(r11Input.getText().toString().trim())) {
			DataUtils.putPreferences("r_11_input", r11Input.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(r12Input.getText().toString().trim())) {
			DataUtils.putPreferences("r_12_input", r12Input.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(r13Input.getText().toString().trim())) {
			DataUtils.putPreferences("r_13_input", r13Input.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(r14Input.getText().toString().trim())) {
			DataUtils.putPreferences("r_14_input", r14Input.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(r15Input.getText().toString().trim())) {
			DataUtils.putPreferences("r_15_input", r15Input.getText().toString().trim());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			saveInput();
			showToast("保存成功");
			finish();
			break;
			
		case R.id.btn_cancel:
			finish();
			break;

		default:
			break;
		}
		
	}

}
  