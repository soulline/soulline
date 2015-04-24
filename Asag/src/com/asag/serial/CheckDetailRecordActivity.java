package com.asag.serial;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.asag.serial.base.BaseActivity;
import com.asag.serial.mode.CheckDetailItem;
import com.asag.serial.mode.PointItemRecord;
import com.asag.serial.utils.DataUtils;

public class CheckDetailRecordActivity extends BaseActivity {

	private TextView top_title_tx, first_title, cang_number_title, cang_number_value, liangzhong_number_title,
	                 liangzhong_number_value, shuliang_number_title, shuliang_number_value, shuliang_number_danwei,
	                 shuifen_number_title, shuifen_number_value, shuifen_number_danwei, chandi_title, chandi_value,
	                 ruku_date_title, ruku_date_value, check_date_title, check_date_value, second_title, bianhao_title,
	                 co2_title, rh_title, t_title, ssi_title, mmi_title, third_title, yisidian_value,
	                 qianzaidian_title, qianzai_value, fourth_title, jianyi_value;
	
	private ListView point_detail_listview;
	
	private RecordItemAdapter adapter;
	
	private CheckDetailItem checkDetail = new CheckDetailItem();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_detail_record);
		initView();
		initTextSize();
		initContent();
	}
	
	private void initContent() {
		checkDetail = (CheckDetailItem) getIntent().getSerializableExtra("check_detail");
		if (checkDetail != null) {
			Log.d("zhao", "checkDetail size : " + checkDetail.pointList.size());
			if (checkDetail.checkType.equals("0")) {
				top_title_tx.setText("粮安监测详细记录");
			} else if (checkDetail.checkType.equals("1")) {
				top_title_tx.setText("粮安检测详细记录");
			}
			cang_number_value.setText(checkDetail.canghao);
			liangzhong_number_value.setText(checkDetail.liangzhong);
			shuliang_number_value.setText(checkDetail. shuliang);
			shuifen_number_value.setText(checkDetail.shuifen);
			chandi_value.setText(checkDetail.chandi);
			ruku_date_value.setText(checkDetail.rukuDate);
			yisidian_value.setText(checkDetail.chuliangState);
			qianzai_value.setText(checkDetail.shuifenState);
			check_date_value.setText(checkDetail.checkDate);
			if (checkDetail.pointList.size() > 0) {
				initAdapter(checkDetail.pointList);
			}
		}
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
	
	private void initAdapter(ArrayList<PointItemRecord> list) {
		if (adapter == null) {
			adapter = new RecordItemAdapter(context);
			adapter.addData(list);
			point_detail_listview.setAdapter(adapter);
		} else {
			adapter.clear();
			adapter.addData(list);
			adapter.notifyDataSetChanged();
		}
	}
	
	private void reloadTextSize(float size) {
		top_title_tx.setTextSize(top_title_tx.getTextSize() * size);
		first_title.setTextSize(first_title.getTextSize() * size);
		cang_number_title.setTextSize(cang_number_title.getTextSize() * size);
		cang_number_value.setTextSize(cang_number_value.getTextSize() * size);
		liangzhong_number_title.setTextSize(liangzhong_number_title.getTextSize() * size);
		liangzhong_number_value.setTextSize(liangzhong_number_value.getTextSize() * size);
		shuliang_number_title.setTextSize(shuliang_number_title.getTextSize() * size);
		shuliang_number_value.setTextSize(shuliang_number_value.getTextSize() * size);
		shuliang_number_danwei.setTextSize(shuliang_number_danwei.getTextSize() * size);
		shuifen_number_title.setTextSize(shuifen_number_title.getTextSize() * size);
		shuifen_number_value.setTextSize(shuifen_number_value.getTextSize() * size);
		shuifen_number_danwei.setTextSize(shuifen_number_danwei.getTextSize() * size);
		chandi_title.setTextSize(chandi_title.getTextSize() * size);
		chandi_value.setTextSize(chandi_value.getTextSize() * size);
		ruku_date_title.setTextSize(ruku_date_title.getTextSize() * size);
		ruku_date_value.setTextSize(ruku_date_value.getTextSize() * size);
		check_date_title.setTextSize(check_date_title.getTextSize() * size);
		check_date_value.setTextSize(check_date_value.getTextSize() * size);
		second_title.setTextSize(second_title.getTextSize() * size);
		bianhao_title.setTextSize(bianhao_title.getTextSize() * size);
		co2_title.setTextSize(co2_title.getTextSize() * size);
		rh_title.setTextSize(rh_title.getTextSize() * size);
		t_title.setTextSize(t_title.getTextSize() * size);
		ssi_title.setTextSize(ssi_title.getTextSize() * size);
		mmi_title.setTextSize(mmi_title.getTextSize() * size);
		third_title.setTextSize(third_title.getTextSize() * size);
		yisidian_value.setTextSize(yisidian_value.getTextSize() * size);
		qianzaidian_title.setTextSize(qianzaidian_title.getTextSize() * size);
		qianzai_value.setTextSize(qianzai_value.getTextSize() * size);
		fourth_title.setTextSize(fourth_title.getTextSize() * size);
		jianyi_value.setTextSize(jianyi_value.getTextSize() * size);
	}
	
	private void initView() {
		top_title_tx = (TextView) findViewById(R.id.top_title_tx);
		first_title = (TextView) findViewById(R.id.first_title);
		cang_number_title = (TextView) findViewById(R.id.cang_number_title);
		cang_number_value = (TextView) findViewById(R.id.cang_number_value);
		liangzhong_number_title = (TextView) findViewById(R.id.liangzhong_number_title);
		liangzhong_number_value = (TextView) findViewById(R.id.liangzhong_number_value);
		shuliang_number_title = (TextView) findViewById(R.id.shuliang_number_title);
		shuliang_number_value = (TextView) findViewById(R.id.shuliang_number_value);
		shuliang_number_danwei = (TextView) findViewById(R.id.shuliang_number_danwei);
		
		shuifen_number_title = (TextView) findViewById(R.id.shuifen_number_title);
		shuifen_number_value = (TextView) findViewById(R.id.shuifen_number_value);
		shuifen_number_danwei = (TextView) findViewById(R.id.shuifen_number_danwei);
		chandi_title = (TextView) findViewById(R.id.chandi_title);
		chandi_value = (TextView) findViewById(R.id.chandi_value);
		ruku_date_title = (TextView) findViewById(R.id.ruku_date_title);
		ruku_date_value = (TextView) findViewById(R.id.ruku_date_value);
		check_date_title = (TextView) findViewById(R.id.check_date_title);
		
		check_date_value = (TextView) findViewById(R.id.check_date_value);
		second_title = (TextView) findViewById(R.id.second_title);
		bianhao_title = (TextView) findViewById(R.id.bianhao_title);
		co2_title = (TextView) findViewById(R.id.co2_title);
		rh_title = (TextView) findViewById(R.id.rh_title);
		t_title = (TextView) findViewById(R.id.t_title);
		
		ssi_title = (TextView) findViewById(R.id.ssi_title);
		mmi_title = (TextView) findViewById(R.id.mmi_title);
		third_title = (TextView) findViewById(R.id.third_title);
		
		yisidian_value = (TextView) findViewById(R.id.yisidian_value);
		qianzaidian_title = (TextView) findViewById(R.id.qianzaidian_title);
		qianzai_value = (TextView) findViewById(R.id.qianzai_value);
		fourth_title = (TextView) findViewById(R.id.fourth_title);
		jianyi_value = (TextView) findViewById(R.id.jianyi_value);
		
		point_detail_listview = (ListView) findViewById(R.id.point_detail_listview);
	}

}
