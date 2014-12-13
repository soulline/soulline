package com.asag.serial;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.asag.serial.base.BaseActivity;

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
  