package com.asag.serial;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.asag.serial.base.BaseActivity;
import com.asag.serial.utils.DataUtils;

public class CheckParamsSetActivity extends BaseActivity implements
		OnClickListener {

	private EditText co2Input, ph3Input, o2Input;

	private EditText t0Input, t1Input, t2Input, t3Input, t4Input, t5Input,
			t6Input, t7Input, t8Input, t9Input, t10Input, t11Input, t12Input,
			t13Input, t14Input, t15Input;

	private EditText r0Input, r1Input, r2Input, r3Input, r4Input, r5Input,
			r6Input, r7Input, r8Input, r9Input, r10Input, r11Input, r12Input,
			r13Input, r14Input, r15Input;

	private TextView topTitleTx;

	private TextView co2_title, ph3_title, o2_title, t_title, t_0_title,
			t_1_title, t_2_title, t_3_title, t_4_title, t_5_title, t_6_title,
			t_7_title, t_8_title, t_9_title, t_10_title, t_11_title,
			t_12_title, t_13_title, t_14_title, t_15_title;

	private TextView rh_title, r_0_title, r_1_title, r_2_title, r_3_title,
			r_4_title, r_5_title, r_6_title, r_7_title, r_8_title, r_9_title,
			r_10_title, r_11_title, r_12_title, r_13_title, r_14_title,
			r_15_title;
	
	private Button btn_ok, btn_cancle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_params_setting);
		initView();
		initTextSize();
	}

	private void initTextSize() {
		int size = DataUtils.getPreferences(DataUtils.KEY_TEXT_SIZE, 5);
		switch (size) {
		case 1:
			reloadTextSize(1.4f);
			break;
		case 2:
			reloadTextSize(1.6f);
			break;
		case 3:
			reloadTextSize(1.8f);
			break;
		case 4:
			reloadTextSize(2.0f);
			break;
		case 5:
			reloadTextSize(2.2f);
			break;

		default:
			break;
		}
	}

	private void reloadTextSize(float size) {
		topTitleTx.setTextSize(topTitleTx.getTextSize() * size);
		co2_title.setTextSize(co2_title.getTextSize() * size);
		ph3_title.setTextSize(ph3_title.getTextSize() * size);
		o2_title.setTextSize(o2_title.getTextSize() * size);
		
		t_title.setTextSize(t_title.getTextSize() * size);
		t_0_title.setTextSize(t_0_title.getTextSize() * size);
		t_1_title.setTextSize(t_1_title.getTextSize() * size);
		t_2_title.setTextSize(t_2_title.getTextSize() * size);
		t_3_title.setTextSize(t_3_title.getTextSize() * size);
		t_4_title.setTextSize(t_4_title.getTextSize() * size);
		t_5_title.setTextSize(t_5_title.getTextSize() * size);
		t_6_title.setTextSize(t_6_title.getTextSize() * size);
		t_7_title.setTextSize(t_7_title.getTextSize() * size);
		t_8_title.setTextSize(t_8_title.getTextSize() * size);
		t_9_title.setTextSize(t_9_title.getTextSize() * size);
		t_10_title.setTextSize(t_10_title.getTextSize() * size);
		t_11_title.setTextSize(t_11_title.getTextSize() * size);
		t_12_title.setTextSize(t_12_title.getTextSize() * size);
		t_13_title.setTextSize(t_13_title.getTextSize() * size);
		t_14_title.setTextSize(t_14_title.getTextSize() * size);
		t_15_title.setTextSize(t_15_title.getTextSize() * size);
		
		rh_title.setTextSize(rh_title.getTextSize() * size);
		r_0_title.setTextSize(r_0_title.getTextSize() * size);
		r_1_title.setTextSize(r_1_title.getTextSize() * size);
		r_2_title.setTextSize(r_2_title.getTextSize() * size);
		r_3_title.setTextSize(r_3_title.getTextSize() * size);
		r_4_title.setTextSize(r_4_title.getTextSize() * size);
		r_5_title.setTextSize(r_5_title.getTextSize() * size);
		r_6_title.setTextSize(r_6_title.getTextSize() * size);
		r_7_title.setTextSize(r_7_title.getTextSize() * size);
		r_8_title.setTextSize(r_8_title.getTextSize() * size);
		r_9_title.setTextSize(r_9_title.getTextSize() * size);
		r_10_title.setTextSize(r_10_title.getTextSize() * size);
		r_11_title.setTextSize(r_11_title.getTextSize() * size);
		r_12_title.setTextSize(r_12_title.getTextSize() * size);
		r_13_title.setTextSize(r_13_title.getTextSize() * size);
		r_14_title.setTextSize(r_14_title.getTextSize() * size);
		r_15_title.setTextSize(r_15_title.getTextSize() * size);
		
		co2Input.setTextSize(co2Input.getTextSize() * size);
		ph3Input.setTextSize(ph3Input.getTextSize() * size);
		o2Input.setTextSize(o2Input.getTextSize() * size);
		t0Input.setTextSize(t0Input.getTextSize() * size);
		t1Input.setTextSize(t1Input.getTextSize() * size);
		t2Input.setTextSize(t2Input.getTextSize() * size);
		t3Input.setTextSize(t3Input.getTextSize() * size);
		t4Input.setTextSize(t4Input.getTextSize() * size);
		t5Input.setTextSize(t5Input.getTextSize() * size);
		t6Input.setTextSize(t6Input.getTextSize() * size);
		t7Input.setTextSize(t7Input.getTextSize() * size);
		t8Input.setTextSize(t8Input.getTextSize() * size);
		t9Input.setTextSize(t9Input.getTextSize() * size);
		t10Input.setTextSize(t10Input.getTextSize() * size);
		t11Input.setTextSize(t11Input.getTextSize() * size);
		t12Input.setTextSize(t12Input.getTextSize() * size);
		t13Input.setTextSize(t13Input.getTextSize() * size);
		t14Input.setTextSize(t14Input.getTextSize() * size);
		t15Input.setTextSize(t15Input.getTextSize() * size);
		
		r0Input.setTextSize(r0Input.getTextSize() * size);
		r1Input.setTextSize(r1Input.getTextSize() * size);
		r2Input.setTextSize(r2Input.getTextSize() * size);
		r3Input.setTextSize(r3Input.getTextSize() * size);
		r4Input.setTextSize(r4Input.getTextSize() * size);
		r5Input.setTextSize(r5Input.getTextSize() * size);
		r6Input.setTextSize(r6Input.getTextSize() * size);
		r7Input.setTextSize(r7Input.getTextSize() * size);
		r8Input.setTextSize(r8Input.getTextSize() * size);
		r9Input.setTextSize(r9Input.getTextSize() * size);
		r10Input.setTextSize(r10Input.getTextSize() * size);
		r11Input.setTextSize(r11Input.getTextSize() * size);
		r12Input.setTextSize(r12Input.getTextSize() * size);
		r13Input.setTextSize(r13Input.getTextSize() * size);
		r14Input.setTextSize(r14Input.getTextSize() * size);
		r15Input.setTextSize(r15Input.getTextSize() * size);
		
		btn_ok.setTextSize(btn_ok.getTextSize() * size);
		btn_cancle.setTextSize(btn_cancle.getTextSize() * size);
	}

	private void initView() {
		topTitleTx = (TextView) findViewById(R.id.top_title_tx);
		co2_title = (TextView) findViewById(R.id.co2_title);
		ph3_title = (TextView) findViewById(R.id.ph3_title);
		o2_title = (TextView) findViewById(R.id.o2_title);

		t_title = (TextView) findViewById(R.id.t_title);
		t_0_title = (TextView) findViewById(R.id.t_0_title);
		t_1_title = (TextView) findViewById(R.id.t_1_title);
		t_2_title = (TextView) findViewById(R.id.t_2_title);
		t_3_title = (TextView) findViewById(R.id.t_3_title);
		t_4_title = (TextView) findViewById(R.id.t_4_title);
		t_5_title = (TextView) findViewById(R.id.t_5_title);
		t_6_title = (TextView) findViewById(R.id.t_6_title);
		t_7_title = (TextView) findViewById(R.id.t_7_title);
		t_8_title = (TextView) findViewById(R.id.t_8_title);
		t_9_title = (TextView) findViewById(R.id.t_9_title);
		t_10_title = (TextView) findViewById(R.id.t_10_title);
		t_11_title = (TextView) findViewById(R.id.t_11_title);
		t_12_title = (TextView) findViewById(R.id.t_12_title);
		t_13_title = (TextView) findViewById(R.id.t_13_title);
		t_14_title = (TextView) findViewById(R.id.t_14_title);
		t_15_title = (TextView) findViewById(R.id.t_15_title);
		
		rh_title = (TextView) findViewById(R.id.rh_title);
		r_0_title = (TextView) findViewById(R.id.r_0_title);
		r_1_title = (TextView) findViewById(R.id.r_1_title);
		r_2_title = (TextView) findViewById(R.id.r_2_title);
		r_3_title = (TextView) findViewById(R.id.r_3_title);
		r_4_title = (TextView) findViewById(R.id.r_4_title);
		r_5_title = (TextView) findViewById(R.id.r_5_title);
		r_6_title = (TextView) findViewById(R.id.r_6_title);
		r_7_title = (TextView) findViewById(R.id.r_7_title);
		r_8_title = (TextView) findViewById(R.id.r_8_title);
		r_9_title = (TextView) findViewById(R.id.r_9_title);
		r_10_title = (TextView) findViewById(R.id.r_10_title);
		r_11_title = (TextView) findViewById(R.id.r_11_title);
		r_12_title = (TextView) findViewById(R.id.r_12_title);
		r_13_title = (TextView) findViewById(R.id.r_13_title);
		r_14_title = (TextView) findViewById(R.id.r_14_title);
		r_15_title = (TextView) findViewById(R.id.r_15_title);
		
		btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_cancle = (Button) findViewById(R.id.btn_cancel);

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
			DataUtils.putPreferences("co2_input", co2Input.getText().toString()
					.trim());
		}
		if (!TextUtils.isEmpty(ph3Input.getText().toString().trim())) {
			DataUtils.putPreferences("ph3_input", ph3Input.getText().toString()
					.trim());
		}
		if (!TextUtils.isEmpty(o2Input.getText().toString().trim())) {
			DataUtils.putPreferences("o2_input", o2Input.getText().toString()
					.trim());
		}
		if (!TextUtils.isEmpty(t0Input.getText().toString().trim())) {
			DataUtils.putPreferences("t_0_input", t0Input.getText().toString()
					.trim());
		}
		if (!TextUtils.isEmpty(t1Input.getText().toString().trim())) {
			DataUtils.putPreferences("t_1_input", t1Input.getText().toString()
					.trim());
		}
		if (!TextUtils.isEmpty(t2Input.getText().toString().trim())) {
			DataUtils.putPreferences("t_2_input", t2Input.getText().toString()
					.trim());
		}
		if (!TextUtils.isEmpty(t3Input.getText().toString().trim())) {
			DataUtils.putPreferences("t_3_input", t3Input.getText().toString()
					.trim());
		}
		if (!TextUtils.isEmpty(t4Input.getText().toString().trim())) {
			DataUtils.putPreferences("t_4_input", t4Input.getText().toString()
					.trim());
		}
		if (!TextUtils.isEmpty(t5Input.getText().toString().trim())) {
			DataUtils.putPreferences("t_5_input", t5Input.getText().toString()
					.trim());
		}
		if (!TextUtils.isEmpty(t6Input.getText().toString().trim())) {
			DataUtils.putPreferences("t_6_input", t6Input.getText().toString()
					.trim());
		}
		if (!TextUtils.isEmpty(t7Input.getText().toString().trim())) {
			DataUtils.putPreferences("t_7_input", t7Input.getText().toString()
					.trim());
		}
		if (!TextUtils.isEmpty(t8Input.getText().toString().trim())) {
			DataUtils.putPreferences("t_8_input", t8Input.getText().toString()
					.trim());
		}
		if (!TextUtils.isEmpty(t9Input.getText().toString().trim())) {
			DataUtils.putPreferences("t_9_input", t9Input.getText().toString()
					.trim());
		}
		if (!TextUtils.isEmpty(t10Input.getText().toString().trim())) {
			DataUtils.putPreferences("t_10_input", t10Input.getText()
					.toString().trim());
		}
		if (!TextUtils.isEmpty(t11Input.getText().toString().trim())) {
			DataUtils.putPreferences("t_11_input", t11Input.getText()
					.toString().trim());
		}
		if (!TextUtils.isEmpty(t12Input.getText().toString().trim())) {
			DataUtils.putPreferences("t_12_input", t12Input.getText()
					.toString().trim());
		}
		if (!TextUtils.isEmpty(t13Input.getText().toString().trim())) {
			DataUtils.putPreferences("t_13_input", t13Input.getText()
					.toString().trim());
		}
		if (!TextUtils.isEmpty(t14Input.getText().toString().trim())) {
			DataUtils.putPreferences("t_14_input", t14Input.getText()
					.toString().trim());
		}
		if (!TextUtils.isEmpty(t15Input.getText().toString().trim())) {
			DataUtils.putPreferences("t_15_input", t15Input.getText()
					.toString().trim());
		}

		if (!TextUtils.isEmpty(r0Input.getText().toString().trim())) {
			DataUtils.putPreferences("r_0_input", r0Input.getText().toString()
					.trim());
		}
		if (!TextUtils.isEmpty(r1Input.getText().toString().trim())) {
			DataUtils.putPreferences("r_1_input", r1Input.getText().toString()
					.trim());
		}
		if (!TextUtils.isEmpty(r2Input.getText().toString().trim())) {
			DataUtils.putPreferences("r_2_input", r2Input.getText().toString()
					.trim());
		}
		if (!TextUtils.isEmpty(r3Input.getText().toString().trim())) {
			DataUtils.putPreferences("r_3_input", r3Input.getText().toString()
					.trim());
		}
		if (!TextUtils.isEmpty(r4Input.getText().toString().trim())) {
			DataUtils.putPreferences("r_4_input", r4Input.getText().toString()
					.trim());
		}
		if (!TextUtils.isEmpty(r5Input.getText().toString().trim())) {
			DataUtils.putPreferences("r_5_input", r5Input.getText().toString()
					.trim());
		}
		if (!TextUtils.isEmpty(r6Input.getText().toString().trim())) {
			DataUtils.putPreferences("r_6_input", r6Input.getText().toString()
					.trim());
		}
		if (!TextUtils.isEmpty(r7Input.getText().toString().trim())) {
			DataUtils.putPreferences("r_7_input", r7Input.getText().toString()
					.trim());
		}
		if (!TextUtils.isEmpty(r8Input.getText().toString().trim())) {
			DataUtils.putPreferences("r_8_input", r8Input.getText().toString()
					.trim());
		}
		if (!TextUtils.isEmpty(r9Input.getText().toString().trim())) {
			DataUtils.putPreferences("r_9_input", r9Input.getText().toString()
					.trim());
		}
		if (!TextUtils.isEmpty(r10Input.getText().toString().trim())) {
			DataUtils.putPreferences("r_10_input", r10Input.getText()
					.toString().trim());
		}
		if (!TextUtils.isEmpty(r11Input.getText().toString().trim())) {
			DataUtils.putPreferences("r_11_input", r11Input.getText()
					.toString().trim());
		}
		if (!TextUtils.isEmpty(r12Input.getText().toString().trim())) {
			DataUtils.putPreferences("r_12_input", r12Input.getText()
					.toString().trim());
		}
		if (!TextUtils.isEmpty(r13Input.getText().toString().trim())) {
			DataUtils.putPreferences("r_13_input", r13Input.getText()
					.toString().trim());
		}
		if (!TextUtils.isEmpty(r14Input.getText().toString().trim())) {
			DataUtils.putPreferences("r_14_input", r14Input.getText()
					.toString().trim());
		}
		if (!TextUtils.isEmpty(r15Input.getText().toString().trim())) {
			DataUtils.putPreferences("r_15_input", r15Input.getText()
					.toString().trim());
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
