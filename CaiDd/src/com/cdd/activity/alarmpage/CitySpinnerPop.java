package com.cdd.activity.alarmpage;

import java.util.ArrayList;

import com.cdd.R;
import com.cdd.mode.CityItemEntry;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;

public class CitySpinnerPop extends PopupWindow {

	
	private Context context;

	private View view;
	
	private OnCityClickListener listener;
	
	private ListView spinnerList;
	
	
	public interface OnCityClickListener {
		public void onClick(CityItemEntry item);
	}
	
	private CitySpinnerAdapter adapter;
	
	public CitySpinnerPop(Context context, OnCityClickListener listener, ArrayList<CityItemEntry> list) {

		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.spinner_pop_menu, null);
		this.listener = listener;
		this.setContentView(view);
		this.setWidth(150);
		this.setHeight(600);
		this.setFocusable(true);
		this.setOutsideTouchable(true);
        this.update();  
        ColorDrawable dw = new ColorDrawable(0000000000);  
        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(R.style.AnimationPreview);
        initView();
        adapter = new CitySpinnerAdapter(context);
        adapter.addData(list);
        spinnerList.setAdapter(adapter);
	
	}
	
	private void initView() {
		spinnerList = (ListView) view.findViewById(R.id.spinner_list);
		spinnerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CityItemEntry item = adapter.getItem(position);
				if (listener != null) {
					listener.onClick(item);
					dismiss();
				}
			}
		});
	}
	
	public void showPopupWindow(View parent) {  
        if (!this.isShowing()) {  
            this.showAsDropDown(parent, 10, 0);  
        } else {  
            this.dismiss();  
        }  
    }

}
