package com.cdd.activity.alarmpage;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.cdd.R;
import com.cdd.activity.alarmpage.CitySpinnerPop.OnCityClickListener;
import com.cdd.activity.alarmpage.ExamSpinnerPop.OnExamClickListener;
import com.cdd.base.BaseActivity;
import com.cdd.mode.AddExamRemindEntry;
import com.cdd.mode.CityItemEntry;
import com.cdd.mode.ForumItem;
import com.cdd.net.RequestListener;
import com.cdd.operater.AddExamRemindOp;
import com.cdd.operater.ExamItemOperater;
import com.cdd.operater.GetCityListOp;

public class AddRemindActivity extends BaseActivity implements OnClickListener {

	private TextView examClassSpinner, citySpinner;

	private ArrayList<ForumItem> examList = new ArrayList<ForumItem>();

	private ArrayList<CityItemEntry> cityList = new ArrayList<CityItemEntry>();

	private ForumItem examSelect = new ForumItem();
	
	private CityItemEntry citySelect = new CityItemEntry();

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.add_remind_activity);
		initView();
		initContent();
	}

	private void initContent() {
		getExamList();
		getCityList();
	}

	private void initView() {
		examClassSpinner = (TextView) findViewById(R.id.exam_class_spinner);
		citySpinner = (TextView) findViewById(R.id.city_spinner);
		examClassSpinner.setOnClickListener(this);
		citySpinner.setOnClickListener(this);
		findViewById(R.id.remind_back_layout).setOnClickListener(this);
		findViewById(R.id.remind_ok_layout).setOnClickListener(this);
	}

	private void getCityList() {
		final GetCityListOp getCityOp = new GetCityListOp(context);
		getCityOp.onRequest(new RequestListener() {

			@Override
			public void onError(Object error) {

			}

			@Override
			public void onCallBack(Object data) {
				cityList = getCityOp.getCityList();
				if (cityList.size() > 0) {
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							citySpinner.setText(cityList.get(0).name);
							citySelect = cityList.get(0);
						}
					});
				}
			}
		});
	}

	private void showExamMenu() {
		ExamSpinnerPop examPop = new ExamSpinnerPop(context,
				new OnExamClickListener() {

					@Override
					public void onClick(ForumItem item) {
						examClassSpinner.setText(item.name);
						examSelect = item;
					}
				}, examList);
		examPop.showPopupWindow(findViewById(R.id.exam_class_spinner_layout));
	}

	private void showCityMenu() {
		CitySpinnerPop examPop = new CitySpinnerPop(context,
				new OnCityClickListener() {

					@Override
					public void onClick(CityItemEntry item) {
						citySpinner.setText(item.name);
						citySelect = item;
					}
				}, cityList);
		examPop.showPopupWindow(findViewById(R.id.city_spinner_layout));
	}

	private void getExamList() {
		final ExamItemOperater examOp = new ExamItemOperater(context);
		examOp.onRequest(new RequestListener() {

			@Override
			public void onError(Object error) {

			}

			@Override
			public void onCallBack(Object data) {
				examList = examOp.getForumList();
				if (examList.size() > 0) {
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							examClassSpinner.setText(examList.get(0).name);
							examSelect = examList.get(0);
						}
					});
				}
			}
		});
	}

	private void addExamCommitRequest(AddExamRemindEntry entry) {
		AddExamRemindOp addOp = new AddExamRemindOp(context);
		addOp.setParams(entry);
		addOp.onRequest(new RequestListener() {
			
			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCallBack(Object data) {
				showToast("添加成功");
				finish();
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.exam_class_spinner:
			showExamMenu();
			break;

		case R.id.city_spinner:
			showCityMenu();
			break;
			
		case R.id.remind_back_layout:
			finish();
			break;
			
		case R.id.remind_ok_layout:
			AddExamRemindEntry entry = new AddExamRemindEntry();
			entry.cityId = citySelect.id;
			entry.itemId = examSelect.id;
			entry.type = "1";
			addExamCommitRequest(entry);
			break;

		default:
			break;
		}

	}

}
